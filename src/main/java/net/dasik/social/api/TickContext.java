/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: TickContext.java (Snapshot 10)
 */
package net.dasik.social.api;

import net.minecraft.util.RandomSource;

/**
 * Provides world-specific context for a single logic tick.
 * Java 25 records provide optimal performance and thread-safety for high-frequency ticking.
 */
public record TickContext(
    SocialEntity entity,
    long gameTime,
    float partialTick,
    RandomSource random
) {
}
