/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Level.java (Snapshot 10)
 */
package net.dasik.social.util;

/**
 * Utility class for time-related calculations within the social engine.
 */
public class TimeUtil {
    /** 
     * Calculates the time distance to a target in a cyclical timeline.
     * @param cycleLength Total length of the cycle (e.g., 24000 for a Minecraft day).
     */
    public static long getCycleDistance(long current, long target, long cycleLength) {
        long dist = target - current % cycleLength;
        if (dist < 0L) {
            dist += cycleLength;
        }
        return dist;
    }

    /** 
     * @return The current in-game day count.
     */
    public static long getGameDay(long gameTime) {
        return gameTime / 24000L;
    }
}

