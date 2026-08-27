# Technical History: DasikLibrary

## Version 1.8.29 (2026-08-27)
- **Mixin Member Naming Standard (`LanguageMixin`)**: Prefixed injected method with `dasik$` (`dasik$injectDynamicGameRuleTranslations`) to ensure 100% compliance with Mixin naming standards.
- **Orphaned Mixin Removal (`PathfinderMobMixin`)**: Safely removed deprecated, unregistered experimental entity mixin file from source tree to guarantee 1:1 parity with `dasik-library.mixins.json`.
- **License Header Standardization**: Injected standard simplified GPLv3 headers across 15 internal and API source files.
- **Build Script Hygiene**: Cleaned obsolete `loom.mixin` configuration block in `build.gradle` for Loom 1.15+ compatibility.

## Version 1.8.28 (2026-08-23)
- **Dynamic GameRule World Save Persistence (`GameRuleMapMixin`)**: Added mixin to vanilla `GameRuleMap` (`SavedData`) to dynamically accept registered dynamic GameRules on `has()`, `get()`, and `set()`.
- **Pre-Command Tree Registry Hook (`CommandRegistrationCallback`)**: Integrated `CommandRegistrationCallback.EVENT` into `DynamicRegistryScanner.subscribe(...)`.

## Version 1.8.27 (2026-08-23)
- **Universal Dynamic Registry Scanner (`DynamicRegistryScanner`)**: Introduced `DynamicRegistryScanner.subscribe(...)` with 3-tier discovery pipeline.
- **On-Demand Dynamic GameRule Unfreeze**: Updated `DynamicGameRuleManager.register()` with `MappedRegistryAccessor`.
- **Pure On-Demand Fast-Math Vision Engine**: Refactored `PlayerVisionTracker` to a 100% on-demand static utility with zero idle background CPU overhead and a 3-stage fast-fail pipeline (Distance -> Vector Dot-Product FOV -> Single Eye Raycast with contact buffer).

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
