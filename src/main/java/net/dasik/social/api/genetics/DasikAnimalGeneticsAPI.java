/*
 * Zenith Sovereign Engineering - Dasik Library
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
// Verified against: LivingEntity.java, GeneticsEngine.java (26.2+)
package net.dasik.social.api.genetics;

import java.util.Optional;
import java.util.UUID;
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
}
