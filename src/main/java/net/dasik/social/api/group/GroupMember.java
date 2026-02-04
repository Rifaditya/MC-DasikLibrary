package net.dasik.social.api.group;

import net.dasik.social.api.group.strategy.FlockingStrategy;
import net.minecraft.world.entity.LivingEntity;

public interface GroupMember<T extends LivingEntity> {
    T getLeader();

    void setLeader(T leader);

    /**
     * @return true if this entity currently has a valid leader.
     */
    boolean hasLeader();

    /**
     * @return perceived size of the group, used for scaling separation.
     */
    int getGroupSize();

    /**
     * @return The strategy used to move towards the leader.
     */
    FlockingStrategy getFlockingStrategy();

    /**
     * @return The parameters (distances, forces) for flocking.
     */
    GroupParameters getGroupParameters();
}
