/*
 * Dasik Library
 * Verified against: EntityType.java (26.2+)
 */
package net.dasik.social.api.genetics;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.EntityType;

public class GeneticsLootRegistry {
    private static final Map<EntityType<?>, GeneticsLootModifier> MODIFIERS = new ConcurrentHashMap<>();

    /**
     * Registers a genetics-based loot modifier for a specific entity type.
     *
     * @param type     The entity type.
     * @param modifier The loot modifier.
     */
    public static void register(EntityType<?> type, GeneticsLootModifier modifier) {
        MODIFIERS.put(type, modifier);
    }

    /**
     * Gets the registered genetics-based loot modifier for an entity type.
     *
     * @param type The entity type.
     * @return The registered modifier, or null if none is registered.
     */
    public static GeneticsLootModifier get(EntityType<?> type) {
        return MODIFIERS.get(type);
    }
}
