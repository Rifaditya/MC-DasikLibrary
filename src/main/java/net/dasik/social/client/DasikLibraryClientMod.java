// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.client;

import net.dasik.social.api.client.prompt.ClientPromptManager;
import net.dasik.social.api.config.DasikClientConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client entrypoint for DasikLibrary.
 * Coordinates client configuration loading, connection session tracking, and prompt ticking.
 */
@Environment(EnvType.CLIENT)
public class DasikLibraryClientMod implements ClientModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|Client");

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing DasikLibrary client module...");

        // 1. Preload client configuration
        DasikClientConfig.load();

        // 2. Wire connection lifecycle events
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            ClientPromptManager.onWorldJoin(client);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            ClientPromptManager.onWorldDisconnect();
        });

        // 3. Wire client tick listener for greeting and milestone pacing
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientPromptManager.onClientTick(client);
        });

        LOGGER.info("DasikLibrary client module initialized successfully.");
    }
}
