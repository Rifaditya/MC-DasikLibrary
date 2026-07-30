/*
 * Zenith Sovereign Engineering - Dasik Library
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
// Verified against: Attributes.java, AttributeModifier.java (26.2+)
package net.dasik.social.api.genetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.world.entity.EntityType;

/**
 * Builder for registering size-linked attribute systems on entity types.
 *
 * <p>Usage example:</p>
 * <pre>{@code
 * SizeStatsBuilder.create(EntityTypes.COW)
 *     .scaleRange(0.8f, 1.3f)
 *     .mutation("triangular", 1.0f, 0.15f)
 *     .linkAttribute("minecraft:max_health", LinkMode.PROPORTIONAL, 1.0f)
 *     .linkAttribute("minecraft:movement_speed", LinkMode.INVERSE, 0.3f)
 *     .linkAttribute("minecraft:attack_damage", LinkMode.PROPORTIONAL, 0.5f)
 *     .inbreedingSizePenalty(0.05f)
 *     .register();
 * }</pre>
 *
 * <p>This registers a {@code "size"} trait in the {@link EntityGeneticsRegistry} that maps
 * to the vanilla {@code minecraft:scale} attribute, plus any number of linked attributes
 * that scale proportionally, inversely, or quadratically with the entity's size.</p>
 *
 * <p>Existing traits registered via direct {@link TraitConfig} constructors are preserved.
 * The builder merges the new {@code "size"} trait alongside any pre-existing traits.</p>
 */
public class SizeStatsBuilder {
    private static final String SIZE_TRAIT_ID = "size";
    private static final String SCALE_ATTRIBUTE = "minecraft:scale";

    private final EntityType<?> entityType;

    // Scale range (maps to minecraft:scale attribute limits)
    private float minScale = 0.8f;
    private float maxScale = 1.3f;

    // Mutation rule for initial roll
    private String mutationType = "triangular";
    private float mutationCenter = 1.0f;
    private float mutationSpread = 0.15f;

    // Inbreeding penalty
    private float inbreedingPenaltyFlat = 0.05f;
    private float inbreedingMultiplier = 0.9f;

    // Linked attributes (size -> other stats)
    private final List<LinkedAttribute> linkedAttributes = new ArrayList<>();

    // Personality-specific mutation overrides
    private final Map<String, MutationRule> personalityOverrides = new HashMap<>();

    private SizeStatsBuilder(EntityType<?> entityType) {
        this.entityType = entityType;
    }

    /**
     * Creates a new SizeStatsBuilder for the given entity type.
     *
     * @param entityType The entity type to register size stats for.
     * @return A new builder instance.
     */
    public static SizeStatsBuilder create(EntityType<?> entityType) {
        return new SizeStatsBuilder(entityType);
    }

    /**
     * Sets the visual scale range for the entity. Maps to the vanilla
     * {@code minecraft:scale} attribute (default 1.0, range 0.0625–16.0).
     *
     * @param minScale Minimum scale factor (e.g., 0.8 for 80% of vanilla size).
     * @param maxScale Maximum scale factor (e.g., 1.3 for 130% of vanilla size).
     * @return This builder.
     */
    public SizeStatsBuilder scaleRange(float minScale, float maxScale) {
        this.minScale = minScale;
        this.maxScale = maxScale;
        GeneticsLimitRegistry.registerMin(entityType, SIZE_TRAIT_ID, (entity, def) -> minScale);
        GeneticsLimitRegistry.registerMax(entityType, SIZE_TRAIT_ID, (entity, def) -> maxScale);
        return this;
    }

    /**
     * Sets dynamic scale range suppliers for the entity.
     *
     * @param minSupplier Dynamic supplier for the minimum scale limit.
     * @param maxSupplier Dynamic supplier for the maximum scale limit.
     * @return This builder.
     */
    public SizeStatsBuilder scaleRange(ScaleLimitSupplier minSupplier, ScaleLimitSupplier maxSupplier) {
        GeneticsLimitRegistry.registerMin(entityType, SIZE_TRAIT_ID, minSupplier);
        GeneticsLimitRegistry.registerMax(entityType, SIZE_TRAIT_ID, maxSupplier);
        return this;
    }

    /**
     * Automatically registers namespaced GameRules for size limits of this entity type,
     * and maps them as dynamic scale range limit suppliers.
     *
     * <p>Key format: {@code <modId>:bd_<entity_name>_min_scale_percent} and
     * {@code <modId>:bd_<entity_name>_max_scale_percent}.</p>
     *
     * @param modId             The mod ID registering the rules.
     * @param category          The GameRule category.
     * @param defaultMinPercent Default minimum scale percentage (e.g. 70).
     * @param defaultMaxPercent Default maximum scale percentage (e.g. 145).
     * @return This builder.
     */
    public SizeStatsBuilder registerGameRules(String modId, net.minecraft.world.level.gamerules.GameRuleCategory category, int defaultMinPercent, int defaultMaxPercent) {
        String rawName = entityType.getDescriptionId();
        String entityName = rawName.substring(rawName.lastIndexOf('.') + 1);
        String minKey = modId + ":bd_" + entityName + "_min_scale_percent";
        String maxKey = modId + ":bd_" + entityName + "_max_scale_percent";

        // Register the GameRules dynamically
        net.dasik.social.api.gamerule.DynamicGameRuleManager.integerRule(minKey, category, defaultMinPercent).min(1).max(1600).register();
        net.dasik.social.api.gamerule.DynamicGameRuleManager.integerRule(maxKey, category, defaultMaxPercent).min(1).max(1600).register();

        // Bind the GameRules to the genetics limits registry
        return scaleRange(
            (entity, def) -> {
                net.minecraft.world.level.gamerules.GameRule<Integer> rule = net.dasik.social.api.gamerule.DynamicGameRuleManager.getIntRule(minKey);
                return rule != null ? net.dasik.social.api.gamerule.DynamicGameRuleManager.getInt(entity.level(), rule) / 100.0f : def;
            },
            (entity, def) -> {
                net.minecraft.world.level.gamerules.GameRule<Integer> rule = net.dasik.social.api.gamerule.DynamicGameRuleManager.getIntRule(maxKey);
                return rule != null ? net.dasik.social.api.gamerule.DynamicGameRuleManager.getInt(entity.level(), rule) / 100.0f : def;
            }
        );
    }

