# Changelog - Dasik Library (1.21.1)

All notable player-facing and developer changes for the 1.21.1 anchor are documented here.

## [1.4.5+1.21.1] - 2026-11-23

### Added
- **Unified Creator Support Prompt Engine (`DasikPromptRegistry`, `ClientPromptManager`, `DasikLibraryClientFabric`, `DasikLibraryClientNeoForge`)**: Added server-safe `DasikPromptRegistry` API, 8-second (160-tick) world-start welcome greeting with dimension portal suppression, and 3-second/5-minute FIFO milestone prompt queue across Fabric and NeoForge.
- **Client Configuration Persistence (`DasikClientConfig`)**: Added atomic client-side JSON persistence (`config/dasik_library_client.json`) with `showSupportPrompts` and `showWorldStartGreeting` toggles.
- **YACL Opt-Out Toggle (`ClientSupportScreenHelper`, `UniversalConfigScreen`, `DasikSupportHelper`)**: Added `Show Support Prompts in Chat` (`createSupportPromptsOption()`) to YACL configuration categories.

## [1.4.4+1.21.1] - 2026-11-21

### Fixed
- **ConfirmLinkScreen URI Migration for MC 26.3+ (`ClientSupportScreenHelper`)**: Migrated `ClientSupportScreenHelper.openUrl` from the deleted `ConfirmLinkScreen.confirmLinkNow(Screen, String)` overload to `ConfirmLinkScreen.confirmLinkNow(Screen, URI)` to eliminate runtime `NoSuchMethodError` when clicking Ko-fi or Discord support buttons in YACL configuration screens on Minecraft 26.3+ and future releases.
- **URI Syntax & Malformed URL Safety**: Added safe `URI.create(url.trim())` parsing guarded against `IllegalArgumentException` and `NullPointerException`, preventing client crashes on invalid or malformed URL strings.
- **Pillar 5 Bytecode Descriptor Contract**: Enforced automated bytecode verification in `ClientSupportScreenHelperTest` asserting `(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V` presence and total elimination of `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V`.

## [1.4.3+1.21.1] - 2026-11-20

### Fixed
- **Fabric API DynamicGameRuleManager ABI Backward Compatibility**: Injected bytecode bridge methods into `DynamicGameRuleManager` in `dasik-library-fabric.jar` for `registerCategory(ResourceLocation, Component)`, `booleanRule(String, CustomGameRuleCategory, boolean)`, and `integerRule(String, CustomGameRuleCategory, int)` accepting/returning Fabric API's `CustomGameRuleCategory`. Eliminates `NoSuchMethodError` crashes when loading pre-1.4.1 Fabric mods alongside newer Multi-Loader mod builds.

## [1.4.2+1.21.1] - 2026-11-16

### Added
- **Transitive Vanilla-Client Server Compatibility (BL-DASIK-021)**:
  - Added embedded English fallback strings (`Component.translatableWithFallback`) across all interactive support and community footers in `DasikSupportHelper`.
  - Guarantees unmodded vanilla clients connecting to servers running Dasik Library receive clean, formatted English messages (`💬 Community: [Join Discord] • ☕ Enjoying the mod? [Support on Ko-fi]`) instead of raw unlocalized translation keys (`dasiklibrary.support.*`).

## [1.4.1+1.21.1]

### Added
- **Native Multi-Loader (Fabric & NeoForge) Architecture**:
  - Re-engineered modular 3-module build pipeline (:common, :fabric, :neoforge) powered by Architectury Loom compiler plugins.
  - Implemented Java standard SPI (`java.util.ServiceLoader`) platform abstraction across `IPlatformHelper` and `IGameRuleHelper`.
  - Native NeoForge 21.1.x compatibility and Fabric parity.
  - Bundles mandatory `pack.mcmeta` (pack format 48) and `neoforge.mods.toml` metadata.

## [1.4.0+1.21.1]

### Added
- **Public Headless Testing & Verification Framework** (`net.dasik.social.api.test`): Added high-performance verification APIs enabling headless integration tests without spawning graphical client windows.
- **Universal Bytecode & Manifest Contract Verifier** (`DasikContractVerifier`): Headless-safe JVM bytecode analyzer inspecting class files and manifests for illegal sidedness, unresolved entrypoints, and deprecated legacy guards.
- **Headless Virtual Player Bot** (`VirtualPlayerBot`): Embedded Netty-backed server-side player bot wrapper simulating player interactions, item usage, block placement, and entity attacks safely during headless tests.
- **Pillar 5 Mixin Linkage Auditor** (`VirtualPlayerBot.auditAllMixins()`): Autonomous audit hook validating SpongePowered Mixin method descriptors across active ClassLoaders.

