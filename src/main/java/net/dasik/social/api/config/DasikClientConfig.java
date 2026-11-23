// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * Client-side configuration persistence for DasikLibrary creator support prompts and greetings.
 * <p>
 * Decoupled from FabricLoader runtime to guarantee crash-free execution in headless unit test runners
 * per {@code [ERR-20261028-001]}.
 */
@Environment(EnvType.CLIENT)
@DasikApiStatus.Public
public class DasikClientConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|ClientConfig");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static DasikClientConfig instance;

    /**
     * Master toggle for all creator support reminders and milestone prompts in chat.
     * Default: {@code true}.
     */
    public boolean showSupportPrompts = true;

    /**
     * Toggle for the world-start welcome greeting sent 8 seconds post-join.
     * Default: {@code true}.
     */
    public boolean showWorldStartGreeting = true;

    /**
     * Resolves the target JSON configuration file path.
     * Safely falls back to {@code config/dasik_library_client.json} when running headless.
     */
    public static Path resolveConfigPath() {
        try {
            FabricLoader loader = FabricLoader.getInstance();
            if (loader != null && loader.getConfigDir() != null) {
                return loader.getConfigDir().resolve("dasik_library_client.json");
            }
        } catch (Throwable ignored) {
            // Fallback for headless unit test runner [ERR-20261028-001]
        }
        return Path.of("config", "dasik_library_client.json");
    }

    /**
     * Returns the singleton client config instance, loading from disk if not yet initialized.
     */
    public static synchronized DasikClientConfig get() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    /**
     * Explicitly sets or overrides the active configuration instance (e.g. for testing).
     */
    public static synchronized void set(DasikClientConfig config) {
        instance = config != null ? config : new DasikClientConfig();
    }

    /**
     * Resets the configuration instance to code defaults without deleting files on disk.
     */
    public static synchronized void resetToDefaults() {
        instance = new DasikClientConfig();
    }

    /**
     * Loads configuration from the default configuration path.
     */
    public static synchronized void load() {
        load(resolveConfigPath());
    }

    /**
     * Loads configuration from a specific path. If the file does not exist, writes code defaults.
     */
    public static synchronized void load(Path path) {
        if (path == null) {
            instance = new DasikClientConfig();
            return;
        }

        if (!Files.exists(path)) {
            instance = new DasikClientConfig();
            instance.save(path);
            return;
        }

        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            DasikClientConfig loaded = GSON.fromJson(reader, DasikClientConfig.class);
            instance = (loaded != null) ? loaded : new DasikClientConfig();
        } catch (Throwable t) {
            LOGGER.error("Failed to load client config from {}, falling back to defaults: {}", path, t.getMessage());
            instance = new DasikClientConfig();
        }
    }

    /**
     * Persists the current configuration to the default configuration path.
     */
    public void save() {
        save(resolveConfigPath());
    }

    /**
     * Persists the current configuration to a specified path atomically.
     */
    public void save(Path path) {
        if (path == null) {
            return;
        }

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            Path tempPath = path.resolveSibling(path.getFileName().toString() + ".tmp");
            try (Writer writer = Files.newBufferedWriter(tempPath, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            }

            try {
                Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (Exception e) {
                Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Throwable t) {
            LOGGER.error("Failed to persist client config to {}: {}", path, t.getMessage());
        }
    }

    public static boolean isShowSupportPrompts() {
        return get().showSupportPrompts;
    }

    public static void setShowSupportPrompts(boolean show) {
        get().showSupportPrompts = show;
        get().save();
    }

    public static boolean isShowWorldStartGreeting() {
        return get().showWorldStartGreeting;
    }

    public static void setShowWorldStartGreeting(boolean show) {
        get().showWorldStartGreeting = show;
        get().save();
    }
}