    /**
     * Sets the mutation rule for how initial size is rolled.
     *
     * @param type   Mutation type: "triangular", "uniform", or "constant".
     * @param center The center value (for triangular) or min (for uniform).
     * @param spread The spread (for triangular) or max (for uniform).
     * @return This builder.
     */
    public SizeStatsBuilder mutation(String type, float center, float spread) {
        this.mutationType = type;
        this.mutationCenter = center;
        this.mutationSpread = spread;
        return this;
    }

    /**
     * Links the entity's size to another attribute.
     *
     * @param attributeId The target attribute (e.g., "minecraft:max_health").
     * @param mode        How size maps to the attribute ({@link LinkMode}).
     * @param strength    Multiplier (1.0 = full effect, 0.5 = half effect).
     * @return This builder.
     */
    public SizeStatsBuilder linkAttribute(String attributeId, LinkMode mode, float strength) {
        this.linkedAttributes.add(new LinkedAttribute(attributeId, mode, strength));
        return this;
    }

    /**
     * Sets the inbreeding penalty for size.
     *
     * @param penalty    Flat penalty subtracted from inbred entities' size.
     * @param multiplier Multiplier applied to inbred entities' size (e.g., 0.9 = 10% smaller).
     * @return This builder.
     */
    public SizeStatsBuilder inbreedingPenalty(float penalty, float multiplier) {
        this.inbreedingPenaltyFlat = penalty;
        this.inbreedingMultiplier = multiplier;
        return this;
    }

    /**
     * Convenience method: sets only the flat inbreeding penalty.
     * The multiplier defaults to 0.9 (10% size reduction).
     *
     * @param penalty Flat penalty subtracted from inbred entities' size.
     * @return This builder.
     */
    public SizeStatsBuilder inbreedingSizePenalty(float penalty) {
        this.inbreedingPenaltyFlat = penalty;
        return this;
    }

    /**
     * Adds a personality-specific mutation override for the size trait.
     *
     * @param personality The personality/variant key (e.g., "aggressive", "timid").
     * @param type        Mutation type: "triangular", "uniform", or "constant".
     * @param param1      First parameter (center or min).
     * @param param2      Second parameter (spread or max).
     * @return This builder.
     */
    public SizeStatsBuilder personalityOverride(String personality, String type, float param1, float param2) {
        this.personalityOverrides.put(personality.toLowerCase(java.util.Locale.ROOT), new MutationRule(type, param1, param2));
        return this;
    }

    /**
     * Registers the size trait and linked attributes.
     *
     * <p>This method:</p>
     * <ol>
     *   <li>Creates a {@link TraitConfig} for the "size" trait mapped to {@code minecraft:scale}</li>
     *   <li>Merges it with any existing traits in {@link EntityGeneticsRegistry}</li>
     *   <li>Registers linked attributes in {@link LinkedAttributeRegistry}</li>
     * </ol>
     */
    public void register() {
        // Build the size TraitConfig (maps to minecraft:scale via ADD_MULTIPLIED_TOTAL)
        TraitConfig sizeTraitConfig = new TraitConfig(
            SIZE_TRAIT_ID,
            SCALE_ATTRIBUTE,
            "ADD_MULTIPLIED_TOTAL",
            inbreedingPenaltyFlat,
            inbreedingMultiplier,
            minScale,
            maxScale
        );

        // Merge with existing traits if the entity already has a GeneticsConfig
        GeneticsConfig existing = EntityGeneticsRegistry.getConfig(entityType);
        Map<String, TraitConfig> mergedTraits = new HashMap<>();
        Map<String, Map<String, MutationRule>> mergedPersonality = new HashMap<>();

        if (existing != null) {
            mergedTraits.putAll(existing.traits());
            mergedPersonality.putAll(existing.personalityMutations());
        }

        // Add the size trait
        mergedTraits.put(SIZE_TRAIT_ID, sizeTraitConfig);

        // Add personality overrides for the size trait
        for (Map.Entry<String, MutationRule> entry : personalityOverrides.entrySet()) {
            mergedPersonality.computeIfAbsent(entry.getKey(), k -> new HashMap<>())
                .put(SIZE_TRAIT_ID, entry.getValue());
        }

        // Add default mutation rule for "normal" personality
        mergedPersonality.computeIfAbsent("normal", k -> new HashMap<>())
            .put(SIZE_TRAIT_ID, new MutationRule(mutationType, mutationCenter, mutationSpread));

        // Register the merged config
        EntityGeneticsRegistry.register(entityType, new GeneticsConfig(mergedTraits, mergedPersonality));

        // Register linked attributes separately
        if (!linkedAttributes.isEmpty()) {
            LinkedAttributeRegistry.register(entityType, SIZE_TRAIT_ID, linkedAttributes);
        }
    }
}
