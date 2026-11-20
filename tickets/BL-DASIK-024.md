# 🎫 [BL-DASIK-024] Fix PlayerVisionTracker ClipContext Null Entity NPE in Anchor 1.20.1

- **Ticket ID**: `[BL-DASIK-024]`
- **Category**: `[BUGFIX]`
- **Priority**: `[HIGH]`
- **Scope**: `Anchor 1.20.1` (`Dasik Library v1.20.1`)
- **Status**: `✅ RESOLVED`
- **Target Repository / Root**: `MC-DasikLibrary` / `Dasik Library v1.20.1`

---

## 🎯 1. Problem Statement & Player Impact
In `PlayerVisionTracker.java` (lines 119 and 140) of `Dasik Library v1.20.1`, `new ClipContext(from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, null)` passed `null` for the entity parameter. In Minecraft 1.20.1, `ClipContext` only has a single public constructor taking an `Entity`, which immediately invokes `CollisionContext.of(entity)` $\rightarrow$ `new EntityCollisionContext(entity)`. During field initialization, `EntityCollisionContext` invokes `entity.isDescending()` on the null entity, causing an uncatchable `NullPointerException` whenever `PlayerVisionTracker.hasLineOfSight` or `hasLineOfSightToBlock` is called.

## 🔬 2. Architectural Blueprint & Target Classes
- Target Class: `net.dasik.social.api.vision.PlayerVisionTracker` in `Dasik Library v1.20.1/Dasik Library 1.20.1/common/src/main/java/`
- Target Method / Call Sites:
  - `hasLineOfSight`: Replace `level.clip(new ClipContext(..., null))` with `BlockGetter.traverseBlocks(from, to, null, ...)` using `CollisionContext.empty()` for visual shapes.
  - `hasLineOfSightToBlock`: Replace `level.clip(new ClipContext(..., null))` with `BlockGetter.traverseBlocks(from, to, null, ...)` using `CollisionContext.empty()` for visual shapes.
  - `hasClearSightline`: Add null check on `observer`, falling back to `hasLineOfSight(level, from, to, contactBuffer)` if null.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- **MC 1.20.1**: Traversal via `BlockGetter.traverseBlocks` with `CollisionContext.empty()`.
- **MC 1.21.1+**: Already uses `CollisionContext.empty()`. Anchor 1.20.1 achieves 100% architectural parity.

## 🛡️ 4. Anti-Goals (Creep Shield)
1. **Zero Sidedness Violation**: Do not import `net.minecraft.client.Minecraft` into common code.
2. **Zero Mathematical Drift**: Maintain identical contact tolerance and hit result resolution.
3. **Zero Anchor Cross-Contamination**: Strictly isolate version bump to MC 1.20.1 (`1.4.4+1.20.1`).

## 🚀 5. Implementation Steps
1. Update `PlayerVisionTracker.java` in `Dasik Library v1.20.1` to use `BlockGetter.traverseBlocks` with `CollisionContext.empty()`.
2. Add unit tests verifying `hasLineOfSight` with null and non-null arguments.
3. Bump `mod_version=1.4.4+1.20.1` in `gradle.properties`.
4. Run `./gradlew test build --no-daemon --console=plain`.
5. Deploy `dasik-library-1.4.4+1.20.1-fabric.jar` to Modrinth test profile and purge `1.4.3`.
6. Mark ticket `✅ RESOLVED`.
