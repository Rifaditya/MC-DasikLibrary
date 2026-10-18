# 🎫 [BL-DASIK-009] SignalBus Watchdog Freeze, Dimension Isolation & Safe Source Casting

- **Ticket ID**: `[BL-DASIK-009]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Target Version**: `All Anchors` (`1.11.3` / `26.2+`, `1.3.1+1.21.11`, `1.3.1+1.21.1`, `1.3.1+1.20.1`)
- **Scope**: `Signal Dispatch & Server Tick Stability` (`net.dasik.social.core.signal.SignalBus`, `net.dasik.social.api.signal.SignalType`)
- **Status**: `✅ RESOLVED`

---

## 🎯 1. Problem Statement & Player Impact
In `SignalBus.java`:
1. **Unconditional Infinite Loop / Watchdog Freeze**: `SignalType.THUNDER` defines `maxRange = Double.MAX_VALUE`. In `SignalBus.dispatch(Signal)`, `(int)Math.floor((origin.x + maxRange) / 16.0)` saturates to `Integer.MAX_VALUE`. The loop `for (int x = minX; x <= maxX; ++x)` never terminates because 32-bit signed integer overflow wraps `Integer.MAX_VALUE + 1` back to `Integer.MIN_VALUE`. If thunder or any large-range signal fires, the server tick hangs permanently until killed by the watchdog.
2. **Unchecked `(SocialEntity)` Cast Crash**: Line 85 executes:
   ```java
   case SAME_SPECIES -> source != null && entity.dasik$getSpeciesId().equals(((SocialEntity)source).dasik$getSpeciesId());
   ```
   `signal.getSource()` is typed as `LivingEntity`. If emitted by a `Player` or non-`SocialEntity` mob, this throws `ClassCastException` and crashes the server.
3. **Cross-Dimension Signal Bleed**: `SPATIAL_INDEX` is keyed solely by 2D packed coordinates (`ChunkPos.pack(x, z)`). Signals emitted in the Nether at `(0, 0)` are received by entities at `(0, 0)` in the Overworld and End.

## 🔬 2. Architectural Blueprint & Target Classes
- **Loop Bounding & Index Iteration**:
  - In `SignalBus.java`: If `maxRange > 256.0` or if the 2D chunk area `(long)(maxX - minX + 1) * (long)(maxZ - minZ + 1) > SPATIAL_INDEX.size()`, bypass the 2D `(x, z)` chunk iteration and iterate `SPATIAL_INDEX.values()` directly.
  - Guard loop indices with safe bounded step arithmetic to prevent integer overflow.
- **Safe Source Verification**:
  - In `shouldReceive()`, replace direct cast with pattern matching:
    ```java
    case SAME_SPECIES -> source instanceof SocialEntity socialSource && entity.dasik$getSpeciesId().equals(socialSource.dasik$getSpeciesId());
    ```
- **Dimension Isolation**:
  - Key `SPATIAL_INDEX` by dimension or check dimension equality in `shouldReceive()`:
    ```java
    if (source != null && entity.dasik$asEntity().level() != source.level()) return false;
    ```

## ⚓ 3. Multi-Era Anchor Parity Specifications
- Applicable across all active anchors: `26.2+`, `1.21.11`, `1.21.1`, and `1.20.1`.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Extreme Coordinates**: Protect against chunk coordinates near `Integer.MAX_VALUE / 16` and `Integer.MIN_VALUE / 16`.
- **Null Source Signals**: Environmental signals without an entity source (e.g. ambient thunder) must resolve cleanly without NPE.

## 🚀 5. Implementation Steps
1. Add spatial index bounding logic and bypass chunk loops for large-radius signals in `SignalBus.java`.
2. Update `shouldReceive` with dimension checks and safe `instanceof SocialEntity` checks.
3. Author unit tests covering `SignalType.THUNDER`, non-`SocialEntity` sources, and cross-dimension isolation.
4. Backport across `1.21.11`, `1.21.1`, and `1.20.1`.

## 🏁 6. 5-Pillar Verification Checklist
- [x] **Pillar 1 (Chaos Fuzzing)**: Dispatch 1,000 signals with `Double.MAX_VALUE`, `Double.POSITIVE_INFINITY`, and `NaN` without server hang.
- [x] **Pillar 2 (Concurrency Load)**: Multi-threaded dispatch without race conditions on `SPATIAL_INDEX`.
- [x] **Pillar 3 (Type Safety)**: Non-`SocialEntity` sources (Players, ArmorStands) dispatch without `ClassCastException`.
- [x] **Pillar 4 (Dimension Isolation)**: Overworld and Nether entities at identical coordinates do not cross-receive signals.
- [x] **Pillar 5 (Multi-Era Parity)**: All tests pass across `26.2+`, `1.21.11`, `1.21.1`, and `1.20.1`.
