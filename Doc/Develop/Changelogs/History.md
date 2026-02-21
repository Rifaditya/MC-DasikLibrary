# Release History

## v1.6.9+build.8 - 2026-02-21

- **Stability**: Made `DynamicGameRuleManager` freeze-safe. It now checks if the GameRule registry is frozen before attempting registration, returning the existing rule or a fallback if registration is no longer possible. This prevents crashes during late-discovery JIT registration (e.g., during world generation).

## v1.6.9+build.7 - 2026-02-21

- **Compatibility**: Reverted Mixin compatibility level from `JAVA_25` to `JAVA_22` to resolve warning.

## v1.6.9+build.6 - 2026-02-21

- **Documentation**: Updated all READMEs and platform pages to highlight the new "Dynamic Translations" feature making GameRules human-readable.
- **Documentation**: Replaced "Architect" with "Creator" in Platform Page Author roles.

## v1.6.9+build.5 - 2026-02-21

- **DynamicGameRuleManager**: Introduced DynamicGameRuleManager capable of dynamic GameRule generation with cached human-readable string values for localization.
- **LanguageMixin**: Injects auto-generated readable GameRule names from `DynamicGameRuleManager` into Minecraft's native language map so they are properly rendered in the Game Rules configuration screen.

## v1.6.9+build.4 - 2026-02-20

- **API**: Implemented missing Leader-Follower System components (`GroupMember`, `FlockType`, `FlockingStrategy`, `GroupParameters`, `AerialFlockingStrategy`, `TerrestrialFlockingStrategy`, `GroupManager`, `FollowLeaderGoal`) to restore Concept Parity.
- **Fixed/Audit**: Addressed technical debt across Leader System. Removed unused `LivingEntity` import from `FollowLeaderGoal`, replaced deprecated `isSolid()` in `AerialFlockingStrategy`, and resolved `@NonNull` warnings in `TerrestrialFlockingStrategy` and `GroupManager` by adding explicit null assertions.

## v1.6.9 - 2026-02-15

- **Reason**: "i don't feel like it imma remove it and maybe work on it later who know when"
- **Purge**: Completely removed Bat Ecology systems from core (breeding, aerial navigation, flocking, size/scaling, aging mixins). Library now focuses on core social scheduler system only. Full architecture archived in `Doc/Archive/bat_ecology_history.md`.
