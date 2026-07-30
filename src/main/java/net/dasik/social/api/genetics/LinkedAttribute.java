/*
 * Zenith Sovereign Engineering - Dasik Library
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
// Verified against: Attributes.java (26.2+)
package net.dasik.social.api.genetics;

/**
 * Describes a single link between a rolled trait (e.g., "size") and a target attribute.
 *
 * <p>For example, linking size to {@code minecraft:max_health} with
 * {@link LinkMode#PROPORTIONAL} and strength {@code 1.0f} means that a
 * 20% larger entity gets 20% more health.</p>
 *
 * @param attributeId The fully qualified attribute ID (e.g., {@code "minecraft:max_health"}).
 * @param mode        How the trait value maps to the attribute modifier.
 * @param strength    Multiplier controlling the effect intensity. 1.0 = full effect, 0.5 = half.
 */
public record LinkedAttribute(
    String attributeId,
    LinkMode mode,
    float strength
) {
    /**
     * Computes the attribute modifier value for a given trait value.
     *
     * <p>The trait value is expected to be a scale factor (e.g., 1.0 = normal, 1.2 = 20% bigger).
     * The modifier is computed as a multiplicative delta from 1.0:</p>
     *
     * <ul>
     *   <li>{@link LinkMode#PROPORTIONAL}: {@code (traitValue - 1.0) * strength}</li>
     *   <li>{@link LinkMode#INVERSE}: {@code -(traitValue - 1.0) * strength}</li>
     *   <li>{@link LinkMode#SQUARED}: {@code (traitValue² - 1.0) * strength}</li>
     * </ul>
     *
     * @param traitValue The rolled trait value (e.g., 1.2 for a 20% larger entity).
     * @return The modifier value to apply via {@code ADD_MULTIPLIED_TOTAL}.
     */
    public float computeModifier(float traitValue) {
        float delta = switch (mode) {
            case PROPORTIONAL -> (traitValue - 1.0f) * strength;
            case INVERSE -> -(traitValue - 1.0f) * strength;
            case SQUARED -> (traitValue * traitValue - 1.0f) * strength;
        };
        return delta;
    }
}
