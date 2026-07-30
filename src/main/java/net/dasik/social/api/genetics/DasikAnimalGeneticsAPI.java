/*
 * Zenith Sovereign Engineering - Dasik Library
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
// Verified against: LivingEntity.java, GeneticsEngine.java (26.2+)
package net.dasik.social.api.genetics;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;

/**
 * Universal, high-level API facade for animal genetics, inheritance, and range stats across all mods.
 */
public class DasikAnimalGeneticsAPI {

    public static boolean hasGenetics(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        return GeneticsEngine.getGenetics(entity).traitsRolled();
    }

    public static float getTrait(LivingEntity entity, String traitId, float defaultValue) {
        if (entity == null) {
            return defaultValue;
        }
        return GeneticsEngine.getGenetics(entity).traits().getOrDefault(traitId, defaultValue);
    }

    public static float getHealthBonus(LivingEntity entity) {
        return getTrait(entity, "max_health", 0.0f);
    }

    public static float getDamageMod(LivingEntity entity) {
        return getTrait(entity, "attack_damage", 0.0f);
    }

    public static float getSpeedMod(LivingEntity entity) {
        return getTrait(entity, "movement_speed", 0.0f);
    }

    public static boolean isInbred(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        return GeneticsEngine.getGenetics(entity).inbred();
    }

