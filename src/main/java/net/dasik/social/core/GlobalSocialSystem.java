package net.dasik.social.core;

import net.dasik.social.api.SocialEntity;
import net.dasik.social.api.SocialEventRegistry;
import net.dasik.social.api.SocialScheduler;
import net.dasik.social.config.PerformanceConfig;
import net.minecraft.server.level.ServerLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicLong;

/**
 * The Highlander Control Center.
 * Ensures strict single-execution per tick regardless of how many mods bundle
 * the library.
 */
public class GlobalSocialSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikHiveMind");

    // Version Handshake
    public static final int ENGINE_VERSION = 200; // v2.0.0 Architecture

    // Idempotency Guard
    private static final AtomicLong LAST_TICK = new AtomicLong(-1);

    /**
     * The Master Pulse.
     * Called by ServerTickEvents.
     */
    public static void pulse(ServerLevel level) {
        long time = level.getGameTime();

        // 1. Highlander Check: Only run once per tick
        if (LAST_TICK.getAndSet(time) == time) {
            return;
        }

        // Lazy Freeze: Lock registry on first pulse (Concept: Registry mutable during
        // init, frozen at runtime)
        if (!SocialEventRegistry.isFrozen()) {
            SocialEventRegistry.freeze();
            LOGGER.info("GlobalSocialSystem: Starting pulse. SocialEventRegistry frozen.");
        }

        // 2. Load Balanced Pulse
        long startNanos = System.nanoTime();
        int budget = PerformanceConfig.ENTITIES_PER_TICK;
        int processed = 0;

        while (processed < budget) {
            // Check Time Budget
            if ((System.nanoTime() - startNanos) / 1000 > PerformanceConfig.PULSE_BUDGET_MICROS) {
                break;
            }

            // O(1) Random Selection
            SocialEntity entity = SocialRegistry.getRandomEntity();
            if (entity == null)
                break; // Registry empty

            SocialScheduler scheduler = entity.dasik$getScheduler();
            if (scheduler != null) {
                scheduler.tick();
            }

            processed++;
        }
    }
}
