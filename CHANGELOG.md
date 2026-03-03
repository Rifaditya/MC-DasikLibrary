# Changelog

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
