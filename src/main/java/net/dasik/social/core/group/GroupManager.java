/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.AABB
 */
package net.dasik.social.core.group;
// Verified against: LivingEntity.java (Snapshot 10)


import java.util.Comparator;
import java.util.List;
import net.dasik.social.api.group.GroupMember;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class GroupManager {
    public static <T extends LivingEntity> void findAndSetLeader(T member, double searchRadius) {
        if (((GroupMember)member).hasLeader()) {
            LivingEntity leader = (LivingEntity)((GroupMember)member).getLeader();
            if (leader == null || !leader.isAlive() || leader.distanceToSqr(member) > Math.pow(searchRadius * 2.0, 2.0)) {
                ((GroupMember)member).setLeader(null);
            } else {
                return;
            }
        }
        AABB box = member.getBoundingBox().inflate(searchRadius);
        List<T> potentialLeaders = (List<T>)member.level().getEntitiesOfClass((Class<T>)member.getClass(), box, (entity) -> entity.isAlive() && entity != member);
        if (potentialLeaders.isEmpty()) {
            return;
        }
        potentialLeaders.add(member);
        LivingEntity electedLeader = potentialLeaders.stream().filter(e -> e != null && e.getUUID() != null).min(Comparator.comparing(e -> e.getUUID().toString())).orElse(member);
        if (electedLeader != member) {
            if (((GroupMember)electedLeader).hasLeader() && ((GroupMember)electedLeader).getLeader() != null) {
                ((GroupMember)member).setLeader(((GroupMember)electedLeader).getLeader());
            } else {
                ((GroupMember)member).setLeader(electedLeader);
            }
        } else {
            ((GroupMember)member).setLeader(null);
        }
    }
}

