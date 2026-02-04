# Changelog

## [1.4.0] - 2026-02-04

### Added

- **Size Variants API**: New `SizeRegistry` and event system allowing deterministic, UUID-based size variations for entities.
- **Dynamic Scaling**: `IScaleProvider` interface allows external mods to define custom scaling logic without base class modification.

## [1.3.0] - 2026-02-04

### Added

- **Flocking API**: Added `GroupMember`, `FlockingStrategy`, `GroupManager`, and `FollowLeaderGoal`.
- **Extensibility**: Core Flocking System allows custom movement strategies (Aerial, Terrestrial, etc.) without library modification.

### Changed

- **API Break**: Migrated Leader-Follower logic from Bat Ecology to Core.

## [1.2.4] - 2026-02-04

### Assets

- **Icon**: Updated `icon.png` with refreshed 512x512 visual.

## [1.2.3] - 2026-02-04

### Fixed

- **Assets**: Moved mod icon to root directory to resolve missing icon issue.

## [1.2.2] - 2026-02-04

### Assets

- **Icon**: Refreshed mod icon with latest 512x512 asset.

## [1.2.1] - 2026-02-03

### Assets

- **Branding**:
  - Updated mod icon to new 512x512 resolution.
  - Added vector version of the Wizaeds O logo (`wizaeds_O.svg`).
  - Added new SVG text banner `dasik_library_text.svg`.

## [1.2.0] - 2026-02-03

### Added

- **Universal Aging System**: `UniversalAgeable` entities now have a properly ticked `age` field.
- **Growth Acceleration**: Feeding babies valid breeding items now speeds up growth by 10% of remaining time.
- **Registry Integration**: Breeding interaction now correctly checks `UniversalBreedingRegistry` for food items.
- **Spawn Egg Fix**: Using spawn eggs on adult Universal entities now correctly spawns babies.

---
