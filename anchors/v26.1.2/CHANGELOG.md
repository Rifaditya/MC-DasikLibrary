# 📜 Changelog: Dasik Library v26.1.2

All notable changes to the Minecraft 26.1.2 version of Dasik Library are documented in this file.

## [1.13.3+26.1.2] - 2026-11-26
### Fixed
- **Dual-ABI `ConfigHelper.save` Restoration (`ConfigHelper`, `:common/build.gradle`)**: Extracted `saveInternal(Path, T, Logger)` and restored `public static <T> void save(Path, T, Logger)` (`)V`) matching `MC-DasikLibrary` (`26.3`) and 10 `26.1.2` consumer mods, paired with an idempotent ASM `compileJava.doLast` `ACC_BRIDGE | ACC_SYNTHETIC` bridge `save(Path, Object, Logger)Z` preserving compatibility with `Better Dogs 5.7.24+26.1.2` (Fabric & NeoForge).
- **Restored `PlayerVisionTracker.canSee(ServerPlayer, Entity)`**: Added the `ServerPlayer` overload delegating to `canSee((Player) player, target, DEFAULT_MAX_DISTANCE, DEFAULT_FOV_DEGREES)` for `Magnet 1.3.18+26.1.2` ABI compatibility.
- **Restored `StochasticUtil` Amplification Helpers**: Ported `getAmplifiedCount(int, int, RandomSource)` and `getAmplifiedProbability(float, int, RandomSource)` from `MC-DasikLibrary` while preserving existing `nextPoisson`, `nextGaussian`, and `rollChance` methods for `Ore Amplifier 1.3.23+26.1.2`.
- **ABI Parity & 5-Pillar Verification (`DasikLibrary2612AbiParityTest`, `ConfigHelperTest`)**: Added chaos fuzzing, concurrent save stress tests, and ASM bytecode descriptor assertions guaranteeing both `save(...)V` and `save(...)Z` exist exactly once with zero duplicates.

## [1.13.2+26.1.2] - 2026-11-23
### Added
- **Unified Creator Support Prompt Engine (`DasikPromptRegistry`, `ClientPromptManager`, `DasikLibraryClientFabric`, `DasikLibraryClientNeoForge`)**: Added server-safe `DasikPromptRegistry` API, 8-second (160-tick) world-start welcome greeting with dimension portal suppression, and 3-second/5-minute FIFO milestone prompt queue across Fabric and NeoForge.
- **Client Configuration Persistence (`DasikClientConfig`)**: Added atomic client-side JSON persistence (`config/dasik_library_client.json`) with `showSupportPrompts` and `showWorldStartGreeting` toggles.
- **YACL Opt-Out Toggle (`ClientSupportScreenHelper`, `UniversalConfigScreen`, `DasikSupportHelper`)**: Added `Show Support Prompts in Chat` (`createSupportPromptsOption()`) to YACL configuration categories.

## [1.13.1+26.1.2] - 2026-11-21
### Fixed
- **ConfirmLinkScreen URI Migration for MC 26.3+ (`ClientSupportScreenHelper`)**: Migrated `ClientSupportScreenHelper.openUrl` from the deleted `ConfirmLinkScreen.confirmLinkNow(Screen, String)` overload to `ConfirmLinkScreen.confirmLinkNow(Screen, URI)` to eliminate runtime `NoSuchMethodError` when clicking Ko-fi or Discord support buttons in YACL configuration screens on Minecraft 26.3+ and future releases.
- **URI Syntax & Malformed URL Safety**: Added safe `URI.create(url.trim())` parsing guarded against `IllegalArgumentException` and `NullPointerException`, preventing client crashes on invalid or malformed URL strings.
- **Pillar 5 Bytecode Descriptor Contract**: Enforced automated bytecode verification in `ClientSupportScreenHelperTest` asserting `(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V` presence and total elimination of `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V`.

## [1.13.0+26.1.2] - 2026-11-07
### Added
- **Native Multi-Loader Architecture**: Complete 3-module decomposition (`:common`, `:fabric`, `:neoforge`) supporting both Fabric and NeoForge loaders on Minecraft 26.1.2 ("Tiny Takeover").
- **Java SPI Platform Abstraction**: Implemented `IPlatformHelper` service interface with `FabricPlatformHelper` and `NeoForgePlatformHelper` for runtime loader queries (`isModLoaded`, `isClient`, `getConfigDir`, `getModVersion`).
- **NeoForge Compatibility**: Added `@Mod("dasik_library")` entrypoint `DasikLibraryNeoForge`, `ClientPlayerNetworkEvent.LoggingIn` subscriber, and `META-INF/neoforge.mods.toml` declaring dependencies for `neoforge` (`[26.1,)`) and `minecraft` (`[26.1.2, 26.2)`).
- **Duck Interface Genetics**: Integrated `GeneticsHolder` with `LivingEntityGeneticsMixin` using `ValueInput`/`ValueOutput` for loader-agnostic NBT save/load persistence.
- **5-Pillar Verification Harness**: 88 unit tests passing 100% green on `:common:test` covering chaos property fuzzing, concurrency load, Brigadier AST commands, and headless simulation.
