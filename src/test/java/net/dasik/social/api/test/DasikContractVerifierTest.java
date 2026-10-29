// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DasikContractVerifier Reality & Fuzz Test Suite")
class DasikContractVerifierTest {

    @Test
    @DisplayName("Self-Contract Verification: MC-DasikLibrary classes adhere to studio contract")
    void testSelfClassesContract() {
        Path classesDir = Path.of("build/classes/java/main");
        if (Files.isDirectory(classesDir)) {
            assertDoesNotThrow(() -> DasikContractVerifier.verifyClassesDirectory(classesDir));
        }
    }

    @Test
    @DisplayName("Pillar 1 Chaos Fuzzing: 10,000 malformed bytecode arrays must never throw unhandled exceptions")
    void testChaosFuzzingConstantPoolParser() {
        Random random = new Random(0xDA51C);
        for (int i = 0; i < 10000; i++) {
            int length = random.nextInt(256);
            byte[] bytes = new byte[length];
            random.nextBytes(bytes);
            // Must return gracefully (empty set or partial set) without crashing with unexpected exceptions
            assertDoesNotThrow(() -> {
                Set<String> result = DasikContractVerifier.extractUtf8Constants(bytes);
                assertNotNull(result);
            });
        }
    }

    @Test
    @DisplayName("Pillar 5 Sidedness: Common class referencing net/minecraft/client/ triggers ContractViolationException")
    void testDetectClientReferenceInCommonClass(@TempDir Path tempDir) throws IOException {
        Path fakeClass = tempDir.resolve("FakeCommonClass.class");
        byte[] bytecode = createSyntheticClassBytes("net/dasik/social/api/CommonService", "net/minecraft/client/Minecraft");
        Files.write(fakeClass, bytecode);

        List<String> violations = new ArrayList<>();
        DasikContractVerifier.verifyClassFile(fakeClass, "FakeCommonClass.class", false, violations);

        assertFalse(violations.isEmpty(), "Expected sidedness violation to be detected");
        assertTrue(violations.stream().anyMatch(v -> v.contains("Pillar 5 Sidedness Violation")),
                "Expected violation message to mention Pillar 5 Sidedness");
    }

    @Test
    @DisplayName("Anti-Legacy Guard Invariant: Any class referencing legacy guard triggers ContractViolationException")
    void testDetectLegacyGuardReference(@TempDir Path tempDir) throws IOException {
        String bannedGuard = "Mod" + "Version" + "Guard";
        Path fakeClass = tempDir.resolve("FakeLegacyClass.class");
        byte[] bytecode = createSyntheticClassBytes("net/dasik/social/api/LegacyService", "net/vanillaoutsider/util/" + bannedGuard);
        Files.write(fakeClass, bytecode);

        List<String> violations = new ArrayList<>();
        DasikContractVerifier.verifyClassFile(fakeClass, "FakeLegacyClass.class", false, violations);

        assertFalse(violations.isEmpty(), "Expected legacy guard violation to be detected");
        assertTrue(violations.stream().anyMatch(v -> v.contains(bannedGuard)),
                "Expected violation message to mention legacy guard");
    }

    @Test
    @DisplayName("Manifest Verification: Detects ID mismatch and missing entrypoints")
    void testFabricModJsonValidation(@TempDir Path tempDir) throws IOException {
        Path manifest = tempDir.resolve("fabric.mod.json");
        Path classesDir = tempDir.resolve("classes");
        Files.createDirectories(classesDir);

        String json = """
        {
          "schemaVersion": 1,
          "id": "test-mod",
          "version": "1.0.0",
          "entrypoints": {
            "main": [
              "net.dasik.test.MissingEntrypoint"
            ]
          }
        }
        """;
        Files.writeString(manifest, json, StandardCharsets.UTF_8);

        List<String> violations = new ArrayList<>();
        // 1. Mod ID mismatch
        DasikContractVerifier.verifyFabricModJson(manifest, classesDir, "expected-id", violations);
        assertTrue(violations.stream().anyMatch(v -> v.contains("Manifest Mod ID Mismatch")));

        // 2. Missing entrypoint
        assertTrue(violations.stream().anyMatch(v -> v.contains("Manifest Entrypoint Resolution Failure")));
    }

    /**
     * Helper to construct a minimal valid JVM class file structure containing specified constant pool strings.
     */
    private static byte[] createSyntheticClassBytes(String className, String... extraUtf8s) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);

        out.writeInt(0xCAFEBABE); // magic
        out.writeShort(0);        // minor
        out.writeShort(65);       // major (Java 21)

        // Count: 1 (index 0) + 1 (this_class Utf8) + 1 (this_class ClassRef) + extraUtf8s.length
        int count = 3 + extraUtf8s.length;
        out.writeShort(count);

        // CP #1: Utf8 className
        out.writeByte(1); // tag Utf8
        byte[] nameBytes = className.getBytes(StandardCharsets.UTF_8);
        out.writeShort(nameBytes.length);
        out.write(nameBytes);

        // CP #2: ClassRef pointing to #1
        out.writeByte(7); // tag Class
        out.writeShort(1);

        // Extra Utf8 constants
        for (String s : extraUtf8s) {
            out.writeByte(1);
            byte[] b = s.getBytes(StandardCharsets.UTF_8);
            out.writeShort(b.length);
            out.write(b);
        }

        // Access flags, this_class, super_class, interfaces_count, fields_count, methods_count, attributes_count
        out.writeShort(0x0021); // ACC_PUBLIC | ACC_SUPER
        out.writeShort(2);      // this_class
        out.writeShort(0);      // super_class (0 for testing)
        out.writeShort(0);      // interfaces_count
        out.writeShort(0);      // fields_count
        out.writeShort(0);      // methods_count
        out.writeShort(0);      // attributes_count

        return baos.toByteArray();
    }
}
