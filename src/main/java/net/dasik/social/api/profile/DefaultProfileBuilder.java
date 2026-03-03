/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.GoalSelector
 */
package net.dasik.social.api.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.dasik.social.api.profile.BehaviorCondition;
import net.dasik.social.api.profile.BehaviorProfile;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;

public class DefaultProfileBuilder
implements BehaviorProfile.Builder {
    private final String id;
    private int priority = 0;
    private final List<BehaviorCondition> conditions = new ArrayList<BehaviorCondition>();
    private final List<GoalEntry> goalEntries = new ArrayList<GoalEntry>();

    public DefaultProfileBuilder(String id) {
        this.id = id;
    }

    @Override
    public BehaviorProfile.Builder priority(int priority) {
        this.priority = priority;
        return this;
    }

    @Override
    public BehaviorProfile.Builder condition(BehaviorCondition condition) {
        if (this.conditions.size() >= 5) {
            throw new IllegalStateException("Max 5 conditions per profile!");
        }
        this.conditions.add(condition);
        return this;
    }

    @Override
    public BehaviorProfile.Builder goals(Consumer<BehaviorProfile.GoalConfigurator> configurator) {
        configurator.accept((prio, goal) -> this.goalEntries.add(new GoalEntry(prio, goal)));
        return this;
    }

    @Override
    public BehaviorProfile build() {
        return new DefaultBehaviorProfile(this.id, this.priority, List.copyOf(this.conditions), List.copyOf(this.goalEntries));
    }

    private record DefaultBehaviorProfile(String id, int priority, List<BehaviorCondition> conditions, List<GoalEntry> goalEntries) implements BehaviorProfile
    {
        @Override
        public String getId() {
            return this.id;
        }

        @Override
        public int getPriority() {
            return this.priority;
        }

        @Override
        public List<BehaviorCondition> getConditions() {
            return this.conditions;
        }

        @Override
        public void applyGoals(Mob mob, GoalSelector goalSelector) {
            for (GoalEntry entry : this.goalEntries) {
                goalSelector.addGoal(entry.priority(), entry.goal());
            }
        }

        @Override
        public void removeGoals(Mob mob, GoalSelector goalSelector) {
            for (GoalEntry entry : this.goalEntries) {
                goalSelector.removeGoal(entry.goal());
            }
        }
    }

    record GoalEntry(int priority, Goal goal) {
    }
}

