package net.dasik.social.api;

import net.minecraft.util.RandomSource;

/**
 * Context object for social tick events.
 * Passed to all {@link SocialEvent} ticks to allow future expansion of data
 * without breaking method signatures.
 * <p>
 * This is part of the 100-Year API Contract.
 */
public interface TickContext {

    /**
     * @return The social entity being ticked.
     */
    SocialEntity entity();

    /**
     * @return Current game time in ticks.
     */
    long gameTime();

    /**
     * @return Partial tick time for interpolation.
     */
    float partialTick();

    /**
     * @return Safe random source (do not use java.util.Random).
     */
    RandomSource random();
}
