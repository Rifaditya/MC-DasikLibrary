// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UniversalCommandRegistryTest {

    @BeforeAll
    public static void setUpAll() {
        try {
            SharedConstants.tryDetectVersion();
            Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
    }

    @Test
    @DisplayName("Pillar 3 Brigadier AST: Validate standard command suite tree structure and subcommands")
    void testStandardSuiteTreeStructure() {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("speed", null, 10, 1, 100);
        bridge.registerBoolean("debug", null, false);

        UniversalCommandRegistry.builder("testmod", dispatcher)
                .withModName("Test Mod")
                .withBridge(bridge)
                .withCustomSubcommand(Commands.literal("custom"))
                .register();

        CommandNode<CommandSourceStack> root = dispatcher.getRoot().getChild("testmod");
        assertNotNull(root, "Root command 'testmod' must be registered");
        assertInstanceOf(LiteralCommandNode.class, root);

        // Verify standard subcommands exist in the AST
        assertNotNull(root.getChild("help"), "help subcommand must exist");
        assertNotNull(root.getChild("status"), "status subcommand must exist");
        assertNotNull(root.getChild("get"), "get subcommand must exist");
        assertNotNull(root.getChild("set"), "set subcommand must exist");
        assertNotNull(root.getChild("reset"), "reset subcommand must exist");
        assertNotNull(root.getChild("reload"), "reload subcommand must exist");
        assertNotNull(root.getChild("custom"), "custom subcommand must exist");

        // Verify 'get' requires a key argument
        CommandNode<CommandSourceStack> getNode = root.getChild("get");
        assertNotNull(getNode.getChild("key"), "get subcommand must require 'key' argument");

        // Verify 'set' requires key and value arguments
        CommandNode<CommandSourceStack> setNode = root.getChild("set");
        CommandNode<CommandSourceStack> setKey = setNode.getChild("key");
        assertNotNull(setKey, "set subcommand must require 'key' argument");
        assertNotNull(setKey.getChild("value"), "set key node must require 'value' argument");
    }

    @Test
    @DisplayName("Verify custom root literal and mod name configuration")
    void testCustomRootLiteral() {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        UniversalCommandRegistry.builder("advancedmod", dispatcher)
                .withRootLiteral("adv")
                .withModName("Advanced Mod")
                .register();

        assertNull(dispatcher.getRoot().getChild("advancedmod"), "Original modId should be overridden by custom root literal");
        assertNotNull(dispatcher.getRoot().getChild("adv"), "Custom root literal 'adv' must be registered");
    }

    @Test
    @DisplayName("Verify build() returns valid LiteralArgumentBuilder without direct dispatch")
    void testBuildWithoutDispatch() {
        UniversalCommandRegistry.Builder builder = UniversalCommandRegistry.builder("deferredmod")
                .withModName("Deferred Mod");

        LiteralArgumentBuilder<CommandSourceStack> tree = builder.build();
        assertNotNull(tree);
        assertEquals("deferredmod", tree.getLiteral());

        // Register to dispatcher later
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        builder.register(dispatcher);
        assertNotNull(dispatcher.getRoot().getChild("deferredmod"));
    }
}
