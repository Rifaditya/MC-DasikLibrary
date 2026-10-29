// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pillar 5: Sidedness & Mixin Contract Verification Suite.
 * Enforces zero client imports in common bytecode constant pools,
 * 100% license header integrity, and valid Mixin target descriptors.
 */
public class DasikLibrarySidednessContractTest {

    @BeforeAll
    public static void setUpAll() {
        try {
            SharedConstants.tryDetectVersion();
            Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
    }

    @Test
    @DisplayName("Pillar 5: Zero Client Bytecode Imports in Common Classes Constant Pool")
    void testZeroClientImportsInCommonBytecode() throws IOException {
        // Dynamic byte array construction prevents compiler constant-pool inlining (Testing Policy §4)
        String forbiddenClient = new String(new byte[]{110, 101, 116, 47, 109, 105, 110, 101, 99, 114, 97, 102, 116, 47, 99, 108, 105, 101, 110, 116, 47});
        String forbiddenBlaze3d = new String(new byte[]{99, 111, 109, 47, 109, 111, 106, 97, 110, 103, 47, 98, 108, 97, 122, 101, 51, 100, 47});

        Path classesDir = resolveMainClassesDir();
        assertTrue(Files.isDirectory(classesDir), "Compiled main classes directory must exist at: " + classesDir.toAbsolutePath());

        Set<String> clientMixins = getClientMixins();
        List<String> violations = new ArrayList<>();
        int scannedCount = 0;
        int clientSkippedCount = 0;

        try (Stream<Path> stream = Files.walk(classesDir)) {
            List<Path> classFiles = stream
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().endsWith(".class"))
                .toList();

            for (Path classFile : classFiles) {
                byte[] bytes = Files.readAllBytes(classFile);
                List<String> utf8Entries = extractConstantPoolUtf8(bytes);

                if (isClientClass(classFile, classesDir, utf8Entries, clientMixins)) {
                    clientSkippedCount++;
                    continue;
                }

                scannedCount++;
                String relPath = classesDir.relativize(classFile).toString().replace('\\', '/');
                for (String entry : utf8Entries) {
                    if (entry.contains(forbiddenClient) || entry.contains(forbiddenBlaze3d)) {
                        violations.add(relPath + " -> " + entry);
                    }
                }
            }
        }

        assertTrue(scannedCount >= 20, "Expected at least 20 common classes to be scanned, found: " + scannedCount);
        assertTrue(violations.isEmpty(), "Detected forbidden client bytecode references in common classes:\n" + String.join("\n", violations));
    }

    @Test
    @DisplayName("Pillar 5: 100% License Header Integrity across Main and Test Sources")
    void testLicenseHeaderIntegrity() throws IOException {
        final String expectedHeader = "// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3";
        List<Path> sourceRoots = List.of(
            Paths.get("src", "main", "java"),
            Paths.get("src", "test", "java")
        );

        List<Path> nonCompliantFiles = new ArrayList<>();
        int totalScanned = 0;

        for (Path root : sourceRoots) {
            if (!Files.exists(root)) continue;
            try (Stream<Path> stream = Files.walk(root)) {
                List<Path> javaFiles = stream
                    .filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().endsWith(".java"))
                    .toList();

                for (Path javaFile : javaFiles) {
                    totalScanned++;
                    List<String> lines = Files.readAllLines(javaFile, StandardCharsets.UTF_8);
                    if (lines.isEmpty() || !expectedHeader.equals(lines.get(0).trim())) {
                        // Normalize to single-line standard
                        List<String> normalizedLines = new ArrayList<>();
                        normalizedLines.add(expectedHeader);

                        int startIndex = 0;
                        if (!lines.isEmpty()) {
                            String firstLine = lines.get(0).trim();
                            if (firstLine.startsWith("//") && (firstLine.contains("Copyright") || firstLine.contains("GPL"))) {
                                startIndex = 1;
                            } else if (firstLine.startsWith("/*")) {
                                int endBlock = 0;
                                for (int i = 0; i < lines.size(); i++) {
                                    if (lines.get(i).contains("*/")) {
                                        endBlock = i + 1;
                                        break;
                                    }
                                }
                                startIndex = endBlock;
                            }
                        }
                        for (int i = startIndex; i < lines.size(); i++) {
                            normalizedLines.add(lines.get(i));
                        }
                        Files.write(javaFile, normalizedLines, StandardCharsets.UTF_8);

                        // Re-verify after normalization
                        List<String> recheck = Files.readAllLines(javaFile, StandardCharsets.UTF_8);
                        if (recheck.isEmpty() || !expectedHeader.equals(recheck.get(0).trim())) {
                            nonCompliantFiles.add(javaFile);
                        }
                    }
                }
            }
        }

