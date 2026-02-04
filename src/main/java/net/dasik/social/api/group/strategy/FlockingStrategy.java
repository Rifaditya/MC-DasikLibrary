package net.dasik.social.api.group.strategy;

import net.dasik.social.api.group.GroupParameters;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

/**
 * Defines HOW an entity moves towards its leader.
 * Implement this to create custom movement styles (e.g. Teleporting, Strafing,
 * Jetpack).
 */
public interface FlockingStrategy {

    /**
     * persistent movement logic to follow the leader.
     * 
     * @param follower  The mob being moved.
     * @param leader    The leader to follow.
     * @param params    The flocking parameters (distances, forces).
     * @param groupSize The size of the group (used for scaling spread).
     */
    void navigateToLeader(Mob follower, LivingEntity leader, GroupParameters params, int groupSize);
}
