package net.dasik.social.util;

import net.minecraft.util.RandomSource;

public class StochasticUtil {
    /**
     * Calculates the new count using stochastic rounding for fractional results.
     * e.g. Count 1 * 50% = 0.5 -> 50% chance of 1, 50% chance of 0.
     * <p>
     * Logic extracted from Ore Amplifier (OreLogic.java).
     * 
     * @param originalCount     The initial count (e.g., number of veins).
     * @param multiplierPercent The multiplier as a percentage (e.g., 50 for 50%,
     *                          200 for 200%).
     * @param random            The RandomSource to use for probability.
     * @return The amplified count, stochastically rounded.
     */
    public static int getAmplifiedCount(int originalCount, int multiplierPercent, RandomSource random) {
        float multiplier = multiplierPercent / 100.0F;
        float targetCount = originalCount * multiplier;

        int baseCount = (int) targetCount;
        float residue = targetCount - baseCount;

        if (random.nextFloat() < residue) {
            baseCount++;
        }

        return baseCount;
    }

    /**
     * Calculates if an event should occur based on an amplified probability.
     * 
     * @param baseProbability   The initial probability (0.0 to 1.0).
     * @param multiplierPercent The multiplier as a percentage.
     * @param random            The RandomSource to use.
     * @return True if the event should occur.
     */
    public static boolean getAmplifiedProbability(float baseProbability, int multiplierPercent, RandomSource random) {
        float multiplier = multiplierPercent / 100.0F;
        float targetProbability = baseProbability * multiplier;

        if (targetProbability >= 1.0F)
            return true;
        if (targetProbability <= 0.0F)
            return false;

        return random.nextFloat() < targetProbability;
    }
}
