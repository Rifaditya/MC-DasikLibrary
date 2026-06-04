/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: ModInitializer.java (Snapshot 10)
 */
package net.dasik.social;

import net.dasik.social.api.vision.PlayerVisionTracker;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(DasikLibraryMod.MOD_ID)
public class DasikLibraryMod {
    public static final String MOD_ID = "dasiklibrary";
    public static final Logger LOGGER = LoggerFactory.getLogger("dasiklibrary");

    public DasikLibraryMod(IEventBus modEventBus) {
        LOGGER.info("Initializing Dasik Library (Engine v{})", 200);
        PlayerVisionTracker.init();
    }
}
