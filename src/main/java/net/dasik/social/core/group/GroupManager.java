// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: LivingEntity.java (Snapshot 10)
package net.dasik.social.core.group;

import java.util.List;
import java.util.UUID;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.dasik.social.api.group.GroupMember;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Manages group logic including leader election and member maintenance.
 */
@DasikApiStatus.Internal
public class GroupManager {
    /**
     * Finds a suitable leader for the member within the search radius.
     * Uses a deterministic election process based on entity UUIDs.
     */
    public static <T extends LivingEntity> void findAndSetLeader(T member, double searchRadius) {
        if (!(member instanceof GroupMember memberInterface)) {
            return;
        }
        
        if (memberInterface.hasLeader()) {
            LivingEntity leader = memberInterface.getLeader();
            if (leader == null || !leader.isAlive() || leader.distanceToSqr(member) > Math.pow(searchRadius * 2.0, 2.0)) {
                memberInterface.setLeader(null);
            } else {
                return;
            }
        }

        AABB box = member.getBoundingBox().inflate(searchRadius);
        @SuppressWarnings("unchecked")
        List<T> potentialLeaders = (List<T>) member.level().getEntitiesOfClass(
            member.getClass(), 
            box, 
            entity -> entity.isAlive() && entity != member
        );

        if (potentialLeaders.isEmpty()) {
            return;
        }

        potentialLeaders.add(member);
        
        // Deterministic election: entity with the lowest UUID becomes the leader
        LivingEntity electedLeader = member;
        UUID lowestUuid = member.getUUID();
        for (LivingEntity e : potentialLeaders) {
            if (e != null && e.getUUID() != null) {
                if (lowestUuid == null || e.getUUID().compareTo(lowestUuid) < 0) {
                    lowestUuid = e.getUUID();
                    electedLeader = e;
                }
            }
        }

        if (electedLeader != member) {
            if (electedLeader instanceof GroupMember leaderInterface) {
                // If the elected entity itself follows someone, follow that person instead (cluster merging)
                if (leaderInterface.hasLeader() && leaderInterface.getLeader() != null) {
                    memberInterface.setLeader(leaderInterface.getLeader());
                } else {
                    memberInterface.setLeader(electedLeader);
                }
            } else {
                memberInterface.setLeader(electedLeader);
            }
        } else {
            memberInterface.setLeader(null);
        }
    }
    public static void computeFlockState(LivingEntity leader, double searchRadius) {
        if (leader == null || !leader.isAlive()) return;
        if (!(leader instanceof GroupMember leaderMember)) return;
        
        net.dasik.social.core.group.FlockState state = leaderMember.getFlockState();
        if (state == null) {
            state = new net.dasik.social.core.group.FlockState();
            leaderMember.setFlockState(state);
        }

        AABB box = leader.getBoundingBox().inflate(searchRadius);
        List<LivingEntity> flock = leader.level().getEntitiesOfClass(
            LivingEntity.class, 
            box, 
            e -> e.isAlive() && e instanceof GroupMember gm && gm.hasLeader() && gm.getLeader() == leader
        );
        
        flock.add(leader);

        double sumX = 0, sumY = 0, sumZ = 0;
        double sumVelX = 0, sumVelY = 0, sumVelZ = 0;
        for (LivingEntity entity : flock) {
            Vec3 pos = entity.position();
            Vec3 vel = entity.getDeltaMovement();
            sumX += pos.x;
            sumY += pos.y;
            sumZ += pos.z;
            sumVelX += vel.x;
            sumVelY += vel.y;
            sumVelZ += vel.z;
        }
        double count = flock.size();
        state.setCenterOfMass(new Vec3(sumX / count, sumY / count, sumZ / count));
        state.setAverageVelocity(new Vec3(sumVelX / count, sumVelY / count, sumVelZ / count));
        state.setMemberCount(flock.size());
        state.setLastUpdateTime(leader.level().getGameTime());
    }
}

