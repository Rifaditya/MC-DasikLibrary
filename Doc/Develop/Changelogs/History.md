## [1.8.1] - 2026-06-05
### Summary
The **"Genetics-Based Loot Modification API"** update. Implements a generic API to dynamically customize entity drops based on genetics traits.
- **Genetics-Based Loot Modification API**: Introduced `GeneticsLootModifier` functional interface and `GeneticsLootRegistry` mapping `EntityType<?>` to modifiers.
- **LivingEntityLootMixin**: Intercepts `LivingEntity.dropFromLootTable` using a `@ModifyVariable` hook to wrap the item stack consumer, allowing registered modifiers to replace, scale, or remove drops dynamically based on entity genetics.

## [1.8.0] - 2026-06-04
### Summary
The **"Universal Genetics & Breeding API"** update. Abstracts and centralizes genetics, selective breeding, inbreeding penalties, and outcross recovery from Better Dogs into a reusable library package.
- **Genetics & Breeding API**: Introduced a generic, entity-agnostic, and attribute-agnostic genetics registry and calculation engine (`net.dasik.social.api.genetics`).
- **EntityGenetics Attachment**: Registered standard persistent Fabric Attachment `dasik-library:genetics` to track parent UUIDs, inbreeding state, and dynamic trait modifiers across world save cycles.
- **Inbreeding Verification & Outcross Recovery**: Added standard calculation formulas for triangular mutations, average inheritance, inbreeding penalties, and genetic outcross recovery.

## [1.7.4] - 2026-05-26
### Summary
The **"Config Infrastructure Standardization"** update. Centralized configuration reading/writing code for all consumer mods.
- **Config Helper**: Introduced a generic `ConfigHelper` providing safe configuration loading, saving, atomic temporary file swaps, size-limit checks, version checking, and backup generation to be shared across consumer mods.

## [1.7.3] - 2026-05-26
### Summary
The **"Game Rule Helper Integration"** update. Implemented standard utility conversion methods for GameRule value queries.
- **GameRule Helpers**: Added `getPct`, `getChance`, `getProb`, and `getDecileFloat` to `DynamicGameRuleManager` to centralize math conversions (percentages, permilles, deciles) for GameRule values.

## [1.7.2] - 2026-05-26
### Summary
The **"Centralized Dual-Side Game Rule Lookup"** update. Implements support for querying game rules correctly on both client levels (singleplayer integrated server) and server levels (dedicated servers).
- **Client-Side Integrated Server Helper**: Introduced `ClientGameRuleHelper` to fetch game rules from the integrated server thread in singleplayer/local play on the client side.
- **Dedicated Server Classloading Protection**: Added client environment checks (`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`) to lazily load the client-side helper, preventing classloading crashes (such as missing `Minecraft` class) on dedicated servers.
- **Dual-Side Game Rule Lookups**: Refactored `DynamicGameRuleManager.getInt` and `DynamicGameRuleManager.getBoolean` to automatically detect environment type, cast `Level` to `ServerLevel` on logical servers, and fallback to `ClientGameRuleHelper` on client environments.

## [1.7.1] - 2026-05-16
### Summary
The **"Stability & Parity"** update. Implemented legacy compatibility measures and finalized the 26.1.2 terrestrial steering suite.
- **Legacy Compatibility Shim**: Added a secondary constructor to `GroupParameters` to restore binary compatibility for mods compiled against older library versions (3-float signature).
- **API Hardening**: Finalized the 10-parameter record structure for advanced terrestrial steering including weights for Cohesion, Separation, and Alignment.

## [1.6.9+build.23] - 2026-04-22
- **Leader Teleportation**: `FollowLeaderGoal` and Flocking Strategies now support teleportation when followers fall too far behind (default: 144 blocks sq), matching vanilla wolf parity.
- **Water Navigation**: Terrestrial group members now temporarily ignore water pathfinding penalties when following a leader across bodies of water.

## [1.6.9+build.16] - 2026-04-15
- **Cached Boids Pattern**: Implemented `FlockState` to shift from $O(N^2)$ sibling polling to $O(N)$ aggregated state computation, drastically reducing server overhead for large swarms.
- **Biomechanical Refactor**: Rewrote `AerialFlockingStrategy` with proper Alignment, Cohesion, and Separation steering.
- **Movement Smoothing**: Replaced terrestrial `navigation.stop()` with Distance-Based Linear Interpolation (Lerp) for jitter-free ground following.
- **Snapshot 26.1 Alignment**: Integrated native support for `Attributes.WAYPOINT_TRANSMIT_RANGE` and `Attributes.WAYPOINT_RECEIVE_RANGE`.
- **Optimization**: Implemented Tick Staggering in `FollowLeaderGoal` to distribute AI load evenly across the tick loop.

## [1.6.9+build.15] - 2026-04-13

### Changed
- **Migration**: Updated workspace to target Minecraft version `26.1.2` ("Tiny Takeover" Release).


# Release History

## v1.6.9+build.13 - 2026-03-04

- **Source Recovery**: Successfully restored the full feature set of `build.10` via high-fidelity decompilation. This process was necessary because local `build.10` source changes had not been synchronized with the Git repository during a transition, necessitating a recovery from the compiled JAR.
- **API Adaptation**: Resolved critical type-safety issues in `DynamicGameRuleManager` caused by Mojang API changes in Snapshot 10.

## v1.6.9+build.12 - 2026-03-04

- **Systemic Refactor**: Overhauled `SocialRegistry` for O(1) shard-based performance and fixed critical unregistration memory leaks.
- **Signal Evolution**: Converted `Signal` and `TickContext` into Java 25 `record`s for enhanced performance and immutability.
- **Interface Injection**: Implemented `PathfinderMobMixin` providing robust `SocialEntity`, `GroupMember`, and `ProfileAware` support for vanilla entities.
- **Protocol Compliance**: Injected mandatory Zenith Sovereign citations (`// Verified against: ...`) across all core API and implementation files.

## v1.6.9+build.11 - 2026-03-03

- **Project Rebuild**: Successfully rebuilt the entire project from decompiled sources with 1:1 logic parity.
- **Zenith Protocol**: Integrated full Zenith compliance, including `withSourcesJar()` and proper artifact archiving.
- **Documentation**: Synchronized all conceptual and technical documentation.

## v1.6.9+build.9 - 2026-02-21

- **Fix**: Translation key mismatch — generated keys now use `Util.makeDescriptionId()` for correct `gamerule.minecraft.<path>` format. Cache-hit path also generates translations.

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
