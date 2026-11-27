# Changelog - Dasik Library (1.21.11)

All notable player-facing and developer changes for the 1.21.11 anchor are documented here.

## [1.4.4+1.21.11] - 2026-11-23

### Added
- **Unified Creator Support Prompt Engine (`DasikPromptRegistry`, `ClientPromptManager`, `DasikLibraryClientFabric`, `DasikLibraryClientNeoForge`)**: Added server-safe `DasikPromptRegistry` API, 8-second (160-tick) world-start welcome greeting with dimension portal suppression, and 3-second/5-minute FIFO milestone prompt queue across Fabric and NeoForge.
- **Client Configuration Persistence (`DasikClientConfig`)**: Added atomic client-side JSON persistence (`config/dasik_library_client.json`) with `showSupportPrompts` and `showWorldStartGreeting` toggles.
- **YACL Opt-Out Toggle (`ClientSupportScreenHelper`, `UniversalConfigScreen`, `DasikSupportHelper`)**: Added `Show Support Prompts in Chat` (`createSupportPromptsOption()`) to YACL configuration categories.

## [1.4.3+1.21.11] - 2026-11-21

### Fixed
- **ConfirmLinkScreen URI Migration for MC 26.3+ (`ClientSupportScreenHelper`)**: Migrated `ClientSupportScreenHelper.openUrl` from the deleted `ConfirmLinkScreen.confirmLinkNow(Screen, String)` overload to `ConfirmLinkScreen.confirmLinkNow(Screen, URI)` to eliminate runtime `NoSuchMethodError` when clicking Ko-fi or Discord support buttons in YACL configuration screens on Minecraft 26.3+ and future releases.
- **URI Syntax & Malformed URL Safety**: Added safe `URI.create(url.trim())` parsing guarded against `IllegalArgumentException` and `NullPointerException`, preventing client crashes on invalid or malformed URL strings.
- **Pillar 5 Bytecode Descriptor Contract**: Enforced automated bytecode verification in `ClientSupportScreenHelperTest` asserting `(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V` presence and total elimination of `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V`.

## [1.4.2+1.21.11] - 2026-11-16

### Added
- **Transitive Vanilla-Client Server Compatibility**: Converted all support footer `Component.translatable(...)` keys in `DasikSupportHelper` (`KEY_DISCORD_CHAT_PROMPT`, `KEY_DISCORD_CHAT_LINK`, `KEY_DISCORD_CHAT_HOVER`, `KEY_CHAT_PROMPT`, `KEY_CHAT_LINK`, `KEY_CHAT_HOVER`) to `Component.translatableWithFallback(...)` with human-readable English fallbacks. Unmodded vanilla clients connecting to Fabric and NeoForge dedicated servers running Dasik Library commands now see formatted support and community links instead of raw translation keys.

## [1.4.1+1.21.11]

### Added
- **Native Multi-Loader Architecture**: Restructured Dasik Library for Minecraft 1.21.11 into a multi-project Gradle build (`:common`, `:fabric`, `:neoforge`) powered by Architectury Loom.
- **NeoForge Distribution**: Added full native NeoForge support (`dasik-library-1.4.1+1.21.11-neoforge.jar`) targeting NeoForge 21.11.x with `META-INF/neoforge.mods.toml` and `pack.mcmeta`.
- **Platform Abstraction Layer (`net.dasik.social.platform`)**: Universal Java Service Provider Interface (SPI) abstraction via `IPlatformHelper` providing side-safe, loader-independent access to mod directories, mod version queries, platform checks (`isModLoaded`), and environment queries (`isClientSide`, `isDevelopmentEnvironment`).
- **Loader-Agnostic Client Greeting**: Decoupled `DasikUpdateGreetingHandler` from Fabric-specific `ClientPlayConnectionEvents`, utilizing universal client event hooks on Fabric and NeoForge (`ClientPlayerNetworkEvent.LoggingIn`) with 0 Fabric classloading leaks.

## [1.4.0+1.21.11]

### Added
- **Public Headless Testing & Verification Framework** (`net.dasik.social.api.test`): Added high-performance verification APIs enabling headless integration tests without spawning graphical client windows.
- **Universal Bytecode & Manifest Contract Verifier** (`DasikContractVerifier`): Headless-safe JVM bytecode analyzer inspecting class files and manifests for illegal sidedness, unresolved entrypoints, and deprecated legacy guards.
- **Headless Virtual Player Bot** (`VirtualPlayerBot`): Embedded Netty-backed server-side player bot wrapper simulating player interactions, item usage, block placement, and entity attacks safely during headless tests.
- **Pillar 5 Mixin Linkage Auditor** (`VirtualPlayerBot.auditAllMixins()`): Autonomous audit hook validating SpongePowered Mixin method descriptors across active ClassLoaders.

