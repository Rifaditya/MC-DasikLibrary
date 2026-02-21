package net.dasik.social.core.group;

import net.dasik.social.api.group.GroupMember;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;

/**
 * Core manager responsible for leader-follower dynamics and discovery.
 */
@SuppressWarnings("null")
public class GroupManager {

    /**
     * Determines if an entity should follow another entity, or become a leader
     * itself.
     * Searches a 32-block radius around the member for potential leaders.
     * Uses UUID comparison to deterministically elect a leader (lowest UUID string
     * wins).
     *
     * @param member       The group member trying to find a leader.
     * @param searchRadius The radius to search for other members.
     * @param <T>          The member type (typically the same class, e.g., Bat or
     *                     Wolf).
     */
    public static <T extends LivingEntity & GroupMember<T>> void findAndSetLeader(T member, double searchRadius) {
        if (member.hasLeader()) {
            // Already has a leader, confirm leader is still valid
            T leader = member.getLeader();
            if (leader == null || !leader.isAlive() || leader.distanceToSqr(member) > Math.pow(searchRadius * 2, 2)) {
                member.setLeader(null);
            } else {
                return; // Nothing to change
            }
        }

        AABB box = member.getBoundingBox().inflate(searchRadius);

        // Generics erasure workaround for getEntitiesOfClass, we search by the concrete
        // class of the member
        @SuppressWarnings("unchecked")
        List<T> potentialLeaders = member.level().getEntitiesOfClass(
                (Class<T>) member.getClass(),
                box,
                entity -> entity.isAlive() && entity != member);

        if (potentialLeaders.isEmpty()) {
            return; // No one around, I am the leader by default
        }

        // Add self to the pool of candidates
        potentialLeaders.add(member);

        // Election Logic: Deterministic via String UUID comparison.
        // Lowest ID becomes the leader of this immediate cluster.
        T electedLeader = potentialLeaders.stream()
                .filter(e -> e != null && e.getUUID() != null)
                .min(Comparator.comparing(e -> e.getUUID().toString()))
                .orElse(member);

        if (electedLeader != member) {
            // Check if the elected leader already has a DIFFERENT leader. If so, follow
            // their leader (chaining up).
            if (electedLeader.hasLeader() && electedLeader.getLeader() != null) {
                member.setLeader(electedLeader.getLeader());
            } else {
                member.setLeader(electedLeader);
            }
        } else {
            // I'm my own leader
            member.setLeader(null);
        }
    }
}
