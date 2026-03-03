/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.goal.Goal
 *  net.minecraft.world.entity.ai.goal.GoalSelector
 */
package net.dasik.social.api.profile;

import java.util.List;
import java.util.function.Consumer;
import net.dasik.social.api.profile.BehaviorCondition;
import net.dasik.social.api.profile.DefaultProfileBuilder;
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

    public void applyGoals(Mob var1, GoalSelector var2);

    public void removeGoals(Mob var1, GoalSelector var2);

    public static Builder builder(String id) {
        return new DefaultProfileBuilder(id);
    }

    public static interface GoalConfigurator {
        public void add(int var1, Goal var2);
    }

    public static interface Builder {
        public Builder priority(int var1);

        public Builder condition(BehaviorCondition var1);

        public Builder goals(Consumer<GoalConfigurator> var1);

        public BehaviorProfile build();
    }
}

