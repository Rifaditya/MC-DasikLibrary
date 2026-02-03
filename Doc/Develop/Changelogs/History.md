# DasikLibrary History

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
