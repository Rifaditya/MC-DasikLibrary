// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.command;

import net.dasik.social.api.annotation.DasikApiStatus;
import net.dasik.social.api.config.DasikSupportHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Standardized feedback formatter for all studio mod commands.
 * Enforces unified color palettes, prefixes, and clickable community footers.
 */
@DasikApiStatus.Public
public final class CommandFeedbackFormatter {

    private CommandFeedbackFormatter() {
    }

    /**
     * Builds the standard gold mod prefix: "§6[ModName]§r "
     */
    public static MutableComponent prefix(String modName) {
        return Component.literal("§6[" + modName + "]§r ");
    }

    /**
     * Builds a success message formatted with prefix and green text.
     */
    public static Component success(String modName, Component message) {
        return prefix(modName).append(Component.literal("§a").append(message));
    }

    /**
     * Builds a success message formatted with prefix and green text.
     */
    public static Component success(String modName, String message) {
        return prefix(modName).append(Component.literal("§a" + message));
    }

    /**
     * Builds a warning message formatted with prefix and yellow text.
     */
    public static Component warning(String modName, Component message) {
        return prefix(modName).append(Component.literal("§e").append(message));
    }

    /**
     * Builds a warning message formatted with prefix and yellow text.
     */
    public static Component warning(String modName, String message) {
        return prefix(modName).append(Component.literal("§e" + message));
    }

    /**
     * Builds an error message formatted with prefix and red text.
     */
    public static Component error(String modName, Component message) {
        return prefix(modName).append(Component.literal("§c").append(message));
    }

    /**
     * Builds an error message formatted with prefix and red text.
     */
    public static Component error(String modName, String message) {
        return prefix(modName).append(Component.literal("§c" + message));
    }

    /**
     * Builds an informational / value message formatted with prefix and aqua text.
     */
    public static Component info(String modName, Component message) {
        return prefix(modName).append(Component.literal("§b").append(message));
    }

    /**
     * Builds an informational / value message formatted with prefix and aqua text.
     */
    public static Component info(String modName, String message) {
        return prefix(modName).append(Component.literal("§b" + message));
    }

    /**
     * Formats a config key in clean white.
     */
    public static MutableComponent formatKey(String key) {
        return Component.literal("§f" + key + "§r");
    }

    /**
     * Formats a config value in clean aqua.
     */
    public static MutableComponent formatValue(Object value) {
        return Component.literal("§b" + value + "§r");
    }

    /**
     * Appends clickable Discord and Ko-fi support footers to the base component.
     */
    public static MutableComponent appendSupportFooters(MutableComponent base) {
        if (base == null) {
            base = Component.empty();
        }
        return base.append(Component.literal("\n"))
                   .append(DasikSupportHelper.getDiscordCommandFooter())
                   .append(Component.literal("\n"))
                   .append(DasikSupportHelper.getKofiCommandFooter());
    }
}
