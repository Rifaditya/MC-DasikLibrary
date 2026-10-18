# Architecture & Symbol Index: Dasik Library

## 1. Mod Metadata & Entrypoint
- **Mod ID**: `dasik-library`
- **Architecture**: Standalone Universal Library (Pure SemVer `dasik-library-x.y.z.jar`, forward-compatible `>=26.1.2-`)
- **Main Entrypoint**: [`DasikLibraryMod.java`](file:///e:/Minecraft%20Project/MC-DasikLibrary/src/main/java/net/dasik/social/DasikLibraryMod.java) (`net.fabricmc.api.ModInitializer`)

## 2. Bytecode Mixin Target Registry (`dasik-library.mixins.json`)
| Target Vanilla Class | Mixin Class | Primary Hook / Purpose |
| :--- | :--- | :--- |
| `net.minecraft.world.level.gamerules.GameRules` | `GameRuleMapMixin.java` | Dynamic runtime GameRule registration & unfreezing |
| `net.minecraft.locale.Language` | `LanguageMixin.java` | Runtime server/client language map injection for dynamic keys |
| `net.minecraft.world.entity.LivingEntity` | `LivingEntityLootMixin.java` | Hive Mind social AI loot drop interception |
| `net.minecraft.core.MappedRegistry` | `MappedRegistryAccessor.java` | Accessor to unfreeze frozen vanilla registries safely |
| `net.minecraft.world.entity.ai.goal.GoalSelector` | `MobGoalAccessor.java` | Accessor to inspect and prioritize entity AI goal sets |
| `net.minecraft.server.players.PlayerList` | `ProfileTriggerMixin.java` | Supporter profile verification & creator perk hooks |

## 3. Core Engine & Shared Subsystems
- **Dynamic GameRules Engine** (`net.dasik.social.api.gamerule`): Runtime dynamic GameRule registration, unfreezing vanilla registries, and dynamic localization mapping.
- **Universal Brigadier Command Suite** (`net.dasik.social.api.command`): Declarative studio command framework (`ModCommandDispatcher`, `UniversalCommandRegistry`, `GameRuleCommandBridge`).
- **Universal Client Configuration GUI** (`net.dasik.social.api.client.gui`): Zero-boilerplate client configuration screens via `UniversalConfigScreen`, `@ConfigOption`, and optional YACL runtime safety bridging.
- **Social AI & Group Dynamics** (`net.dasik.social.api.group`, `net.dasik.social.core.group`, `net.dasik.social.ai.goal`): Flock management, `FollowLeaderGoal`, threat propagation, and leader-follower topologies.
- **Genetics & Size Stats Engine** (`net.dasik.social.api.genetics`): Multi-trait inheritance, inbreeding risk analysis, saturated bounds, and scale linked attributes.
- **Signal Bus & Spatial Indexing** (`net.dasik.social.signal`, `net.dasik.social.core`): High-speed concurrent spatial signal dispatching, chunk indexing, and weak-reference entity registry (`SocialRegistry`).
- **Creator Support & Social Links** (`net.dasik.social.api.config`, `net.dasik.social.api.client.gui`): Non-intrusive support integration (`DasikSupportHelper`, `ClientSupportScreenHelper`, `SocialLinks`).

## 4. API Governance & Side-Safety
- **Public API**: Located under `net.dasik.social.api.*` and annotated with `@DasikApiStatus.Public`.
- **Internal Core**: Located under `net.dasik.social.core.*` and internal helpers annotated with `@DasikApiStatus.Internal`.
- **Side Safety**: Server-first architecture with strict `@Environment(EnvType.CLIENT)` isolation for UI/screen helpers in `net.dasik.social.api.client.*`.
