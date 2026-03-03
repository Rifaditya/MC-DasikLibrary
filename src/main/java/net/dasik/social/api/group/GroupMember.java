/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.Nullable
 */
package net.dasik.social.api.group;

import net.dasik.social.api.group.FlockType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface GroupMember<T extends LivingEntity> {
    @Nullable
    public T getLeader();

    public boolean hasLeader();

    public void setLeader(@Nullable T var1);

    public int getGroupSize();

    public FlockType getFlockType();
}

