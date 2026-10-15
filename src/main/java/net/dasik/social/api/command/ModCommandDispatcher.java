// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Central registry and Fabric event listener for mod command suites.
 * Coordinates initialization with Fabric's CommandRegistrationCallback.
 */
@DasikApiStatus.Public
public final class ModCommandDispatcher {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModCommandDispatcher.class);
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean(false);

    private static final Map<String, Consumer<CommandDispatcher<CommandSourceStack>>> DISPATCH_CALLBACKS = new ConcurrentHashMap<>();
    private static final Map<String, Consumer<UniversalCommandRegistry.Builder>> SUITE_CONFIGURATORS = new ConcurrentHashMap<>();
    private static final List<LiteralArgumentBuilder<CommandSourceStack>> CUSTOM_COMMANDS = new CopyOnWriteArrayList<>();

    private ModCommandDispatcher() {
    }

    /**
     * Initializes the command dispatcher and hooks into Fabric's CommandRegistrationCallback.
     * Safe to invoke multiple times; subsequent calls are no-ops.
     */
    public static void init() {
        if (INITIALIZED.compareAndSet(false, true)) {
            CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
                dispatchAll(dispatcher, registryAccess, environment);
            });
            LOGGER.info("ModCommandDispatcher initialized and registered CommandRegistrationCallback.");
        }
    }

    /**
     * Registers a consumer that configures and builds a complete UniversalCommandRegistry suite for the mod.
     *
     * @param modId The mod identifier (used as default root command literal).
     * @param configurator Consumer providing fluent configuration for the UniversalCommandRegistry builder.
     */
    public static void registerSuite(String modId, Consumer<UniversalCommandRegistry.Builder> configurator) {
        Objects.requireNonNull(modId, "modId cannot be null");
        Objects.requireNonNull(configurator, "configurator cannot be null");
        SUITE_CONFIGURATORS.put(modId, configurator);
    }

    /**
     * Registers a custom command dispatch callback for a mod.
     *
     * @param modId The mod identifier.
     * @param callback Consumer accepting the CommandDispatcher.
     */
    public static void register(String modId, Consumer<CommandDispatcher<CommandSourceStack>> callback) {
        Objects.requireNonNull(modId, "modId cannot be null");
        Objects.requireNonNull(callback, "callback cannot be null");
        DISPATCH_CALLBACKS.put(modId, callback);
    }

    /**
     * Registers a standalone raw LiteralArgumentBuilder command tree.
     *
     * @param command The literal argument builder root node.
     */
    public static void registerCommand(LiteralArgumentBuilder<CommandSourceStack> command) {
        Objects.requireNonNull(command, "command cannot be null");
        CUSTOM_COMMANDS.add(command);
    }

    /**
     * Dispatches all registered mod commands, suites, and callbacks into the provided CommandDispatcher.
     */
    public static void dispatchAll(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registryAccess, Commands.CommandSelection environment) {
        Objects.requireNonNull(dispatcher, "dispatcher cannot be null");

        // 1. Dispatch custom commands
        for (LiteralArgumentBuilder<CommandSourceStack> custom : CUSTOM_COMMANDS) {
            try {
                dispatcher.register(custom);
            } catch (Exception e) {
                LOGGER.error("Failed to register custom command node '{}'", custom.getLiteral(), e);
            }
        }

        // 2. Dispatch command suites
        for (Map.Entry<String, Consumer<UniversalCommandRegistry.Builder>> entry : SUITE_CONFIGURATORS.entrySet()) {
            try {
                UniversalCommandRegistry.Builder builder = UniversalCommandRegistry.builder(entry.getKey(), dispatcher);
                entry.getValue().accept(builder);
                builder.register();
            } catch (Exception e) {
                LOGGER.error("Failed to register UniversalCommandRegistry suite for mod '{}'", entry.getKey(), e);
            }
        }

        // 3. Dispatch general callbacks
        for (Map.Entry<String, Consumer<CommandDispatcher<CommandSourceStack>>> entry : DISPATCH_CALLBACKS.entrySet()) {
            try {
                entry.getValue().accept(dispatcher);
            } catch (Exception e) {
                LOGGER.error("Failed to execute command dispatch callback for mod '{}'", entry.getKey(), e);
            }
        }
    }

    public static boolean isInitialized() {
        return INITIALIZED.get();
    }

    /**
     * Clears all registered callbacks and suites. Primarily intended for isolated unit testing.
     */
    public static void clearForTesting() {
        SUITE_CONFIGURATORS.clear();
        DISPATCH_CALLBACKS.clear();
        CUSTOM_COMMANDS.clear();
    }
}
