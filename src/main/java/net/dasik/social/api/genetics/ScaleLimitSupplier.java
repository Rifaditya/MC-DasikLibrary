// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: LivingEntity.java (26.2+)
package net.dasik.social.api.genetics;

import net.minecraft.world.entity.LivingEntity;

/**
 * Functional interface to retrieve dynamic min/max limits for genetics traits.
 *
 * <p>Allows limits to be fetched dynamically at runtime from configs or GameRules
 * instead of being static values.</p>
 */
@FunctionalInterface
public interface ScaleLimitSupplier {
    /**
     * Gets the limit value.
     *
     * @param entity       The entity whose trait limit is being computed.
     * @param defaultValue The default limit value from the static trait configuration.
     * @return The computed limit value.
     */
    float getLimit(LivingEntity entity, float defaultValue);
}
