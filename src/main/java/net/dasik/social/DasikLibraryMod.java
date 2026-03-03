/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ModInitializer
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package net.dasik.social;

import net.dasik.social.api.vision.PlayerVisionTracker;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DasikLibraryMod
implements ModInitializer {
    public static final String MOD_ID = "dasik-library";
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"dasik-library");

    public void onInitialize() {
        LOGGER.info("Initializing Dasik Library (Engine v{})", (Object)200);
        PlayerVisionTracker.init();
    }
}

