/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.level.ChunkPos
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package net.dasik.social.signal;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.dasik.social.api.Scope;
import net.dasik.social.api.SignalType;
import net.dasik.social.api.SocialEntity;
import net.dasik.social.signal.Signal;
import net.dasik.social.util.ObjectPool;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SignalBus {
    private static final ConcurrentHashMap<Long, Set<WeakReference<SocialEntity>>> SPATIAL_INDEX = new ConcurrentHashMap();
    private static final ObjectPool<Signal> SIGNAL_POOL = new ObjectPool<Signal>(Signal::new, 256);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static void broadcast(SignalType type, Vec3 origin, @Nullable LivingEntity source, Scope scope) {
        Signal signal = SIGNAL_POOL.acquire();
        signal.init(type, origin, source, scope);
        try {
            SignalBus.dispatch(signal);
        }
        finally {
            SIGNAL_POOL.release(signal);
        }
    }

    private static void dispatch(Signal signal) {
        double maxRange = signal.getType().getMaxRange();
        Vec3 origin = signal.getOrigin();
        int minX = (int)Math.floor((origin.x - maxRange) / 16.0);
        int maxX = (int)Math.floor((origin.x + maxRange) / 16.0);
        int minZ = (int)Math.floor((origin.z - maxRange) / 16.0);
        int maxZ = (int)Math.floor((origin.z + maxRange) / 16.0);
        for (int x = minX; x <= maxX; ++x) {
            for (int z = minZ; z <= maxZ; ++z) {
                long chunkKey = ChunkPos.pack((int)x, (int)z);
                Set<WeakReference<SocialEntity>> chunkEntities = SPATIAL_INDEX.get(chunkKey);
                if (chunkEntities == null) continue;
                SignalBus.processChunk(chunkEntities, signal);
            }
        }
    }

    private static void processChunk(Set<WeakReference<SocialEntity>> chunkEntities, Signal signal) {
        Iterator<WeakReference<SocialEntity>> it = chunkEntities.iterator();
        while (it.hasNext()) {
            SocialEntity entity = (SocialEntity)it.next().get();
            if (entity == null || entity.dasik$asEntity().isRemoved()) {
                it.remove();
                continue;
            }
            if (!SignalBus.shouldReceive(entity, signal)) continue;
            entity.dasik$getScheduler().onSignalReceived(signal);
        }
    }

    private static boolean shouldReceive(SocialEntity entity, Signal signal) {
        LivingEntity target;
        double range = entity.dasik$getSignalRange(signal.getType());
        if (signal.getOrigin().distanceToSqr(entity.dasik$asEntity().position()) > range * range) {
            return false;
        }
        LivingEntity source = signal.getSource();
        if (source == (target = entity.dasik$asEntity())) {
            return false;
        }
        switch (signal.getScope()) {
            case PUBLIC: {
                return true;
            }
            case SAME_SPECIES: {
                return source != null && entity.dasik$getSpeciesId().equals(((SocialEntity)source).dasik$getSpeciesId());
            }
            case DIRECTED: {
                return false;
            }
            case PRIVATE: {
                return false;
            }
        }
        return true;
    }

    public static void updatePosition(SocialEntity entity, ChunkPos oldPos, ChunkPos newPos) {
        if (oldPos != null) {
            SignalBus.removeFromChunk(entity, oldPos.pack());
        }
        if (newPos != null) {
            SignalBus.addToChunk(entity, newPos.pack());
        }
    }

    private static void addToChunk(SocialEntity entity, long chunkKey) {
        SPATIAL_INDEX.computeIfAbsent(chunkKey, k -> ConcurrentHashMap.newKeySet()).add(new WeakReference<SocialEntity>(entity));
    }

    private static void removeFromChunk(SocialEntity entity, long chunkKey) {
        Set<WeakReference<SocialEntity>> set = SPATIAL_INDEX.get(chunkKey);
        if (set != null) {
            set.removeIf(ref -> {
                SocialEntity e = (SocialEntity)ref.get();
                return e == null || e == entity;
            });
        }
    }
}

