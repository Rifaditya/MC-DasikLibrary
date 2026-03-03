/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.api.group.strategy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

/**
 * Flocking strategy for flying entities.
 * Focuses on 3D spatial positioning and world avoidance.
 */
public class AerialFlockingStrategy implements FlockingStrategy {
    @Override
    public void execute(LivingEntity mob, LivingEntity leader, GroupParameters params) {
        if (!(mob instanceof Mob bat)) {
            return;
        }

        Vec3 leaderPos = leader.position();
        Vec3 myPos = bat.position();
        Vec3 delta = leaderPos.subtract(myPos);
        double distSq = delta.lengthSqr();
        Vec3 newVelocity = bat.getDeltaMovement();

        if (distSq > (double)(params.cohesionRadius() * params.cohesionRadius())) {
            newVelocity = newVelocity.add(delta.normalize().scale(0.05));
        }
        if (distSq < (double)(params.separationRadius() * params.separationRadius())) {
            newVelocity = newVelocity.subtract(delta.normalize().scale(0.1));
        }

        BlockPos pos = bat.blockPosition();
        if (bat.level().getBlockState(pos.below()).blocksMotion()) {
            newVelocity = newVelocity.add(0.0, 0.1, 0.0);
        }
        if (bat.level().getBlockState(pos.above()).blocksMotion()) {
            newVelocity = newVelocity.add(0.0, -0.1, 0.0);
        }

        double maxSpeed = params.maxSpeed();
        if (newVelocity.lengthSqr() > maxSpeed * maxSpeed) {
            newVelocity = newVelocity.normalize().scale(maxSpeed);
        }
        bat.setDeltaMovement(newVelocity);
    }
}
