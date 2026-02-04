package net.dasik.social.api.group.strategy;

import net.dasik.social.api.group.GroupParameters;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class Strategies {

    /**
     * Standard Boid-like steering for air entities.
     * Logic ported from Bat Ecology.
     */
    public static final FlockingStrategy AERIAL = (follower, leader, params, groupSize) -> {
        if (leader == null || follower == leader)
            return;

        double dx = leader.getX() - follower.getX();
        double dy = leader.getY() - follower.getY();
        double dz = leader.getZ() - follower.getZ();
        double distSq = dx * dx + dy * dy + dz * dz;

        // Dynamic spread calculation (sqrt scaling)
        double baseColonySize = 10.0;
        double spreadMult = (groupSize <= baseColonySize) ? 1.0 : Math.sqrt((double) groupSize / baseColonySize);

        double effectiveMinDist = params.cohesionRadius() * spreadMult; // Use larger radius for spread
        double effectiveMaxDist = params.separationRadius() * spreadMult; // Wait, Bat logic naming might be inverse?

        // Re-reading Bat logic:
        // minDistSq = (BASE_MIN_DIST * spread) ^ 2
        // maxDistSq = (BASE_MAX_DIST * spread) ^ 2
        // if dist > min && dist < max -> Apply Cohesion

        double minDistSq = (params.separationRadius() * spreadMult) * (params.separationRadius() * spreadMult);
        double maxDistSq = (params.cohesionRadius() * spreadMult) * (params.cohesionRadius() * spreadMult);

        Vec3 movement = follower.getDeltaMovement();
        boolean changed = false;

        // 1. Cohesion (Steer towards leader if within range)
        if (distSq > minDistSq && distSq < maxDistSq) {
            double strength = params.cohesionForce() / spreadMult;
            movement = movement.add(dx * strength, dy * strength, dz * strength);
            changed = true;
        }

        // 2. Floor Avoidance (Soft height check)
        BlockPos pos = follower.blockPosition();
        boolean closeToGround = false;
        for (int i = 1; i <= 5; i++) {
            if (!follower.level().isEmptyBlock(pos.below(i))) {
                closeToGround = true;
                break;
            }
        }

        if (closeToGround) {
            movement = movement.add(0.0, 0.08, 0.0);
            changed = true;
        }

        if (changed) {
            if (movement.lengthSqr() > params.maxSpeed() * params.maxSpeed()) {
                movement = movement.normalize().scale(params.maxSpeed());
            }
            follower.setDeltaMovement(movement);
        }
    };

    /**
     * Standard pathfinding for ground entities.
     * Uses Navigation system.
     */
    public static final FlockingStrategy TERRESTRIAL = (follower, leader, params, groupSize) -> {
        if (leader == null || follower == leader)
            return;

        double distSq = follower.distanceToSqr(leader);
        double stopDistSq = params.separationRadius() * params.separationRadius();

        // If too close, stop or move away?
        // For basic following: simple moveTo

        if (distSq > stopDistSq) {
            follower.getNavigation().moveTo(leader, 1.0);
        } else {
            // Too close, maybe stop to avoid crowding
            follower.getNavigation().stop();
        }
    };
}
