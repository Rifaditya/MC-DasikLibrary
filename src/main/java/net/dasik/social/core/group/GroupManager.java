package net.dasik.social.core.group;

import net.dasik.social.api.group.GroupMember;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.*;

/**
 * Handles group logic: Finding neighbors, electing leaders.
 */
public class GroupManager {

    // Cache could be added here similar to BatSwarmManager if performance requires
    // it.
    // For now, valid implementation is on-demand or cached in implementation.

    /**
     * Scans for nearby group members and elects a leader deterministically.
     * 
     * @param entity       The entity looking for a group.
     * @param clazz        The class of entity to group with.
     * @param searchRadius Box radius to search.
     * @return The elected leader, or null.
     */
    public static <T extends LivingEntity> T electLeader(T entity, Class<T> clazz, double searchRadius) {
        if (!(entity.level() instanceof ServerLevel))
            return null;
        if (!(entity instanceof GroupMember))
            return null;

        AABB searchBox = entity.getBoundingBox().inflate(searchRadius);
        List<T> nearby = entity.level().getEntitiesOfClass(clazz, searchBox, e -> {
            return e.isAlive() && e instanceof GroupMember; // Filter valid members
        });

        if (nearby.isEmpty())
            return null;

        // Sort mainly by UUID to be stable/deterministic
        nearby.sort(Comparator.comparing(e -> e.getUUID().toString()));

        return nearby.getFirst();
    }

    /**
     * Calculates the group size locally.
     */
    public static <T extends LivingEntity> int countGroupSize(T entity, Class<T> clazz, double searchRadius) {
        AABB searchBox = entity.getBoundingBox().inflate(searchRadius);
        return entity.level().getEntitiesOfClass(clazz, searchBox, e -> e instanceof GroupMember).size();
    }
}
