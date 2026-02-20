package net.dasik.social.api.group.strategy;

/**
 * Parameters guiding the flocking movement algorithm.
 * 
 * @param cohesionRadius   The distance at which the entity will try to stick
 *                         close to the leader.
 * @param separationRadius The distance at which the entity will try to move
 *                         away from other flock members to avoid crowding.
 * @param maxSpeed         The maximum speed the entity can travel while
 *                         flocking / following.
 */
public record GroupParameters(float cohesionRadius, float separationRadius, float maxSpeed) {

    // Default sensible values, consuming mods can override.
    public static final GroupParameters DEFAULT_AERIAL = new GroupParameters(3.0F, 1.0F, 0.4F);
    public static final GroupParameters DEFAULT_TERRESTRIAL = new GroupParameters(5.0F, 1.5F, 1.2F);
}
