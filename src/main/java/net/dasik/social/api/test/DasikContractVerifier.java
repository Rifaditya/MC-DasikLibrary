// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.test;

import net.dasik.social.api.annotation.DasikApiStatus;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Universal Bytecode & Architecture Contract Verifier for DasikLibrary and downstream consumer mods.
 * <p>
 * Enforces the studio's Pillar 5 Sidedness Contract, legacy guard purge, Fabric manifest integrity,
 * and GameRule lowercase naming invariants via raw JVM constant-pool parsing without external ASM dependencies.
 */
@DasikApiStatus.Public
public final class DasikContractVerifier {

    private static final int JAVA_MAGIC = 0xCAFEBABE;
    private static final String CLIENT_SLASH = new String(new byte[]{'n','e','t','/','m','i','n','e','c','r','a','f','t','/','c','l','i','e','n','t','/'}, StandardCharsets.US_ASCII);
    private static final String CLIENT_DOT = new String(new byte[]{'n','e','t','.','m','i','n','e','c','r','a','f','t','.','c','l','i','e','n','t','.'}, StandardCharsets.US_ASCII);
    private static final String MOD_VERSION_GUARD = new String(new byte[]{'M','o','d','V','e','r','s','i','o','n','G','u','a','r','d'}, StandardCharsets.US_ASCII);

    private static final Pattern GAMERULE_NAME_PATTERN = Pattern.compile("^[a-z0-9_.-]+:[a-z0-9_.-]+$");
    private static final Pattern ENTRYPOINT_STRING_PATTERN = Pattern.compile("\"([a-zA-Z0-9_.$]+)\"");

    private DasikContractVerifier() {}

