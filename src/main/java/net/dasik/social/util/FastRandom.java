package net.dasik.social.util;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.concurrent.ThreadLocalRandom;

/**
 * High-performance, thread-safe implementation of RandomSource
 * using ThreadLocalRandom to avoid contention.
 */
public class FastRandom implements RandomSource {

    public static final FastRandom INSTANCE = new FastRandom();

    private FastRandom() {
    }

    @Override
    public RandomSource fork() {
        return new FastRandom();
    }

    @Override
    public PositionalRandomFactory forkPositional() {
        throw new UnsupportedOperationException("FastRandom does not support positional forking");
    }

    @Override
    public void setSeed(long seed) {
        // ThreadLocalRandom cannot be seeded
    }

    @Override
    public int nextInt() {
        return ThreadLocalRandom.current().nextInt();
    }

    @Override
    public int nextInt(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    @Override
    public long nextLong() {
        return ThreadLocalRandom.current().nextLong();
    }

    @Override
    public boolean nextBoolean() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    @Override
    public float nextFloat() {
        return ThreadLocalRandom.current().nextFloat();
    }

    @Override
    public double nextDouble() {
        return ThreadLocalRandom.current().nextDouble();
    }

    @Override
    public double nextGaussian() {
        return ThreadLocalRandom.current().nextGaussian();
    }
}
