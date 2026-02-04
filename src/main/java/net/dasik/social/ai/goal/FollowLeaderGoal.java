package net.dasik.social.ai.goal;

import net.dasik.social.api.group.GroupMember;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Generic goal for following a leader in a flock/school/pack.
 * 
 * @param <T> The type of entity (Must be Mob & GroupMember)
 */
public class FollowLeaderGoal extends Goal {

    private final Mob mob;
    private final GroupMember<?> member;

    public FollowLeaderGoal(Mob mob) {
        this.mob = mob;
        if (!(mob instanceof GroupMember)) {
            throw new IllegalArgumentException("Mob must implement GroupMember");
        }
        this.member = (GroupMember<?>) mob;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!(mob.level() instanceof ServerLevel))
            return false;

        // Ensure we have a leader
        // Note: Leader election is usually handled by the Entity tick or a separate
        // sensor.
        // If the entity doesn't have a leader, we can't follow.

        return member.hasLeader() && member.getLeader() != null && member.getLeader().isAlive()
                && member.getLeader() != mob;
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public void tick() {
        LivingEntity leader = member.getLeader();
        if (leader != null) {
            member.getFlockingStrategy().navigateToLeader(
                    mob,
                    leader,
                    member.getGroupParameters(),
                    member.getGroupSize());
        }
    }
}
