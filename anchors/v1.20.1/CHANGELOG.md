# Changelog - Dasik Library (1.20.1)

All notable player-facing and developer changes for the 1.20.1 anchor are documented here.

## [1.4.7+1.20.1] - 2026-11-23

### Added
- **Unified Creator Support Prompt Engine (`DasikPromptRegistry`, `ClientPromptManager`, `DasikLibraryClientFabric`, `DasikLibraryClientNeoForge`)**: Added server-safe `DasikPromptRegistry` API, 8-second (160-tick) world-start welcome greeting with dimension portal suppression, and 3-second/5-minute FIFO milestone prompt queue across Fabric and Forge.
- **Client Configuration Persistence (`DasikClientConfig`)**: Added atomic client-side JSON persistence (`config/dasik_library_client.json`) with `showSupportPrompts` and `showWorldStartGreeting` toggles.
- **YACL Opt-Out Toggle (`ClientSupportScreenHelper`, `UniversalConfigScreen`, `DasikSupportHelper`)**: Added `Show Support Prompts in Chat` (`createSupportPromptsOption()`) to YACL configuration categories.

## [1.4.6+1.20.1] - 2026-11-21

### Fixed
- **ConfirmLinkScreen URL & Screen Cast Hardening (`ClientSupportScreenHelper`) ([BL-DASIK-023])**:
  - Added safe `URI.create(url.trim())` syntax validation and error handling for `openUrl` calls, catching malformed URL strings without unhandled exceptions or client crashes.
  - Replaced raw `(Screen) parentScreen` casts with safe `parentScreen instanceof Screen s ? s : null` narrowing across `openUrl`, `openKofi`, and `openDiscord`, preventing `ClassCastException` on untyped objects.
  - Added Multi-Loader platform environment check via `Services.PLATFORM.isClient()` preventing dedicated server ClassLoader crashes.
  - Verified 5-Pillar test suite in `ClientSupportScreenHelperTest` validating headless execution, null/blank inputs, malformed URIs, and bytecode descriptor references.

## [1.4.5+1.20.1] - 2026-11-20

### Fixed
- **DynamicGameRuleManager Duplicate Method ClassFormatError ([BL-DASIK-025])**:
  - Enforced strict idempotency in `FabricBridgeInjector` via ASM `visitMethod` deduplication, preventing duplicate `registerCategory`, `booleanRule`, and `integerRule` bytecode emissions across repeated builds and remapping phases.
  - Resolves `java.lang.ClassFormatError: Duplicate method name "registerCategory"` during KnotClassLoader initialization on Fabric 1.20.1.

## [1.4.4+1.20.1] (Broken) - 2026-11-20

### Fixed
- **PlayerVisionTracker ClipContext Null Entity NPE Safety ([BL-DASIK-024])**:
  - Replaced ambiguous `level.clip(new ClipContext(..., null))` in `hasLineOfSight` and `hasLineOfSightToBlock` with `BlockGetter.traverseBlocks` using `CollisionContext.empty()`.
  - Added null check on `observer` in `hasClearSightline`.
  - Resolves latent `NullPointerException: Cannot invoke "net.minecraft.class_1297.method_21752()" because "$$0" is null` during raycast checks without an active entity on MC 1.20.1.

## [1.4.3+1.20.1] (Broken) - 2026-11-20

### Fixed
- **Fabric API DynamicGameRuleManager ABI Backward Compatibility**: Injected bytecode bridge methods into `DynamicGameRuleManager` in `dasik-library-fabric.jar` for `registerCategory(ResourceLocation, Component)`, `booleanRule(String, CustomGameRuleCategory, boolean)`, and `integerRule(String, CustomGameRuleCategory, int)` accepting/returning Fabric API's `CustomGameRuleCategory`. Eliminates `NoSuchMethodError` crashes when loading pre-1.4.1 Fabric mods alongside newer Multi-Loader mod builds.

## [1.4.2+1.20.1] - 2026-11-16

### Fixed
- **Forge 1.20.1 Handshake Compatibility**: Registered `IExtensionPoint.DisplayTest` with `NetworkConstants.IGNORESERVERONLY` in `DasikLibraryNeoForge` and set `displayTest="IGNORE_SERVER_VERSION"` in both `mods.toml` and `neoforge.mods.toml`. Dedicated servers running Dasik Library on Forge/NeoForge 1.20.1 no longer reject unmodded vanilla clients during network handshake.
- **Transitive Vanilla Client Fallback Text**: Converted all 6 creator support chat prompts, links, and tooltips in `DasikSupportHelper` (`getDiscordCommandFooter()`, `getKofiCommandFooter()`) from bare `Component.translatable` to `Component.translatableWithFallback`. Prevents unmodded vanilla clients on dedicated servers from seeing raw dotted translation keys (`dasiklibrary.support.*`).

## [1.4.1+1.20.1]

### Added
- **Native Multi-Loader (Fabric & NeoForge) Architecture**:
  - Re-engineered modular 3-module build pipeline (:common, :fabric, :neoforge) powered by Architectury Loom compiler plugins.
  - Implemented Java standard SPI (java.util.ServiceLoader) platform abstraction across IPlatformHelper and IGameRuleHelper.
  - Native Forge/NeoForge SRG refmap remapping with full binary compatibility across Forge 47.x and NeoForge 1.20.1.
  - Bundles mandatory pack.mcmeta (pack format 15) and dual metadata (mods.toml and 
eoforge.mods.toml).

