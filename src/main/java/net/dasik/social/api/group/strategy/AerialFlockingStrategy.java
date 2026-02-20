package net.dasik.social.api.group.strategy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class AerialFlockingStrategy implements FlockingStrategy {

    @Override
    public void execute(LivingEntity mob, LivingEntity leader, GroupParameters params) {
        if (!(mob instanceof Mob bat))
            return;

        // Cohesion: Move towards leader
        Vec3 leaderPos = leader.position();
        Vec3 myPos = bat.position();
        Vec3 delta = leaderPos.subtract(myPos);
        double distSq = delta.lengthSqr();

        Vec3 newVelocity = bat.getDeltaMovement();

        // If we're further than the cohesion radius, accelerate towards the leader
        if (distSq > params.cohesionRadius() * params.cohesionRadius()) {
            newVelocity = newVelocity.add(delta.normalize().scale(0.05));
        }

        // Separation: If we're too close to the leader, push away slightly
        if (distSq < params.separationRadius() * params.separationRadius()) {
            newVelocity = newVelocity.subtract(delta.normalize().scale(0.1));
        }

        // Basic Environment avoidance (Floor/Ceiling)
        BlockPos pos = bat.blockPosition();
        if (bat.level().getBlockState(pos.below()).blocksMotion()) {
            newVelocity = newVelocity.add(0, 0.1, 0); // Push up
        }
        if (bat.level().getBlockState(pos.above()).blocksMotion()) {
            newVelocity = newVelocity.add(0, -0.1, 0); // Push down
        }

        // Clamp speed
        double speedSq = newVelocity.lengthSqr();
        if (speedSq > params.maxSpeed() * params.maxSpeed()) {
            newVelocity = newVelocity.normalize().scale(params.maxSpeed());
        }

        bat.setDeltaMovement(newVelocity);
    }
}
