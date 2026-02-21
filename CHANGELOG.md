# Changelog

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