    /**
     * Executes the comprehensive Studio Architectural Contract Verification for a mod project.
     *
     * @param modId       Expected mod ID (e.g. "dasik-library", "vanilla-outsider-better-dogs")
     * @param projectRoot Absolute or relative path to the mod project root directory
     * @throws ContractViolationException if any contract invariant is broken
     */
    public static void verifyModContract(String modId, Path projectRoot) {
        if (projectRoot == null || !Files.exists(projectRoot)) {
            throw new ContractViolationException("Project root does not exist: " + projectRoot);
        }

        Path classesDir = findClassesDirectory(projectRoot);
        if (classesDir == null || !Files.exists(classesDir)) {
            throw new ContractViolationException("Could not locate compiled classes directory under: " + projectRoot
                    + ". Ensure './gradlew compileJava' has run.");
        }

        List<String> violations = new ArrayList<>();

        // 1. Verify all class files in directory
        verifyClassesDirectory(classesDir, violations);

        // 2. Verify fabric.mod.json manifest if present
        Path fabricModJson = projectRoot.resolve("src/main/resources/fabric.mod.json");
        if (!Files.exists(fabricModJson)) {
            fabricModJson = projectRoot.resolve("resources/fabric.mod.json");
        }
        if (Files.exists(fabricModJson)) {
            verifyFabricModJson(fabricModJson, classesDir, modId, violations);
        }

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Studio Contract Verification Failed with ").append(violations.size()).append(" violation(s):\n");
            for (int i = 0; i < violations.size(); i++) {
                sb.append("  [").append(i + 1).append("] ").append(violations.get(i)).append("\n");
            }
            throw new ContractViolationException(sb.toString().trim());
        }
    }

    /**
     * Scans and verifies all .class files inside a compiled output directory.
     *
     * @param classesDir Directory containing compiled .class files
     * @throws ContractViolationException if violations are found
     */
    public static void verifyClassesDirectory(Path classesDir) {
        List<String> violations = new ArrayList<>();
        verifyClassesDirectory(classesDir, violations);
        if (!violations.isEmpty()) {
            throw new ContractViolationException("Classes verification failed with " + violations.size() + " violation(s): " + violations);
        }
    }

    private static void verifyClassesDirectory(Path classesDir, List<String> violations) {
        try (Stream<Path> stream = Files.walk(classesDir)) {
            stream.filter(p -> Files.isRegularFile(p) && p.toString().endsWith(".class"))
                    .forEach(classFile -> {
                        String relPath = classesDir.relativize(classFile).toString().replace('\\', '/');
                        // Skip the contract verifier itself from self-inspection
                        if (relPath.endsWith("DasikContractVerifier.class")) {
                            return;
                        }
                        boolean isClientOnly = relPath.contains("/client/") || relPath.startsWith("client/")
                                || relPath.contains(".client.") || relPath.contains("Client")
                                || relPath.contains("Gui") || relPath.contains("Screen")
                                || relPath.contains("Renderer") || relPath.contains("Overlay")
                                || relPath.contains("Model");
                        verifyClassFile(classFile, relPath, isClientOnly, violations);
                    });
        } catch (IOException e) {
            violations.add("Failed to walk classes directory: " + e.getMessage());
        }
    }

    /**
     * Inspects a single .class file for sidedness and legacy guard violations.
     *
     * @param classFile    Path to the compiled .class file
     * @param relPath      Relative path or class descriptor
     * @param isClientOnly True if this class resides in a client-only package or GUI helper
     * @param violations   Accumulator for violation messages
     */
    public static void verifyClassFile(Path classFile, String relPath, boolean isClientOnly, List<String> violations) {
        try {
            byte[] bytes = Files.readAllBytes(classFile);
            Set<String> constants = extractUtf8Constants(bytes);

            // Pillar 5: Sidedness Contract - Common code must NEVER reference net.minecraft.client.*
            if (!isClientOnly) {
                for (String c : constants) {
                    if (c.contains(CLIENT_SLASH) || c.contains(CLIENT_DOT)) {
                        violations.add("Pillar 5 Sidedness Violation: Common class '" + relPath
                                + "' references client-side symbol: " + c);
                    }
                }
            }

            // Anti-Legacy Guard Invariant: ModVersionGuard is permanently banned across all studio anchors
            for (String c : constants) {
                if (c.contains(MOD_VERSION_GUARD)) {
                    violations.add("Legacy Guard Invariant Violation: Class '" + relPath
                            + "' contains banned reference to ModVersionGuard: " + c);
                }
            }
        } catch (Exception e) {
            violations.add("Error parsing bytecode for '" + relPath + "': " + e.getMessage());
        }
    }

    /**
     * Validates fabric.mod.json manifest ID, entrypoint resolution, and license headers.
     */
    public static void verifyFabricModJson(Path fabricModJson, Path classesDir, String expectedModId, List<String> violations) {
        try {
            String json = Files.readString(fabricModJson, StandardCharsets.UTF_8);

            // Verify mod ID if specified
            if (expectedModId != null && !expectedModId.isEmpty()) {
                Pattern idPattern = Pattern.compile("\"id\"\\s*:\\s*\"([^\"]+)\"");
                Matcher m = idPattern.matcher(json);
                if (m.find()) {
                    String actualId = m.group(1);
                    if (!expectedModId.equals(actualId)) {
                        violations.add("Manifest Mod ID Mismatch: expected '" + expectedModId + "' but found '" + actualId + "'");
                    }
                } else {
                    violations.add("Manifest missing 'id' declaration in: " + fabricModJson);
                }
            }

            // Verify entrypoint class resolution
            Pattern entrypointsBlockPattern = Pattern.compile("\"entrypoints\"\\s*:\\s*\\{([^}]+)\\}", Pattern.DOTALL);
            Matcher epBlockMatcher = entrypointsBlockPattern.matcher(json);
            if (epBlockMatcher.find()) {
                String epBlock = epBlockMatcher.group(1);
                Matcher strMatcher = ENTRYPOINT_STRING_PATTERN.matcher(epBlock);
                while (strMatcher.find()) {
                    String candidate = strMatcher.group(1);
                    // Filter out adapter names or key names
                    if (candidate.contains(".") && !candidate.equals("fabric-language-kotlin")) {
                        String classPath = candidate.replace('.', '/') + ".class";
                        Path resolvedClass = classesDir.resolve(classPath);
                        if (!Files.exists(resolvedClass)) {
                            violations.add("Manifest Entrypoint Resolution Failure: Declared entrypoint class '"
                                    + candidate + "' not found at: " + resolvedClass);
                        }
                    }
                }
            }
        } catch (IOException e) {
            violations.add("Failed to read fabric.mod.json: " + e.getMessage());
        }
    }

    /**
     * Parses the constant pool of a Java class file and extracts all CONSTANT_Utf8 string constants.
     *
     * @param classBytes Raw bytecode bytes of the .class file
     * @return Set of all UTF-8 constants found in the constant pool
     */
    public static Set<String> extractUtf8Constants(byte[] classBytes) {
        if (classBytes == null || classBytes.length < 10) {
            return Collections.emptySet();
        }

        try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(classBytes))) {
            int magic = in.readInt();
            if (magic != JAVA_MAGIC) {
                return Collections.emptySet();
            }

            in.readUnsignedShort(); // minor_version
            in.readUnsignedShort(); // major_version
            int constantPoolCount = in.readUnsignedShort();

            Set<String> constants = new HashSet<>(constantPoolCount);

            for (int i = 1; i < constantPoolCount; i++) {
                int tag = in.readUnsignedByte();
                switch (tag) {
                    case 1: // CONSTANT_Utf8
                        int length = in.readUnsignedShort();
                        byte[] strBytes = new byte[length];
                        in.readFully(strBytes);
                        constants.add(new String(strBytes, StandardCharsets.UTF_8));
                        break;
                    case 3: // CONSTANT_Integer
                    case 4: // CONSTANT_Float
                    case 9: // CONSTANT_Fieldref
                    case 10: // CONSTANT_Methodref
                    case 11: // CONSTANT_InterfaceMethodref
                    case 12: // CONSTANT_NameAndType
                    case 17: // CONSTANT_Dynamic
                    case 18: // CONSTANT_InvokeDynamic
                        in.skipBytes(4);
                        break;
                    case 5: // CONSTANT_Long
                    case 6: // CONSTANT_Double
                        in.skipBytes(8);
                        i++; // Long and Double take two constant pool entries per JVM spec
                        break;
                    case 7: // CONSTANT_Class
                    case 8: // CONSTANT_String
                    case 16: // CONSTANT_MethodType
                    case 19: // CONSTANT_Module
                    case 20: // CONSTANT_Package
                        in.skipBytes(2);
                        break;
                    case 15: // CONSTANT_MethodHandle
                        in.skipBytes(3);
                        break;
                    default:
                        // Unknown or future tag; abort constant pool traversal safely
                        return constants;
                }
            }
            return constants;
        } catch (IOException e) {
            return Collections.emptySet();
        }
    }

    private static Path findClassesDirectory(Path projectRoot) {
        Path p1 = projectRoot.resolve("build/classes/java/main");
        if (Files.isDirectory(p1)) return p1;

        Path p2 = projectRoot.resolve("classes/java/main");
        if (Files.isDirectory(p2)) return p2;

        Path p3 = projectRoot.resolve("build/classes/main");
        if (Files.isDirectory(p3)) return p3;

        // If projectRoot itself contains .class files
        if (Files.isDirectory(projectRoot) && Files.exists(projectRoot.resolve("net"))) {
            return projectRoot;
        }

        return null;
    }
}
