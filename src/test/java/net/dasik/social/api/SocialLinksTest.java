// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class SocialLinksTest {

    @Test
    @DisplayName("Verify all social URL constants start with https:// and are well-formed URIs")
    public void testUrlsAreWellFormedUri() {
        String[] urls = {
                SocialLinks.DISCORD_INVITE_URL,
                SocialLinks.KOFI_URL,
                SocialLinks.GITHUB_URL,
                SocialLinks.MOD_PORTAL_URL
        };

        for (String url : urls) {
            assertNotNull(url, "Social link constant must not be null");
            assertTrue(url.startsWith("https://"), "URL constant must start with https://: " + url);
            assertDoesNotThrow(() -> {
                URI parsed = URI.create(url);
                assertNotNull(parsed.getHost(), "URL host must be valid for: " + url);
            }, "URL must parse into a valid URI: " + url);
        }

        assertEquals("https://discord.gg/VSHP6mw4qY", SocialLinks.DISCORD_INVITE_URL);
        assertEquals("https://ko-fi.com/dasikigaijin", SocialLinks.KOFI_URL);
        assertEquals("https://github.com/Rifaditya", SocialLinks.GITHUB_URL);
        assertEquals("https://dasik-mc-studio.pages.dev", SocialLinks.MOD_PORTAL_URL);
    }

    @Test
    @DisplayName("Verify cached URI getters return valid URIs matching the string constants")
    public void testCachedUrisMatchConstants() {
        assertEquals(URI.create(SocialLinks.DISCORD_INVITE_URL), SocialLinks.getDiscordUri());
        assertEquals(URI.create(SocialLinks.KOFI_URL), SocialLinks.getKofiUri());
        assertEquals(URI.create(SocialLinks.GITHUB_URL), SocialLinks.getGithubUri());
        assertEquals(URI.create(SocialLinks.MOD_PORTAL_URL), SocialLinks.getModPortalUri());

        // Verify caching returns the same instance
        assertSame(SocialLinks.getDiscordUri(), SocialLinks.getDiscordUri());
        assertSame(SocialLinks.getKofiUri(), SocialLinks.getKofiUri());
        assertSame(SocialLinks.getGithubUri(), SocialLinks.getGithubUri());
        assertSame(SocialLinks.getModPortalUri(), SocialLinks.getModPortalUri());
    }

    @Test
    @DisplayName("Verify openUrl, openDiscord, and openKofi handle headless execution safely without unhandled exceptions")
    public void testOpenUrlHeadlessSafety() {
        // Assert openUrl with null screen and Discord URL runs safely
        assertDoesNotThrow(() -> SocialLinks.openUrl(null, SocialLinks.DISCORD_INVITE_URL));

        // Assert openDiscord and openKofi run safely
        assertDoesNotThrow(() -> SocialLinks.openDiscord(null));
        assertDoesNotThrow(() -> SocialLinks.openKofi(null));

        // Assert null and blank URL handling
        assertDoesNotThrow(() -> SocialLinks.openUrl(null, null));
        assertDoesNotThrow(() -> SocialLinks.openUrl(null, ""));
        assertDoesNotThrow(() -> SocialLinks.openUrl(null, "   "));
    }
}
