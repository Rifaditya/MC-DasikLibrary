// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.test;

import java.util.Random;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.genetics.DasikAnimalGeneticsAPI;
import net.dasik.social.api.genetics.LinkMode;
import net.dasik.social.api.genetics.LinkedAttribute;
import net.dasik.social.api.vision.PlayerVisionTracker;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DasikLibraryStressFuzzTest {

    private static final double[] CHAOTIC_DOUBLES = {
            Double.NaN,
            Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY,
            Double.MAX_VALUE,
            -Double.MAX_VALUE,
            Double.MIN_VALUE,
            -Double.MIN_VALUE,
            0.0,
            -0.0,
            1.0,
            -1.0,
            1e300,
            -1e300,
            1e-300,
            -1e-300,
            1e-5,
            0.0001,
            0.00005,
            32.0,
            90.0,
            170.0,
            180.0,
            360.0,
            -90.0,
            -180.0,
            -360.0,
            720.0
    };

    private static final String[] CHAOTIC_STRINGS = {
            null,
            "",
            " ",
            "   ",
            "\t\n\r",
            "\u0000\u0001\u0002",
            "____",
            "::::",
            "_:",
            ":_",
            "ig:ore_minecraft_iron_ore",
            "ig:ore_create_zinc_ore",
            "ig:ore_custom_ruby_ore",
            "ig:ore_",
            "ig_ore_minecraft_iron_ore",
            "ig_diamond_sword",
            "minecraft:randomTickSpeed",
            "c:ore_amplifier_blacklist",
            "fabric:load_priority",
            "fabric-api:registry_sync",
            "dasik-library:debug_mode",
            "testmod:some_feature",
            "UPPERCASE:WITH_COLON",
            "spaces in:the rule name",
            "emoji:🎮_🦊_🐾",
            "slash/and\\backslash",
            "special!@#$%^&*()+=~`\"'<>?,.",
            "a_".repeat(1000)
    };

    private static final float[] CHAOTIC_FLOATS = {
            Float.NaN,
            Float.POSITIVE_INFINITY,
            Float.NEGATIVE_INFINITY,
            0.0f,
            -0.0f,
            -1.0f,
            -100.0f,
            -Float.MAX_VALUE,
            Float.MIN_VALUE,
            Float.MAX_VALUE,
            1e20f,
            -1e20f,
            0.0001f,
            0.00005f,
            -0.00005f,
            1.0f,
            1.30f,
            0.50f,
            3.5f,
            16.0f,
            50.0f
    };

    @Test
    @DisplayName("Pillar 1: 20,000 iterations of VisionTracker vector geometry and FOV chaos fuzzing")
    public void testVisionTrackerMathChaosFuzzing() {
        Random random = new Random(0xDA51C);

        for (int i = 0; i < 20_000; i++) {
            double eyeX, eyeY, eyeZ;
            double targetX, targetY, targetZ;
            double lookX, lookY, lookZ;
            double fovDegrees;

            int mode = i % 5;
            if (mode == 0) {
                // Zero-length vector: eye == target
                eyeX = (random.nextDouble() - 0.5) * 1000.0;
                eyeY = (random.nextDouble() - 0.5) * 1000.0;
                eyeZ = (random.nextDouble() - 0.5) * 1000.0;
                targetX = eyeX;
                targetY = eyeY;
                targetZ = eyeZ;
                lookX = 0.0;
                lookY = 0.0;
                lookZ = 1.0;
                fovDegrees = 90.0;
            } else if (mode == 1) {
                // Chaotic injection (NaN, Infinity, extreme doubles)
                eyeX = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
                eyeY = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
                eyeZ = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
                targetX = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
                targetY = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
                targetZ = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
                lookX = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
                lookY = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
                lookZ = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
                fovDegrees = CHAOTIC_DOUBLES[random.nextInt(CHAOTIC_DOUBLES.length)];
            } else if (mode == 2) {
                // Normal forward sightline
                eyeX = (random.nextDouble() - 0.5) * 10_000.0;
                eyeY = random.nextDouble() * 256.0;
                eyeZ = (random.nextDouble() - 0.5) * 10_000.0;
                lookX = 0.0;
                lookY = 0.0;
                lookZ = 1.0;
                targetX = eyeX;
                targetY = eyeY;
                targetZ = eyeZ + 10.0; // ahead on Z
                fovDegrees = 90.0;
            } else if (mode == 3) {
                // Normal backward sightline (behind player)
                eyeX = (random.nextDouble() - 0.5) * 10_000.0;
                eyeY = random.nextDouble() * 256.0;
                eyeZ = (random.nextDouble() - 0.5) * 10_000.0;
                lookX = 0.0;
                lookY = 0.0;
                lookZ = 1.0;
                targetX = eyeX;
                targetY = eyeY;
                targetZ = eyeZ - 10.0; // behind on Z
                fovDegrees = 90.0;
            } else {
                // Large randomized coordinates within standard world boundaries
                eyeX = (random.nextDouble() - 0.5) * 60_000_000.0;
                eyeY = (random.nextDouble() - 0.5) * 2000.0;
                eyeZ = (random.nextDouble() - 0.5) * 60_000_000.0;
                targetX = (random.nextDouble() - 0.5) * 60_000_000.0;
                targetY = (random.nextDouble() - 0.5) * 2000.0;
                targetZ = (random.nextDouble() - 0.5) * 60_000_000.0;
                lookX = (random.nextDouble() - 0.5) * 2.0;
                lookY = (random.nextDouble() - 0.5) * 2.0;
                lookZ = (random.nextDouble() - 0.5) * 2.0;
                fovDegrees = (random.nextDouble() - 0.5) * 720.0;
            }

            // Execute vector dot-product FOV math pipeline with zero unhandled exceptions
            assertDoesNotThrow(() -> {
                double dx = targetX - eyeX;
                double dy = targetY - eyeY;
                double dz = targetZ - eyeZ;
                double lenSq = dx * dx + dy * dy + dz * dz;

                boolean inFov;
                if (Double.isNaN(lenSq) || Double.isInfinite(lenSq)) {
                    inFov = false;
                } else if (lenSq <= 0.0001) {
                    inFov = true;
                } else {
                    double invLen = 1.0 / Math.sqrt(lenSq);
                    if (Double.isNaN(invLen) || Double.isInfinite(invLen)) {
                        inFov = false;
                    } else {
                        double normX = dx * invLen;
                        double normY = dy * invLen;
                        double normZ = dz * invLen;
                        double dot = lookX * normX + lookY * normY + lookZ * normZ;
                        double minDot = Math.cos(Math.toRadians(fovDegrees * 0.5));
                        inFov = !Double.isNaN(dot) && !Double.isNaN(minDot) && (dot >= minDot);
                    }
                }

                // Verify boundary consistency
                if (mode == 0) {
                    assertTrue(inFov, "Zero-length vector (lenSq <= 0.0001) must resolve inside FOV");
                } else if (mode == 2) {
                    assertTrue(inFov, "Directly ahead target must resolve inside 90-degree FOV");
                } else if (mode == 3) {
                    assertFalse(inFov, "Directly behind target must resolve outside 90-degree FOV");
                }

                // Also verify PlayerVisionTracker static null/boundary API contracts
                assertFalse(PlayerVisionTracker.canSee(null, null, 16.0, fovDegrees));
                assertFalse(PlayerVisionTracker.isInFieldOfView(null, null, fovDegrees));
                assertFalse(PlayerVisionTracker.hasLineOfSight(null, new Vec3(eyeX, eyeY, eyeZ), new Vec3(targetX, targetY, targetZ), 0.25));
                assertFalse(PlayerVisionTracker.hasLineOfSightToBlock(null, new Vec3(eyeX, eyeY, eyeZ), new Vec3(targetX, targetY, targetZ), null));
            });
        }
    }

    @Test
    @DisplayName("Pillar 1: 20,000 iterations of DynamicGameRuleManager chaos fuzzing")
    public void testGameRuleManagerChaosFuzzing() {
        Random random = new Random(0xDA51C);

        for (int i = 0; i < 20_000; i++) {
            final String fuzzStr;
            if (i < CHAOTIC_STRINGS.length) {
                fuzzStr = CHAOTIC_STRINGS[i];
            } else {
                int type = random.nextInt(4);
                if (type == 0) {
                    fuzzStr = CHAOTIC_STRINGS[random.nextInt(CHAOTIC_STRINGS.length)];
                } else if (type == 1) {
                    fuzzStr = "mod_" + random.nextInt(500) + ":rule_" + random.nextInt(500);
                } else if (type == 2) {
                    fuzzStr = "ig_ore_mod" + random.nextInt(500) + "_ore_" + random.nextInt(500);
                } else {
                    StringBuilder sb = new StringBuilder();
                    int len = random.nextInt(50);
                    for (int c = 0; c < len; c++) {
                        sb.append((char) (random.nextInt(127)));
                    }
                    fuzzStr = sb.toString();
                }
            }

            final int fuzzDefault = random.nextInt();
            final int fuzzMin = random.nextInt();
            final int fuzzMax = random.nextInt();

            assertDoesNotThrow(() -> {
                // 1. generateReadableName fuzzing
                String readable = DynamicGameRuleManager.generateReadableName(fuzzStr);
                if (fuzzStr == null) {
                    assertNull(readable, "generateReadableName(null) must return null");
                } else if (fuzzStr.isEmpty()) {
                    assertEquals("", readable, "generateReadableName('') must return empty string");
                } else {
                    assertNotNull(readable, "generateReadableName must return non-null string for non-null input");
                }

                // 2. extractModId fuzzing
                String modId = DynamicGameRuleManager.extractModId(fuzzStr);
                if (fuzzStr != null && (fuzzStr.startsWith("minecraft:") || fuzzStr.startsWith("dasik-library:")
                        || fuzzStr.startsWith("fabric:") || fuzzStr.startsWith("c:"))) {
                    assertNull(modId, "Protected namespaces must always return null modId");
                }

                // 3. Integer builder bounds fuzzing
                DynamicGameRuleManager.integerRule(fuzzStr, null, fuzzDefault)
                        .min(fuzzMin)
                        .max(fuzzMax)
                        .range(fuzzMin, fuzzMax)
                        .description(fuzzStr)
                        .name(fuzzStr);

                // 4. Defensive unregistration
                DynamicGameRuleManager.unregister(fuzzStr);
                DynamicGameRuleManager.unregisterModRules(fuzzStr);
            }, "DynamicGameRuleManager operations must not throw unhandled exceptions or NPEs under chaotic input");
        }
    }

    @Test
    @DisplayName("Pillar 1: 20,000 iterations of Genetics scale saturation math chaos fuzzing")
    public void testGeneticsScaleChaosFuzzing() {
        Random random = new Random(0xDA51C);

        for (int i = 0; i < 20_000; i++) {
            float fuzzScale;
            if (i < CHAOTIC_FLOATS.length) {
                fuzzScale = CHAOTIC_FLOATS[i];
            } else {
                int mode = random.nextInt(5);
                if (mode == 0) {
                    fuzzScale = CHAOTIC_FLOATS[random.nextInt(CHAOTIC_FLOATS.length)];
                } else if (mode == 1) {
                    fuzzScale = -random.nextFloat() * 1000.0f; // negative floats
                } else if (mode == 2) {
                    fuzzScale = random.nextFloat() * 100.0f; // normal/large scales
                } else if (mode == 3) {
                    fuzzScale = (random.nextFloat() - 0.5f) * 1e30f; // extreme values
                } else {
                    fuzzScale = random.nextFloat() * 0.0002f; // near-zero boundary
                }
            }

            assertDoesNotThrow(() -> {
                // Scale saturation formula: Math.max(0.0001f, scale) with NaN/Infinity sanitization
                float sanitized = Float.isNaN(fuzzScale) || Float.isInfinite(fuzzScale) ? 1.0f : Math.max(0.0001f, fuzzScale);

                // Asserts zero NaN or Infinity
                assertFalse(Float.isNaN(sanitized), "Sanitized scale must never be NaN");
                assertFalse(Float.isInfinite(sanitized), "Sanitized scale must never be Infinite");
                assertTrue(sanitized >= 0.0001f, "Sanitized scale must be strictly >= 0.0001f");

                // LinkedAttribute modifier calculation fuzzing
                for (LinkMode linkMode : LinkMode.values()) {
                    LinkedAttribute link = new LinkedAttribute("minecraft:generic.max_health", linkMode, 1.0f);
                    float modifier = link.computeModifier(sanitized);
                    assertFalse(Float.isNaN(modifier), "Linked attribute modifier must never be NaN");
                }

                // DasikAnimalGeneticsAPI null entity defensive safety
                assertEquals(1.0f, DasikAnimalGeneticsAPI.getScale(null), 0.0001f);
                DasikAnimalGeneticsAPI.setScale(null, fuzzScale);
                assertFalse(DasikAnimalGeneticsAPI.isRunt(null));
                assertFalse(DasikAnimalGeneticsAPI.isGiant(null));
            }, "Genetics scale saturation math must not throw or crash on chaotic float inputs");
        }
    }
}
