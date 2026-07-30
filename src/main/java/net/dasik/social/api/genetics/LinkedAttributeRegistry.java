/*
 * Zenith Sovereign Engineering - Dasik Library
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
// Verified against: EntityType.java (26.2+)
package net.dasik.social.api.genetics;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.EntityType;
import org.jetbrains.annotations.NotNull;

/**
 * Static registry that maps {@code EntityType + traitId} to a list of linked attributes.
 *
 * <p>This registry is separate from {@link TraitConfig} to preserve backward compatibility.
 * Existing mods that construct {@code TraitConfig} directly are unaffected.</p>
 *
 * <p>Linked attributes are automatically applied by {@link GeneticsEngine#applyGeneticsModifiers}
 * when a trait value is present on an entity.</p>
 */
public class LinkedAttributeRegistry {
    // EntityType -> (traitId -> List<LinkedAttribute>)
    private static final Map<EntityType<?>, Map<String, List<LinkedAttribute>>> LINKS = new ConcurrentHashMap<>();

    /**
     * Registers a list of linked attributes for a specific entity type and trait.
     *
     * @param type    The entity type.
     * @param traitId The trait ID (e.g., "size").
     * @param links   The linked attributes to associate with this trait.
     */
    public static void register(EntityType<?> type, String traitId, List<LinkedAttribute> links) {
        LINKS.computeIfAbsent(type, k -> new ConcurrentHashMap<>()).put(traitId, List.copyOf(links));
    }

    /**
     * Returns the linked attributes for a specific entity type and trait.
     *
     * @param type    The entity type.
     * @param traitId The trait ID.
     * @return An unmodifiable list of linked attributes, or an empty list if none are registered.
     */
    @NotNull
    public static List<LinkedAttribute> getLinks(EntityType<?> type, String traitId) {
        Map<String, List<LinkedAttribute>> traitLinks = LINKS.get(type);
        if (traitLinks == null) return Collections.emptyList();
        return traitLinks.getOrDefault(traitId, Collections.emptyList());
    }
}
