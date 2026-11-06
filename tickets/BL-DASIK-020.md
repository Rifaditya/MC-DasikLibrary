# 🎫 [BL-DASIK-020] Native Multi-Loader Port (Fabric + NeoForge) for Minecraft 26.1.2

- **Ticket ID**: `[BL-DASIK-020]`
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Target Version**: `1.13.0+26.1.2`
- **Scope**: `Native Multi-Loader 3-Module Conversion (:common, :fabric, :neoforge)`
- **Status**: `✅ RESOLVED`
- **Governance Mandate**: Item 1.5 of the [Studio Constitution](file:///e:/Minecraft%20Project/.agents/rules/Studio%20Constitution.md) & [Multi-Loader & NeoForge Engineering Policy](file:///e:/Minecraft%20Project/.agents/rules/Multi-Loader%20&%20NeoForge%20Engineering%20Policy.md)
- **Grilling Status**: ✅ PASSED

---

## 🎯 1. Problem Statement & Player Impact
Downstream mods such as **Vanilla Outsider: Better Dogs 26.1.2** require `dasik_library` version `[1.11.2,)` on NeoForge 26.1.2. Previously, modern Dasik Library builds for 26.x were single-loader Fabric only. When launching NeoForge 26.1.2 profiles with Better Dogs, Minecraft crashed with `ModLoadingException: Mod better_dogs requires dasik_library 1.11.2 or above; Currently, dasik_library is not installed`.

To eliminate this blocker and fulfill studio anchor parity under the Multi-Era Anchor Parity Law, Dasik Library 26.1.2 has been ported to a native 3-module multi-loader architecture (`:common`, `:fabric`, `:neoforge`).

### 1.3 🚫 Anti-Goals
1. **Zero Runtime Multi-Loader Dependency**: Never introduce `architectury-api` or external multi-loader runtime libraries into compiled production JARs. Production JARs must be 100% native standalone (`dasik-library-1.13.0+26.1.2-fabric.jar` and `dasik-library-1.13.0+26.1.2-neoforge.jar`).
2. **Zero Loader Leaks in Common**: Never import `net.fabricmc.*` or `net.neoforged.*` in `:common` runtime code. Client networking events and platform helpers are abstracted via Java SPI (`IPlatformHelper`).
3. **Zero Public API Regressions**: Never break or alter existing public API method signatures in `net.dasik.social.api.*`. Downstream mods compiled against earlier library versions must run seamlessly without recompilation.

---

## 🔬 2. Architectural Blueprint & Target Implementation

### 1. 3-Module Layout
- Root: `build.gradle` (Architectury plugin 3.5-SNAPSHOT, Loom 1.14-SNAPSHOT `loom-no-remap`, Java 25), `settings.gradle` (includes `:common`, `:fabric`, `:neoforge`), `gradle.properties` (shared properties, JVM args `-Xmx3G -XX:+UseG1GC`).
- `:common`: Universal domain logic (genetics, boids AI, size stats, math, platform SPI interfaces, and unit tests).
- `:fabric`: `DasikLibraryFabric.java`, `DasikLibraryClientFabric.java`, `FabricPlatformHelper.java`, `fabric.mod.json`, `CommandRegistrationCallback` registration.
- `:neoforge`: `DasikLibraryNeoForge.java`, `DasikLibraryClientNeoForge.java`, `NeoForgePlatformHelper.java`, `META-INF/neoforge.mods.toml`, `pack.mcmeta`, `@SubscribeEvent RegisterCommandsEvent`, and `@SubscribeEvent ClientPlayerNetworkEvent.LoggingIn`.

### 2. Java SPI Platform Abstraction
- Exposes `IPlatformHelper` in `net.dasik.social.platform.services`:
  - `getPlatformName()`
  - `isModLoaded(String modId)`
  - `isDevelopmentEnvironment()`
  - `isClient()`
  - `getConfigDir()`
  - `getModVersion(String modId, String defaultVal)`
- Access through `Services.PLATFORM`.

### 3. Genetics & Duck Interface Universal Persistence
- Maintained `GeneticsHolder` duck interface injected into `LivingEntity` via `LivingEntityGeneticsMixin` using `ValueInput`/`ValueOutput` for loader-agnostic NBT persistence across world saves.

---

## 🛡️ 3. Edge Cases & Blast Radius Analysis

| Dimension | Risk / Failure Mode | Active Safeguard |
| :--- | :--- | :--- |
| **NeoForge Dependency** | `better_dogs` requires `dasik_library >= 1.11.2` | `META-INF/neoforge.mods.toml` declares `modId="dasik_library"` with version `1.13.0+26.1.2`, satisfying requirement. |
| **Classloading Hazard** | Client classloading on dedicated servers | Separated entrypoints (`DasikLibraryClientNeoForge` with `Dist.CLIENT`), `@Environment(EnvType.CLIENT)` annotations, and `Services.PLATFORM.isClient()` guards. |
| **26.1.2 Bytecode Shifts** | `ChunkPos.pack()` and 3-arg `interactOn` | Adapted `SignalBus` spatial index to use `ChunkPos.pack()` and updated `VirtualPlayerBot` test helper to pass target position. |
| **Chat Component API** | 26.x chat signature mismatch | Injected `addClientSystemMessage` in `DasikUpdateGreetingHandler` replacing legacy 1.21 `addMessage`. |

---

## ⚓ 4. Multi-Era Anchor Parity Specifications
- **MC 1.20.1**: Multi-Loader (`:common`, `:fabric`, `:neoforge`), published as `1.4.1+1.20.1`.
- **MC 1.21.1**: Multi-Loader (`:common`, `:fabric`, `:neoforge`), published as `1.4.1+1.21.1`.
- **MC 1.21.11**: Multi-Loader (`:common`, `:fabric`, `:neoforge`), published as `1.4.1+1.21.11`.
- **MC 26.1.2**: Multi-Loader (`:common`, `:fabric`, `:neoforge`), published as `1.13.0+26.1.2` (This ticket).
- **MC 26.x**: Sovereign Lead anchor.

---

## 🏁 5. Verification Checklist
- [x] Root `./gradlew :common:test` passes 100% green (88 tests).
- [x] `./gradlew build` builds both distribution JARs (`dasik-library-1.13.0+26.1.2-fabric.jar`, `dasik-library-1.13.0+26.1.2-neoforge.jar`).
- [x] `neoforge.mods.toml` bundled declaring `modId="dasik_library"`, `version="1.13.0+26.1.2"`, `neoforge >= 26.1`, `minecraft [26.1.2, 26.2)`.
- [x] `pack.mcmeta` bundled with canonical `pack_format: 48`.
- [x] NeoForge JAR deployed to `C:\Users\fmrif\AppData\Roaming\ModrinthApp\profiles\NeoForge 26.1.2\mods\`.
- [x] Dual JARs archived to release hub (`minecraft-mod-release-hub/archives/Dasik Library/`) and workspace archive directories.
