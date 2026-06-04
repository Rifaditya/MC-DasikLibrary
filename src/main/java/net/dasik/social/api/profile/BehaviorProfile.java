/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Mob.java (Snapshot 10)
 */
package net.dasik.social.api.profile;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;

public interface BehaviorProfile {
    public String getId();

    public int getPriority();

    public List<BehaviorCondition> getConditions();

    default public int getMatchScore(Mob mob) {
        return (int)this.getConditions().stream().filter(c -> c.test(mob)).count();
    }

    public void applyGoals(Mob mob, GoalSelector goalSelector);

    public void removeGoals(Mob mob, GoalSelector goalSelector);

    public static Builder builder(String id) {
        return new DefaultProfileBuilder(id);
    }

    public static interface GoalConfigurator {
        public void add(int priority, Goal goal);
    }

    public static interface Builder {
        public Builder priority(int priority);

        public Builder condition(BehaviorCondition condition);

        public Builder goals(Consumer<GoalConfigurator> configurator);

        public BehaviorProfile build();
    }
}
