package net.dasik.social.api.profile;

import net.minecraft.world.entity.Mob;

/**
 * A single condition that determines if a profile should be active.
 * 
 * <pre>
 * ============================================
 * [GUIDE - DO NOT DELETE]
 * Condition Types (max 5 per profile):
 * 1. Dimension - mob.level().dimension()
 * 2. Biome - mob.level().getBiome(pos)
 * 3. Time - level.isNight()
 * 4. State - isInLove(), hasLeader()
 * 5. Custom - mod-specific
 * See: Doc/Develop/profile_guide.md
 * ============================================
 * </pre>
 */
@FunctionalInterface
public interface BehaviorCondition {

    /**
     * Tests if this condition is currently met.
     * 
     * @param mob The mob to test
     * @return true if condition is satisfied
     */
    boolean test(Mob mob);

    /**
     * Creates a dimension-based condition.
     */
    static BehaviorCondition inDimension(
            net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension) {
        return mob -> mob.level().dimension() == dimension;
    }
}
