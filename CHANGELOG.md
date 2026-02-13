# Changelog

## [1.6.3] - 2026-02-10

### Changed

- **Social Hooks API**: Added `onSocialGoalStart()` / `onSocialGoalStop()` default methods to `UniversalAgeable`. Consumer mods override these to handle entity-specific behavior (e.g., waking resting Bats).
- **Decoupled Consumer Types**: Removed hardcoded `instanceof Bat` / `setResting(false)` from `UniversalBreedGoal` and `UniversalTemptGoal` — library no longer references consumer types.

### Fixed

- **Redundant Scaling**: Removed `dasik$scaleAge` injection that duplicated the `Attributes.SCALE` modifier logic.
- **Cleanup**: Removed unused `Predicate` import from `UniversalBreedingRegistry`, orphaned `CallbackInfoReturnable` import from `UniversalAgeableMixin`.

## [1.5.2] - 2026-02-06

### Fixed

- **AI Suppression**: Implemented missing `movementSuppressed` flag in `UniversalAgeableMixin`.
- **UniversalBreedGoal**: Added movement suppression support and Bat resting state override.

## [1.5.1] - 2026-02-06

### Fixed

- **Snapshot 6 Crash Fix**: Added defensive check for `minecraft:tempt_range` attribute in `UniversalTemptGoal`, preventing `IllegalArgumentException` on Bats and other mobs missing the attribute.

## [1.5.0] - 2026-02-06

### Added

- `UniversalTemptGoal`: Allows any `UniversalAgeable` mob to follow players holding breeding items.
- **Ambient AI Suppression**: Added `AmbientAiMixin` to suppress legacy AI (e.g., Bat wandering) when a mob is participating in social interactions (following player, breeding).
- Support for dynamic movement suppression via `UniversalAgeable` interface.
- Support for non-pathfinder mobs in temptation goals via manual `MoveControl` overrides.

## [1.4.5] - 2026-02-04

### Fixed

- **Cleanup**: Resolved internal lint issues (unused imports and static access warnings) in `UniversalAgeableMixin` and `UniversalMobMixin`.
- **Stabilization**: Verified final interaction signatures for Snapshot 6.

## [1.4.4] - 2026-02-06

### Fixed

- **Snapshot 6 Compatibility**: Split `UniversalAgeableMixin` logic. Scaling/State remains on `LivingEntity`, while Interactions moved to new `UniversalMobMixin` targeting `Mob` with the updated Snapshot 6 signature: `interact(Player, InteractionHand, Vec3)`.

## [1.4.3] - 2026-02-05

### Fixed

- **Snapshot 6 Compatibility**: Migrated `UniversalAgeableMixin` target from `Mob` to `LivingEntity` to correctly hook inherited `getAgeScale` method (per Mixin inheritance rules).

## [1.4.2] - 2026-02-05

### Fixed

- **Snapshot 6 Compatibility**: Migrated from `getDimensions` (now `final` in `LivingEntity`) to `getAgeScale` for universal scaling. Resolves `InvalidInjectionException` on startup.

## [1.4.1] - 2026-02-05

### Fixed

- **Snapshot 6 Compatibility**: Updated `UniversalAgeableMixin` persistence signatures to use `ValueOutput` and `ValueInput`.
- **Core Fix**: Registered `UniversalAgeableMixin` in `fabric.mod.json` (was previously inactive).

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
