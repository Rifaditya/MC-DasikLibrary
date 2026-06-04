/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.api.group.strategy;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.dasik.social.api.group.GroupMember;
import net.dasik.social.core.group.FlockState;
import java.util.List;

/**
 * Flocking strategy for flying entities.
 * Focuses on 3D spatial positioning and world avoidance using the Cached Boids Pattern.
 */
public class AerialFlockingStrategy implements FlockingStrategy {
    @Override
    public void execute(LivingEntity mob, LivingEntity leader, GroupParameters params) {
        if (!(mob instanceof Mob bat)) {
            return;
        }

        double leaderDistSq = bat.distanceToSqr(leader);
        if (params.canTeleport() && leaderDistSq >= params.teleportDistance()) {
            this.tryToTeleportToLeader(bat, leader);
            return;
        }

        GroupMember leaderGM = (GroupMember) leader;
        FlockState state = leaderGM.getFlockState();
        if (state == null) {
            // Fallback while waiting for GroupManager compute
            Vec3 fallbackDir = leader.position().subtract(bat.position());
            if (fallbackDir.lengthSqr() > params.cohesionRadius() * params.cohesionRadius()) {
                bat.setDeltaMovement(bat.getDeltaMovement().add(fallbackDir.normalize().scale(0.05)));
            }
            return; 
        }

        Vec3 myPos = bat.position();
        Vec3 newVelocity = bat.getDeltaMovement();

        // Cohesion: Steer towards the flock's center of mass
        Vec3 com = state.getCenterOfMass();
        Vec3 cohesionDir = com.subtract(myPos);
        double distToComSq = cohesionDir.lengthSqr();
        
        if (distToComSq > (double)(params.cohesionRadius() * params.cohesionRadius())) {
            newVelocity = newVelocity.add(cohesionDir.normalize().scale(0.05));
        }

        // Alignment: Steer towards the flock's average velocity
        Vec3 avgVel = state.getAverageVelocity();
        if (avgVel.lengthSqr() > 0.001) {
            newVelocity = newVelocity.add(avgVel.normalize().scale(0.05));
        }

        // Separation: Fast proximity check against immediately nearby entities (radius 1.0)
        List<LivingEntity> peers = bat.level().getEntitiesOfClass(
            LivingEntity.class, 
            bat.getBoundingBox().inflate(1.0), 
            e -> e != bat && e.isAlive()
        );

        for (LivingEntity peer : peers) {
            Vec3 avoidDir = myPos.subtract(peer.position());
            double distSq = avoidDir.lengthSqr();
            if (distSq < 1.0 && distSq > 0.0001) {
                newVelocity = newVelocity.add(avoidDir.normalize().scale(0.1 / Math.sqrt(distSq)));
            }
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

    protected void tryToTeleportToLeader(Mob mob, LivingEntity leader) {
        BlockPos targetPos = leader.blockPosition();
        for (int attempt = 0; attempt < 10; attempt++) {
            int xd = mob.getRandom().nextIntBetweenInclusive(-3, 3);
            int zd = mob.getRandom().nextIntBetweenInclusive(-3, 3);
            if (Math.abs(xd) >= 2 || Math.abs(zd) >= 2) {
                int yd = mob.getRandom().nextIntBetweenInclusive(-3, 3);
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
            return true;
        }
    }

    private boolean canTeleportTo(Mob mob, BlockPos pos) {
        BlockPos delta = pos.subtract(mob.blockPosition());
        return mob.level().noCollision(mob, mob.getBoundingBox().move(delta));
    }
}
