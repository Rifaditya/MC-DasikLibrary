package net.dasik.social.api.profile;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import java.util.List;
import java.util.function.Consumer;

/**
 * A complete behavior profile with goals and activation conditions.
 * 
 * <pre>
 * ============================================
 * [GUIDE - DO NOT DELETE]
 * Profile Rules:
 * - Max 5 conditions per profile
 * - Higher matchScore = higher priority
 * - Ties broken by profile priority value
 * See: Doc/Develop/profile_guide.md
 * ============================================
 * </pre>
 */
public interface BehaviorProfile {

    /**
     * Unique identifier for this profile.
     */
    String getId();

    /**
     * Priority for tiebreaking (higher = preferred).
     */
    int getPriority();

    /**
     * List of conditions (max 5).
     */
    List<BehaviorCondition> getConditions();

    /**
     * Calculates how many conditions match for this mob.
     */
    default int getMatchScore(Mob mob) {
        return (int) getConditions().stream()
                .filter(c -> c.test(mob))
                .count();
    }

    /**
     * Applies goals to the mob's goalSelector.
     * Called when this profile becomes active.
     */
    void applyGoals(Mob mob, GoalSelector goalSelector);

    /**
     * Removes goals from the mob's goalSelector.
     * Called when switching away from this profile.
     */
    void removeGoals(Mob mob, GoalSelector goalSelector);

    // ========================================
    // Builder Factory
    // ========================================

    static Builder builder(String id) {
        return new DefaultProfileBuilder(id);
    }

    interface Builder {
        Builder priority(int priority);

        Builder condition(BehaviorCondition condition);

        Builder goals(Consumer<GoalConfigurator> configurator);

        BehaviorProfile build();
    }

    interface GoalConfigurator {
        void add(int priority, net.minecraft.world.entity.ai.goal.Goal goal);
    }
}
