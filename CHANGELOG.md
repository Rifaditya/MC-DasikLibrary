# Changelog

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
