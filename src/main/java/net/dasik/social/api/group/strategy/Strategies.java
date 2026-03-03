/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.api.group.strategy;

/**
 * Standard collection of flocking strategies.
 */
public class Strategies {
    public static final FlockingStrategy AERIAL = new AerialFlockingStrategy();
    public static final FlockingStrategy TERRESTRIAL = new TerrestrialFlockingStrategy();
}