## [1.3.0+1.21.11]

### Added
- **Zero-Boilerplate Universal YACL Config Screen Generator** (`UniversalConfigScreen`): Ported 1-line client configuration GUI generator for YetAnotherConfigLib (YACL) v3 with full ModMenu integration (`createUniversalFactory`).
- **Side-Safe Configuration Field Annotation** (`@ConfigOption`): Added `@ConfigOption` annotation in `net.dasik.social.api.config` for declaring config fields with 0 client imports in common code.
- **Knot ClassLoader Reflection Safety Bridge** (`YaclSafetyBridge`): Dynamic reflection wrapper for YACL v3 allowing mods to offer rich client configuration screens when YACL is present while gracefully falling back when YACL is absent.
- **Dynamic 2-Way GameRule Configuration Synchronization** (`DynamicGameRuleConfigSync`): Bidirectional real-time synchronization between in-game GameRules, client GUI option changes, and custom JSON configuration files.
- **Automated Creator Support Integration**: Automatically injects centralized Discord community and Ko-fi creator support buttons into every generated configuration category.
- **5-Pillar Autonomous Test Verification**: Added extensive chaos fuzzing (10k property iterations), multi-threaded concurrency stress testing (50 threads), and Knot ClassLoader isolation tests.

## [1.2.0+1.21.11]

### Added
- **Universal Brigadier Command Framework**: Complete studio command suite system ported to Minecraft 1.21.11.
  - `UniversalCommandRegistry`: Fluent builder constructing standardized studio command suites (`/<root> help`, `status`, `get`, `set`, `reset`, `reload`) with automatic tab completions and dual-level permission gating (All / Gamemasters).
  - `GameRuleCommandBridge`: Reactive two-way synchronizer bridging Brigadier commands, Minecraft `GameRule<?>` entries, and persistent JSON configs with saturated arithmetic bounds and warning telemetry.
  - `CommandFeedbackFormatter`: Unified gold mod prefix (`§6[ModName]§r`), color-coded status states (success, warning, error, info), and clickable Discord & Ko-fi interactive footers.
  - `ModCommandDispatcher`: Thread-safe central registry hooked directly into Fabric's `CommandRegistrationCallback`.
  - Automatic `ModCommandDispatcher.init()` lifecycle hook in `DasikLibraryMod.onInitialize()`.

## [1.1.0+1.21.11]

### Added
- **Social Event Scheduler**: Universal `SocialScheduler` and `EntitySocialScheduler` supporting delayed, interval, recurring, and conditional execution of entity behaviors and world callbacks.
- **Signal Bus System**: Decoupled pub/sub event bus (`SignalBus`, `Signal`, `SignalType`) enabling lightweight inter-system messaging without direct couplings.
- **Behavior Profiles**: Pluggable `BehaviorProfileManager`, `EntityBehaviorProfile`, and `BehaviorProfileRegistry` for dynamic entity personality and state management.
- **Genetics Loot Modifiers**: Dynamic `GeneticsLootModifier` and `GeneticsLootRegistry` injecting gene-influenced drops into vanilla loot tables.
- **Dynamic Registry Scanner & Performance Utilities**: `DynamicRegistryScanner`, `ScaleLimitSupplier`, and `PerformanceConfig` optimizations for seamless multi-era compatibility.

## [1.0.0+1.21.11]

### Added
- **Dynamic GameRule Engine**: Universal `DynamicGameRuleManager` with fluent builders and automatic in-game translation injection (`LanguageMixin`).
- **Genetics & DNA Engine**: Resilient `EntityGenetics` architecture backed by native Fabric Data AttachmentType persistence.
- **Size & Trait Scaling**: `SizeStatsBuilder` mapped to native `Attributes.SCALE` with client-side visual scaling and linked attribute scaling.
- **Swarm & Group AI**: Boids-inspired murmuration flocking with `FlockState`, `FollowLeaderGoal`, terrestrial and aerial flocking strategies, and deterministic leader election.
- **High-Performance Vision Engine**: On-demand `PlayerVisionTracker` with 3-stage fast-fail pipeline (distance -> dot product FOV -> line-of-sight raycasting).
- **Creator Support & Navigation**: Clickable chat footers (`/dasik` or mod command hooks) for Ko-fi and Discord, plus reflection-isolated YACL `ButtonOption` and `GuiHelper`.
