// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: LivingEntity.java (Snapshot 10)
package net.dasik.social.api.group.strategy;

import net.minecraft.world.entity.LivingEntity;

/**
 * Functional interface for group movement heuristics.
 */
public interface FlockingStrategy {
    public void execute(LivingEntity mob, LivingEntity leader, GroupParameters params);
}
