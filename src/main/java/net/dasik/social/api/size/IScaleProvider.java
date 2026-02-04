package net.dasik.social.api.size;

import net.minecraft.world.entity.Entity;

/**
 * Interface for providing dynamic scaling logic for an entity.
 */
@FunctionalInterface
public interface IScaleProvider {
    /**
     * persistent determines the scale factor for the given entity.
     *
     * @param entity        The entity to scale.
     * @param originalScale The entity's original base scale (usually 1.0).
     * @return The new scale multiplier.
     */
    float getScale(Entity entity, float originalScale);
}
