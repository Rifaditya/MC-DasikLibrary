/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.api.group.strategy;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

/**
 * Flocking strategy for land-bound entities.
 * Focuses on cohesion and separation while respecting ground navigation boundaries.
 */
public class TerrestrialFlockingStrategy implements FlockingStrategy {
    @Override
    public void execute(LivingEntity mob, LivingEntity leader, GroupParameters params) {
        if (!(mob instanceof Mob groundedMob) || leader == null) {
            return;
        }

        double distSq = groundedMob.distanceToSqr(leader);
        PathNavigation navigation = groundedMob.getNavigation();

        if (distSq < (double)(params.separationRadius() * params.separationRadius())) {
            navigation.stop();
            return;
        }

        if (distSq > (double)(params.cohesionRadius() * params.cohesionRadius())) {
            double speedMod = params.maxSpeed();
            if (distSq > (double)(params.cohesionRadius() * 2.0f * (params.cohesionRadius() * 2.0f))) {
                speedMod *= 1.5;
            }
            navigation.moveTo(leader, speedMod);
        } else if (groundedMob.getRandom().nextInt(10) == 0) {
            navigation.stop();
            groundedMob.getLookControl().setLookAt(leader, 10.0f, (float)groundedMob.getMaxHeadXRot());
        }
    }
}
