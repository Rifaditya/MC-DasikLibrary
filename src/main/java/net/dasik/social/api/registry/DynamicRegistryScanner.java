// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.registry;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

/**
 * Universal Dynamic Registry Scanner.
 * Allows mods to subscribe to any vanilla or custom registry (Items, EntityTypes, Blocks, etc.)
 * to discover modded additions and register dynamic GameRules or configurations.
 */
public class DynamicRegistryScanner {

    /**
     * Subscribes a consumer callback to a target registry.
     * 1. Scans all elements currently present in the registry.
     * 2. Listens for any elements added dynamically by other mods after registration.
     * 3. Runs a final safety sweep during ServerLifecycleEvents.SERVER_STARTING when all mods are guaranteed to be loaded.
     *
     * @param registry Target registry (e.g. BuiltInRegistries.ITEM, BuiltInRegistries.ENTITY_TYPE, BuiltInRegistries.BLOCK)
     * @param filter Predicate to filter relevant objects
     * @param consumer Callback receiving (Identifier id, T object)
     */
    public static <T> void subscribe(
            Registry<T> registry,
            Predicate<T> filter,
            BiConsumer<Identifier, T> consumer
    ) {
        Set<Identifier> processed = ConcurrentHashMap.newKeySet();

        // 1. Initial pass on existing elements
        scanRegistry(registry, filter, consumer, processed);

        // 2. Real-time listener for entries added during mod initialization
        try {
            RegistryEntryAddedCallback.event(registry).register((rawId, id, object) -> {
                if (filter.test(object) && processed.add(id)) {
                    consumer.accept(id, object);
                }
            });
        } catch (Throwable ignored) {}

        // 3. Final safety sweep on server starting (ensuring 100% mod coverage before commands/world load)
        try {
            ServerLifecycleEvents.SERVER_STARTING.register(server -> {
                scanRegistry(registry, filter, consumer, processed);
            });
        } catch (Throwable ignored) {}
    }

    private static <T> void scanRegistry(
            Registry<T> registry,
            Predicate<T> filter,
            BiConsumer<Identifier, T> consumer,
            Set<Identifier> processed
    ) {
        for (T object : registry) {
            Identifier id = registry.getKey(object);
            if (id != null && filter.test(object) && processed.add(id)) {
                consumer.accept(id, object);
            }
        }
    }
}
