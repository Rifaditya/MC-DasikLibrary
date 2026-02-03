package net.dasik.social.core;

import net.dasik.social.api.SocialEntity;
import net.dasik.social.util.FastRandom;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The Hyper-Scalable Registry.
 * Handles tracking for potentially 100,000+ entities with minimal contention.
 * <p>
 * Key Features:
 * 1. Sharded storage (concurrency)
 * 2. Species Index (O(1) lookup)
 * 3. Lazy Array Rebuild (O(1) random pulse)
 */
public class SocialRegistry {

    // --- Sharding & Indexing ---

    // Shard count based on CPU cores to minimize lock contention during
    // registration
    private static final int SHARD_COUNT = Math.max(8, Runtime.getRuntime().availableProcessors() * 2);

    // Main storage: Sharded Sets of WeakReferences
    // We use WeakReference to ensure we never hold onto dead entities if unregister
    // fails
    private static final Set<WeakReference<SocialEntity>>[] SHARDS;

    // Species Index: Map<SpeciesID, Queue<Entity>>
    private static final ConcurrentHashMap<String, ConcurrentLinkedQueue<WeakReference<SocialEntity>>> SPECIES_INDEX = new ConcurrentHashMap<>(
            512);

    // --- O(1) Fast Access ---

    private static final AtomicInteger ENTITY_COUNT = new AtomicInteger(0);

    // The "snapshot" array for O(1) random access. Rebuilt lazily.
    // Volatile for safe publication.
    private static volatile SocialEntity[] FAST_ACCESS_ARRAY = new SocialEntity[0];

    // Versioning to trigger rebuilds
    private static final AtomicLong INDEX_VERSION = new AtomicLong(0);
    private static volatile long ARRAY_VERSION = 0;

    static {
        // Initialize shards
        SHARDS = new Set[SHARD_COUNT];
        for (int i = 0; i < SHARD_COUNT; i++) {
            SHARDS[i] = ConcurrentHashMap.newKeySet();
        }
    }

    /**
     * Registers a social entity into the system.
     * Thread-safe and non-blocking.
     */
    public static void register(SocialEntity entity) {
        if (entity == null)
            return;

        // Circuit Breaker check would go here (delegated to Config later)
        if (ENTITY_COUNT.get() >= 250_000)
            return; // Hard safe guard for now

        int shardIdx = getShardIndex(entity);
        SHARDS[shardIdx].add(new WeakReference<>(entity));

        // Add to Species Index
        SPECIES_INDEX.computeIfAbsent(entity.dasik$getSpeciesId(), k -> new ConcurrentLinkedQueue<>())
                .add(new WeakReference<>(entity));

        ENTITY_COUNT.incrementAndGet();
        INDEX_VERSION.incrementAndGet(); // Mark dirty
    }

    /**
     * Unregisters an entity.
     */
    public static void unregister(SocialEntity entity) {
        if (entity == null)
            return;

        int shardIdx = getShardIndex(entity);
        Set<WeakReference<SocialEntity>> shard = SHARDS[shardIdx];

        // Removal requires iteration in WeakRef set?
        // ConcurrentHashMap.KeySet doesn't support easy removal by value equality of
        // referent
        // We might need to prune periodically or use a better structure.
        // For now, we rely on WeakReference to clear memory, and lazy rebuild to clear
        // indexing.

        // Mark dirty so the array gets rebuilt eventually
        INDEX_VERSION.incrementAndGet();
        // Optimistic decrement (approximate)
        ENTITY_COUNT.decrementAndGet();
    }

    /**
     * Gets a completely random entity from the registry in O(1) time.
     * Returns null if registry is empty.
     */
    @Nullable
    public static SocialEntity getRandomEntity() {
        // Check if array needs rebuild
        if (INDEX_VERSION.get() != ARRAY_VERSION) {
            rebuildFastArray();
        }

        SocialEntity[] array = FAST_ACCESS_ARRAY;
        if (array.length == 0)
            return null;

        return array[FastRandom.INSTANCE.nextInt(array.length)];
    }

    private static synchronized void rebuildFastArray() {
        if (INDEX_VERSION.get() == ARRAY_VERSION)
            return; // Double check locking

        List<SocialEntity> liveEntities = new ArrayList<>(ENTITY_COUNT.get() + 100);

        // Iterate shards and collect live objects
        for (Set<WeakReference<SocialEntity>> shard : SHARDS) {
            Iterator<WeakReference<SocialEntity>> it = shard.iterator();
            while (it.hasNext()) {
                WeakReference<SocialEntity> ref = it.next();
                SocialEntity ent = ref.get();
                if (ent == null || ent.dasik$asEntity().isRemoved()) {
                    it.remove(); // Cleanup dead references
                } else {
                    liveEntities.add(ent);
                }
            }
        }

        FAST_ACCESS_ARRAY = liveEntities.toArray(new SocialEntity[0]);
        ENTITY_COUNT.set(liveEntities.size()); // Correct count
        ARRAY_VERSION = INDEX_VERSION.get();
    }

    private static int getShardIndex(SocialEntity entity) {
        // Uniform distribution based on System identity hash
        return (System.identityHashCode(entity) & 0x7FFFFFFF) % SHARD_COUNT;
    }

    /**
     * Checks if an entity is registered.
     */
    public static boolean contains(SocialEntity entity) {
        if (entity == null)
            return false;
        int shardIdx = getShardIndex(entity);
        Set<WeakReference<SocialEntity>> shard = SHARDS[shardIdx];

        for (WeakReference<SocialEntity> ref : shard) {
            if (ref.get() == entity)
                return true;
        }
        return false;
    }

    /**
     * @return Estimated count of tracked entities.
     */
    public static int getCount() {
        return ENTITY_COUNT.get();
    }
}
