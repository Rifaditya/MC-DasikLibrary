/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.dasik.social.core;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import net.dasik.social.api.SocialEntity;
import net.dasik.social.util.FastRandom;
import org.jetbrains.annotations.Nullable;

public class SocialRegistry {
    private static final int SHARD_COUNT = Math.max(8, Runtime.getRuntime().availableProcessors() * 2);
    private static final Set<WeakReference<SocialEntity>>[] SHARDS;
    private static final ConcurrentHashMap<String, ConcurrentLinkedQueue<WeakReference<SocialEntity>>> SPECIES_INDEX;
    private static final AtomicInteger ENTITY_COUNT;
    private static volatile SocialEntity[] FAST_ACCESS_ARRAY;
    private static final AtomicLong INDEX_VERSION;
    private static volatile long ARRAY_VERSION;

    public static void register(SocialEntity entity) {
        if (entity == null) {
            return;
        }
        if (ENTITY_COUNT.get() >= 250000) {
            return;
        }
        int shardIdx = SocialRegistry.getShardIndex(entity);
        SHARDS[shardIdx].add(new WeakReference<SocialEntity>(entity));
        SPECIES_INDEX.computeIfAbsent(entity.dasik$getSpeciesId(), k -> new ConcurrentLinkedQueue()).add(new WeakReference<SocialEntity>(entity));
        ENTITY_COUNT.incrementAndGet();
        INDEX_VERSION.incrementAndGet();
    }

    public static void unregister(SocialEntity entity) {
        if (entity == null) {
            return;
        }
        int shardIdx = SocialRegistry.getShardIndex(entity);
        Set<WeakReference<SocialEntity>> shard = SHARDS[shardIdx];
        INDEX_VERSION.incrementAndGet();
        ENTITY_COUNT.decrementAndGet();
    }

    @Nullable
    public static SocialEntity getRandomEntity() {
        SocialEntity[] array;
        if (INDEX_VERSION.get() != ARRAY_VERSION) {
            SocialRegistry.rebuildFastArray();
        }
        if ((array = FAST_ACCESS_ARRAY).length == 0) {
            return null;
        }
        return array[FastRandom.INSTANCE.nextInt(array.length)];
    }

    private static synchronized void rebuildFastArray() {
        if (INDEX_VERSION.get() == ARRAY_VERSION) {
            return;
        }
        ArrayList<SocialEntity> liveEntities = new ArrayList<SocialEntity>(ENTITY_COUNT.get() + 100);
        for (Set<WeakReference<SocialEntity>> shard : SHARDS) {
            Iterator<WeakReference<SocialEntity>> it = shard.iterator();
            while (it.hasNext()) {
                WeakReference<SocialEntity> ref = it.next();
                SocialEntity ent = (SocialEntity)ref.get();
                if (ent == null || ent.dasik$asEntity().isRemoved()) {
                    it.remove();
                    continue;
                }
                liveEntities.add(ent);
            }
        }
        FAST_ACCESS_ARRAY = liveEntities.toArray(new SocialEntity[0]);
        ENTITY_COUNT.set(liveEntities.size());
        ARRAY_VERSION = INDEX_VERSION.get();
    }

    private static int getShardIndex(SocialEntity entity) {
        return (System.identityHashCode(entity) & Integer.MAX_VALUE) % SHARD_COUNT;
    }

    public static boolean contains(SocialEntity entity) {
        if (entity == null) {
            return false;
        }
        int shardIdx = SocialRegistry.getShardIndex(entity);
        Set<WeakReference<SocialEntity>> shard = SHARDS[shardIdx];
        for (WeakReference<SocialEntity> ref : shard) {
            if (ref.get() != entity) continue;
            return true;
        }
        return false;
    }

    public static int getCount() {
        return ENTITY_COUNT.get();
    }

    static {
        SPECIES_INDEX = new ConcurrentHashMap(512);
        ENTITY_COUNT = new AtomicInteger(0);
        FAST_ACCESS_ARRAY = new SocialEntity[0];
        INDEX_VERSION = new AtomicLong(0L);
        ARRAY_VERSION = 0L;
        SHARDS = new Set[SHARD_COUNT];
        for (int i = 0; i < SHARD_COUNT; ++i) {
            SocialRegistry.SHARDS[i] = ConcurrentHashMap.newKeySet();
        }
    }
}

