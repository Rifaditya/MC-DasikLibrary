// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: ModInitializer.java (Snapshot 10)
package net.dasik.social;

import net.dasik.social.api.command.ModCommandDispatcher;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.vision.PlayerVisionTracker;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DasikLibraryMod implements ModInitializer {
    public static final String MOD_ID = "dasik-library";
    public static final Logger LOGGER = LoggerFactory.getLogger("dasik-library");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Dasik Library (Engine v{})", 200);
        PlayerVisionTracker.init();
        ModCommandDispatcher.init();
        var ignored = net.dasik.social.api.genetics.GeneticsEngine.GENETICS;

        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            int pruned = DynamicGameRuleManager.pruneOrphanedRules();
            if (pruned > 0) {
                LOGGER.info("DynamicGameRuleManager: Pruned {} orphaned dynamic GameRule(s) from uninstalled mods.", pruned);
            }
        });
    }
}
