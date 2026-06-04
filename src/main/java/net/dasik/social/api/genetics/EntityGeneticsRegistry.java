/*
 * Dasik Library
 * Verified against: EntityType.java (26.1.2+)
 */
package net.dasik.social.api.genetics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.Nullable;

public class EntityGeneticsRegistry {
    private static final Map<EntityType<?>, GeneticsConfig> CONFIGS = new ConcurrentHashMap<>();

    public static void register(EntityType<?> entityType, GeneticsConfig config) {
        CONFIGS.put(entityType, config);
    }

    @Nullable
    public static GeneticsConfig getConfig(EntityType<?> entityType) {
        return CONFIGS.get(entityType);
    }
}