## [1.4.0+1.20.1]

### Added
- **Public Headless Testing & Verification Framework** (`net.dasik.social.api.test`): Added high-performance verification APIs enabling headless integration tests without spawning graphical client windows.
- **Universal Bytecode & Manifest Contract Verifier** (`DasikContractVerifier`): Headless-safe JVM bytecode analyzer inspecting class files and manifests for illegal sidedness, unresolved entrypoints, and deprecated legacy guards.
- **Headless Virtual Player Bot** (`VirtualPlayerBot`): Embedded Netty-backed server-side player bot wrapper simulating player interactions, item usage, block placement, and entity attacks safely during headless tests.
- **Pillar 5 Mixin Linkage Auditor** (`VirtualPlayerBot.auditAllMixins()`): Autonomous audit hook validating SpongePowered Mixin method descriptors across active ClassLoaders.

## [1.3.0+1.20.1]

### Added
- **Zero-Boilerplate Universal YACL Config Screen Generator** (`UniversalConfigScreen`): Ported 1-line client configuration GUI generator for YetAnotherConfigLib (YACL) v3 with full ModMenu integration (`createUniversalFactory`).
- **Side-Safe Configuration Field Annotation** (`@ConfigOption`): Added `@ConfigOption` annotation in `net.dasik.social.api.config` for declaring config fields with 0 client imports in common code.
- **Knot ClassLoader Reflection Safety Bridge** (`YaclSafetyBridge`): Dynamic reflection wrapper for YACL v3 allowing mods to offer rich client configuration screens when YACL is present while gracefully falling back when YACL is absent.
- **Dynamic 2-Way GameRule Configuration Synchronization** (`DynamicGameRuleConfigSync`): Bidirectional real-time synchronization between in-game GameRules, client GUI option changes, and custom JSON configuration files.
- **Automated Creator Support Integration**: Automatically injects centralized Discord community and Ko-fi creator support buttons into every generated configuration category.
- **5-Pillar Autonomous Test Verification**: Added extensive chaos fuzzing (10k property iterations), multi-threaded concurrency stress testing (50 threads), and Knot ClassLoader isolation tests.

## [1.2.0+1.20.1]

### Added
- **Universal Brigadier Command Framework**: Complete studio command suite system ported to Minecraft 1.20.1.
  - `UniversalCommandRegistry`: Fluent builder constructing standardized studio command suites (`/<root> help`, `status`, `get`, `set`, `reset`, `reload`) with automatic tab completions and dual-level permission gating (All / Gamemasters).
  - `GameRuleCommandBridge`: Reactive two-way synchronizer bridging Brigadier commands, Minecraft `GameRules.Key<?>` entries, and persistent JSON configs with saturated arithmetic bounds and warning telemetry.
  - `CommandFeedbackFormatter`: Unified gold mod prefix (`§6[ModName]§r`), color-coded status states (success, warning, error, info), and clickable Discord & Ko-fi interactive footers.
  - `ModCommandDispatcher`: Thread-safe central registry hooked directly into Fabric's `CommandRegistrationCallback`.
  - Automatic `ModCommandDispatcher.init()` lifecycle hook in `DasikLibraryMod.onInitialize()`.

## [1.1.0+1.20.1]

### Added
- **Social Event Scheduler**: Universal `SocialScheduler` and `EntitySocialScheduler` supporting delayed, interval, recurring, and conditional execution of entity behaviors and world callbacks.
- **Signal Bus System**: Decoupled pub/sub event bus (`SignalBus`, `Signal`, `SignalType`) enabling lightweight inter-system messaging without direct couplings.
- **Behavior Profiles**: Pluggable `BehaviorProfileManager`, `EntityBehaviorProfile`, and `BehaviorProfileRegistry` for dynamic entity personality and state management.
- **Genetics Loot Modifiers**: Dynamic `GeneticsLootModifier` and `GeneticsLootRegistry` injecting gene-influenced drops into vanilla loot tables.
- **Dynamic Registry Scanner & Performance Utilities**: `DynamicRegistryScanner`, `ScaleLimitSupplier`, and `PerformanceConfig` optimizations for seamless multi-era compatibility.

## [1.0.0+1.20.1]

### Added
- **Dynamic GameRule Engine**: Universal `DynamicGameRuleManager` with fluent builders and automatic in-game translation injection (`LanguageMixin`).
- **Genetics & DNA Engine**: Resilient `EntityGenetics` architecture injected via `GeneticsHolder` duck interface on `LivingEntity` with automatic NBT save/load persistence.
- **Size & Trait Scaling**: `SizeStatsBuilder` with client-side visual scaling (`LivingEntityRendererMixin`) and linked attribute scaling (health, damage, speed).
- **Swarm & Group AI**: Boids-inspired murmuration flocking with `FlockState`, `FollowLeaderGoal`, terrestrial and aerial flocking strategies, and deterministic leader election.
- **High-Performance Vision Engine**: On-demand `PlayerVisionTracker` with 3-stage fast-fail pipeline (distance -> dot product FOV -> line-of-sight raycasting).
- **Creator Support & Navigation**: Clickable chat footers (`/dasik` or mod command hooks) for Ko-fi and Discord, plus reflection-isolated YACL `ButtonOption` and `GuiHelper`.
