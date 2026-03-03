/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: GroupMember.java (Snapshot 10)
 */
package net.dasik.social.api.group;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Interface for entities that can participate in flocking or group behaviors.
 */
public interface GroupMember {
    @Nullable
    public LivingEntity getLeader();

    public boolean hasLeader();

    public void setLeader(@Nullable LivingEntity leader);

    public int getGroupSize();

    public FlockType getFlockType();
}

