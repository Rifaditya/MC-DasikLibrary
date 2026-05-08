# Changelog

## [1.6.9+build.24] - 2026-05-08

### Added
- **Enchantment API**: Added `DynamicEnchantmentManager` to handle GameRule-based enchantment capping in Mixins.
- **Projectile API**: Added `ProjectileEffectHelper` to encapsulate threshold-based high-velocity effects (Sonic Juice).

## [1.6.9+build.23] - 2026-04-22

### Added
- **Leader Teleportation**: `FollowLeaderGoal` and Flocking Strategies now support teleportation when followers fall too far behind (default: 144 blocks sq), matching vanilla wolf parity.
- **Water Navigation**: Terrestrial group members now temporarily ignore water pathfinding penalties when following a leader across bodies of water.

## [1.6.9+build.22] - 2026-04-16

### Changed
- **Dynamic AI Parameters**: Removed `final` modifier from `FollowLeaderGoal.parameters`, enabling runtime AI behavior shifts without goal reconstruction.
- **New API**: Added `FollowLeaderGoal.setParameters(GroupParameters)` — allows subclasses and consumer mods to update flocking parameters live (e.g., from a GameRule change).

## [1.6.9+build.21] - 2026-04-16

### Added
- **FlockState API Expansion**: Added `memberCount` field and accessors to `FlockState`.
- **Infrastructure**: `GroupManager.computeFlockState()` now automatically calculates and caches the total member count of a pack, allowing mods to read the size without performing $O(N)$ entity searches.

## [1.6.9+build.20] - 2026-04-15

### Changed
- **Infrastructure**: Upgraded Fabric Loader to `0.19.1` to provide native support for **Java 25** Mixins.
- **Sovereign Compliance**: Restored `compatibilityLevel: "JAVA_25"` in `dasik-library.mixins.json`, removing all Knot/Fabric subsystem warnings.
- **Dependencies**: Synchronized with Fabric API `0.145.4+26.1.2`.

## [1.6.9+build.16] - 2026-04-15

### Added
- **Cached Boids Pattern**: Implemented `FlockState` and `computeFlockState` to shift from $O(N^2)$ calculations to efficient $O(N)$ aggregated state computation.
- **Biomechanical Steering**: Full Alignment, Cohesion, and Separation logic for `AerialFlockingStrategy`.
- **Snapshot 26.1 Attributes**: Native integration with `Attributes.WAYPOINT_TRANSMIT_RANGE` and `Attributes.WAYPOINT_RECEIVE_RANGE`.

### Changed
- **Performance**: Implemented Tick Staggering in `FollowLeaderGoal` to spread AI load over 10 ticks.
- **Movement Smoothing**: Switched `TerrestrialFlockingStrategy` to Distance-Based Linear Interpolation (Lerp) for smoother ground movement.

## [1.6.9+build.15] - 2026-04-13

### Changed
- **Migration**: Updated workspace to target Minecraft version `26.1.2` ("Tiny Takeover" Release).
- **API Break Fix**: Remapped `DynamicGameRuleManager` gamerule retrievals. `Level` and `ClientLevel` no longer natively carry `getGameRules()`. Wrapped getters downstream to correctly filter and cast into `ServerLevel` bound logic.

## [1.6.9+build.14] - 2026-03-04

### Added
- **DynamicGameRuleManager UI Tooltips**: Refactored `DynamicGameRuleManager` to use a Builder pattern, allowing mods to supply custom localized names and descriptions (tooltips) for their gamerules.

### Changed
- **API**: Deprecated old static functional methods `registerInteger` and `registerBoolean` in favor of the new parameterized Builder pattern, keeping them entirely functional for backward compatibility.

## [1.6.9+build.13] - 2026-03-04

### Added
- **Source Recovery**: Successfully restored the full feature set of `build.10` via high-fidelity decompilation (CFR). This process was necessary because local `build.10` source changes had not been synchronized with the Git repository during a transition, necessitating a recovery from the compiled JAR.
- **Snapshot Stability**: Final architectural validation for Minecraft Snapshot 10 (26.1 Snapshot 10).

### Fixed
- **Mojang API Adaptation**: Corrected `DynamicGameRuleManager` type-safety and registry access issues following Snapshot 10's API modifications.

## [1.6.9+build.12] - 2026-03-04

### Added
- **Systemic Refactor**: Overhauled `SocialRegistry` for O(1) shard-based performance and fixed critical unregistration memory leaks.
- **Signal Evolution**: Converted `Signal` and `TickContext` into Java 25 `record`s for enhanced performance and immutability.
- **Interface Injection**: Implemented `PathfinderMobMixin` providing robust `SocialEntity`, `GroupMember`, and `ProfileAware` support for vanilla entities.
- **Protocol Compliance**: Injected mandatory Zenith Sovereign citations (`// Verified against: ...`) across all core API and implementation files.

### Changed
- **Version Bump**: Incrementing to build.12 following systemic core overhaul.

## [1.6.9+build.11] - 2026-03-03

### Added
- **Project Rebuild**: Successfully rebuilt the entire project from decompiled sources with 1:1 logic parity.
- **Zenith Protocol**: Integrated full Zenith compliance, including `withSourcesJar()` and proper artifact archiving.
- **Documentation**: Synchronized all conceptual and technical documentation with the official repository.

### Fixed
- **Decompilation Artifacts**: Resolved all type inference issues, generic mismatches, and lambda errors in the rebuilt source code.
- **Stability**: Fixed a critical `ClassCastException` risk in `GroupManager` and refined `EntitySocialScheduler` inner class logic.


## [1.6.9+build.9] - 2026-02-21

### Fixed

- **DynamicGameRuleManager**: Fixed translation key mismatch — generated keys now use `Util.makeDescriptionId()` to produce `gamerule.minecraft.<path>` format matching what Minecraft looks up. Previously generated `gamerule.<ruleName>` which never matched.
- **DynamicGameRuleManager**: Cache-hit path (rule already in registry) now also generates translations via `putIfAbsent`.

## [1.6.9+build.8] - 2026-02-21

### Fixed

- **Stability**: Made `DynamicGameRuleManager` freeze-safe. It now checks if the GameRule registry is frozen before attempting registration, returning the existing rule or a fallback if registration is no longer possible. This prevents crashes during late-discovery JIT registration (e.g., during world generation).

## [1.6.9+build.7] - 2026-02-21

### Fixed

- **Compatibility**: Reverted Mixin compatibility level from `JAVA_25` to `JAVA_22` to resolve warning.

## [1.6.9+build.6] - 2026-02-21

### Added

- **Documentation**: Updated all READMEs and platform pages to highlight the new "Dynamic Translations" feature making GameRules human-readable.

### Changed

- **Documentation**: Replaced "Architect" with "Creator" in Platform Page Author roles.

## [1.6.9+build.5] - 2026-02-21

### Added

- **DynamicGameRuleManager**: Introduced DynamicGameRuleManager capable of dynamic GameRule generation with cached human-readable string values for localization.
- **LanguageMixin**: Injects auto-generated readable GameRule names from `DynamicGameRuleManager` into Minecraft's native language map so they are properly rendered in the Game Rules configuration screen.

## [1.6.9+build.4] - 2026-02-19

### Added

- **Leader-Follower API**: Introduced modular `GroupMember`, `FlockingStrategy`, and `GroupManager` for generic swarming and flocking (Aerial and Terrestrial).
- **FollowLeaderGoal**: Added vanilla-compatible AI Goal for flocking coordination.
