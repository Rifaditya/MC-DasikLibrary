/*
 * Dasik Library
 * Verified against: AttributeModifier.java (26.1.2+)
 */
package net.dasik.social.api.genetics;

import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public record TraitConfig(
    String traitId,
    String attributeId, // E.g., "minecraft:generic.max_health"
    String operation,   // E.g., "ADD_VALUE", "ADD_MULTIPLIED_TOTAL"
    float inbreedingPenaltyFlat,
    float inbreedingMultiplier,
    float minLimit,
    float maxLimit
) {
    public AttributeModifier.Operation getOperation() {
        try {
            return AttributeModifier.Operation.valueOf(operation);
        } catch (IllegalArgumentException e) {
            return AttributeModifier.Operation.ADD_VALUE;
        }
    }
}
