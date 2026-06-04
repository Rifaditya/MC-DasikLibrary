/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: ChunkPos.java (Snapshot 10)
 */
package net.dasik.social.signal;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.dasik.social.api.Scope;
import net.dasik.social.api.SignalType;
import net.dasik.social.api.SocialEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Global signal dispatcher. Handles spatial indexing and delivery of signals to SocialEntities.
 */
public class SignalBus {
    private static final ConcurrentHashMap<Long, Set<WeakReference<SocialEntity>>> SPATIAL_INDEX = new ConcurrentHashMap<>();

    /**
     * Broadcasts a signal to all SocialEntities within range.
     */
    public static void broadcast(SignalType type, Vec3 origin, @Nullable LivingEntity source, Scope scope) {
        Signal signal = new Signal(type, origin, source, scope);
        dispatch(signal);
    }

    private static void dispatch(Signal signal) {
        double maxRange = signal.type().getMaxRange();
        Vec3 origin = signal.origin();
        
        int minX = (int)Math.floor((origin.x - maxRange) / 16.0);
        int maxX = (int)Math.floor((origin.x + maxRange) / 16.0);
        int minZ = (int)Math.floor((origin.z - maxRange) / 16.0);
        int maxZ = (int)Math.floor((origin.z + maxRange) / 16.0);

        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                long chunkKey = ChunkPos.pack(x, z);
                Set<WeakReference<SocialEntity>> chunkEntities = SPATIAL_INDEX.get(chunkKey);
                if (chunkEntities == null) continue;
                processChunk(chunkEntities, signal);
            }
        }
    }

    private static void processChunk(Set<WeakReference<SocialEntity>> chunkEntities, Signal signal) {
        Iterator<WeakReference<SocialEntity>> it = chunkEntities.iterator();
        while (it.hasNext()) {
            WeakReference<SocialEntity> ref = it.next();
            SocialEntity entity = ref.get();
            
            if (entity == null || entity.dasik$asEntity().isRemoved()) {
                it.remove();
                continue;
            }
            
            if (!shouldReceive(entity, signal)) continue;
            
            var scheduler = entity.dasik$getScheduler();
            if (scheduler != null) {
                scheduler.onSignalReceived(signal);
            }
        }
    }

    private static boolean shouldReceive(SocialEntity entity, Signal signal) {
        double range = entity.dasik$getSignalRange(signal.type());
        if (signal.origin().distanceToSqr(entity.dasik$asEntity().position()) > range * range) {
            return false;
        }

        LivingEntity source = signal.source();
        LivingEntity target = entity.dasik$asEntity();
        
        if (source == target) {
            return false;
        }

        return switch (signal.scope()) {
            case PUBLIC -> true;
            case SAME_SPECIES -> source != null && entity.dasik$getSpeciesId().equals(((SocialEntity)source).dasik$getSpeciesId());
            case DIRECTED, PRIVATE -> false;
        };
    }

    public static void updatePosition(SocialEntity entity, @Nullable ChunkPos oldPos, @Nullable ChunkPos newPos) {
        if (oldPos != null) {
            removeFromChunk(entity, oldPos.pack());
        }
        if (newPos != null) {
            addToChunk(entity, newPos.pack());
        }
    }

    private static void addToChunk(SocialEntity entity, long chunkKey) {
        SPATIAL_INDEX.computeIfAbsent(chunkKey, k -> ConcurrentHashMap.newKeySet()).add(new WeakReference<>(entity));
    }

    private static void removeFromChunk(SocialEntity entity, long chunkKey) {
        Set<WeakReference<SocialEntity>> set = SPATIAL_INDEX.get(chunkKey);
        if (set != null) {
            set.removeIf(ref -> {
                SocialEntity e = ref.get();
                return e == null || e == entity;
            });
        }
    }
}
