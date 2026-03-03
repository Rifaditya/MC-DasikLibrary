/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.util;

public class TimeUtil {
    public static long getCycleDistance(long current, long target, long cycleLength) {
        long dist = target - current % cycleLength;
        if (dist < 0L) {
            dist += cycleLength;
        }
        return dist;
    }

    public static long getGameDay(long gameTime) {
        return gameTime / 24000L;
    }
}

