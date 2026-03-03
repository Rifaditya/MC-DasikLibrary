/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.core.group;

import java.util.Comparator;
import java.util.List;
import net.dasik.social.api.group.GroupMember;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

/**
 * Manages group logic including leader election and member maintenance.
 */
public class GroupManager {
    /**
     * Finds a suitable leader for the member within the search radius.
     * Uses a deterministic election process based on entity UUIDs.
     */
    public static <T extends LivingEntity> void findAndSetLeader(T member, double searchRadius) {
        GroupMember memberInterface = (GroupMember) member;
        
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
        LivingEntity electedLeader = potentialLeaders.stream()
            .filter(e -> e != null && e.getUUID() != null)
            .min(Comparator.comparing(LivingEntity::getUUID))
            .orElse(member);

        if (electedLeader != member) {
            GroupMember leaderInterface = (GroupMember) electedLeader;
            // If the elected entity itself follows someone, follow that person instead (cluster merging)
            if (leaderInterface.hasLeader() && leaderInterface.getLeader() != null) {
                memberInterface.setLeader(leaderInterface.getLeader());
            } else {
                memberInterface.setLeader(electedLeader);
            }
        } else {
            memberInterface.setLeader(null);
        }
    }
}

