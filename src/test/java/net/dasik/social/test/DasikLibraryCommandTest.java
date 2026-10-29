// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.test;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.ArrayList;
import java.util.List;
import net.dasik.social.api.command.GameRuleCommandBridge;
import net.dasik.social.api.command.UniversalCommandRegistry;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DasikLibraryCommandTest {

    @BeforeAll
    public static void setUpAll() {
        try {
            SharedConstants.tryDetectVersion();
            Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
    }

    private static class RecordingCommandSource implements CommandSource {
        final List<Component> messages = new ArrayList<>();

        @Override
        public void sendSystemMessage(Component message) {
            this.messages.add(message);
        }

        @Override
        public boolean acceptsSuccess() {
            return true;
        }

        @Override
        public boolean acceptsFailure() {
            return true;
        }

        @Override
        public boolean shouldInformAdmins() {
            return false;
        }
    }

    private CommandSourceStack createSource(boolean isOp, RecordingCommandSource recordingSource) {
        CommandSource source = recordingSource != null ? recordingSource : CommandSource.NULL;
        PermissionSet permissions = isOp ? PermissionSet.ALL_PERMISSIONS : PermissionSet.NO_PERMISSIONS;
        return new CommandSourceStack(
                source,
                Vec3.ZERO,
                Vec2.ZERO,
                null,
                permissions,
                isOp ? "ServerAdmin" : "PlayerUser",
                Component.literal(isOp ? "ServerAdmin" : "PlayerUser"),
                null,
                null
        );
    }

    private CommandDispatcher<CommandSourceStack> createTestDispatcher(GameRuleCommandBridge bridge) {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        UniversalCommandRegistry.builder("dasiklib", dispatcher)
                .withModName("Dasik Library")
                .withBridge(bridge)
                .withStatusSupplier(level -> List.of("Flock Radius: " + bridge.getValue(level, "flock_radius"), "Mode: " + bridge.getValue(level, "mode")))
                .withReloadAction(server -> {})
                .register();
        return dispatcher;
    }

    @Test
    @DisplayName("Pillar 3 Brigadier AST: Validate standard suite tree hierarchy and node structure")
    void testStandardSuiteTreeStructure() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("flock_radius", null, 32, 1, 128);
        bridge.registerBoolean("enable_social", null, true);
        bridge.registerString("mode", null, "standard");

        CommandDispatcher<CommandSourceStack> dispatcher = createTestDispatcher(bridge);
        CommandNode<CommandSourceStack> root = dispatcher.getRoot().getChild("dasiklib");

        assertNotNull(root, "Root node 'dasiklib' must be registered in the dispatcher");
        assertInstanceOf(LiteralCommandNode.class, root);

        // Subcommands verification
        CommandNode<CommandSourceStack> helpNode = root.getChild("help");
        CommandNode<CommandSourceStack> statusNode = root.getChild("status");
        CommandNode<CommandSourceStack> getNode = root.getChild("get");
        CommandNode<CommandSourceStack> setNode = root.getChild("set");
        CommandNode<CommandSourceStack> resetNode = root.getChild("reset");
        CommandNode<CommandSourceStack> reloadNode = root.getChild("reload");

        assertNotNull(helpNode, "help subcommand must exist");
        assertNotNull(statusNode, "status subcommand must exist");
        assertNotNull(getNode, "get subcommand must exist");
        assertNotNull(setNode, "set subcommand must exist");
        assertNotNull(resetNode, "reset subcommand must exist");
        assertNotNull(reloadNode, "reload subcommand must exist");

        // Validate argument hierarchy
        CommandNode<CommandSourceStack> getKey = getNode.getChild("key");
        assertNotNull(getKey, "get subcommand must have 'key' argument");

        CommandNode<CommandSourceStack> setKey = setNode.getChild("key");
        assertNotNull(setKey, "set subcommand must have 'key' argument");
        CommandNode<CommandSourceStack> setValue = setKey.getChild("value");
        assertNotNull(setValue, "set subcommand key must have 'value' argument");
    }

    @Test
    @DisplayName("Pillar 3 Brigadier AST: Permission gating blocks non-OP sources from mutating commands")
    void testPermissionGatingAST() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("flock_radius", null, 32, 1, 128);
        bridge.registerBoolean("enable_social", null, true);

        CommandDispatcher<CommandSourceStack> dispatcher = createTestDispatcher(bridge);
        CommandNode<CommandSourceStack> root = dispatcher.getRoot().getChild("dasiklib");

        CommandSourceStack nonOp = createSource(false, null);
        CommandSourceStack op = createSource(true, null);

        // Non-OP queries permitted
        assertTrue(root.getChild("help").canUse(nonOp), "Non-OP must be able to use help");
        assertTrue(root.getChild("status").canUse(nonOp), "Non-OP must be able to use status");
        assertTrue(root.getChild("get").canUse(nonOp), "Non-OP must be able to use get");

        // Non-OP mutations rejected
        assertFalse(root.getChild("set").canUse(nonOp), "Non-OP must be rejected from set");
        assertFalse(root.getChild("reset").canUse(nonOp), "Non-OP must be rejected from reset");
        assertFalse(root.getChild("reload").canUse(nonOp), "Non-OP must be rejected from reload");

        // OP mutations permitted
        assertTrue(root.getChild("set").canUse(op), "OP source must be permitted on set");
        assertTrue(root.getChild("reset").canUse(op), "OP source must be permitted on reset");
        assertTrue(root.getChild("reload").canUse(op), "OP source must be permitted on reload");
    }

    @Test
    @DisplayName("Pillar 3 Brigadier AST: Tab completion suggestions for root subcommands")
    void testRootTabCompletionSuggestions() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("flock_radius", null, 32, 1, 128);

        CommandDispatcher<CommandSourceStack> dispatcher = createTestDispatcher(bridge);
        CommandSourceStack source = createSource(true, null);

        ParseResults<CommandSourceStack> parse = dispatcher.parse("dasiklib ", source);
        Suggestions suggestions = dispatcher.getCompletionSuggestions(parse).join();
        List<String> texts = suggestions.getList().stream().map(Suggestion::getText).toList();

        assertTrue(texts.contains("help"), "Suggestions must contain help");
        assertTrue(texts.contains("status"), "Suggestions must contain status");
        assertTrue(texts.contains("get"), "Suggestions must contain get");
        assertTrue(texts.contains("set"), "Suggestions must contain set");
        assertTrue(texts.contains("reset"), "Suggestions must contain reset");
        assertTrue(texts.contains("reload"), "Suggestions must contain reload");
    }

    @Test
    @DisplayName("Pillar 3 Brigadier AST: Tab completion suggestions for keys and values")
    void testKeyAndValueTabCompletionSuggestions() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("flock_radius", null, 32, 1, 128);
        bridge.registerBoolean("enable_social", null, true);
        bridge.registerString("mode", null, "standard");

        CommandDispatcher<CommandSourceStack> dispatcher = createTestDispatcher(bridge);
        CommandSourceStack nonOp = createSource(false, null);
        CommandSourceStack op = createSource(true, null);

        // Suggest keys on 'get '
        ParseResults<CommandSourceStack> getParse = dispatcher.parse("dasiklib get ", nonOp);
        Suggestions getSuggestions = dispatcher.getCompletionSuggestions(getParse).join();
        List<String> getKeys = getSuggestions.getList().stream().map(Suggestion::getText).toList();

        assertTrue(getKeys.contains("flock_radius"), "get suggestions must include flock_radius");
        assertTrue(getKeys.contains("enable_social"), "get suggestions must include enable_social");
        assertTrue(getKeys.contains("mode"), "get suggestions must include mode");

        // Suggest keys on 'set '
        ParseResults<CommandSourceStack> setParse = dispatcher.parse("dasiklib set ", op);
        Suggestions setSuggestions = dispatcher.getCompletionSuggestions(setParse).join();
        List<String> setKeys = setSuggestions.getList().stream().map(Suggestion::getText).toList();

        assertTrue(setKeys.contains("flock_radius"));
        assertTrue(setKeys.contains("enable_social"));
        assertTrue(setKeys.contains("mode"));

        // Suggest boolean values on 'set enable_social '
        ParseResults<CommandSourceStack> boolValParse = dispatcher.parse("dasiklib set enable_social ", op);
        Suggestions boolValSuggestions = dispatcher.getCompletionSuggestions(boolValParse).join();
        List<String> boolVals = boolValSuggestions.getList().stream().map(Suggestion::getText).toList();

        assertTrue(boolVals.contains("true"), "boolean suggestions must include 'true'");
        assertTrue(boolVals.contains("false"), "boolean suggestions must include 'false'");

        // Suggest default integer value on 'set flock_radius '
        ParseResults<CommandSourceStack> intValParse = dispatcher.parse("dasiklib set flock_radius ", op);
        Suggestions intValSuggestions = dispatcher.getCompletionSuggestions(intValParse).join();
        List<String> intVals = intValSuggestions.getList().stream().map(Suggestion::getText).toList();

        assertTrue(intVals.contains("32"), "integer suggestions must suggest default value '32'");
    }

    @Test
    @DisplayName("Pillar 3 Execution: Verify dispatch execution, mutation, and permission rejection")
    void testCommandExecutionAndFeedback() throws Exception {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("flock_radius", null, 32, 1, 128);
        bridge.registerBoolean("enable_social", null, true);

        RecordingCommandSource nonOpSink = new RecordingCommandSource();
        RecordingCommandSource opSink = new RecordingCommandSource();
        CommandSourceStack nonOp = createSource(false, nonOpSink);
        CommandSourceStack op = createSource(true, opSink);

        CommandDispatcher<CommandSourceStack> dispatcher = createTestDispatcher(bridge);

        // 1. Non-OP executes help
        int helpRes = dispatcher.execute("dasiklib help", nonOp);
        assertEquals(1, helpRes, "Help execution must return 1");
        assertFalse(nonOpSink.messages.isEmpty(), "Help output must be delivered to source");

        // 2. Non-OP executes get
        int getRes = dispatcher.execute("dasiklib get flock_radius", nonOp);
        assertEquals(1, getRes, "Get execution must return 1");
        boolean containsValue = nonOpSink.messages.stream().anyMatch(c -> c.getString().contains("32"));
        assertTrue(containsValue, "Get command response must output the current value 32");

        // 3. Non-OP attempts mutation: Rejected with CommandSyntaxException
        assertThrows(CommandSyntaxException.class, () -> {
            dispatcher.execute("dasiklib set flock_radius 64", nonOp);
        }, "Non-OP execution of set command must throw CommandSyntaxException");
        assertEquals(32, bridge.getValue(null, "flock_radius"), "Value must remain unchanged after rejected mutation");

        // 4. OP executes mutation
        int setRes = dispatcher.execute("dasiklib set flock_radius 64", op);
        assertEquals(1, setRes, "OP set execution must return 1");
        assertEquals(64, bridge.getValue(null, "flock_radius"), "Bridge value must be updated to 64");

        // 5. OP executes reset
        int resetRes = dispatcher.execute("dasiklib reset", op);
        assertEquals(1, resetRes, "Reset execution must return 1");
        assertEquals(32, bridge.getValue(null, "flock_radius"), "Reset must restore default value 32");
    }
}
