// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.client.gui;

import net.dasik.social.api.SocialLinks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClientSupportScreenHelperTest {

    @Test
    @DisplayName("Verify createYaclButton and createDiscordYaclButton execute headlessly without unhandled exceptions")
    public void testCreateYaclButtonsHeadless() {
        assertDoesNotThrow(ClientSupportScreenHelper::createYaclButton);
        assertDoesNotThrow(ClientSupportScreenHelper::createDiscordYaclButton);
    }

    @Test
    @DisplayName("Verify openUrl, openDiscord, and openKofi handle headless execution safely without unhandled exceptions")
    public void testOpenUrlHeadlessSafety() {
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, SocialLinks.DISCORD_INVITE_URL));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openDiscord(null));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openKofi(null));

        // Null and blank handling
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, null));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, ""));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(null, "   "));
    }

    @Test
    @DisplayName("Verify untyped Object overloads handle null and arbitrary objects safely")
    public void testUntypedObjectOverloads() {
        Object dummyNonScreen = new Object();
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openUrl(dummyNonScreen, SocialLinks.KOFI_URL));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openDiscord(dummyNonScreen));
        assertDoesNotThrow(() -> ClientSupportScreenHelper.openKofi(dummyNonScreen));
    }
}
