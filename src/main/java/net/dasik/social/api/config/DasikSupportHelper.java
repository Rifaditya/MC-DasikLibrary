// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft.java (26.2+)
package net.dasik.social.api.config;

import net.dasik.social.api.annotation.DasikApiStatus;
import net.dasik.social.api.client.gui.ClientSupportScreenHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.function.Consumer;

/**
 * Universal helper for integrating creator support links (Ko-fi) across ModMenu,
 * in-game configuration screens (YACL / Cloth Config), and Brigadier commands.
 */
@DasikApiStatus.Public
public class DasikSupportHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|Support");

    public static final String KOFI_URL = "https://ko-fi.com/dasikigaijin";

    public static final String KEY_LABEL = "dasiklibrary.support.kofi.label";
    public static final String KEY_BUTTON = "dasiklibrary.support.kofi.button";
    public static final String KEY_TOOLTIP = "dasiklibrary.support.kofi.tooltip";
    public static final String KEY_CHAT_PROMPT = "dasiklibrary.support.kofi.chat_prompt";
    public static final String KEY_CHAT_LINK = "dasiklibrary.support.kofi.chat_link";
    public static final String KEY_CHAT_HOVER = "dasiklibrary.support.kofi.chat_hover";

    public static final String KEY_DISCORD_LABEL = "dasiklibrary.support.discord.label";
    public static final String KEY_DISCORD_BUTTON = "dasiklibrary.support.discord.button";
    public static final String KEY_DISCORD_TOOLTIP = "dasiklibrary.support.discord.tooltip";
    public static final String KEY_DISCORD_CHAT_PROMPT = "dasiklibrary.support.discord.chat_prompt";
    public static final String KEY_DISCORD_CHAT_LINK = "dasiklibrary.support.discord.chat_link";
    public static final String KEY_DISCORD_CHAT_HOVER = "dasiklibrary.support.discord.chat_hover";

    public static Component getLabelText() {
        return Component.translatable(KEY_LABEL);
    }

    public static Component getButtonText() {
        return Component.translatable(KEY_BUTTON);
    }

    public static Component getTooltipText() {
        return Component.translatable(KEY_TOOLTIP);
    }

    public static Component getDiscordLabelText() {
        return Component.translatable(KEY_DISCORD_LABEL);
    }

    public static Component getDiscordButtonText() {
        return Component.translatable(KEY_DISCORD_BUTTON);
    }

    public static Component getDiscordTooltipText() {
        return Component.translatable(KEY_DISCORD_TOOLTIP);
    }

    /**
     * Builds a formatted, clickable chat component for Discord community link.
     * Renders: "💬 Community: [Join Discord]"
     */
    public static Component getDiscordCommandFooter() {
        MutableComponent prompt = Component.translatable(KEY_DISCORD_CHAT_PROMPT)
                .withStyle(ChatFormatting.BLUE);

        MutableComponent link = Component.translatable(KEY_DISCORD_CHAT_LINK)
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.AQUA)
                        .withBold(true)
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent.OpenUrl(net.dasik.social.api.SocialLinks.getDiscordUri()))
                        .withHoverEvent(new HoverEvent.ShowText(Component.translatable(KEY_DISCORD_CHAT_HOVER)))
                );

        return prompt.append(link);
    }

    /**
     * Builds a formatted, clickable chat component for Ko-fi creator support.
     * Renders: "☕ Enjoying the mod? [Support on Ko-fi]"
     */
    public static Component getKofiCommandFooter() {
        MutableComponent prompt = Component.translatable(KEY_CHAT_PROMPT)
                .withStyle(ChatFormatting.GRAY);

        MutableComponent link = Component.translatable(KEY_CHAT_LINK)
                .withStyle(Style.EMPTY
                        .withColor(ChatFormatting.GOLD)
                        .withBold(true)
                        .withUnderlined(true)
                        .withClickEvent(new ClickEvent.OpenUrl(URI.create(KOFI_URL)))
                        .withHoverEvent(new HoverEvent.ShowText(Component.translatable(KEY_CHAT_HOVER)))
                );

        return prompt.append(link);
    }

    /**
     * Builds a combined single-line formatted, clickable chat component for Brigadier command footers.
     * Renders: "💬 Community: [Join Discord] • ☕ Support: [Support on Ko-fi]"
     */
    public static Component getCommandFooter() {
        MutableComponent discordSection = (MutableComponent) getDiscordCommandFooter();
        MutableComponent separator = Component.literal(" • ").withStyle(ChatFormatting.DARK_GRAY);
        Component kofiSection = getKofiCommandFooter();

        return discordSection.append(separator).append(kofiSection);
    }

    /**
     * Appends the standard support footer to a command feedback consumer.
     */
    public static void appendCommandFooter(Consumer<Component> feedbackConsumer) {
        if (feedbackConsumer != null) {
            feedbackConsumer.accept(getCommandFooter());
        }
    }

    /**
     * Safely opens the official Discord community server in the player's default browser via ConfirmLinkScreen.
     * Client-only operation.
     */
    @Environment(EnvType.CLIENT)
    public static void openDiscord(@Nullable Object parentScreen) {
        ClientSupportScreenHelper.openDiscord(parentScreen);
    }

    /**
     * Safely opens the creator's Ko-fi page in the player's default browser via ConfirmLinkScreen.
     * Client-only operation.
     */
    @Environment(EnvType.CLIENT)
    public static void openKofi(@Nullable Object parentScreen) {
        ClientSupportScreenHelper.openKofi(parentScreen);
    }

    /**
     * Safely opens any external URL via Minecraft's standard ConfirmLinkScreen.
     * Client-only operation.
     */
    @Environment(EnvType.CLIENT)
    public static void openUrl(@Nullable Object parentScreen, String url) {
        ClientSupportScreenHelper.openUrl(parentScreen, url);
    }

    /**
     * Dynamically builds a YetAnotherConfigLib (YACL) ButtonOption for Ko-fi creator support.
     * Delegates to {@link ClientSupportScreenHelper#createYaclButton()} to preserve dedicated server sidedness.
     * Client-only operation.
     *
     * @return the built YACL ButtonOption/Option instance, or null if YACL is absent or an error occurs.
     */
    @Environment(EnvType.CLIENT)
    public static Object createYaclButton() {
        return ClientSupportScreenHelper.createYaclButton();
    }

    /**
     * Dynamically builds a YetAnotherConfigLib (YACL) ButtonOption for official Discord community server.
     * Delegates to {@link ClientSupportScreenHelper#createDiscordYaclButton()} to preserve dedicated server sidedness.
     * Client-only operation.
     *
     * @return the built YACL ButtonOption/Option instance, or null if YACL is absent or an error occurs.
     */
    @Environment(EnvType.CLIENT)
    public static Object createDiscordYaclButton() {
        return ClientSupportScreenHelper.createDiscordYaclButton();
    }
}
