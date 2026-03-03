/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.level.Level
 */
package net.dasik.social.api.profile;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface BehaviorCondition {
    public boolean test(Mob var1);

    public static BehaviorCondition inDimension(ResourceKey<Level> dimension) {
        return mob -> mob.level().dimension() == dimension;
    }
}