    public static Optional<UUID> getParent1Uuid(LivingEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }
        return GeneticsEngine.getGenetics(entity).parent1Uuid();
    }

    public static Optional<UUID> getParent2Uuid(LivingEntity entity) {
        if (entity == null) {
            return Optional.empty();
        }
        return GeneticsEngine.getGenetics(entity).parent2Uuid();
    }

    public static void inherit(LivingEntity baby, LivingEntity parent1, LivingEntity parent2, String variantKey) {
        if (baby == null || parent1 == null || parent2 == null) {
            return;
        }
        GeneticsEngine.inheritGenetics(baby, parent1, parent2, variantKey);
    }

    public static void rollStats(LivingEntity entity, String variantKey) {
        if (entity == null) {
            return;
        }
        GeneticsEngine.rollInitialStats(entity, variantKey);
    }

    // ========== Size-Stats & Scale API ==========

    public static float getScale(LivingEntity entity) {
        if (entity == null) {
            return 1.0f;
        }
        float scale = getTrait(entity, "scale", 1.0f);
        if (Float.isNaN(scale) || Float.isInfinite(scale)) {
            return 1.0f;
        }
        return Math.clamp(scale, 0.5f, 2.0f);
    }

    public static void setScale(LivingEntity entity, float scale) {
        if (entity == null) {
            return;
        }
        float clampedScale = Float.isNaN(scale) || Float.isInfinite(scale) ? 1.0f : Math.clamp(scale, 0.5f, 2.0f);
        EntityGenetics old = GeneticsEngine.getGenetics(entity);
        java.util.Map<String, Float> newTraits = new java.util.HashMap<>(old.traits());
        newTraits.put("scale", clampedScale);
        GeneticsEngine.setGenetics(entity, new EntityGenetics(
                old.parent1Uuid(),
                old.parent2Uuid(),
                old.inbred(),
                old.traitsRolled(),
                newTraits
        ));
        GeneticsEngine.applyGeneticsModifiers(entity);
    }

    public static boolean isRunt(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        return getScale(entity) < 0.85f || isInbred(entity);
    }

    public static boolean isGiant(LivingEntity entity) {
        if (entity == null) {
            return false;
        }
        return getScale(entity) > 1.15f;
    }

    // ========== Kinship & Pedigree API ==========

    public static boolean isRelated(LivingEntity animal1, LivingEntity animal2) {
        if (animal1 == null || animal2 == null) {
            return false;
        }
        EntityGenetics d1 = GeneticsEngine.getGenetics(animal1);
        EntityGenetics d2 = GeneticsEngine.getGenetics(animal2);
        return GeneticsEngine.checkInbreeding(animal1, animal2, d1, d2);
    }

    public static boolean isParentOf(LivingEntity parent, LivingEntity offspring) {
        if (parent == null || offspring == null) {
            return false;
        }
        UUID parentUuid = parent.getUUID();
        return getParent1Uuid(offspring).map(parentUuid::equals).orElse(false)
                || getParent2Uuid(offspring).map(parentUuid::equals).orElse(false);
    }

    public static boolean areSiblings(LivingEntity animal1, LivingEntity animal2) {
        if (animal1 == null || animal2 == null || animal1.equals(animal2)) {
            return false;
        }
        UUID p1_1 = getParent1Uuid(animal1).orElse(null);
        UUID p1_2 = getParent2Uuid(animal1).orElse(null);
        UUID p2_1 = getParent1Uuid(animal2).orElse(null);
        UUID p2_2 = getParent2Uuid(animal2).orElse(null);

        if (p1_1 != null && (p1_1.equals(p2_1) || p1_1.equals(p2_2))) {
            return true;
        }
        if (p1_2 != null && (p1_2.equals(p2_1) || p1_2.equals(p2_2))) {
            return true;
        }
        return false;
    }

    public static int predictInbreedingRiskPercent(LivingEntity parent1, LivingEntity parent2) {
        if (parent1 == null || parent2 == null) {
            return 0;
        }
        if (isParentOf(parent1, parent2) || isParentOf(parent2, parent1)) {
            return 100;
        }
        UUID p1_1 = getParent1Uuid(parent1).orElse(null);
        UUID p1_2 = getParent2Uuid(parent1).orElse(null);
        UUID p2_1 = getParent1Uuid(parent2).orElse(null);
        UUID p2_2 = getParent2Uuid(parent2).orElse(null);

        if (p1_1 == null && p1_2 == null && p2_1 == null && p2_2 == null) {
            return 0;
        }

        boolean match1 = p1_1 != null && (p1_1.equals(p2_1) || p1_1.equals(p2_2));
        boolean match2 = p1_2 != null && (p1_2.equals(p2_1) || p1_2.equals(p2_2));

        if (match1 && match2) {
            return 100;
        } else if (match1 || match2) {
            return 50;
        }
        return 0;
    }

    // ========== Dynamic Trait Modifiers & Stat Reset API ==========

    public static void setTrait(LivingEntity entity, String traitId, float value) {
        if (entity == null || traitId == null) {
            return;
        }
        EntityGenetics old = GeneticsEngine.getGenetics(entity);
        java.util.Map<String, Float> newTraits = new java.util.HashMap<>(old.traits());
        newTraits.put(traitId, value);
        GeneticsEngine.setGenetics(entity, new EntityGenetics(
                old.parent1Uuid(),
                old.parent2Uuid(),
                old.inbred(),
                true,
                newTraits
        ));
        GeneticsEngine.applyGeneticsModifiers(entity);
    }

    public static void modifyTrait(LivingEntity entity, String traitId, float delta) {
        if (entity == null || traitId == null) {
            return;
        }
        float current = getTrait(entity, traitId, 0.0f);
        setTrait(entity, traitId, current + delta);
    }

    public static void resetGenetics(LivingEntity entity) {
        if (entity == null) {
            return;
        }
        GeneticsEngine.setGenetics(entity, EntityGenetics.DEFAULT);

        GeneticsConfig config = EntityGeneticsRegistry.getConfig(entity.getType());
        if (config != null) {
            for (Map.Entry<String, TraitConfig> entry : config.traits().entrySet()) {
                String traitId = entry.getKey();
                TraitConfig trait = entry.getValue();
                if (trait.attributeId().isEmpty()) continue;

                var attributeTypeOpt = BuiltInRegistries.ATTRIBUTE.get(Identifier.parse(trait.attributeId()));
                if (attributeTypeOpt.isEmpty()) continue;

                var attribute = entity.getAttribute(attributeTypeOpt.get());
                if (attribute != null) {
                    Identifier modifierId = Identifier.fromNamespaceAndPath(net.dasik.social.DasikLibraryMod.MOD_ID, "genetics_" + traitId);
                    attribute.removeModifier(modifierId);
                }
            }
        }
    }
}
