/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Mob.java (Snapshot 10)
 */
package net.dasik.social.api.profile;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface BehaviorCondition {
    public boolean test(Mob mob);

    public static BehaviorCondition inDimension(ResourceKey<Level> dimension) {
        return mob -> mob.level().dimension() == dimension;
    }
}
