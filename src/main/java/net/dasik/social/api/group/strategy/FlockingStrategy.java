package net.dasik.social.api.group.strategy;

import net.minecraft.world.entity.LivingEntity;

/**
 * The strategy pattern interface defining HOW an entity moves towards its
 * leader.
 */
public interface FlockingStrategy {

    /**
     * Executes the flocking movement logic for a single tick.
     * 
     * @param mob    The entity performing the movement.
     * @param leader The leader the entity is following.
     * @param params The flocking configuration parameters (speed, distances).
     */
    void execute(LivingEntity mob, LivingEntity leader, GroupParameters params);
}
