package net.dasik.social.api.group;

public record GroupParameters(
        double cohesionRadius, // Max distance to be considered "close enough" to flock
        double separationRadius, // Distance to start avoiding collision
        double maxSpeed, // Max velocity for aerial movement
        double separationForce, // How hard to push away from neighbors
        double cohesionForce // How hard to pull towards leader
) {
    public static final GroupParameters DEFAULT_AERIAL = new GroupParameters(16.0, 2.0, 0.5, 0.1, 0.05);
    public static final GroupParameters DEFAULT_TERRESTRIAL = new GroupParameters(10.0, 1.5, 1.0, 1.0, 1.0);
}
