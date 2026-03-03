/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.phys.Vec3
 */
package net.dasik.social.api.group.strategy;

import net.dasik.social.api.group.strategy.FlockingStrategy;
import net.dasik.social.api.group.strategy.GroupParameters;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

public class AerialFlockingStrategy
implements FlockingStrategy {
    @Override
    public void execute(LivingEntity mob, LivingEntity leader, GroupParameters params) {
        double speedSq;
        if (!(mob instanceof Mob)) {
            return;
        }
        Mob bat = (Mob)mob;
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
        if ((speedSq = newVelocity.lengthSqr()) > (double)(params.maxSpeed() * params.maxSpeed())) {
            newVelocity = newVelocity.normalize().scale((double)params.maxSpeed());
        }
        bat.setDeltaMovement(newVelocity);
    }
}

