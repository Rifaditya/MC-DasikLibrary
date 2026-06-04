/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: ServerLevel.java (Snapshot 10)
 */
package net.dasik.social.core;

import java.util.concurrent.atomic.AtomicLong;
import net.dasik.social.api.SocialEntity;
import net.dasik.social.api.SocialEventRegistry;
import net.dasik.social.api.SocialScheduler;
import net.dasik.social.config.PerformanceConfig;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The core engine of the social system. Manages the global ticking pulse and event registration lifecycle.
 */
public class GlobalSocialSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikHiveMind");
    public static final int ENGINE_VERSION = 200;
    private static final AtomicLong LAST_TICK = new AtomicLong(-1L);
    private static int throttleInterval = 1;

    public static void setThrottle(int interval) {
        throttleInterval = Math.max(1, interval);
    }

    /**
     * Executes the social pulse for the given level.
     * Manages performance budgets to ensure server stability.
     */
    public static void pulse(ServerLevel level) {
        SocialEntity entity;
        long time = level.getGameTime();
        
        if (LAST_TICK.getAndSet(time) == time) {
            return;
        }
        
        if (time % (long)throttleInterval != 0L) {
            return;
        }
        
        if (!SocialEventRegistry.isFrozen()) {
            SocialEventRegistry.freeze();
            LOGGER.info("GlobalSocialSystem: Starting pulse. SocialEventRegistry frozen.");
        }
        
        long startNanos = System.nanoTime();
        int budget = PerformanceConfig.ENTITIES_PER_TICK;
        
        for (int processed = 0; processed < budget && (System.nanoTime() - startNanos) / 1000L <= PerformanceConfig.PULSE_BUDGET_MICROS && (entity = SocialRegistry.getRandomEntity()) != null; ++processed) {
            SocialScheduler scheduler = entity.dasik$getScheduler();
            if (scheduler == null) continue;
            scheduler.tick();
        }
    }
}