## [1.3.0+1.21.1]

### Added
- **Zero-Boilerplate Universal YACL Config Screen Generator** (`UniversalConfigScreen`): Ported 1-line client configuration GUI generator for YetAnotherConfigLib (YACL) v3 with full ModMenu integration (`createUniversalFactory`).
- **Side-Safe Configuration Field Annotation** (`@ConfigOption`): Added `@ConfigOption` annotation in `net.dasik.social.api.config` for declaring config fields with 0 client imports in common code.
- **Knot ClassLoader Reflection Safety Bridge** (`YaclSafetyBridge`): Dynamic reflection wrapper for YACL v3 allowing mods to offer rich client configuration screens when YACL is present while gracefully falling back when YACL is absent.
- **Dynamic 2-Way GameRule Configuration Synchronization** (`DynamicGameRuleConfigSync`): Bidirectional real-time synchronization between in-game GameRules, client GUI option changes, and custom JSON configuration files.
- **Automated Creator Support Integration**: Automatically injects centralized Discord community and Ko-fi creator support buttons into every generated configuration category.
- **5-Pillar Autonomous Test Verification**: Added extensive chaos fuzzing (10k property iterations), multi-threaded concurrency stress testing (50 threads), and Knot ClassLoader isolation tests.

## [1.2.0+1.21.1]

### Added
- **Universal Brigadier Command Framework**: Complete studio command suite system ported to Minecraft 1.21.1.
  - `UniversalCommandRegistry`: Fluent builder constructing standardized studio command suites (`/<root> help`, `status`, `get`, `set`, `reset`, `reload`) with automatic tab completions and dual-level permission gating (All / Gamemasters).
  - `GameRuleCommandBridge`: Reactive two-way synchronizer bridging Brigadier commands, Minecraft `GameRules.Key<?>` entries, and persistent JSON configs with saturated arithmetic bounds and warning telemetry.
  - `CommandFeedbackFormatter`: Unified gold mod prefix (`§6[ModName]§r`), color-coded status states (success, warning, error, info), and clickable Discord & Ko-fi interactive footers.
  - `ModCommandDispatcher`: Thread-safe central registry hooked directly into Fabric's `CommandRegistrationCallback`.
  - Automatic `ModCommandDispatcher.init()` lifecycle hook in `DasikLibraryMod.onInitialize()`.

## [1.1.0+1.21.1]

### Added
- **Social Event Scheduler**: Universal `SocialScheduler` and `EntitySocialScheduler` supporting delayed, interval, recurring, and conditional execution of entity behaviors and world callbacks.
- **Signal Bus System**: Decoupled pub/sub event bus (`SignalBus`, `Signal`, `SignalType`) enabling lightweight inter-system messaging without direct couplings.
- **Behavior Profiles**: Pluggable `BehaviorProfileManager`, `EntityBehaviorProfile`, and `BehaviorProfileRegistry` for dynamic entity personality and state management.
- **Genetics Loot Modifiers**: Dynamic `GeneticsLootModifier` and `GeneticsLootRegistry` injecting gene-influenced drops into vanilla loot tables.
- **Dynamic Registry Scanner & Performance Utilities**: `DynamicRegistryScanner`, `ScaleLimitSupplier`, and `PerformanceConfig` optimizations for seamless multi-era compatibility.

## [1.0.0+1.21.1]

### Added
- **Dynamic GameRule Engine**: Universal `DynamicGameRuleManager` with fluent builders and automatic in-game translation injection (`LanguageMixin`).
- **Genetics & DNA Engine**: Resilient `EntityGenetics` architecture injected via `GeneticsHolder` duck interface on `LivingEntity` with automatic NBT save/load persistence.
- **Size & Trait Scaling**: `SizeStatsBuilder` mapped to native `Attributes.SCALE` with client-side visual scaling and linked attribute scaling.
- **Swarm & Group AI**: Boids-inspired murmuration flocking with `FlockState`, `FollowLeaderGoal`, terrestrial and aerial flocking strategies, and deterministic leader election.
- **High-Performance Vision Engine**: On-demand `PlayerVisionTracker` with 3-stage fast-fail pipeline (distance -> dot product FOV -> line-of-sight raycasting).
- **Creator Support & Navigation**: Clickable chat footers (`/dasik` or mod command hooks) for Ko-fi and Discord, plus reflection-isolated YACL `ButtonOption` and `GuiHelper`.
