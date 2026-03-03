/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.level.levelgen.PositionalRandomFactory
 */
package net.dasik.social.util;

import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public class FastRandom
implements RandomSource {
    public static final FastRandom INSTANCE = new FastRandom();

    private FastRandom() {
    }

    public RandomSource fork() {
        return new FastRandom();
    }

    public PositionalRandomFactory forkPositional() {
        throw new UnsupportedOperationException("FastRandom does not support positional forking");
    }

    public void setSeed(long seed) {
    }

    public int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    public int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    public long nextLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    public boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    public float nextFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    public double nextGaussian() {
        return ThreadLocalRandom.current().nextGaussian();
    }
}

