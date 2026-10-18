// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Mob.java (Snapshot 10)
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
import net.minecraft.world.level.pathfinder.PathType;

/**
 * AI goal for entities to follow a designated group leader using specific flocking strategies.
 */
public class FollowLeaderGoal<T extends Mob> extends Goal {
    protected final T mob;
    protected GroupParameters parameters;
    protected final double searchRadius;
    protected final FlockingStrategy defaultStrategy;
    protected int timeToRecalcPath = 0;
    protected int ticksSinceManagerCheck = 0;
    protected float oldWaterCost;

    public FollowLeaderGoal(T mob, GroupParameters parameters, double searchRadius) {
        this.mob = mob;
        this.parameters = parameters;
        this.searchRadius = searchRadius;
        this.defaultStrategy = (mob instanceof GroupMember gm && gm.getFlockType() == FlockType.AERIAL) 
            ? Strategies.AERIAL : Strategies.TERRESTRIAL;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    /**
     * Updates the flocking parameters at runtime.
     * Safe to call periodically (e.g., on GameRule change) from a subclass tick.
     * Verified against: FollowLeaderGoal.java (26.*)
     */
    public void setParameters(GroupParameters parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean canUse() {
        if (!(this.mob instanceof GroupMember member)) {
            return false;
        }
        if (this.ticksSinceManagerCheck++ > 30 + this.mob.getRandom().nextInt(20)) {
            this.ticksSinceManagerCheck = 0;
            GroupManager.findAndSetLeader(this.mob, this.getReceiveRange(this.mob));
        }
        LivingEntity rawLeader = member.getLeader();
        if (!(rawLeader instanceof Mob leader) || !this.isValidLeader((T) leader)) {
            return false;
        }
        return this.mob.distanceToSqr(leader) > (double)(this.parameters.startDistance() * this.parameters.startDistance());
    }

    @Override
    public boolean canContinueToUse() {
        if (!(this.mob instanceof GroupMember member)) {
            return false;
        }
        LivingEntity rawLeader = member.getLeader();
        if (!(rawLeader instanceof Mob leader) || !this.isValidLeader((T) leader)) {
            return false;
        }
        return this.mob.distanceToSqr(leader) > (double)(this.parameters.stopDistance() * this.parameters.stopDistance());
    }

    protected boolean isValidLeader(T leader) {
        if (leader == null || !leader.isAlive()) {
            return false;
        }
        double range = Math.max(this.getTransmitRange(leader), this.getReceiveRange(this.mob));
        if (this.mob != null && this.mob.distanceToSqr(leader) > range * range) {
            if (this.mob instanceof GroupMember gm) {
                gm.setLeader(null);
            }
            return false;
        }
        return true;
    }

    private double getTransmitRange(LivingEntity leader) {
        if (leader != null && leader.getAttributes().hasAttribute(net.minecraft.world.entity.ai.attributes.Attributes.WAYPOINT_TRANSMIT_RANGE)) {
            return leader.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.WAYPOINT_TRANSMIT_RANGE);
        }
        return this.searchRadius * 3.0; // Fallback to original logic
    }

    private double getReceiveRange(LivingEntity follower) {
        if (follower != null && follower.getAttributes().hasAttribute(net.minecraft.world.entity.ai.attributes.Attributes.WAYPOINT_RECEIVE_RANGE)) {
            return follower.getAttributeValue(net.minecraft.world.entity.ai.attributes.Attributes.WAYPOINT_RECEIVE_RANGE);
        }
        return this.searchRadius; // Fallback to original logic
    }

    @Override
    public void start() {
        if (this.mob == null) {
            return;
        }
        // Tick Staggering: Offset the path recalc so flock members don't lag spike the server on the same tick
        this.timeToRecalcPath = this.mob.getId() % 10;
        this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
        this.mob.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    @Override
    public void stop() {
        if (this.mob instanceof GroupMember gm && gm.getFlockType() == FlockType.TERRESTRIAL) {
            this.mob.getNavigation().stop();
        }
        if (this.mob != null) {
            this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
        }
        this.timeToRecalcPath = 0;
    }

    @Override
    public void tick() {
        if (!(this.mob instanceof GroupMember member)) {
            return;
        }
        LivingEntity rawLeader = member.getLeader();
        if (!(rawLeader instanceof Mob leader)) {
            return;
        }
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (leader instanceof GroupMember leaderGM) {
                net.dasik.social.core.group.FlockState state = leaderGM.getFlockState();
                if (state == null || this.mob.level().getGameTime() - state.getLastUpdateTime() > 20) {
                    GroupManager.computeFlockState(leader, this.getTransmitRange(leader));
                }
            }
            this.defaultStrategy.execute(this.mob, leader, this.parameters);
        }
    }
}
