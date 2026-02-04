package net.dasik.social.impl.size;

import net.dasik.social.api.size.IScaleProvider;
import net.dasik.social.api.size.ScaleRange;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SizeRegistry {
    private static final Map<EntityType<?>, IScaleProvider> PROVIDERS = new HashMap<>();

    /**
     * Registers a fixed scale range for an entity type.
     * The actual scale will be deterministically selected from this range based on
     * the entity's UUID.
     *
     * @param type  The entity type to register.
     * @param range The allowed scale range.
     */
    public static void register(EntityType<?> type, ScaleRange range) {
        register(type, (entity, original) -> {
            // Deterministic random based on UUID
            UUID uuid = entity.getUUID();
            long hash = uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits();
            // Normalize to 0.0 - 1.0 (using absolute value to avoid negatives)
            float randomFactor = (Math.abs(hash) % 10000) / 10000f;

            return range.min() + (randomFactor * (range.max() - range.min()));
        });
    }

    /**
     * Registers a custom scale provider for an entity type.
     *
     * @param type     The entity type to register.
     * @param provider The provider logic.
     */
    public static void register(EntityType<?> type, IScaleProvider provider) {
        PROVIDERS.put(type, provider);
    }

    /**
     * Gets the provider for a specific entity type, if currently registered.
     *
     * @param type The entity type.
     * @return The provider, or null if none registered.
     */
    @Nullable
    public static IScaleProvider getProvider(EntityType<?> type) {
        return PROVIDERS.get(type);
    }
}
