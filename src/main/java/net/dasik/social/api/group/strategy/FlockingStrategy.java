/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 */
package net.dasik.social.api.group.strategy;

import net.dasik.social.api.group.strategy.GroupParameters;
import net.minecraft.world.entity.LivingEntity;

public interface FlockingStrategy {
    public void execute(LivingEntity var1, LivingEntity var2, GroupParameters var3);
}

