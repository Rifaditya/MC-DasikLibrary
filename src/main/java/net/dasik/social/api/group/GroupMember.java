package net.dasik.social.api.group;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Interface implemented by Entities (or injected via Mixin) that can join and
 * follow a leader.
 * 
 * @param <T> The type of LivingEntity that implements this interface.
 */
public interface GroupMember<T extends LivingEntity> {

    /**
     * @return The current leader of this entity, or null if it has no leader or is
     *         the leader itself.
     */
    @Nullable
    T getLeader();

    /**
     * @return true if this entity currently has an active leader.
     */
    boolean hasLeader();

    /**
     * Sets the leader for this entity.
     * 
     * @param leader The leader to follow, or null to clear the leader.
     */
    void setLeader(@Nullable T leader);

    /**
     * @return The perceived size of the group, usually delegated to the leader's
     *         group size tracker.
     */
    int getGroupSize();

    /**
     * Defines whether this entity uses aerial (3D) or terrestrial (ground-based)
     * movement logic.
     * 
     * @return The FlockType.
     */
    FlockType getFlockType();
}
