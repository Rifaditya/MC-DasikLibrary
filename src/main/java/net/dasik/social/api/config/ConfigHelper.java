/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Files.java (Java 25)
 */
package net.dasik.social.api.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.BiConsumer;
import java.util.function.ToIntFunction;

/**
 * Standard generic configuration manager offering file-size safety, atomic swaps, and backup generation.
 */
public class ConfigHelper {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1MB

    /**
     * Loads a configuration from a file. If the file does not exist, copies the default from resources or generates defaults.
     * Enforces a 1MB file size limit, UTF-8 encoding, and performs automatic backup creation on version bump.
     */
    public static <T> T load(
            Path configPath,
            T defaultInstance,
            Class<T> clazz,
            int defaultVersion,
            ToIntFunction<T> versionGetter,
            BiConsumer<T, Integer> versionSetter,
            String resourcePath,
            Logger logger) {

        if (!Files.exists(configPath)) {
            if (resourcePath != null) {
                logger.info("Configuration file {} not found, copying default configuration from resources", configPath.getFileName());
                try (InputStream in = clazz.getResourceAsStream(resourcePath)) {
                    if (in != null) {
                        Files.createDirectories(configPath.getParent());
                        Files.copy(in, configPath);
                        return loadFromFile(configPath, defaultInstance, clazz, defaultVersion, versionGetter, versionSetter, logger);
                    } else {
                        logger.warn("Default configuration resource {} not found in JAR! Generating from code defaults.", resourcePath);
                    }
                } catch (Exception e) {
                    logger.error("Failed to copy default configuration from resources, generating defaults from code.", e);
                }
            }
            save(configPath, defaultInstance, logger);
            return defaultInstance;
        }

        return loadFromFile(configPath, defaultInstance, clazz, defaultVersion, versionGetter, versionSetter, logger);
    }

    private static <T> T loadFromFile(
            Path configPath,
            T defaultInstance,
            Class<T> clazz,
            int defaultVersion,
            ToIntFunction<T> versionGetter,
            BiConsumer<T, Integer> versionSetter,
            Logger logger) {
        try {
            long size = Files.size(configPath);
            if (size > MAX_FILE_SIZE) {
                logger.error("Configuration file {} is too large ({} bytes). Using defaults for safety!", configPath.getFileName(), size);
                return defaultInstance;
            }

            try (Reader reader = Files.newBufferedReader(configPath, StandardCharsets.UTF_8)) {
                T loadedInstance = GSON.fromJson(reader, clazz);
                if (loadedInstance != null) {
                    int loadedVersion = versionGetter.applyAsInt(loadedInstance);
                    if (loadedVersion < defaultVersion) {
                        logger.info("Old configuration version {} detected for {}. Backing up and updating to {}...", 
                                loadedVersion, configPath.getFileName(), defaultVersion);
                        createBackup(configPath, logger);
                        versionSetter.accept(loadedInstance, defaultVersion);
                        save(configPath, loadedInstance, logger);
                    } else {
                        // Resave to keep config synchronized with any new code defaults
                        save(configPath, loadedInstance, logger);
                    }
                    return loadedInstance;
                }
            }
        } catch (Exception e) {
            logger.error("Critical error loading configuration for {}. Using defaults.", configPath.getFileName(), e);
        }
        return defaultInstance;
    }

    /**
     * Saves a configuration to a file atomically via temp swapping.
     */
    public static <T> void save(Path configPath, T instance, Logger logger) {
        try {
            Files.createDirectories(configPath.getParent());
            Path tempPath = configPath.resolveSibling(configPath.getFileName().toString() + ".tmp");
            
            try (Writer writer = Files.newBufferedWriter(tempPath, StandardCharsets.UTF_8)) {
                GSON.toJson(instance, writer);
            }

            try {
                Files.move(tempPath, configPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (Exception e) {
                Files.move(tempPath, configPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception e) {
            logger.error("Failed to save configuration safely for {}!", configPath.getFileName(), e);
        }
    }

    /**
     * Creates a backup of the config file.
     */
    private static void createBackup(Path configPath, Logger logger) {
        try {
            Path backupPath = configPath.resolveSibling(configPath.getFileName().toString() + ".bak");
            Files.copy(configPath, backupPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            logger.error("Failed to create configuration backup for {}!", configPath.getFileName(), e);
        }
    }
}
