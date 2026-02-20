package net.dasik.social.util;

/**
 * Advanced Chronos Engineering.
 * Handles Minecraft 24,000-tick cycle arithmetic with stride safety.
 */
public class TimeUtil {

    /**
     * Calculates the positive distance from current to target in a cyclic system.
     * 
     * @param current     The current time.
     * @param target      The target time.
     * @param cycleLength The length of the cycle (e.g., 24000).
     * @return The distance to be traveled (positive).
     */
    public static long getCycleDistance(long current, long target, long cycleLength) {
        long dist = (target - (current % cycleLength));
        if (dist < 0) {
            dist += cycleLength;
        }
        return dist;
    }

    /**
     * @param gameTime The total game time.
     * @return The current number of elapsed game days (0-indexed).
     */
    public static long getGameDay(long gameTime) {
        return gameTime / 24000L;
    }
}
