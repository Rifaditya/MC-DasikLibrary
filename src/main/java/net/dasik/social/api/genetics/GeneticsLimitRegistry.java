/*
 * Zenith Sovereign Engineering - Dasik Library
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
// Verified against: EntityType.java (26.2+)
package net.dasik.social.api.genetics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

/**
 * Thread-safe static registry to associate dynamic min/max scale limit suppliers
 * with specific EntityTypes and traits.
 */
public class GeneticsLimitRegistry {
    private static final Map<EntityType<?>, Map<String, ScaleLimitSupplier>> MIN_LIMITS = new ConcurrentHashMap<>();
    private static final Map<EntityType<?>, Map<String, ScaleLimitSupplier>> MAX_LIMITS = new ConcurrentHashMap<>();

    /**
     * Registers a minimum limit supplier for a specific entity type and trait.
     *
     * @param type     The entity type.
     * @param traitId  The trait ID (e.g., "size").
     * @param supplier The dynamic limit supplier.
     */
    public static void registerMin(EntityType<?> type, String traitId, ScaleLimitSupplier supplier) {
        MIN_LIMITS.computeIfAbsent(type, k -> new ConcurrentHashMap<>()).put(traitId, supplier);
    }

    /**
     * Registers a maximum limit supplier for a specific entity type and trait.
     *
     * @param type     The entity type.
     * @param traitId  The trait ID (e.g., "size").
     * @param supplier The dynamic limit supplier.
     */
    public static void registerMax(EntityType<?> type, String traitId, ScaleLimitSupplier supplier) {
        MAX_LIMITS.computeIfAbsent(type, k -> new ConcurrentHashMap<>()).put(traitId, supplier);
    }

    /**
     * Computes the dynamic minimum limit for a trait on a living entity.
     *
     * @param entity       The living entity.
     * @param traitId      The trait ID.
     * @param defaultValue The default fallback value.
     * @return The computed minimum limit.
     */
    public static float getMin(@NotNull LivingEntity entity, String traitId, float defaultValue) {
        Map<String, ScaleLimitSupplier> map = MIN_LIMITS.get(entity.getType());
        if (map == null) return defaultValue;
        ScaleLimitSupplier supplier = map.get(traitId);
        return supplier != null ? supplier.getLimit(entity, defaultValue) : defaultValue;
    }

    /**
     * Computes the dynamic maximum limit for a trait on a living entity.
     *
     * @param entity       The living entity.
     * @param traitId      The trait ID.
     * @param defaultValue The default fallback value.
     * @return The computed maximum limit.
     */
    public static float getMax(@NotNull LivingEntity entity, String traitId, float defaultValue) {
        Map<String, ScaleLimitSupplier> map = MAX_LIMITS.get(entity.getType());
        if (map == null) return defaultValue;
        ScaleLimitSupplier supplier = map.get(traitId);
        return supplier != null ? supplier.getLimit(entity, defaultValue) : defaultValue;
    }
}
