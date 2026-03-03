/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.Goal$Flag
 */
package net.dasik.social.ai.goal;
// Verified against: Mob.java (Snapshot 10)


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

public class FollowLeaderGoal<T extends Mob>
extends Goal {
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
        this.defaultStrategy = mob != null && ((GroupMember)mob).getFlockType() == FlockType.AERIAL ? Strategies.AERIAL : Strategies.TERRESTRIAL;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (this.mob == null) {
            return false;
        }
        if (this.ticksSinceManagerCheck++ > 30 + this.mob.getRandom().nextInt(20)) {
            this.ticksSinceManagerCheck = 0;
            GroupManager.findAndSetLeader(this.mob, this.searchRadius);
        }
        return this.isValidLeader((T)((GroupMember)this.mob).getLeader());
    }

    public boolean canContinueToUse() {
        if (this.mob == null) {
            return false;
        }
        return this.isValidLeader((T)((GroupMember)this.mob).getLeader());
    }

    protected boolean isValidLeader(T leader) {
        double distSq;
        if (leader == null || !leader.isAlive()) {
            return false;
        }
        if (this.mob != null && (distSq = this.mob.distanceToSqr(leader)) > Math.pow(this.searchRadius * 3.0, 2.0)) {
            ((GroupMember)this.mob).setLeader(null);
            return false;
        }
        return true;
    }

    public void start() {
        this.timeToRecalcPath = 0;
    }

    public void stop() {
        if (this.mob != null && ((GroupMember)this.mob).getFlockType() == FlockType.TERRESTRIAL) {
            this.mob.getNavigation().stop();
        }
        this.timeToRecalcPath = 0;
    }

    public void tick() {
        if (this.mob == null) {
            return;
        }
        Mob leader = (Mob)((GroupMember)this.mob).getLeader();
        if (leader == null) {
            return;
        }
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.defaultStrategy.execute((LivingEntity)this.mob, (LivingEntity)leader, this.parameters);
        }
    }
}

