// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.LiteralCommandNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Universal fluent Brigadier tree builder constructing standard studio command suites
 * (/<root> help, status, get, set, reset, reload).
 */
@DasikApiStatus.Public
public final class UniversalCommandRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(UniversalCommandRegistry.class);

    private UniversalCommandRegistry() {
    }

    public static Builder builder(String modId, CommandDispatcher<CommandSourceStack> dispatcher) {
        return new Builder(modId, dispatcher);
    }

    public static Builder builder(String modId) {
        return new Builder(modId, null);
    }

    public static class Builder {
        private final String modId;
        private CommandDispatcher<CommandSourceStack> dispatcher;
        private String rootLiteral;
        private String modName;
        private GameRuleCommandBridge bridge;
        private Function<ServerLevel, List<String>> statusSupplier;
        private Consumer<MinecraftServer> reloadAction;
        private final List<LiteralArgumentBuilder<CommandSourceStack>> customSubcommands = new ArrayList<>();

        public Builder(String modId, CommandDispatcher<CommandSourceStack> dispatcher) {
            this.modId = Objects.requireNonNull(modId, "modId cannot be null");
            this.dispatcher = dispatcher;
            this.rootLiteral = modId.toLowerCase(Locale.ROOT);
            this.modName = formatDefaultModName(modId);
        }

        public Builder withDispatcher(CommandDispatcher<CommandSourceStack> dispatcher) {
            this.dispatcher = dispatcher;
            return this;
        }

        public Builder withRootLiteral(String rootLiteral) {
            this.rootLiteral = Objects.requireNonNull(rootLiteral, "rootLiteral cannot be null").toLowerCase(Locale.ROOT);
            return this;
        }

        public Builder withModName(String modName) {
            this.modName = Objects.requireNonNull(modName, "modName cannot be null");
            return this;
        }

        public Builder withBridge(GameRuleCommandBridge bridge) {
            this.bridge = bridge;
            return this;
        }

        public Builder withStatusSupplier(Function<ServerLevel, List<String>> statusSupplier) {
            this.statusSupplier = statusSupplier;
            return this;
        }

        public Builder withReloadAction(Consumer<MinecraftServer> reloadAction) {
            this.reloadAction = reloadAction;
            return this;
        }

        public Builder withCustomSubcommand(LiteralArgumentBuilder<CommandSourceStack> subcmd) {
            if (subcmd != null) {
                this.customSubcommands.add(subcmd);
            }
            return this;
        }

        public LiteralArgumentBuilder<CommandSourceStack> build() {
            LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal(rootLiteral)
                    .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                    .executes(this::executeRoot);

            // Help subcommand (LEVEL_ALL)
            LiteralArgumentBuilder<CommandSourceStack> helpNode = Commands.literal("help")
                    .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                    .executes(this::executeHelp);
            root.then(helpNode);

            // Status subcommand (LEVEL_ALL)
            LiteralArgumentBuilder<CommandSourceStack> statusNode = Commands.literal("status")
                    .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                    .executes(this::executeStatus);
            root.then(statusNode);

            // Get subcommand (LEVEL_ALL)
            LiteralArgumentBuilder<CommandSourceStack> getNode = Commands.literal("get")
                    .requires(Commands.hasPermission(Commands.LEVEL_ALL))
                    .then(Commands.argument("key", StringArgumentType.string())
                            .suggests((ctx, suggestionsBuilder) -> {
                                if (bridge != null) {
                                    return SharedSuggestionProvider.suggest(bridge.getRegisteredKeys(), suggestionsBuilder);
                                }
                                return Suggestions.empty();
                            })
                            .executes(this::executeGet));
            root.then(getNode);

            // Set subcommand (LEVEL_GAMEMASTERS)
            LiteralArgumentBuilder<CommandSourceStack> setNode = Commands.literal("set")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .then(Commands.argument("key", StringArgumentType.string())
                            .suggests((ctx, suggestionsBuilder) -> {
                                if (bridge != null) {
                                    return SharedSuggestionProvider.suggest(bridge.getRegisteredKeys(), suggestionsBuilder);
                                }
                                return Suggestions.empty();
                            })
                            .then(Commands.argument("value", StringArgumentType.greedyString())
                                    .suggests((ctx, suggestionsBuilder) -> {
                                        try {
                                            String key = StringArgumentType.getString(ctx, "key");
                                            if (bridge != null) {
                                                GameRuleCommandBridge.RuleEntry entry = bridge.getRuleEntry(key);
                                                if (entry != null) {
                                                    if (entry.getType() == GameRuleCommandBridge.Type.BOOLEAN) {
                                                        return SharedSuggestionProvider.suggest(List.of("true", "false"), suggestionsBuilder);
                                                    } else if (entry.getDefaultValue() != null) {
                                                        return SharedSuggestionProvider.suggest(List.of(String.valueOf(entry.getDefaultValue())), suggestionsBuilder);
                                                    }
                                                }
                                            }
                                        } catch (Exception ignored) {
                                        }
                                        return Suggestions.empty();
                                    })
                                    .executes(this::executeSet)));
            root.then(setNode);

            // Reset subcommand (LEVEL_GAMEMASTERS)
            LiteralArgumentBuilder<CommandSourceStack> resetNode = Commands.literal("reset")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .executes(this::executeReset);
            root.then(resetNode);

            // Reload subcommand (LEVEL_GAMEMASTERS)
            LiteralArgumentBuilder<CommandSourceStack> reloadNode = Commands.literal("reload")
                    .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                    .executes(this::executeReload);
            root.then(reloadNode);

            // Custom subcommands
            for (LiteralArgumentBuilder<CommandSourceStack> custom : customSubcommands) {
                root.then(custom);
            }

            return root;
        }

        public LiteralCommandNode<CommandSourceStack> register() {
            if (this.dispatcher == null) {
                throw new IllegalStateException("Cannot register command tree without a CommandDispatcher. Call withDispatcher(...) first.");
            }
            return this.dispatcher.register(build());
        }

        public LiteralCommandNode<CommandSourceStack> register(CommandDispatcher<CommandSourceStack> dispatcher) {
            this.dispatcher = dispatcher;
            return register();
        }

        private int executeRoot(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            source.sendSuccess(() -> CommandFeedbackFormatter.info(modName,
                    Component.literal("Type ")
                            .append(Component.literal("§f/" + rootLiteral + " help§b")
                                    .withStyle(style -> style
                                            .withClickEvent(new ClickEvent.SuggestCommand("/" + rootLiteral + " help"))
                                            .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to run help")))))
                            .append(" to view available commands.")), false);
            return 1;
        }

        private int executeHelp(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            MutableComponent sheet = Component.literal("§6=== [" + modName + "] Commands Cheat Sheet ===§r\n");

            sheet.append(buildHelpLine("/" + rootLiteral + " help", "dasiklibrary.command.help.desc", "Show this command cheat sheet", "/" + rootLiteral + " help"))
                    .append("\n")
                    .append(buildHelpLine("/" + rootLiteral + " status", "dasiklibrary.command.status.desc", "Display current configuration state", "/" + rootLiteral + " status"))
                    .append("\n")
                    .append(buildHelpLine("/" + rootLiteral + " get <key>", "dasiklibrary.command.get.desc", "Query a configuration rule value", "/" + rootLiteral + " get "))
                    .append("\n")
                    .append(buildHelpLine("/" + rootLiteral + " set <key> <val>", "dasiklibrary.command.set.desc", "Modify a configuration rule value", "/" + rootLiteral + " set "))
                    .append("\n")
                    .append(buildHelpLine("/" + rootLiteral + " reset", "dasiklibrary.command.reset.desc", "Reset all configuration rules to defaults", "/" + rootLiteral + " reset"))
                    .append("\n")
                    .append(buildHelpLine("/" + rootLiteral + " reload", "dasiklibrary.command.reload.desc", "Reload configuration from disk", "/" + rootLiteral + " reload"));

            for (LiteralArgumentBuilder<CommandSourceStack> custom : customSubcommands) {
                sheet.append("\n")
                     .append(buildHelpLine("/" + rootLiteral + " " + custom.getLiteral(), "dasiklibrary.command.custom.desc", "Custom subcommand", "/" + rootLiteral + " " + custom.getLiteral()));
            }

            sheet = CommandFeedbackFormatter.appendSupportFooters(sheet);
            final Component finalSheet = sheet;
            source.sendSuccess(() -> finalSheet, false);
            return 1;
        }

        private MutableComponent buildHelpLine(String syntax, String transKey, String fallback, String suggestCmd) {
            MutableComponent line = Component.literal("§b" + syntax + " §7- ");
            line.append(Component.translatableWithFallback(transKey, fallback));
            return line.withStyle(style -> style
                    .withClickEvent(new ClickEvent.SuggestCommand(suggestCmd))
                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to suggest " + suggestCmd))));
        }

        private int executeStatus(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            MutableComponent msg = Component.literal("§6=== [" + modName + "] Configuration Status ===§r\n");

            boolean hasContent = false;
            if (statusSupplier != null) {
                try {
                    List<String> customLines = statusSupplier.apply(source.getLevel());
                    if (customLines != null && !customLines.isEmpty()) {
                        for (String cl : customLines) {
                            msg.append(Component.literal("§7- §e" + cl + "\n"));
                            hasContent = true;
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Failed to query statusSupplier for {}", modName, e);
                }
            }

            if (bridge != null) {
                Set<String> keys = bridge.getRegisteredKeys();
                for (String key : keys) {
                    Object val = bridge.getValue(source.getLevel(), key);
                    GameRuleCommandBridge.RuleEntry entry = bridge.getRuleEntry(key);
                    Object defVal = entry != null ? entry.getDefaultValue() : "?";
                    msg.append(Component.literal("§7- §f" + key + "§7: §b" + val + " §8(default: " + defVal + ")\n")
                            .withStyle(style -> style
                                    .withClickEvent(new ClickEvent.SuggestCommand("/" + rootLiteral + " get " + key))
                                    .withHoverEvent(new HoverEvent.ShowText(Component.literal("Click to query " + key)))));
                    hasContent = true;
                }
            }

            if (!hasContent) {
                msg.append(Component.literal("§7No active configuration rules registered."));
            }

            final Component finalMsg = msg;
            source.sendSuccess(() -> finalMsg, false);
            return 1;
        }

        private int executeGet(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            String key = StringArgumentType.getString(context, "key");
            if (bridge == null) {
                source.sendFailure(CommandFeedbackFormatter.error(modName, "No configuration bridge registered."));
                return 0;
            }
            GameRuleCommandBridge.RuleEntry entry = bridge.getRuleEntry(key);
            if (entry == null) {
                source.sendFailure(CommandFeedbackFormatter.error(modName, "Unknown configuration key '" + key + "'."));
                return 0;
            }
            Object val = bridge.getValue(source.getLevel(), key);
            source.sendSuccess(() -> CommandFeedbackFormatter.info(modName, "§f" + key + " §7= §b" + val), false);
            return 1;
        }

        private int executeSet(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            String key = StringArgumentType.getString(context, "key");
            String val = StringArgumentType.getString(context, "value");
            if (bridge == null) {
                source.sendFailure(CommandFeedbackFormatter.error(modName, "No configuration bridge registered."));
                return 0;
            }
            GameRuleCommandBridge.SetResult result = bridge.setValue(source.getLevel(), key, val);
            switch (result.getStatus()) {
                case SUCCESS -> {
                    source.sendSuccess(() -> CommandFeedbackFormatter.success(modName,
                            "Set §f" + key + "§a to §b" + result.getValue()), true);
                    return 1;
                }
                case SATURATED_WARNING -> {
                    source.sendSuccess(() -> CommandFeedbackFormatter.warning(modName, result.getMessage()), true);
                    return 1;
                }
                case ERROR_NOT_FOUND, ERROR_PARSE -> {
                    source.sendFailure(CommandFeedbackFormatter.error(modName, result.getMessage()));
                    return 0;
                }
            }
            return 0;
        }

        private int executeReset(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            if (bridge == null) {
                source.sendFailure(CommandFeedbackFormatter.error(modName, "No configuration bridge registered."));
                return 0;
            }
            bridge.resetToDefaults(source.getLevel());
            source.sendSuccess(() -> CommandFeedbackFormatter.success(modName, "Reset all configuration settings to default values."), true);
            return 1;
        }

        private int executeReload(CommandContext<CommandSourceStack> context) {
            CommandSourceStack source = context.getSource();
            if (reloadAction != null) {
                try {
                    reloadAction.accept(source.getServer());
                    source.sendSuccess(() -> CommandFeedbackFormatter.success(modName, "Configuration reloaded successfully."), true);
                    return 1;
                } catch (Exception e) {
                    LOGGER.error("Failed to execute reloadAction for {}", modName, e);
                    source.sendFailure(CommandFeedbackFormatter.error(modName, "Failed to reload configuration: " + e.getMessage()));
                    return 0;
                }
            } else {
                source.sendSuccess(() -> CommandFeedbackFormatter.info(modName, "No reload action registered."), false);
                return 1;
            }
        }

        private static String formatDefaultModName(String modId) {
            if (modId == null || modId.isEmpty()) {
                return "Mod";
            }
            String cleaned = modId.replace('-', ' ').replace('_', ' ');
            String[] parts = cleaned.split("\\s+");
            StringBuilder sb = new StringBuilder();
            for (String part : parts) {
                if (!part.isEmpty()) {
                    sb.append(Character.toUpperCase(part.charAt(0)));
                    if (part.length() > 1) {
                        sb.append(part.substring(1));
                    }
                    sb.append(" ");
                }
            }
            return sb.toString().trim();
        }
    }
}
