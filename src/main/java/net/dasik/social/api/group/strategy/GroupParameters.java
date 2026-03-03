/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.api.group.strategy;

/**
 * Parameters governing flocking behavior heuristics.
 */
public record GroupParameters(float cohesionRadius, float separationRadius, float maxSpeed) {
    public static final GroupParameters DEFAULT_AERIAL = new GroupParameters(3.0f, 1.0f, 0.4f);
    public static final GroupParameters DEFAULT_TERRESTRIAL = new GroupParameters(5.0f, 1.5f, 1.2f);
}
