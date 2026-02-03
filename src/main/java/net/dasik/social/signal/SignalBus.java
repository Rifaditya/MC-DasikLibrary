package net.dasik.social.signal;

import net.dasik.social.api.Scope;
import net.dasik.social.api.SignalType;
import net.dasik.social.api.SocialEntity;
import net.dasik.social.util.ObjectPool;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The central nervous system for inter-entity communication.
 * Handles spatial indexing and signal broadcasting.
 */
public class SignalBus {

    // Spatial Index: Chunk -> Entities in that chunk
    private static final ConcurrentHashMap<Long, Set<WeakReference<SocialEntity>>> SPATIAL_INDEX = new ConcurrentHashMap<>();

    // Pool for zero-allocation broadcasting
    private static final ObjectPool<Signal> SIGNAL_POOL = new ObjectPool<>(Signal::new, 256);

    /**
     * Broadcasts a signal to relevant entities.
     * 
     * @param type   The type of signal.
     * @param origin The origin point.
     * @param source The source entity (optional).
     * @param scope  The visibility scope.
     */
    public static void broadcast(SignalType type, Vec3 origin, @Nullable LivingEntity source, Scope scope) {
        Signal signal = SIGNAL_POOL.acquire();
        signal.init(type, origin, source, scope);

        try {
            dispatch(signal);
        } finally {
            SIGNAL_POOL.release(signal);
        }
    }

    private static void dispatch(Signal signal) {
        double maxRange = signal.getType().getMaxRange();
        Vec3 origin = signal.getOrigin();

        // Determine affected chunks
        int minX = (int) Math.floor((origin.x - maxRange) / 16.0);
        int maxX = (int) Math.floor((origin.x + maxRange) / 16.0);
        int minZ = (int) Math.floor((origin.z - maxRange) / 16.0);
        int maxZ = (int) Math.floor((origin.z + maxRange) / 16.0);

        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                long chunkKey = ChunkPos.pack(x, z);
                Set<WeakReference<SocialEntity>> chunkEntities = SPATIAL_INDEX.get(chunkKey);

                if (chunkEntities != null) {
                    processChunk(chunkEntities, signal);
                }
            }
        }
    }

    private static void processChunk(Set<WeakReference<SocialEntity>> chunkEntities, Signal signal) {
        Iterator<WeakReference<SocialEntity>> it = chunkEntities.iterator();
        while (it.hasNext()) {
            SocialEntity entity = it.next().get();
            if (entity == null || entity.dasik$asEntity().isRemoved()) {
                // Lazy cleanup
                it.remove();
                continue;
            }

            if (shouldReceive(entity, signal)) {
                entity.dasik$getScheduler().onSignalReceived(signal);
            }
        }
    }

    private static boolean shouldReceive(SocialEntity entity, Signal signal) {
        // 1. Range Check (Modular override)
        double range = entity.dasik$getSignalRange(signal.getType());
        if (signal.getOrigin().distanceToSqr(entity.dasik$asEntity().position()) > range * range) {
            return false;
        }

        // 2. Scope Check
        LivingEntity source = signal.getSource();
        LivingEntity target = entity.dasik$asEntity();

        if (source == target)
            return false; // Don't hear own signals?

        switch (signal.getScope()) {
            case PUBLIC:
                return true;
            case SAME_SPECIES:
                return source != null
                        && entity.dasik$getSpeciesId().equals(((SocialEntity) source).dasik$getSpeciesId());
            case DIRECTED:
                // Needs logic to check if target is intended recipient.
                // For now, assume if range matches? No, Directed implies specific target.
                // Simplification for now: PUBLIC/SAME_SPECIES are main ones.
                return false;
            case PRIVATE:
                return false;
            default:
                return true;
        }
    }

    // --- Index Management ---

    public static void updatePosition(SocialEntity entity, ChunkPos oldPos, ChunkPos newPos) {
        if (oldPos != null) {
            removeFromChunk(entity, oldPos.pack());
        }
        if (newPos != null) {
            addToChunk(entity, newPos.pack());
        }
    }

    private static void addToChunk(SocialEntity entity, long chunkKey) {
        SPATIAL_INDEX.computeIfAbsent(chunkKey, k -> ConcurrentHashMap.newKeySet())
                .add(new WeakReference<>(entity));
    }

    private static void removeFromChunk(SocialEntity entity, long chunkKey) {
        Set<WeakReference<SocialEntity>> set = SPATIAL_INDEX.get(chunkKey);
        if (set != null) {
            // Expensive removal? Using synchronized removal or periodic cleanup is better.
            // But ConcurrentHashMap.KeySet.remove() works if equals/hashCode match?
            // WeakReference equals relies on reference equality typically.
            // Just assume lazy cleanup will handle it eventually?
            // Or explicitly iterate.

            // For robustness, we iterate.
            set.removeIf(ref -> {
                SocialEntity e = ref.get();
                return e == null || e == entity;
            });
        }
    }
}
