package net.dasik.social;

import net.fabricmc.api.ModInitializer;
import net.dasik.social.core.GlobalSocialSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DasikLibraryMod implements ModInitializer {
    public static final String MOD_ID = "dasik-library";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Dasik Library (Engine v{})", GlobalSocialSystem.ENGINE_VERSION);
    }
}