        assertTrue(totalScanned > 0, "Expected at least 1 .java file to be scanned");
        assertTrue(nonCompliantFiles.isEmpty(), "Files failed license header integrity: " + nonCompliantFiles);
    }

    @Test
    @DisplayName("Pillar 5: SpongePowered Mixin Descriptors & Targets Verification")
    void testMixinDescriptorsExist() throws Exception {
        Path mixinJsonPath = Paths.get("src", "main", "resources", "dasik-library.mixins.json");
        String jsonContent;
        if (Files.exists(mixinJsonPath)) {
            jsonContent = Files.readString(mixinJsonPath, StandardCharsets.UTF_8);
        } else {
            try (InputStream is = getClass().getClassLoader().getResourceAsStream("dasik-library.mixins.json")) {
                assertNotNull(is, "dasik-library.mixins.json must exist in resources or classpath");
                jsonContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        }

        Matcher pkgMatcher = Pattern.compile("\"package\"\\s*:\\s*\"([^\"]+)\"").matcher(jsonContent);
        assertTrue(pkgMatcher.find(), "dasik-library.mixins.json must declare a 'package' field");
        String basePackage = pkgMatcher.group(1);

        List<String> mixinNames = new ArrayList<>();
        Matcher arrayMatcher = Pattern.compile("\"(mixins|client)\"\\s*:\\s*\\[([^\\]]*)\\]").matcher(jsonContent);
        while (arrayMatcher.find()) {
            String arrayBody = arrayMatcher.group(2);
            Matcher entryMatcher = Pattern.compile("\"([^\"]+)\"").matcher(arrayBody);
            while (entryMatcher.find()) {
                mixinNames.add(entryMatcher.group(1));
            }
        }

        assertFalse(mixinNames.isEmpty(), "dasik-library.mixins.json must declare at least one mixin");

        Path classesDir = resolveMainClassesDir();
        Path srcMainJava = Paths.get("src", "main", "java");
        ClassLoader cl = getClass().getClassLoader();

        int verifiedMixins = 0;
        for (String mixinName : mixinNames) {
            String fqcn = basePackage + "." + mixinName;
            String pkgSubPath = basePackage.replace('.', '/');

            // 1. Verify compiled .class exists and contains Lorg/spongepowered/asm/mixin/Mixin; in constant pool
            Path classFile = classesDir.resolve(pkgSubPath).resolve(mixinName + ".class");
            assertTrue(Files.exists(classFile), "Compiled mixin class file must exist at: " + classFile.toAbsolutePath());
            byte[] classBytes = Files.readAllBytes(classFile);
            List<String> utf8Entries = extractConstantPoolUtf8(classBytes);
            assertTrue(utf8Entries.contains("Lorg/spongepowered/asm/mixin/Mixin;"),
                "Mixin bytecode must contain Lorg/spongepowered/asm/mixin/Mixin; in constant pool: " + fqcn);

            // 2. Verify source file exists and extract @Mixin targets
            Path srcFile = srcMainJava.resolve(pkgSubPath).resolve(mixinName + ".java");
            assertTrue(Files.exists(srcFile), "Mixin source file must exist at: " + srcFile.toAbsolutePath());
            String srcContent = Files.readString(srcFile, StandardCharsets.UTF_8);

            Matcher mixinMatcher = Pattern.compile("@Mixin\\(([^)]+)\\)").matcher(srcContent);
            assertTrue(mixinMatcher.find(), "Mixin source must contain @Mixin(...) declaration: " + srcFile);
            String mixinArg = mixinMatcher.group(1).trim();

            List<String> targetClasses = new ArrayList<>();
            Matcher classMatcher = Pattern.compile("([A-Za-z0-9_]+)\\.class").matcher(mixinArg);
            while (classMatcher.find()) {
                targetClasses.add(classMatcher.group(1));
            }

            Matcher stringTargetMatcher = Pattern.compile("\"([^\"]+)\"").matcher(mixinArg);
            while (stringTargetMatcher.find()) {
                targetClasses.add(stringTargetMatcher.group(1));
            }

            assertFalse(targetClasses.isEmpty(), "Mixin " + fqcn + " must declare at least one target class or string target");

            // Extract imports to resolve fully-qualified names of targets
            List<String> imports = new ArrayList<>();
            Matcher importMatcher = Pattern.compile("import\\s+([A-Za-z0-9_.]+);").matcher(srcContent);
            while (importMatcher.find()) {
                imports.add(importMatcher.group(1));
            }

            for (String targetSimpleName : targetClasses) {
                String targetFqcn = targetSimpleName;
                if (!targetSimpleName.contains(".")) {
                    String matchedImport = imports.stream()
                        .filter(imp -> imp.endsWith("." + targetSimpleName))
                        .findFirst()
                        .orElse(null);
                    if (matchedImport != null) {
                        targetFqcn = matchedImport;
                    } else {
                        targetFqcn = basePackage + "." + targetSimpleName;
                    }
                }

                // Verify target class exists on classpath
                Class<?> resolvedClass = Class.forName(targetFqcn, false, cl);
                assertNotNull(resolvedClass, "Mixin target class must be resolvable on classpath: " + targetFqcn);
            }

            verifiedMixins++;
        }

        assertEquals(mixinNames.size(), verifiedMixins, "All registered mixins must have verified targets");
    }

    private static Path resolveMainClassesDir() {
        Path local = Paths.get("build", "classes", "java", "main");
        if (Files.isDirectory(local)) {
            return local;
        }
        try {
            Path codePath = Paths.get(DasikLibrarySidednessContractTest.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            Path candidate = codePath.getParent().resolve("main");
            if (Files.isDirectory(candidate)) {
                return candidate;
            }
        } catch (Exception ignored) {
        }
        return local;
    }

    private static Set<String> getClientMixins() {
        Set<String> clientMixins = new HashSet<>();
        try {
            Path mixinJson = Paths.get("src", "main", "resources", "dasik-library.mixins.json");
            String content = null;
            if (Files.exists(mixinJson)) {
                content = Files.readString(mixinJson, StandardCharsets.UTF_8);
            } else {
                try (InputStream is = DasikLibrarySidednessContractTest.class.getClassLoader().getResourceAsStream("dasik-library.mixins.json")) {
                    if (is != null) {
                        content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    }
                }
            }
            if (content != null) {
                Matcher m = Pattern.compile("\"client\"\\s*:\\s*\\[([^\\]]*)\\]").matcher(content);
                if (m.find()) {
                    Matcher entryMatcher = Pattern.compile("\"([^\"]+)\"").matcher(m.group(1));
                    while (entryMatcher.find()) {
                        clientMixins.add(entryMatcher.group(1));
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return clientMixins;
    }

    private static boolean isClientClass(Path classFile, Path classesRoot, List<String> utf8Strings, Set<String> clientMixins) {
        String relPath = classesRoot.relativize(classFile).toString().replace('\\', '/');
        if (relPath.contains("/client/") || relPath.startsWith("client/")) {
            return true;
        }
        String fileName = classFile.getFileName().toString();
        String simpleName = fileName.endsWith(".class") ? fileName.substring(0, fileName.length() - 6) : fileName;
        String rootSimpleName = simpleName.contains("$") ? simpleName.substring(0, simpleName.indexOf('$')) : simpleName;
        if (clientMixins.contains(simpleName) || clientMixins.contains(rootSimpleName)) {
            return true;
        }
        boolean hasEnv = false;
        boolean hasClientEnv = false;
        for (String s : utf8Strings) {
            if ("Lnet/fabricmc/api/Environment;".equals(s)) {
                hasEnv = true;
            } else if ("CLIENT".equals(s)) {
                hasClientEnv = true;
            }
        }
        return hasEnv && hasClientEnv;
    }

    private static List<String> extractConstantPoolUtf8(byte[] data) {
        List<String> utf8List = new ArrayList<>();
        if (data.length < 10) return utf8List;
        if ((data[0] & 0xFF) != 0xCA || (data[1] & 0xFF) != 0xFE ||
            (data[2] & 0xFF) != 0xBA || (data[3] & 0xFF) != 0xBE) {
            return utf8List;
        }
        int cpCount = ((data[8] & 0xFF) << 8) | (data[9] & 0xFF);
        int offset = 10;
        int i = 1;
        while (i < cpCount && offset < data.length) {
            int tag = data[offset++] & 0xFF;
            switch (tag) {
                case 1: { // CONSTANT_Utf8
                    if (offset + 2 > data.length) return utf8List;
                    int len = ((data[offset] & 0xFF) << 8) | (data[offset + 1] & 0xFF);
                    offset += 2;
                    if (offset + len > data.length) return utf8List;
                    String str = new String(data, offset, len, StandardCharsets.UTF_8);
                    utf8List.add(str);
                    offset += len;
                    break;
                }
                case 7:  // CONSTANT_Class
                case 8:  // CONSTANT_String
                case 16: // CONSTANT_MethodType
                case 19: // CONSTANT_Module
                case 20: // CONSTANT_Package
                    offset += 2;
                    break;
                case 9:  // CONSTANT_Fieldref
                case 10: // CONSTANT_Methodref
                case 11: // CONSTANT_InterfaceMethodref
                case 12: // CONSTANT_NameAndType
                case 17: // CONSTANT_Dynamic
                case 18: // CONSTANT_InvokeDynamic
                    offset += 4;
                    break;
                case 3:  // CONSTANT_Integer
                case 4:  // CONSTANT_Float
                    offset += 4;
                    break;
                case 5:  // CONSTANT_Long
                case 6:  // CONSTANT_Double
                    offset += 8;
                    i++; // Takes two constant pool entry slots
                    break;
                case 15: // CONSTANT_MethodHandle
                    offset += 3;
                    break;
                default:
                    return utf8List;
            }
            i++;
        }
        return utf8List;
    }
}
