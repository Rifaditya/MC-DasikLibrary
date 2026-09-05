// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft.java (26.2+), ConfirmLinkScreen.java (26.2+)
package net.dasik.social.api.config;

import net.fabricmc.api.EnvType;
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
public class DasikSupportHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|Support");

    public static final String KOFI_URL = "https://ko-fi.com/dasikigaijin";

    public static final String KEY_BUTTON = "dasiklibrary.support.kofi.button";
    public static final String KEY_TOOLTIP = "dasiklibrary.support.kofi.tooltip";
    public static final String KEY_CHAT_PROMPT = "dasiklibrary.support.kofi.chat_prompt";
    public static final String KEY_CHAT_LINK = "dasiklibrary.support.kofi.chat_link";
    public static final String KEY_CHAT_HOVER = "dasiklibrary.support.kofi.chat_hover";

    public static Component getButtonText() {
        return Component.translatable(KEY_BUTTON);
    }

    public static Component getTooltipText() {
        return Component.translatable(KEY_TOOLTIP);
    }

    /**
     * Builds a formatted, clickable chat component for Brigadier command footers.
     * Renders: "☕ Enjoying the mod? [Support on Ko-fi]"
     */
    public static Component getCommandFooter() {
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
     * Appends the standard support footer to a command feedback consumer.
     */
    public static void appendCommandFooter(Consumer<Component> feedbackConsumer) {
        if (feedbackConsumer != null) {
            feedbackConsumer.accept(getCommandFooter());
        }
    }

    /**
     * Safely opens the creator's Ko-fi page in the player's default browser via ConfirmLinkScreen.
     * Gated by client environment check to prevent server classloader exceptions.
     */
    public static void openKofi(Screen parentScreen) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            LOGGER.warn("Cannot open browser URL from dedicated server environment.");
            return;
        }
        openUrl(parentScreen, KOFI_URL);
    }

    /**
     * Safely opens any external URL via Minecraft's standard ConfirmLinkScreen.
     */
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
     *
     * @return the built YACL ButtonOption/Option instance, or null if YACL is absent or an error occurs.
     */
    public static Object createYaclButton() {
        try {
            Class<?> buttonOptClass = Class.forName("dev.isxander.yacl3.api.ButtonOption");
            Method createBuilderMethod = buttonOptClass.getMethod("createBuilder");
            Object builder = createBuilderMethod.invoke(null);
            Class<?> builderClass = builder.getClass();

            findMethod(builderClass, "name").invoke(builder, getButtonText());

            Class<?> descClass = Class.forName("dev.isxander.yacl3.api.OptionDescription");
            Method descCreateBuilderMethod = descClass.getMethod("createBuilder");
            Object descBuilder = descCreateBuilderMethod.invoke(null);
            findMethod(descBuilder.getClass(), "text").invoke(descBuilder, new Object[]{new Object[]{getTooltipText()}});
            Object desc = findMethod(descBuilder.getClass(), "build").invoke(descBuilder);
            findMethod(builderClass, "description").invoke(builder, desc);

            Consumer<Screen> action = DasikSupportHelper::openKofi;
            Method actionMethod = null;
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("action") && m.getParameterCount() == 1 && m.getParameterTypes()[0] == Consumer.class) {
                    actionMethod = m;
                    break;
                }
            }
            if (actionMethod != null) {
                actionMethod.invoke(builder, action);
            } else {
                BiConsumer<Screen, Object> biAction = (screen, opt) -> openKofi(screen);
                for (Method m : builderClass.getMethods()) {
                    if (m.getName().equals("action") && m.getParameterCount() == 1 && m.getParameterTypes()[0] == BiConsumer.class) {
                        m.invoke(builder, biAction);
                        break;
                    }
                }
            }

            return findMethod(builderClass, "build").invoke(builder);
        } catch (Throwable t) {
            LOGGER.debug("YACL not present or failed to build YACL button option: {}", t.getMessage());
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
