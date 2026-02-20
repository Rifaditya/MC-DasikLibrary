package net.dasik.social.api.group.strategy;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class TerrestrialFlockingStrategy implements FlockingStrategy {

    @Override
    public void execute(LivingEntity mob, LivingEntity leader, GroupParameters params) {
        if (!(mob instanceof Mob groundedMob) || leader == null)
            return;

        double distSq = groundedMob.distanceToSqr(leader);
        PathNavigation navigation = groundedMob.getNavigation();

        // Separation: Too close, stop moving towards leader (let engine handle basic
        // push if extremely close)
        if (distSq < params.separationRadius() * params.separationRadius()) {
            navigation.stop();
            return;
        }

        // Cohesion: If outside cohesion radius, pathfind to leader
        if (distSq > params.cohesionRadius() * params.cohesionRadius()) {

            // Speed up if we are very far behind (e.g., more than double the cohesion
            // radius)
            double speedMod = params.maxSpeed();
            if (distSq > (params.cohesionRadius() * 2) * (params.cohesionRadius() * 2)) {
                speedMod *= 1.5;
            }

            navigation.moveTo(leader, speedMod);
        } else {
            // We are inside the cohesion and separation 'sweet spot'.
            // Small chance to just align rotation and stop to look natural.
            if (groundedMob.getRandom().nextInt(10) == 0) {
                navigation.stop();
                groundedMob.getLookControl().setLookAt(leader, 10.0F, (float) groundedMob.getMaxHeadXRot());
            }
        }
    }
}
