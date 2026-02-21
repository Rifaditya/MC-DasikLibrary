package net.dasik.social.ai.goal;

import net.dasik.social.api.group.FlockType;
import net.dasik.social.api.group.GroupMember;
import net.dasik.social.api.group.strategy.FlockingStrategy;
import net.dasik.social.api.group.strategy.GroupParameters;
import net.dasik.social.api.group.strategy.Strategies;
import net.dasik.social.core.group.GroupManager;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

/**
 * Generic AI Goal that drives the Follow-Leader flocking behavior.
 * Can be added to any Mob that implements GroupMember.
 *
 * @param <T> The concrete type of the Mob implementing GroupMember.
 */
@SuppressWarnings("null")
public class FollowLeaderGoal<T extends Mob & GroupMember<T>> extends Goal {

    protected final T mob;
    protected final GroupParameters parameters;
    protected final double searchRadius;
    protected final FlockingStrategy defaultStrategy;

    protected int timeToRecalcPath = 0;
    protected int ticksSinceManagerCheck = 0;

    public FollowLeaderGoal(T mob, GroupParameters parameters, double searchRadius) {
        this.mob = mob;
        this.parameters = parameters;
        this.searchRadius = searchRadius;

        if (mob != null && mob.getFlockType() == FlockType.AERIAL) {
            this.defaultStrategy = Strategies.AERIAL;
        } else {
            this.defaultStrategy = Strategies.TERRESTRIAL;
        }

        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (mob == null)
            return false;
        // Run discovery every 1-2 seconds (20-40 ticks) to reduce lag rather than every
        // tick
        if (this.ticksSinceManagerCheck++ > 30 + mob.getRandom().nextInt(20)) {
            this.ticksSinceManagerCheck = 0;
            GroupManager.findAndSetLeader(mob, searchRadius);
        }

        return this.isValidLeader(mob.getLeader());
    }

    @Override
    public boolean canContinueToUse() {
        if (mob == null)
            return false;
        return this.isValidLeader(mob.getLeader());
    }

    protected boolean isValidLeader(T leader) {
        if (leader == null || !leader.isAlive()) {
            return false;
        }

        // Stop following if they are way too far
        if (mob != null) {
            double distSq = mob.distanceToSqr(leader);
            if (distSq > Math.pow(searchRadius * 3, 2)) {
                mob.setLeader(null);
                return false;
            }
        }

        return true;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        if (mob != null && mob.getFlockType() == FlockType.TERRESTRIAL) {
            mob.getNavigation().stop();
        }
        this.timeToRecalcPath = 0;
    }

    @Override
    public void tick() {
        if (mob == null)
            return;
        T leader = mob.getLeader();
        if (leader == null)
            return;

        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.defaultStrategy.execute(mob, leader, parameters);
        }
    }
}
