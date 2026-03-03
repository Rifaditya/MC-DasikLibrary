/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Mob.java (Snapshot 10)
 */
package net.dasik.social.ai.goal;

import java.util.EnumSet;
import net.dasik.social.api.group.FlockType;
import net.dasik.social.api.group.GroupMember;
import net.dasik.social.api.group.strategy.FlockingStrategy;
import net.dasik.social.api.group.strategy.GroupParameters;
import net.dasik.social.api.group.strategy.Strategies;
import net.dasik.social.core.group.GroupManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * AI goal for entities to follow a designated group leader using specific flocking strategies.
 */
public class FollowLeaderGoal<T extends Mob> extends Goal {
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
        this.defaultStrategy = mob != null && ((GroupMember) mob).getFlockType() == FlockType.AERIAL 
            ? Strategies.AERIAL : Strategies.TERRESTRIAL;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob == null) {
            return false;
        }
        if (this.ticksSinceManagerCheck++ > 30 + this.mob.getRandom().nextInt(20)) {
            this.ticksSinceManagerCheck = 0;
            GroupManager.findAndSetLeader(this.mob, this.searchRadius);
        }
        @SuppressWarnings("unchecked")
        T leader = (T) ((GroupMember) this.mob).getLeader();
        return this.isValidLeader(leader);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob == null) {
            return false;
        }
        @SuppressWarnings("unchecked")
        T leader = (T) ((GroupMember) this.mob).getLeader();
        return this.isValidLeader(leader);
    }

    protected boolean isValidLeader(T leader) {
        if (leader == null || !leader.isAlive()) {
            return false;
        }
        if (this.mob != null && this.mob.distanceToSqr(leader) > Math.pow(this.searchRadius * 3.0, 2.0)) {
            ((GroupMember) this.mob).setLeader(null);
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
    }

    @Override
    public void stop() {
        if (this.mob != null && ((GroupMember) this.mob).getFlockType() == FlockType.TERRESTRIAL) {
            this.mob.getNavigation().stop();
        }
        this.timeToRecalcPath = 0;
    }

    @Override
    public void tick() {
        if (this.mob == null) {
            return;
        }
        Mob leader = (Mob) ((GroupMember) this.mob).getLeader();
        if (leader == null) {
            return;
        }
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.defaultStrategy.execute(this.mob, leader, this.parameters);
        }
    }
}
