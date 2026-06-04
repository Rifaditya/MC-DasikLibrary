/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.api.group.strategy;

/**
 * Parameters governing flocking behavior heuristics.
 */
public record GroupParameters(
    float cohesionRadius, 
    float separationRadius, 
    float maxSpeed, 
    boolean canTeleport, 
    float teleportDistance,
    float startDistance,
    float stopDistance,
    float alignmentWeight,
    float cohesionWeight,
    float separationWeight
) {
    /**
     * Legacy constructor for binary compatibility (Build 1.6.9 and earlier).
     * Delegates to canonical constructor with default Zenith terrestrial boid parameters.
     */
    public GroupParameters(float cohesionRadius, float separationRadius, float maxSpeed) {
        this(cohesionRadius, separationRadius, maxSpeed, true, 144.0f, 6.0f, 2.0f, 0.0f, 0.0f, 0.0f);
    }

    public static final GroupParameters DEFAULT_AERIAL = new GroupParameters(3.0f, 1.0f, 0.4f, true, 144.0f, 6.0f, 2.0f, 0.05f, 0.05f, 0.1f);
    public static final GroupParameters DEFAULT_TERRESTRIAL = new GroupParameters(5.0f, 1.5f, 1.2f, true, 144.0f, 6.0f, 2.0f, 0.0f, 0.0f, 0.0f);
}
