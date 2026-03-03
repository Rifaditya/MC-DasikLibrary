/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 */
package net.dasik.social.util;

import net.minecraft.util.RandomSource;

public class StochasticUtil {
    public static int getAmplifiedCount(int originalCount, int multiplierPercent, RandomSource random) {
        float multiplier = (float)multiplierPercent / 100.0f;
        float targetCount = (float)originalCount * multiplier;
        int baseCount = (int)targetCount;
        float residue = targetCount - (float)baseCount;
        if (random.nextFloat() < residue) {
            ++baseCount;
        }
        return baseCount;
    }

    public static boolean getAmplifiedProbability(float baseProbability, int multiplierPercent, RandomSource random) {
        float multiplier = (float)multiplierPercent / 100.0f;
        float targetProbability = baseProbability * multiplier;
        if (targetProbability >= 1.0f) {
            return true;
        }
        if (targetProbability <= 0.0f) {
            return false;
        }
        return random.nextFloat() < targetProbability;
    }
}

