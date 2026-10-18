// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.genetics;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

public class GeneticsScaleTest {

    private static Unsafe unsafe;
    private static int entityIdCounter = 1000;

    static {
        try {
            SharedConstants.tryDetectVersion();
            Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
    }

    @BeforeAll
    public static void setUpAll() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to acquire Unsafe", e);
        }
    }

    private static Zombie createDummyEntity() {
        try {
            Zombie zombie = (Zombie) unsafe.allocateInstance(Zombie.class);
            Field idField = Entity.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(zombie, ++entityIdCounter);

            Field uuidField = Entity.class.getDeclaredField("uuid");
            uuidField.setAccessible(true);
            uuidField.set(zombie, UUID.randomUUID());
            return zombie;
        } catch (Exception e) {
            throw new RuntimeException("Failed to allocate dummy Zombie entity", e);
        }
    }

    @Test
    @DisplayName("Verify scale factor delta modifier math")
    public void testScaleModifierCalculation() {
        // Base Minecraft scale attribute is 1.0.
        // A scale factor of 1.0 represents vanilla normal size (modifier must be 0.0).
        float normalScale = 1.0f;
        float normalModifier = normalScale - 1.0f;
        Assertions.assertEquals(0.0f, normalModifier, 0.0001f, "1.0x scale should produce 0.0 modifier delta");

        // A scale factor of 1.30 represents 130% size (+30% modifier).
        float largeScale = 1.30f;
        float largeModifier = largeScale - 1.0f;
        Assertions.assertEquals(0.30f, largeModifier, 0.0001f, "1.30x scale should produce +0.30 modifier delta");

        // A scale factor of 0.50 represents 50% size (-50% modifier).
        float smallScale = 0.50f;
        float smallModifier = smallScale - 1.0f;
        Assertions.assertEquals(-0.50f, smallModifier, 0.0001f, "0.50x scale should produce -0.50 modifier delta");
    }

    @Test
    @DisplayName("Verify linked attribute calculations from scale factor")
    public void testLinkedAttributeModifierCalculation() {
        LinkedAttribute propLink = new LinkedAttribute("minecraft:generic.max_health", LinkMode.PROPORTIONAL, 1.0f);
        Assertions.assertEquals(0.30f, propLink.computeModifier(1.30f), 0.0001f);
        Assertions.assertEquals(-0.50f, propLink.computeModifier(0.50f), 0.0001f);
        Assertions.assertEquals(0.0f, propLink.computeModifier(1.00f), 0.0001f);

        LinkedAttribute invLink = new LinkedAttribute("minecraft:generic.movement_speed", LinkMode.INVERSE, 0.5f);
        Assertions.assertEquals(-0.15f, invLink.computeModifier(1.30f), 0.0001f);
        Assertions.assertEquals(0.25f, invLink.computeModifier(0.50f), 0.0001f);
    }

    @Test
    @DisplayName("Pillar 4: Verify getScale() and setScale() support uncapped scales without [0.5, 2.0] clamping")
    public void testUncappedScaleSupport() {
        Zombie entity = createDummyEntity();

        // 1. Extreme small scale (0.1f) — must not be clamped to 0.5f
        DasikAnimalGeneticsAPI.setScale(entity, 0.1f);
        Assertions.assertEquals(0.1f, DasikAnimalGeneticsAPI.getScale(entity), 0.0001f,
                "0.1f scale must not be clamped to 0.5f");
        Assertions.assertTrue(DasikAnimalGeneticsAPI.isRunt(entity), "0.1f entity must be classified as runt (< 0.85f)");
        Assertions.assertFalse(DasikAnimalGeneticsAPI.isGiant(entity), "0.1f entity must not be classified as giant");

        // 2. Large custom scale (3.5f) — must not be clamped to 2.0f
        DasikAnimalGeneticsAPI.setScale(entity, 3.5f);
        Assertions.assertEquals(3.5f, DasikAnimalGeneticsAPI.getScale(entity), 0.0001f,
                "3.5f scale must not be clamped to 2.0f");
        Assertions.assertTrue(DasikAnimalGeneticsAPI.isGiant(entity), "3.5f entity must be classified as giant (> 1.15f)");
        Assertions.assertFalse(DasikAnimalGeneticsAPI.isRunt(entity), "3.5f entity must not be classified as runt");

        // 3. Minecraft vanilla scale attribute max limit (16.0f)
        DasikAnimalGeneticsAPI.setScale(entity, 16.0f);
        Assertions.assertEquals(16.0f, DasikAnimalGeneticsAPI.getScale(entity), 0.0001f,
                "16.0f scale must be preserved accurately");

        // 4. Freedom Over Anti-Crash: Mega entity scales beyond 16.0f (e.g. 50.0f)
        DasikAnimalGeneticsAPI.setScale(entity, 50.0f);
        Assertions.assertEquals(50.0f, DasikAnimalGeneticsAPI.getScale(entity), 0.0001f,
                "50.0f scale must be preserved without arbitrary nanny limits");
    }

    @Test
    @DisplayName("Pillar 2: Verify backward compatibility fallback when entity only has legacy 'scale' trait")
    public void testBackwardCompatibilityLegacyScaleTraitFallback() {
        Zombie legacyEntity = createDummyEntity();

        // Entity has ONLY legacy "scale": 2.5f trait (no "size" trait)
        EntityGenetics legacyGenetics = new EntityGenetics(
                Optional.empty(),
                Optional.empty(),
                false,
                true,
                Map.of("scale", 2.5f)
        );
        GeneticsEngine.setGenetics(legacyEntity, legacyGenetics);

        // getScale() must fallback to "scale" trait seamlessly
        float resolvedScale = DasikAnimalGeneticsAPI.getScale(legacyEntity);
        Assertions.assertEquals(2.5f, resolvedScale, 0.0001f,
                "getScale() must fallback to legacy 'scale' trait when 'size' is absent");
        Assertions.assertTrue(DasikAnimalGeneticsAPI.isGiant(legacyEntity),
                "Legacy entity with scale 2.5f must be recognized as giant");

        // Legacy runt entity
        Zombie legacyRunt = createDummyEntity();
        GeneticsEngine.setGenetics(legacyRunt, new EntityGenetics(
                Optional.empty(),
                Optional.empty(),
                false,
                true,
                Map.of("scale", 0.6f)
        ));
        Assertions.assertEquals(0.6f, DasikAnimalGeneticsAPI.getScale(legacyRunt), 0.0001f);
        Assertions.assertTrue(DasikAnimalGeneticsAPI.isRunt(legacyRunt),
                "Legacy runt with scale 0.6f must be recognized as runt");

        // Precedence test: when BOTH "size" and "scale" are present, "size" must take precedence
        Zombie dualTraitEntity = createDummyEntity();
        GeneticsEngine.setGenetics(dualTraitEntity, new EntityGenetics(
                Optional.empty(),
                Optional.empty(),
                false,
                true,
                Map.of("size", 1.4f, "scale", 0.8f)
        ));
        Assertions.assertEquals(1.4f, DasikAnimalGeneticsAPI.getScale(dualTraitEntity), 0.0001f,
                "'size' trait must take precedence over legacy 'scale' trait");
    }

    @Test
    @DisplayName("Pillar 5: Verify setScale() stores trait under SizeStatsBuilder.SIZE_TRAIT_ID ('size')")
    public void testTraitParityStoresUnderSizeTraitId() {
        Zombie entity = createDummyEntity();

        // Verify constant contract
        Assertions.assertEquals("size", SizeStatsBuilder.SIZE_TRAIT_ID,
                "SizeStatsBuilder.SIZE_TRAIT_ID must equal 'size'");

        // setScale must store under SizeStatsBuilder.SIZE_TRAIT_ID
        DasikAnimalGeneticsAPI.setScale(entity, 1.8f);

        EntityGenetics genetics = GeneticsEngine.getGenetics(entity);
        Assertions.assertTrue(genetics.traits().containsKey(SizeStatsBuilder.SIZE_TRAIT_ID),
                "Genetics traits map must contain key 'size'");
        Assertions.assertEquals(1.8f, genetics.traits().get(SizeStatsBuilder.SIZE_TRAIT_ID), 0.0001f,
                "Trait value for 'size' must match set scale");
    }

    @Test
    @DisplayName("Pillar 1: Verify saturated arithmetic and defensive sanitization against NaN, Infinity, negative values, and null")
    public void testSaturatedArithmeticAndDefensiveSanitization() {
        Zombie entity = createDummyEntity();

        // 1. Float.NaN must sanitize safely to 1.0f
        DasikAnimalGeneticsAPI.setScale(entity, Float.NaN);
        Assertions.assertEquals(1.0f, DasikAnimalGeneticsAPI.getScale(entity), 0.0001f,
                "Float.NaN must sanitize to default 1.0f scale");

        // 2. Float.POSITIVE_INFINITY must sanitize safely to 1.0f
        DasikAnimalGeneticsAPI.setScale(entity, Float.POSITIVE_INFINITY);
        Assertions.assertEquals(1.0f, DasikAnimalGeneticsAPI.getScale(entity), 0.0001f,
                "Float.POSITIVE_INFINITY must sanitize to default 1.0f scale");

        // 3. Float.NEGATIVE_INFINITY must sanitize safely to 1.0f
        DasikAnimalGeneticsAPI.setScale(entity, Float.NEGATIVE_INFINITY);
        Assertions.assertEquals(1.0f, DasikAnimalGeneticsAPI.getScale(entity), 0.0001f,
                "Float.NEGATIVE_INFINITY must sanitize to default 1.0f scale");

        // 4. Negative scales must clamp to positive epsilon (0.0001f) to prevent inverted model rendering
        DasikAnimalGeneticsAPI.setScale(entity, -100.0f);
        Assertions.assertEquals(0.0001f, DasikAnimalGeneticsAPI.getScale(entity), 0.00001f,
                "Negative scales must be bounded to positive epsilon 0.0001f");

        DasikAnimalGeneticsAPI.setScale(entity, 0.0f);
        Assertions.assertEquals(0.0001f, DasikAnimalGeneticsAPI.getScale(entity), 0.00001f,
                "Zero scale must be bounded to positive epsilon 0.0001f");

        // 5. Extreme large values (Pillar 1 Chaos Fuzzing)
        DasikAnimalGeneticsAPI.setScale(entity, 1_000_000.0f);
        Assertions.assertEquals(1_000_000.0f, DasikAnimalGeneticsAPI.getScale(entity), 0.1f,
                "1,000,000.0f scale must be accepted without arithmetic overflow or arbitrary capping");

        // 6. Null entity safety
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(1.0f, DasikAnimalGeneticsAPI.getScale(null),
                    "Null entity must return default 1.0f scale");
            DasikAnimalGeneticsAPI.setScale(null, 2.5f);
            Assertions.assertFalse(DasikAnimalGeneticsAPI.isRunt(null), "Null entity cannot be runt");
            Assertions.assertFalse(DasikAnimalGeneticsAPI.isGiant(null), "Null entity cannot be giant");
        });
    }
}
