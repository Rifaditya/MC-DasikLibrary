# Technical History: DasikLibrary

## Version 1.8.9 (2026-08-01)
- **Minecraft 26.1.2+ Compatibility Restoration**: Adjusted `fabric.mod.json` bounds (`">=26.1.2-"`) and updated `ModVersionGuard` check target (`EntityType`) to guarantee seamless operation on Minecraft 26.1.2 as well as 26.2+.

## Version 1.8.8 (2026-07-31)
- **Dynamic Trait Modifiers & Stat Reset API**: Added `setTrait`, `modifyTrait`, and `resetGenetics` to `DasikAnimalGeneticsAPI` for dynamic stat modification and attribute resets.

## Version 1.8.7 (2026-07-31)
- **Kinship & Pedigree API Expansion**: Added `isRelated`, `isParentOf`, `areSiblings`, and `predictInbreedingRiskPercent` to `DasikAnimalGeneticsAPI` for family tree tracking and inbreeding risk prediction.

## Version 1.8.6 (2026-07-31)
- **Universal DasikAnimalGeneticsAPI Facade**: Introduced high-level, entity-agnostic facade (`net.dasik.social.api.genetics.DasikAnimalGeneticsAPI`) providing standardized genetics getters/setters (`hasGenetics`, `getTrait`, `getHealthBonus`, `getDamageMod`, `getSpeedMod`, `isInbred`, `inherit`, `rollStats`).
- **Size-Stats & Scale API Expansion**: Added `getScale`, `setScale`, `isRunt` (`scale < 0.85f`), and `isGiant` (`scale > 1.15f`) to `DasikAnimalGeneticsAPI` with NaN/Infinity protection and `[0.5f, 2.0f]` clamping.

## Version 1.8.5 (2026-07-22)
- **ModVersionGuard Patch**: Fixed ClassLoader lookup using Knot's context/current ClassLoader.

## Version 1.8.3 (2026-07-20)
- **Minecraft 26.2 Target**: Aligned with Minecraft 26.2.
