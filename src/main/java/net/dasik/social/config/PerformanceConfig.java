package net.dasik.social.config;

/**
 * Tunable performance configuration for the Hive Mind.
 * Values can be hot-swapped or loaded from config files.
 */
public class PerformanceConfig {

    /**
     * Circuit Breaker: Max tracked entities before rejecting new registrations.
     * Prevents memory exhaustion from runaway spawning.
     */
    public static int MAX_ENTITIES = 250_000;

    /**
     * Number of random entities to pulse per tick.
     */
    public static int ENTITIES_PER_TICK = 8;

    /**
     * Max time (in microseconds) the pulse can take per tick.
     * 500us = 0.5ms = 1% of a 50ms tick.
     */
    public static long PULSE_BUDGET_MICROS = 500;

    /**
     * If true, throttle pulses dynamically based on server load.
     */
    public static boolean ENABLE_ADAPTIVE_THROTTLING = true;

    /**
     * Enable debug metrics logging.
     */
    public static boolean ENABLE_METRICS = false;
}
