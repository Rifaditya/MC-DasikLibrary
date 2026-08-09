# Dasik Library Wiki

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

Welcome to the official technical documentation for **Dasik Library**, the shared Hive Mind social AI engine, genetics framework, Boids flocking calculator, and dynamic GameRule infrastructure for Fabric Minecraft mods.

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 💡 Architectural Philosophy

Dasik Library operates under two core architectural principles:

1. **"Thin Mod, Fat Library"**: Modular consumer mods focus strictly on entity content and registration. Complex math, tick scheduling, genetic inheritance, Boids steering vector calculations, and NBT state serialization are centralized within Dasik Library.
2. **"One Brain, Many Minds"**: Centralized pulse execution via `GlobalSocialSystem` enforces the **Highlander Rule** (strictly 1 global tick cycle per game tick) to manage thousands of active entities with low performance impact.

---

## 📦 Minecraft Versions Directory

* [[MC 26.2 Guide|Minecraft-26.2-Guide]] — Installation and configuration for Minecraft 26.2+.
* [[Version Compatibility|Version-Compatibility]] — Multi-version compatibility lifecycle matrix (`>=26.1.2-` to `26.2+`), Knot ClassLoader safety, and version guard rules.

---

## 🎮 Core Systems & Mechanics Matrix

Explore the complete technical mechanics and configuration guides:

* [[Hive Mind Social System|Hive-Mind-Social-System]] — Single-pulse engine, Highlander Rule, $O(1)$ shard-based `SocialRegistry`, and tick budgets.
* [[Social Scheduler & Events|Social-Scheduler-and-Events]] — `EntitySocialScheduler`, dual-track Mood/Ambient execution, `PriorityTier`, and `SocialEventRegistry`.
* [[Animal Genetics Engine|Animal-Genetics-Engine]] — `EntityGenetics` attachment, DNA long encoding, size-stats & scale bounds (`0.1x` - `3.0x`), runt indicators, and inheritance math.
* [[Genetics API & Pedigree|Genetics-API-and-Pedigree]] — `DasikAnimalGeneticsAPI` facade, Kinship & Pedigree family tree calculation, inbreeding risk prediction formulas, and dynamic trait reset/modifiers.
* [[Genetics Loot Modifiers|Genetics-Loot-Modifiers]] — `GeneticsLootModifier`, `GeneticsLootRegistry`, and `LivingEntityLootMixin` drop wrapping logic.
* [[Leader Follower & Flocking|Leader-Follower-and-Flocking]] — `GroupMember`, `FollowLeaderGoal`, Aerial & Terrestrial Boids flocking strategies, and `GroupParameters` weights.
* [[Dynamic GameRules Manager|Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`, dynamic registration, English translation injection, bold category headers (`§l`), and math helpers (`getPct`/`getChance`).
* [[GameRule Codec & Serialization|GameRule-Codec-and-Serialization]] — Integer GameRule bounds validation (`Integer.MIN_VALUE` fallback) preventing `SavedDataStorage.encodeUnchecked` crashes.
* [[Client GameRule & GUI Helpers|Client-GameRule-and-GUI-Helpers]] — `ClientGameRuleHelper` integrated server lookups, `GuiHelper`, `ConfigHelper` atomic JSON swaps, and server crash safety gating.
* [[Dynamic Enchantments & Vision|Dynamic-Enchantments-and-Vision]] — `DynamicEnchantmentManager` runtime injection, and `PlayerVisionTracker` raycasting frustum checks.
* [[Stochastic & Math Utilities|Stochastic-and-Math-Utilities]] — `FastRandom` XORSHIFT algorithms, `StochasticUtil` decile/permille rolls, and `TimeUtil` tick-to-second ($20\text{ ticks} = 1\text{s}$) conversions.
* [[Stale Attribute Purging & Scale|Stale-Attribute-Purging-and-Scale]] — Attribute modifier purging rules, `ADD_VALUE` `-1.0f` scale base offset math, and `genetics_` modifier safety.
* [[ModVersionGuard & Startup Safety|ModVersionGuard-and-Startup-Safety]] — `ModVersionGuard` Knot ClassLoader safety (`Thread.currentThread().getContextClassLoader()`), and pre-release startup crash prevention.

---

## 💻 Developer & Technical Reference

* [[Developer Setup & Building|Developer-Setup-and-Building]] — JDK 25 prerequisites, Gradle 9.3+, `./gradlew build --no-daemon`, and `./gradlew test`.
* [[Architecture & Package Layout|Architecture-and-Package-Layout]] — Complete ASCII package tree (`ai`, `api`, `config`, `core`, `mixin`, `util`) and thread-safety models.
* [[Mixin Reference & Hooks|Mixin-Reference-and-Hooks]] — Detailed Mixin breakdown table (`LanguageMixin`, `LivingEntityLootMixin`, `MobGoalAccessor`, `PathfinderMobMixin`, `ProfileTriggerMixin`).
* [[Behavior Profiles & Conditions|Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`, `BehaviorProfile`, `BehaviorCondition`, `ProfileAware`, and `DefaultProfileBuilder`.
* [[Consumer Mods Integration Guide|Consumer-Mods-Integration-Guide]] — Integration guide and code snippets for consumer mods (*Better Dogs*, *Natural Reproduction*, *Collapsible Game Rule Screen*, *Bat Ecology*, *Ore Amplifier*).

---

## 🔗 External Links

* [[GitHub Repository|Home]]
* [Modrinth Project Page](https://modrinth.com/mod/dasik-library)
* [CurseForge Project Page](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
