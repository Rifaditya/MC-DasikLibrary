/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.api.group.strategy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

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

        if (params.canTeleport() && distSq >= params.teleportDistance()) {
            this.tryToTeleportToLeader(groundedMob, leader);
            return;
        }

        if (distSq < (double)(params.separationRadius() * params.separationRadius())) {
            double distance = Math.sqrt(distSq);
            // Distance-Based Linear Interpolation (Lerp) on the speedModifier.
            double speedMod = params.maxSpeed() * (distance / params.separationRadius());
            
            if (distance < 0.5) {
                navigation.stop(); // Only fully stop if extremely close to prevent pushing/jitter
            } else {
                navigation.moveTo(leader, speedMod);
            }
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

    protected void tryToTeleportToLeader(Mob mob, LivingEntity leader) {
        BlockPos targetPos = leader.blockPosition();
        for (int attempt = 0; attempt < 10; attempt++) {
            int xd = mob.getRandom().nextIntBetweenInclusive(-3, 3);
            int zd = mob.getRandom().nextIntBetweenInclusive(-3, 3);
            if (Math.abs(xd) >= 2 || Math.abs(zd) >= 2) {
                int yd = mob.getRandom().nextIntBetweenInclusive(-1, 1);
                if (this.maybeTeleportTo(mob, targetPos.getX() + xd, targetPos.getY() + yd, targetPos.getZ() + zd)) {
                    return;
                }
            }
        }
    }

    private boolean maybeTeleportTo(Mob mob, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (!this.canTeleportTo(mob, pos)) {
            return false;
        } else {
            mob.snapTo((double)x + 0.5, (double)y, (double)z + 0.5, mob.getYRot(), mob.getXRot());
            mob.getNavigation().stop();
            return true;
        }
    }

    private boolean canTeleportTo(Mob mob, BlockPos pos) {
        PathType pathType = WalkNodeEvaluator.getPathTypeStatic(mob, pos);
        if (pathType != PathType.WALKABLE) {
            return false;
        } else {
            BlockState blockStateBelow = mob.level().getBlockState(pos.below());
            if (blockStateBelow.getBlock() instanceof LeavesBlock) {
                return false;
            } else {
                BlockPos delta = pos.subtract(mob.blockPosition());
                return mob.level().noCollision(mob, mob.getBoundingBox().move(delta));
            }
        }
    }
}
