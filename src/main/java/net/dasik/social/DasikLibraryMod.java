/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: ModInitializer.java (Snapshot 10)
 */
package net.dasik.social;

import net.dasik.social.api.vision.PlayerVisionTracker;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DasikLibraryMod implements ModInitializer {
    public static final String MOD_ID = "dasik-library";
    public static final Logger LOGGER = LoggerFactory.getLogger("dasik-library");

    @Override
    public void onInitialize() {
        net.dasik.social.util.ModVersionGuard.checkClass("Dasik Library", "net.minecraft.world.entity.EntityTypes");
        LOGGER.info("Initializing Dasik Library (Engine v{})", 200);
        PlayerVisionTracker.init();
        var ignored = net.dasik.social.api.genetics.GeneticsEngine.GENETICS;
    }
}
