# Release History

## v1.6.4 - 2026-02-13

- **API**: Introduced `StandardAerialNavigation` for robust flying AI. Prevents "ground walking" by penalizing non-air nodes.

## v1.6.3 - 2026-02-130

- **Social Hooks API**: Added `onSocialGoalStart()` / `onSocialGoalStop()` default methods to `UniversalAgeable`.
- **Decoupled Consumer Types**: Removed hardcoded `instanceof Bat` / `setResting(false)` from `UniversalBreedGoal` and `UniversalTemptGoal`.
- **Cleanup**: Removed redundant `dasik$scaleAge` injection, unused `Predicate` import, orphaned `CallbackInfoReturnable` import.

## v1.5.2

- Fixed AI suppression logic in `UniversalAgeableMixin`.
- Enhanced `UniversalBreedGoal` with movement suppression and Bat resting state support.

## [1.5.1] - 2026-02-06

### Fixed

- **Snapshot 6 Crash Fix**: Added defensive check for `minecraft:tempt_range` attribute in `UniversalTemptGoal`. Prevents `IllegalArgumentException` on Bats and other mobs missing the attribute.

## [1.5.0] - 2026-02-06

### Added

- `UniversalTemptGoal`: Allows any `UniversalAgeable` mob to follow players holding breeding items.
- **Ambient AI Suppression**: Added `AmbientAiMixin` to suppress legacy AI (e.g., Bat wandering) when a mob is participating in social interactions (following player, breeding).
- Support for dynamic movement suppression via `UniversalAgeable` interface.
- Support for non-pathfinder mobs in temptation goals via manual `MoveControl` overrides.

## [1.4.5] - 2026-02-04

### Fixed

- **Snapshot 6 Alignment**: Completed the stabilization phase for Snapshot 6.
  - Split `UniversalAgeableMixin` into Scaling (LivingEntity) and Interaction (Mob) modules.
  - Updated `interact` signature to include `Vec3`.
  - Targeted inherited methods correctly by shifting to defining classes.
- **Persistence**: Aligned `ValueOutput`/`ValueInput` signatures with Snapshot 6 standards.
- **Cleanup**: Resolved unused imports and static access warnings.

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

## [1.1.0] - 2026-02-02

### Added

- **Universal Breeding API**:
  - `UniversalAgeable` interface/mixin allows any Mob to have baby states and breeding logic.
  - `UniversalBreedingRegistry` allows defining breeding items/cooldowns for any entity.
  - `UniversalBreedGoal` provides AI for finding partners and breeding.
  - `UniversalRendererMixin` handles visual scaling for universal babies.

## [1.0.2] - 2026-02-01

- Initial Release
