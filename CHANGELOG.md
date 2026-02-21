# Changelog

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

## [1.6.9+build.3] - 2026-02-19

### Added

- **TimeUtil**: Shared Minecraft time arithmetic.
- **Throttling API**: Added `GlobalSocialSystem.setThrottle` to allow external regulation of performance during intensive operations (e.g., Time Warping).

### Fixed

- Updated internal logic to support variable pulse intervals.

## [1.6.9+build.2] - 2026-02-19

### Added

- **StochasticUtil**: Added `getAmplifiedProbability` for probability-based scaling (used for Rarity Filter).

## [1.6.9+build.1] - 2026-02-19

### Added

- **StochasticUtil**: Added `getAmplifiedCount` (extracted from Ore Amplifier).

### Changed

- **Versioning**: Adopted Build Number policy (`+build.N`).

## [1.6.9] - 2026-02-15

### Removed (BREAKING)

- **Breeding System**: Deleted `UniversalAgeable`, `UniversalBreedGoal`, `UniversalBreedingRegistry`, `UniversalTemptGoal`.
- **Aerial Navigation**: Deleted `StandardAerialNavigation`, `UniversalRandomPos`.
- **Flocking System**: Deleted `FollowLeaderGoal`, `GroupMember`, `GroupParameters`, `FlockingStrategy`, `Strategies`, `GroupManager`.
- **Size System**: Deleted `IScaleProvider`, `ScaleRange`, `SizeEvents`, `SizeRegistry`.
- **Mixins**: Deleted `UniversalAgeableMixin`, `AmbientAiMixin`, `UniversalMobMixin`.
- **Mod Init**: Removed `SizeEvents` entity load registration.

## [1.6.8] - 2026-02-14

### Fixed

- Fixed `UniversalTemptGoal` for flying mobs (targets eye level instead of feet).

## [1.6.6] - 2026-02-13

### Changed

- **StandardAerialNavigation**: Increased ground pathfinding penalty from `4.0F` to `64.0F` to strongly discourage walking.

## [1.6.5] - 2026-02-13

### Added

- **UniversalRandomPos**: Utility for finding random positions for any `Mob` (not just `PathfinderMob`).

## [1.6.4] - 2026-02-13

### Added

- **StandardAerialNavigation**: A reusable `FlyingPathNavigation` implementation that penalizes ground usage.

## [1.6.3] - 2026-02-13

### Changed

- **UniversalAgeable**: Added `onSocialGoalStart` and `onSocialGoalStop` hooks.
- **Goals**: Refactored `UniversalBreedGoal` and `UniversalTemptGoal` to use new hooks instead of casting to `Bat`.
- **Cleanup**: Removed redundant mixin injections.

## [1.6.2] - 2026-02-04

### Added

- **Size Registry**: Added `DasikLibraryConfig` and `SizeRegistry` for defining entity scale limits.
- **Commands**: Added `/dasik reload` command.

## [1.6.1] - 2026-02-04

### Fixed

- **Flocking**: Fixed crash when leader is null in `FlockingManager`.

## [1.6.0] - 2026-02-04

### Added

- **Flocking System**: Added `FlockingManager` and `FollowLeaderGoal`.

## [1.5.2] - 2026-02-04

### Changed

- **Icon**: Updated mod icon to 512x512.

## [1.5.1] - 2026-02-03

### Fixed

- **Docs**: Updated documentation links.

## [1.5.0] - 2026-02-03

### Added

- **Universal Aging**: Added `UniversalAgeable` interface and mixins.
