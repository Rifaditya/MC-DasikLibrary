// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api;

import net.dasik.social.api.annotation.DasikApiStatus;
import net.dasik.social.api.client.gui.ClientSupportScreenHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import java.net.URI;

/**
 * Centralized registry of official social links, repository endpoints, and community resources
 * for the Dasik modding ecosystem.
 * <p>
 * Provides constant URLs, cached {@link URI} instances, and client-safe helpers
 * for opening external web links in the player's default browser via {@link ClientSupportScreenHelper}.
 */
@DasikApiStatus.Public
public final class SocialLinks {

    public static final String DISCORD_INVITE_URL = "https://discord.gg/EV99bgAFqb";
    public static final String KOFI_URL = "https://ko-fi.com/dasikigaijin";
    public static final String GITHUB_URL = "https://github.com/Rifaditya";
    public static final String MOD_PORTAL_URL = "https://dasik-mc-studio.pages.dev";

    private static final URI DISCORD_URI = URI.create(DISCORD_INVITE_URL);
    private static final URI KOFI_URI = URI.create(KOFI_URL);
    private static final URI GITHUB_URI = URI.create(GITHUB_URL);
    private static final URI MOD_PORTAL_URI = URI.create(MOD_PORTAL_URL);

    private SocialLinks() {}

    /**
     * Returns the cached {@link URI} for the official Discord community server.
     *
     * @return the Discord community URI
     */
    public static URI getDiscordUri() {
        return DISCORD_URI;
    }

    /**
     * Returns the cached {@link URI} for creator Ko-fi support.
     *
     * @return the Ko-fi support URI
     */
    public static URI getKofiUri() {
        return KOFI_URI;
    }

    /**
     * Returns the cached {@link URI} for the developer GitHub profile.
     *
     * @return the developer GitHub URI
     */
    public static URI getGithubUri() {
        return GITHUB_URI;
    }

    /**
     * Returns the cached {@link URI} for the Dasik MC Studio mod portal.
     *
     * @return the Dasik MC Studio mod portal URI
     */
    public static URI getModPortalUri() {
        return MOD_PORTAL_URI;
    }

    /**
     * Safely opens the official Discord community invite in the player's default browser via {@link ClientSupportScreenHelper}.
     * Client-only operation.
     *
     * @param parentScreen the current screen to return to after confirmation, or null
     */
    @Environment(EnvType.CLIENT)
    public static void openDiscord(@Nullable Object parentScreen) {
        ClientSupportScreenHelper.openDiscord(parentScreen);
    }

    /**
     * Safely opens the creator's Ko-fi page in the player's default browser via {@link ClientSupportScreenHelper}.
     * Client-only operation.
     *
     * @param parentScreen the current screen to return to after confirmation, or null
     */
    @Environment(EnvType.CLIENT)
    public static void openKofi(@Nullable Object parentScreen) {
        ClientSupportScreenHelper.openKofi(parentScreen);
    }

    /**
     * Safely opens any external URL via {@link ClientSupportScreenHelper}.
     * Client-only operation.
     *
     * @param parentScreen the current screen to return to after confirmation, or null
     * @param url the external URL to open
     */
    @Environment(EnvType.CLIENT)
    public static void openUrl(@Nullable Object parentScreen, String url) {
        ClientSupportScreenHelper.openUrl(parentScreen, url);
    }
}
