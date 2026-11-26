# 🎫 [BL-DASIK-026] Restore `26.1.2` DasikLibrary ABI Parity (`ConfigHelper.save` void, `PlayerVisionTracker.canSee(ServerPlayer)`, `StochasticUtil`)

- **Ticket ID**: `[BL-DASIK-026]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Target Version**: `Dasik Library v26.1.2` (`1.13.3+26.1.2`)
- **Scope**: `Dasik Library v26.1.2/Dasik Library 26.1.2/common/src/main/java/net/dasik/social/api/config/ConfigHelper.java`, `PlayerVisionTracker.java`, `net/dasik/social/util/StochasticUtil.java`, ABI parity tests, Modrinth `Fabric 26.1.2` profile sync
- **Status**: `✅ RESOLVED`
- **Grilling Status**: `✅ PASSED`
- **Target Repository / Root**: `Dasik Library v26.1.2` (tracked in [`MC-DasikLibrary`](file:///E:/Minecraft%20Project/MC-DasikLibrary/BACKLOG.md))
- **Umbrella Reference**: [`BL-WS-051`](file:///E:/Minecraft%20Project/tickets/BL-WS-051.md)
- **Date Added**: `2026-11-25`

---

## 🎯 1. Problem Statement & Player Impact

Auditing both Modrinth test profiles (`code_auditor.py --check-profiles`) and `minecraft-mod-release-hub/archives/` revealed **3 binary ABI divergences** in `Dasik Library v26.1.2` (`dasik-library-1.13.2+26.1.2-fabric.jar`) relative to `MC-DasikLibrary` (`26.3`, `1.13.2.jar`) and older `26.1.2` builds, plus an outdated `dasik-library-1.13.0+26.1.2-fabric.jar` sitting in `ModrinthApp/profiles/Fabric 26.1.2/mods/`:

1. **`ConfigHelper.save(Path, T, Logger)` return type drift (Dual-ABI Requirement)**:
   - `MC-DasikLibrary` (`26.3`): `public static <T> void save(Path configPath, T instance, Logger logger)` (`)V`)
   - `Dasik Library v26.1.2`: `public static <T> boolean save(Path configPath, T instance, Logger logger)` (`)Z`)
   - **10 archived `26.1.2` mods** (`Agrarian Reform 2.2.29`, `Better Bats 1.1.41`, `Better Crossbows 1.0.14`, `Durability Multiplier 1.1.23`, `Item Clumps 1.4.0`, `Magnet 1.3.18`, `Ore Amplifier 1.3.23`, `Potion Stacker Addon 1.0.0`, `Stack Size Adjuster 1.4.19`, `Stew Stacker Addon 1.0.3`) invoke `ConfigHelper.save(...)V`.
   - **CRITICAL DUAL-ABI DISCOVERY**: `Better Dogs 5.7.24+26.1.2` (`vanilla-outsider-better-dogs-5.7.24+26.1.2-fabric.jar` & `-neoforge.jar`) in `Fabric 26.1.2`, `NeoForge 26.1.2`, and `hub/archives/Better Dogs/` invokes `ConfigHelper.save(...)Z`! Purely reverting to `)V` without a bytecode bridge would break `Better Dogs 5.7.24+26.1.2`. *(Note: internal callers inside `26.1.2/ConfigHelper.java:61,92,94,122` discard the `boolean` return value.)*
2. **`PlayerVisionTracker.canSee(ServerPlayer, Entity)` missing in `26.1.2`**:
   - Exists in `MC-DasikLibrary` (`PlayerVisionTracker.java:100`), omitted from `26.1.2` (`Player` overloads only) -> breaks `Magnet 1.3.18+26.1.2` (`MagnetCommand`).
3. **`StochasticUtil` method mismatch in `26.1.2`**:
   - `MC-DasikLibrary` (`StochasticUtil.java:8,19`) provides `getAmplifiedCount(int, int, RandomSource)` and `getAmplifiedProbability(float, int, RandomSource)`; `26.1.2` only has `nextPoisson` / `nextGaussian` / `rollChance` -> breaks `Ore Amplifier 1.3.23+26.1.2` (`OreLogic`, `RarityFilterMixin`).

---

## 🔬 2. Architectural Blueprint

1. **Dual-ABI `ConfigHelper.save(...)` (`void` Source + Idempotent `)Z` Bridge, `26.1.2`)**:
   - Introduce package-private `static <T> boolean saveInternal(Path configPath, T instance, Logger logger)` in `Dasik Library v26.1.2`'s `ConfigHelper.java`.
   - Change `public static <T> void save(Path configPath, T instance, Logger logger)` to `void` (`)V`) delegating to `saveInternal(...)`, while keeping `public static <T> boolean save(Path configPath, T config)` delegating to `saveInternal(configPath, config, LOGGER)`.
   - Inject an idempotent ASM `compileJava.doLast` bytecode bridge in `:common/build.gradle` (`[PAT-20261120-004]` / `[ERR-20261120-004]`) that checks `existingMethods` before emitting `ACC_PUBLIC | ACC_STATIC | ACC_BRIDGE | ACC_SYNTHETIC` `save(Ljava/nio/file/Path;Ljava/lang/Object;Lorg/slf4j/Logger;)Z` delegating to `saveInternal(...)Z`. This satisfies BOTH the 10 `)V` mods AND `Better Dogs 5.7.24+26.1.2` (`)Z`).
   - Update `ConfigHelperTest.java:165-166` (`testSaveAtomicity`) to call `void save(...)` without expecting a `boolean` return, and verify `saveInternal(...)` return value directly.
2. **Restore `PlayerVisionTracker.canSee(ServerPlayer, Entity)` (`26.1.2`)**:
   - Add the `ServerPlayer` overload delegating cleanly to `canSee((Player) player, target, DEFAULT_MAX_DISTANCE, DEFAULT_FOV_DEGREES)`.
3. **Add `StochasticUtil.getAmplifiedCount` & `getAmplifiedProbability` (`26.1.2`)**:
   - Port both methods from `MC-DasikLibrary/src/main/java/net/dasik/social/util/StochasticUtil.java` alongside existing `nextPoisson` / `nextGaussian` / `rollChance`.
4. **Bytecode ABI Regression Guard & 5-Point Deploy**:
   - Add `DasikLibrary2612AbiParityTest` asserting exact JVM descriptors (`ConfigHelper;save(...)V`, `ConfigHelper;save(...)Z` with zero duplicate methods, `PlayerVisionTracker;canSee(ServerPlayer, Entity)Z`, `StochasticUtil;getAmplifiedCount(IILnet/minecraft/util/RandomSource;)I`, `StochasticUtil;getAmplifiedProbability(FILnet/minecraft/util/RandomSource;)Z`), deploy `1.13.3+26.1.2` (Fabric + NeoForge) to all 5 distribution points, and purge stale `1.13.0` from BOTH `ModrinthApp/profiles/Fabric 26.1.2/mods/` and `ModrinthApp/profiles/NeoForge 26.1.2/mods/`.

---

## 🛡️ 3. Blast Radius & Multi-Era Parity Table

| Target | Risk / Failure Mode | Blast Radius | Mitigation |
| :--- | :--- | :--- | :--- |
| **`26.1.2` Fabric & NeoForge Consumer Mods** | `NoSuchMethodError` on `ConfigHelper.save` (`)V` OR `)Z`), `PlayerVisionTracker.canSee`, `StochasticUtil` | All 10 `)V` mods + `Better Dogs 5.7.24+26.1.2` (`)Z`) | Dual-ABI (`void` Java + idempotent ASM `)Z` bridge) + `code_auditor.py --check-profiles` |
| **Dedicated Server Sidedness** | Introducing client `Screen` imports in common `DasikSupportHelper` | Server boot crash | Zero changes to `DasikSupportHelper.openKofi(Object)`; keep `ClientSupportScreenHelper` isolated |
| **Idempotent ASM Injection (`[ERR-20261120-004]`)** | Duplicate `save(...)Z` method emitted across repeated builds causing `ClassFormatError` | Startup crash in KnotClassLoader | Track `existingMethods` in `visitMethod` and skip bridge emission if already present |

---

## 🚫 4. Anti-Goals (Creep Shield)

1. ❌ **Do NOT restore `DasikSupportHelper.openKofi(Screen)`** — exposing `net.minecraft.client.gui.screens.Screen` on `DasikSupportHelper` violates Pillar 5 Sidedness on Dedicated Servers (`BL-DASIK-008`); consumer mods (`BL-NR-020`, `BL-CGR-007`) must migrate to `ClientSupportScreenHelper.openKofi(Screen)` or `openKofi((Object) ...)`.
2. ❌ **Do NOT remove existing `StochasticUtil` methods (`nextPoisson`, `nextGaussian`, `rollChance`)** — add missing methods without breaking any existing callers.
3. ❌ **Do NOT bundle new library features or touch `1.20.1`–`1.21.11` / `26.3` anchors** beyond verifying parity.

---

## 🏁 5. 5-Pillar Verification Checklist

- [x] **Pillar 1 (Chaos Property Fuzzing)**: `StochasticUtil` & `ConfigHelper` fuzz tests pass across extreme counts, probabilities (`0f`, `1f`, `NaN`), and invalid paths.
- [x] **Pillar 2 (Concurrency Load)**: Concurrent `ConfigHelper.save` stress test passes cleanly.
- [x] **Pillar 3 (Command / Registry / ABI Reflection)**: Bytecode descriptor test verifies all 4 restored JVM method signatures in compiled `.class` files.
- [x] **Pillar 4 (GameTests)**: `./gradlew test build --no-daemon --console=plain` passes across Fabric + NeoForge `26.1.2`.
- [x] **Pillar 5 (Sidedness & Profile Audit)**: `code_auditor.py --check-profiles` reports `0` `ConfigHelper`/`PlayerVisionTracker`/`StochasticUtil` errors in `Fabric 26.1.2`.
