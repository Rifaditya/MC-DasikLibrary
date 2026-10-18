// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft.java (26.2+), ConfirmLinkScreen.java (26.2+)
package net.dasik.social.api.client.gui;

import net.dasik.social.api.SocialLinks;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.dasik.social.api.config.DasikSupportHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.ConfirmLinkScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;

/**
 * Dedicated client-side helper for opening external links via Minecraft confirmation screens
 * and dynamically building YACL creator support buttons.
 * <p>
 * Sidedness Isolation: All references to {@link Screen}, {@link ConfirmLinkScreen}, and
 * synthetic lambda methods generated for GUI action callbacks reside strictly in this
 * {@code @Environment(EnvType.CLIENT)} class, preventing dedicated server ClassLoader crashes.
 */
@Environment(EnvType.CLIENT)
@DasikApiStatus.Public
public final class ClientSupportScreenHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|ClientSupport");

    private ClientSupportScreenHelper() {}

    /**
     * Safely opens any external URL in the player's default browser via {@link ConfirmLinkScreen}.
     *
     * @param parentScreen the current screen to return to after confirmation, or null
     * @param url the external URL to open
     */
    public static void openUrl(@Nullable Screen parentScreen, String url) {
        if (url == null || url.isBlank()) {
            LOGGER.warn("Cannot open empty or null URL.");
            return;
        }

        try {
            if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
                LOGGER.warn("Cannot open browser URL from dedicated server environment.");
                return;
            }
        } catch (Throwable t) {
            // FabricLoader may not be initialized in non-Fabric test runners or headless tests
            LOGGER.debug("FabricLoader environment check skipped: {}", t.getMessage());
        }

        try {
            ConfirmLinkScreen.confirmLinkNow(parentScreen, url);
        } catch (Throwable t) {
            LOGGER.error("Failed to open confirmation screen for URL: {}", url, t);
        }
    }

    /**
     * Safely opens any external URL in the player's default browser with an untyped parent screen object.
     *
     * @param parentScreen untyped parent screen (cast to {@link Screen}), or null
     * @param url the external URL to open
     */
    public static void openUrl(@Nullable Object parentScreen, String url) {
        openUrl(parentScreen instanceof Screen screen ? screen : null, url);
    }

    /**
     * Safely opens the official Discord community server in the player's default browser via {@link ConfirmLinkScreen}.
     *
     * @param parentScreen the current screen to return to after confirmation, or null
     */
    public static void openDiscord(@Nullable Screen parentScreen) {
        openUrl(parentScreen, SocialLinks.DISCORD_INVITE_URL);
    }

    /**
     * Safely opens the official Discord community server in the player's default browser with an untyped parent screen object.
     *
     * @param parentScreen untyped parent screen (cast to {@link Screen}), or null
     */
    public static void openDiscord(@Nullable Object parentScreen) {
        openDiscord(parentScreen instanceof Screen screen ? screen : null);
    }

    /**
     * Safely opens the creator's Ko-fi page in the player's default browser via {@link ConfirmLinkScreen}.
     *
     * @param parentScreen the current screen to return to after confirmation, or null
     */
    public static void openKofi(@Nullable Screen parentScreen) {
        openUrl(parentScreen, SocialLinks.KOFI_URL);
    }

    /**
     * Safely opens the creator's Ko-fi page in the player's default browser with an untyped parent screen object.
     *
     * @param parentScreen untyped parent screen (cast to {@link Screen}), or null
     */
    public static void openKofi(@Nullable Object parentScreen) {
        openKofi(parentScreen instanceof Screen screen ? screen : null);
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

            // 1. Row label name
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("name") && m.getParameterCount() == 1) {
                    m.invoke(builder, DasikSupportHelper.getLabelText());
                    break;
                }
            }

            // 2. Text displayed inside the button plate widget
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("text") && m.getParameterCount() == 1) {
                    m.invoke(builder, DasikSupportHelper.getButtonText());
                    break;
                }
            }

            // 3. Tooltip description via OptionDescription (using Collection overload for clean reflection)
            Class<?> descClass = Class.forName("dev.isxander.yacl3.api.OptionDescription");
            Method descCreateBuilderMethod = descClass.getMethod("createBuilder");
            Object descBuilder = descCreateBuilderMethod.invoke(null);
            for (Method m : descBuilder.getClass().getMethods()) {
                if (m.getName().equals("text") && m.getParameterCount() == 1 && java.util.Collection.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.invoke(descBuilder, java.util.List.of(DasikSupportHelper.getTooltipText()));
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
     *
     * @return the built YACL ButtonOption/Option instance, or null if YACL is absent or an error occurs.
     */
    public static Object createDiscordYaclButton() {
        try {
            Class<?> buttonOptClass = Class.forName("dev.isxander.yacl3.api.ButtonOption");
            Method createBuilderMethod = buttonOptClass.getMethod("createBuilder");
            Object builder = createBuilderMethod.invoke(null);
            Class<?> builderClass = builder.getClass();

            // 1. Row label name
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("name") && m.getParameterCount() == 1) {
                    m.invoke(builder, DasikSupportHelper.getDiscordLabelText());
                    break;
                }
            }

            // 2. Text displayed inside the button plate widget
            for (Method m : builderClass.getMethods()) {
                if (m.getName().equals("text") && m.getParameterCount() == 1) {
                    m.invoke(builder, DasikSupportHelper.getDiscordButtonText());
                    break;
                }
            }

            // 3. Tooltip description via OptionDescription (using Collection overload for clean reflection)
            Class<?> descClass = Class.forName("dev.isxander.yacl3.api.OptionDescription");
            Method descCreateBuilderMethod = descClass.getMethod("createBuilder");
            Object descBuilder = descCreateBuilderMethod.invoke(null);
            for (Method m : descBuilder.getClass().getMethods()) {
                if (m.getName().equals("text") && m.getParameterCount() == 1 && java.util.Collection.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.invoke(descBuilder, java.util.List.of(DasikSupportHelper.getDiscordTooltipText()));
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
}
