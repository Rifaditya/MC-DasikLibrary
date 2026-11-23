// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Headless unit verification suite for {@link DasikClientConfig}.
 * Validates defaults, serialization round-trips, fallback handling, and headless test resilience.
 */
class DasikClientConfigTest {

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        DasikClientConfig.resetToDefaults();
    }

    @AfterEach
    void tearDown() {
        DasikClientConfig.resetToDefaults();
    }

    @Test
    @DisplayName("Pillar 2: Verify default values for client support prompts")
    void testDefaultValues() {
        DasikClientConfig config = new DasikClientConfig();
        assertTrue(config.showSupportPrompts, "showSupportPrompts must default to true");
        assertTrue(config.showWorldStartGreeting, "showWorldStartGreeting must default to true");
    }

    @Test
    @DisplayName("Pillar 2: Verify JSON save and load round-trip")
    void testSaveAndLoadRoundTrip() {
        Path configFile = tempDir.resolve("test_client_config.json");

        DasikClientConfig config = new DasikClientConfig();
        config.showSupportPrompts = false;
        config.showWorldStartGreeting = false;
        config.save(configFile);

        assertTrue(Files.exists(configFile), "Config file must be created on disk");

        DasikClientConfig.load(configFile);
        DasikClientConfig loaded = DasikClientConfig.get();

        assertNotNull(loaded, "Loaded config must not be null");
        assertFalse(loaded.showSupportPrompts, "showSupportPrompts must be false after loading");
        assertFalse(loaded.showWorldStartGreeting, "showWorldStartGreeting must be false after loading");
    }

    @Test
    @DisplayName("Pillar 2: Verify corrupted JSON file falls back to defaults safely")
    void testCorruptedJsonFallback() throws IOException {
        Path configFile = tempDir.resolve("corrupted_client_config.json");
        Files.writeString(configFile, "{ invalid json content ... !!!", StandardCharsets.UTF_8);

        assertDoesNotThrow(() -> DasikClientConfig.load(configFile), "Loading corrupted JSON must not throw");
        DasikClientConfig loaded = DasikClientConfig.get();
        assertNotNull(loaded, "Config must fall back to default instance");
        assertTrue(loaded.showSupportPrompts, "showSupportPrompts must fall back to true");
        assertTrue(loaded.showWorldStartGreeting, "showWorldStartGreeting must fall back to true");
    }

    @Test
    @DisplayName("Pillar 2: Verify headless path resolution safety [ERR-20261028-001]")
    void testResolveConfigPathHeadlessSafety() {
        assertDoesNotThrow(DasikClientConfig::resolveConfigPath, "resolveConfigPath must never throw in headless test");
        Path path = DasikClientConfig.resolveConfigPath();
        assertNotNull(path, "Resolved path must not be null");
        assertTrue(path.toString().endsWith("dasik_library_client.json"), "Resolved path must point to dasik_library_client.json");
    }

    @Test
    @DisplayName("Pillar 2: Verify static convenience getters and setters")
    void testStaticGettersAndSetters() {
        DasikClientConfig.setShowSupportPrompts(false);
        assertFalse(DasikClientConfig.isShowSupportPrompts());

        DasikClientConfig.setShowWorldStartGreeting(false);
        assertFalse(DasikClientConfig.isShowWorldStartGreeting());

        DasikClientConfig.setShowSupportPrompts(true);
        assertTrue(DasikClientConfig.isShowSupportPrompts());

        DasikClientConfig.setShowWorldStartGreeting(true);
        assertTrue(DasikClientConfig.isShowWorldStartGreeting());
    }
}
