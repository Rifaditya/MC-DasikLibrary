# 🎫 [BL-DASIK-019] Native Multi-Loader Port (Fabric + NeoForge) for Minecraft 1.21.11

- **Ticket ID**: `[BL-DASIK-019]`
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Target Version**: `1.4.1+1.21.11`
- **Scope**: `Native Multi-Loader 3-Module Conversion (:common, :fabric, :neoforge)`
- **Status**: `✅ RESOLVED`
- **Master Workspace Reference**: [`BL-WS-041`](file:///e:/Minecraft%20Project/tickets/BL-WS-041.md)
- **Governance Mandate**: Item 1.5 of the [Studio Constitution](file:///e:/Minecraft%20Project/.agents/rules/Studio%20Constitution.md) & [Multi-Loader & NeoForge Engineering Policy](file:///e:/Minecraft%20Project/.agents/rules/Multi-Loader%20&%20NeoForge%20Engineering%20Policy.md)
- **Grilling Status**: ✅ PASSED

---

## 🎯 1. Problem Statement & Player Impact
Currently, **Dasik Library v1.21.11** is authored as a single-loader Fabric mod (`net.fabricmc.fabric-loom-remap`). Downstream mods such as **Better Dogs 1.21.11** cannot be ported to NeoForge without a native NeoForge release of Dasik Library 1.21.11. To support NeoForge players on Minecraft 1.21.11 while maintaining 100% backward compatibility for Fabric players, Dasik Library must be converted into a native 3-module multi-loader architecture (`:common`, `:fabric`, `:neoforge`).

### 1.3 🚫 Anti-Goals
1. **Zero Runtime Multi-Loader Dependency**: Never introduce `architectury-api` or external multi-loader runtime libraries into compiled production JARs. Production JARs must be 100% native standalone (`dasik-library-1.4.1+1.21.11-fabric.jar` and `dasik-library-1.4.1+1.21.11-neoforge.jar`).
2. **Zero Loader Leaks in Common**: Never import `net.fabricmc.*` or `net.neoforged.*` in `:common` runtime code. Client networking events and platform helpers must be abstracted via Java SPI (`IPlatformHelper`, `IGameRuleHelper`).
3. **Zero Public API Regressions**: Never break or alter existing public API method signatures in `net.dasik.social.api.*`. Downstream Fabric mods compiled against `1.4.0+1.21.11` must run seamlessly without recompilation.

---

## 🔬 2. Architectural Blueprint & Target Implementation

### 1. 3-Module Layout
- Root: `build.gradle` (Architectury plugin 3.4-SNAPSHOT, Loom 1.11-SNAPSHOT), `settings.gradle` (includes `:common`, `:fabric`, `:neoforge`), `gradle.properties` (shared properties, JVM args `-Xmx3G`).
- `:common`: Universal domain logic (genetics, boids AI, size stats, math, platform SPI interfaces, and unit tests).
- `:fabric`: `DasikLibraryFabric.java`, `DasikLibraryFabricClient.java`, `FabricPlatformHelper.java`, `fabric.mod.json`, `ClientPlayConnectionEvents.JOIN` registration.
- `:neoforge`: `DasikLibraryNeoForge.java`, `DasikLibraryNeoForgeClient.java`, `NeoForgePlatformHelper.java`, `META-INF/neoforge.mods.toml`, `pack.mcmeta`, `@SubscribeEvent ClientPlayerNetworkEvent.LoggingIn`.

### 2. Java SPI Platform Abstraction
- Expose `IPlatformHelper` in `net.dasik.social.platform.services`:
  - `getPlatformName()`
  - `isModLoaded(String modId)`
  - `isDevelopmentEnvironment()`
  - `isClient()`
  - `getConfigDir()`
  - `getModVersion(String modId, String defaultVal)`
- Access through `Services.PLATFORM`.
- Replace all direct `FabricLoader.getInstance()` calls in `:common` with `Services.PLATFORM`.

### 3. Sided Client Update Greeting Isolation
- Clean `DasikUpdateGreetingHandler.java` of `ClientPlayConnectionEvents` and `FabricLoader`.
- Expose pure static `onClientJoin()` in `:common`.
- Register `ClientPlayConnectionEvents.JOIN` strictly in `:fabric`.
- Register `@SubscribeEvent ClientPlayerNetworkEvent.LoggingIn` on `Bus.GAME` strictly in `:neoforge`.

### 4. Genetics & Duck Interface Universal Persistence
- Maintain `GeneticsHolder` duck interface injected into `LivingEntity` via `LivingEntityGeneticsMixin` for loader-agnostic NBT persistence, replacing single-loader Fabric Attachment API in `:common`.

---

## 🛡️ 3. Edge Cases & Blast Radius Analysis

| Dimension | Risk / Failure Mode | Active Safeguard |
| :--- | :--- | :--- |
| **Vanilla Contraptions** | Zero entity ticking or world mutation | Dasik Library is purely a foundation API; no vanilla blocks, items, or ticking entities modified. |
| **World Saves** | NBT corruption or missing attachment keys | `LivingEntityGeneticsMixin` serializes to `dasik_library:genetics` CompoundTag matching legacy NBT keys. |
| **Redstone / Timing** | Event listener execution delay | Greeting handler runs asynchronously on world join event; zero tick overhead. |
| **Multiplayer Safety** | Server crash from client classloading | Client classes guarded with `@Environment(EnvType.CLIENT)`, separated entrypoints, and `Services.PLATFORM.isClient()` checks. |

---

## ⚓ 4. Multi-Era Anchor Parity Specifications
- **MC 1.20.1**: Multi-Loader (`:common`, `:fabric`, `:neoforge`), published as `1.4.1+1.20.1`.
- **MC 1.21.1**: Multi-Loader (`:common`, `:fabric`, `:neoforge`), published as `1.4.1+1.21.1`.
- **MC 1.21.11**: This ticket converts 1.21.11 to Multi-Loader, publishing `1.4.1+1.21.11`.
- **MC 26.x**: Sovereign Lead anchor.

---

## 🏁 5. Verification Checklist
- [x] Root `./gradlew :common:test` passes 100% green.
- [x] `./gradlew :fabric:remapJar :neoforge:remapJar` builds both distribution JARs.
- [x] `javap -c` disassembles `:common` classes confirming zero `net/fabricmc` imports.
- [x] `pack.mcmeta` bundled with `pack_format: 48` (1.21.11 canonical format).
- [x] Dual JARs archived to release hub and local archive directories.
- [x] `RELEASE_QUEUE.md` and `CHANGELOG.md` updated with `1.4.1+1.21.11`.
