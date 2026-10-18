# 🎫 [BL-DASIK-012] SocialRegistry Heap Leak Purge, Level Retention Guard & Hot-Path Zero-Allocation Polish

- **Ticket ID**: `[BL-DASIK-012]`
- **Category**: `[PERF]`
- **Priority**: `[HIGH]`
- **Target Version**: `All Anchors` (`1.11.3` / `26.2+`, `1.3.1+1.21.11`, `1.3.1+1.21.1`, `1.3.1+1.20.1`)
- **Scope**: `Memory Management & Hot-Path Performance` (`net.dasik.social.core.scheduler.SocialRegistry`, `net.dasik.social.core.group.GroupManager`, `net.dasik.social.core.genetics.GeneticsEngine`, `net.dasik.social.api.event.SocialEvent`)
- **Status**: `✅ RESOLVED`

---

## 🎯 1. Problem Statement & Player Impact
Several subsystems in `MC-DasikLibrary` exhibit memory retention and unnecessary hot-path allocations:
1. **Unbounded Heap Leak in `SocialRegistry`**:
   `SPECIES_INDEX` (`ConcurrentHashMap<ResourceLocation, ConcurrentLinkedQueue<WeakReference<SocialEntity>>>`) allocates a new `WeakReference` node on every entity registration, is **never queried anywhere in the codebase**, and never cleans cleared nodes—leaking queue nodes over long server runtimes.
2. **`ServerLevel` Retention via `FAST_ACCESS_ARRAY`**:
   `FAST_ACCESS_ARRAY` holds strong references to `SocialEntity` instances and only refreshes lazily during `getRandomEntity()`. When worlds unload or servers shut down, these strong references pin entire `ServerLevel` and `Chunk` trees in heap memory.
3. **Negative `ENTITY_COUNT` Drift**:
   `SocialRegistry.unregister()` decrements `ENTITY_COUNT` without checking if the entity was actually registered, which can corrupt the count and cause `IllegalArgumentException` in `new ArrayList<>(ENTITY_COUNT.get() + 100)`.
4. **Hot-Path Allocations in `GroupManager`**:
   `computeFlockState()` creates `2 * flock.size()` transient `Vec3` allocations per tick, while `findAndSetLeader()` creates a Stream and lambda comparator per check.
5. **Inverted Boolean Contract on `SocialEvent.tick()`**:
   `SocialEvent.tick()` Javadoc says `@return True if event should continue`, but `EntitySocialScheduler` treats `true` as `finished` and removes it immediately.
6. **Division-by-Zero in `GeneticsEngine`**:
   `inheritGenetics` divides by `inbreedingMultiplier` without guarding against `0.0f`, producing `Float.NaN` that poisons entity traits.

## 🔬 2. Architectural Blueprint & Target Classes
- **`SocialRegistry.java` Sanitization**:
  - Purge dead `SPECIES_INDEX` map and its allocations.
  - Implement a `clear()` / `onServerStop()` hook to null out `FAST_ACCESS_ARRAY` and active schedulers upon world unload.
  - Fix `unregister()` to only decrement `ENTITY_COUNT` if `ACTIVE_SCHEDULERS.remove(uuid) != null`.
- **`GroupManager.java` Zero-Allocation Refactor**:
  - Accumulate primitive `sumX, sumY, sumZ` instead of chained `Vec3.add()`.
  - Replace Stream with a standard non-allocating `for` loop to find the leader entity.
- **`SocialEvent.java` Contract Alignment**:
  - Align Javadoc and `EntitySocialScheduler.java` so method return semantics match documentation cleanly.
- **`GeneticsEngine.java` Division Guard & Attribute ID Update**:
  - Guard `inbreedingMultiplier <= 0.0f` to prevent `Float.NaN`.
  - Check both `"minecraft:max_health"` and `"minecraft:generic.max_health"` in `applyGeneticsModifiers`.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- Apply fixes across `MC-DasikLibrary` (`26.2+`), `1.21.11`, `1.21.1`, and `1.20.1`.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Zero Entities Registered**: `getRandomEntity()` returns `Optional.empty()` cleanly without array bounds exceptions.
- **Extreme Flock Sizes**: Flocks with 500+ entities calculate state at $O(N)$ with 0B transient allocations per tick.

## 🚀 5. Implementation Steps
1. Clean up `SocialRegistry.java` (purge `SPECIES_INDEX`, fix counter, add lifecycle clearing).
2. Refactor `GroupManager.java` vector accumulation to primitives.
3. Add `inbreedingMultiplier <= 0.0f` guard in `GeneticsEngine.java`.
4. Fix `DynamicEnchantmentManager.java` keySet map mutation.
5. Author memory and concurrency unit tests.
6. Backport across `1.21.11`, `1.21.1`, and `1.20.1`.

## 🏁 6. 5-Pillar Verification Checklist
- [x] **Pillar 1 (Zero-Allocation Tick)**: `computeFlockState` produces 0 transient `Vec3` heap allocations per tick.
- [x] **Pillar 2 (Memory Leak Freedom)**: Server stop / world unload clears `FAST_ACCESS_ARRAY`.
- [x] **Pillar 3 (Arithmetic Safety)**: Zero `Float.NaN` produced even with extreme 0.0f config inputs.
- [x] **Pillar 4 (Unit Tests)**: All scheduler and genetics tests pass.
- [x] **Pillar 5 (Multi-Era Parity)**: All 4 version anchors updated in lockstep.
