// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft.java (26.2+), ConfirmLinkScreen.java (26.2+)
package net.dasik.social.api.config;

import net.dasik.social.api.annotation.DasikApiStatus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.function.BiConsumer;
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
    public static void openDiscord(Screen parentScreen) {
        net.dasik.social.api.SocialLinks.openDiscord(parentScreen);
    }

    /**
     * Safely opens the creator's Ko-fi page in the player's default browser via ConfirmLinkScreen.
     * Gated by client environment check to prevent server classloader exceptions.
     * Client-only operation.
     */
    @Environment(EnvType.CLIENT)
    public static void openKofi(Screen parentScreen) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            LOGGER.warn("Cannot open browser URL from dedicated server environment.");
            return;
        }
        openUrl(parentScreen, KOFI_URL);
    }

    /**
     * Safely opens any external URL via Minecraft's standard ConfirmLinkScreen.
     * Client-only operation.
     */
    @Environment(EnvType.CLIENT)
    public static void openUrl(Screen parentScreen, String url) {
        try {
            ConfirmLinkScreen.confirmLinkNow(parentScreen, url);
        } catch (Throwable t) {
            LOGGER.error("Failed to open confirmation screen for URL: {}", url, t);
        }
    }

    /**
     * Dynamically builds a YetAnotherConfigLib (YACL) ButtonOption for Ko-fi creator support.
     * Uses reflection so Dasik Library does not need a hard compile-time or runtime dependency on YACL.
     * Client-only operation.
     *
     * @return the built YACL ButtonOption/Option instance, or null if YACL is absent or an error occurs.
     */
    @Environment(EnvType.CLIENT)
    public static Object createYaclButton() {
        try {
            Class<?> buttonOptClass = Class.forName("dev.isxander.yacl3.api.ButtonOption");
            Method createBuilderMethod = buttonOptClass.getMethod("createBuilder");
            Object builder = createBuilderMethod.invoke(null);
            Class<?> builderClass = builder.getClass();

            // 1. Row label name
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("name") && m.getParameterCount() == 1) {
                    m.invoke(builder, getLabelText());
                    break;
                }
            }

            // 2. Text displayed inside the button plate widget
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("text") && m.getParameterCount() == 1) {
                    m.invoke(builder, getButtonText());
                    break;
                }
            }

            // 3. Tooltip description via OptionDescription (using Collection overload for clean reflection)
            Class<?> descClass = Class.forName("dev.isxander.yacl3.api.OptionDescription");
            Method descCreateBuilderMethod = descClass.getMethod("createBuilder");
            Object descBuilder = descCreateBuilderMethod.invoke(null);
            for (Method m : descBuilder.getClass().getMethods()) {
                if (m.getName().equals("text") && m.getParameterCount() == 1 && java.util.Collection.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.invoke(descBuilder, java.util.List.of(getTooltipText()));
                    break;
                }
            }
            Object desc = descBuilder.getClass().getMethod("build").invoke(descBuilder);
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("description") && m.getParameterCount() == 1) {
                    m.invoke(builder, desc);
                    break;
                }
            }

            // 4. Click Action
            BiConsumer<Object, Object> biAction = (screen, opt) -> openKofi((Screen) screen);
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("action") && m.getParameterCount() == 1 && BiConsumer.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.invoke(builder, biAction);
                    break;
                }
            }

            Method buildMethod = builderClass.getMethod("build");
            return buildMethod.invoke(builder);
        } catch (Throwable t) {
            LOGGER.warn("Failed to build YACL button option via reflection: {}", t.getMessage());
            return null;
        }
    }

    /**
     * Dynamically builds a YetAnotherConfigLib (YACL) ButtonOption for official Discord community server.
     * Uses reflection so Dasik Library does not need a hard compile-time or runtime dependency on YACL.
     * Client-only operation.
     *
     * @return the built YACL ButtonOption/Option instance, or null if YACL is absent or an error occurs.
     */
    @Environment(EnvType.CLIENT)
    public static Object createDiscordYaclButton() {
        try {
            Class<?> buttonOptClass = Class.forName("dev.isxander.yacl3.api.ButtonOption");
            Method createBuilderMethod = buttonOptClass.getMethod("createBuilder");
            Object builder = createBuilderMethod.invoke(null);
            Class<?> builderClass = builder.getClass();

            // 1. Row label name
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("name") && m.getParameterCount() == 1) {
                    m.invoke(builder, getDiscordLabelText());
                    break;
                }
            }

            // 2. Text displayed inside the button plate widget
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("text") && m.getParameterCount() == 1) {
                    m.invoke(builder, getDiscordButtonText());
                    break;
                }
            }

            // 3. Tooltip description via OptionDescription (using Collection overload for clean reflection)
            Class<?> descClass = Class.forName("dev.isxander.yacl3.api.OptionDescription");
            Method descCreateBuilderMethod = descClass.getMethod("createBuilder");
            Object descBuilder = descCreateBuilderMethod.invoke(null);
            for (Method m : descBuilder.getClass().getMethods()) {
                if (m.getName().equals("text") && m.getParameterCount() == 1 && java.util.Collection.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.invoke(descBuilder, java.util.List.of(getDiscordTooltipText()));
                    break;
                }
            }
            Object desc = descBuilder.getClass().getMethod("build").invoke(descBuilder);
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("description") && m.getParameterCount() == 1) {
                    m.invoke(builder, desc);
                    break;
                }
            }

            // 4. Click Action
            BiConsumer<Object, Object> biAction = (screen, opt) -> openDiscord((Screen) screen);
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("action") && m.getParameterCount() == 1 && BiConsumer.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.invoke(builder, biAction);
                    break;
                }
            }

            Method buildMethod = builderClass.getMethod("build");
            return buildMethod.invoke(builder);
        } catch (Throwable t) {
            LOGGER.warn("Failed to build Discord YACL button option via reflection: {}", t.getMessage());
            return null;
        }
    }

    private static Method findMethod(Class<?> clazz, String name) {
        for (Method m : clazz.getMethods()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        throw new RuntimeException("Method " + name + " not found on " + clazz.getName());
    }
}
