<p align="center">
<img src="https://raw.githubusercontent.com/Rifaditya/MC-DasikLibrary/master/Images/Dasik%20page%20banner.jpg" alt="Dasik Library Banner" width="85%">
</p>
<p align="center">
<a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
<a href="https://discord.gg/EV99bgAFqb"><img src="https://img.shields.io/badge/Discord-Join_Community-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="Join Discord"></a>
<img src="https://img.shields.io/badge/Architecture-Universal_Core_Library-blueviolet?style=for-the-badge" alt="Universal Core Library">
<img src="https://img.shields.io/badge/Environment-Client_&_Server-success?style=for-the-badge" alt="Client & Server">
<img src="https://img.shields.io/badge/Language-Java_25-orange?style=for-the-badge&logo=java" alt="Java 25">
<img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
<img src="https://img.shields.io/badge/Minecraft-26.1.2+-brightgreen?style=for-the-badge" alt="Minecraft 26.1.2+">
</p>

## 🧠 Dasik Library

> **"The universal Hive Mind foundation powering autonomous animal genetics, dynamic GameRules, and high-performance flocking across Minecraft."**

---

## 📖 Introduction

Developing modern Minecraft Fabric mods often means facing recurring architectural roadblocks: registering dynamic GameRules on the fly without causing immutable registry lockouts, synchronizing animal genetics and scale attributes across world save cycles, orchestrating swarming and flocking AI without decimating server tick rates, and integrating optional client configuration GUIs without causing fatal `ClassNotFoundException` crashes on headless dedicated servers.

**Dasik Library** is the universal standalone foundation engineered to solve these challenges. It provides the high-performance architectural backbone powering every mod across the **Vanilla Outsider**, **Delayed Gratification**, and **Instant Gratification** collections. Designed with strict zero-allocation hot paths, Knot ClassLoader isolation, and modular APIs, it equips developers and players with an enterprise-grade framework for modern Minecraft.

> **This is a Standalone Universal Core Library / API.**
> * **Universal Single-JAR Standard:** Dasik Library ships a single universal cross-version JAR (`>=26.1.2-`) using pure SemVer. It seamlessly loads across **Minecraft 26.1.2, 26.2, 26.3**, and future snapshots.
> * **Both Client & Dedicated Server Ready:** Fully compatible with singleplayer clients and multiplayer dedicated servers.
> * **Zero Standalone Gameplay Bloat:** Does not add arbitrary gameplay items or world generation on its own. It runs quietly in the background, providing shared AI, genetics, and registry infrastructure for dependent mods.

---

## ✨ Features

### 🧬 Autonomous Animal Genetics & Breeding API
- **Persistent Entity Genetics:** Attaches an entity-agnostic genetic data model to living entities via persistent Fabric attachments (`dasik-library:genetics`), preserving lineage, UUIDs, and generational traits across world saves.
- **Triangular Mutation & Outcross Recovery:** Implements natural biological breeding math, including triangular mutation distribution, parent trait averaging, multi-tier inbreeding penalties, and genetic outcross recovery.
- **Size-Stats Builder & Linked Attributes:** Easily bind entity physical scale (`minecraft:scale`, customizable from `0.1f` to `3.0f`) to secondary attributes (Max Health, Movement Speed, Attack Damage) via `LinkedAttributeRegistry` with linear, inverse, or quadratic scaling.
- **Genetics-Driven Loot Modification:** Uses `GeneticsLootRegistry` and loot hooks to dynamically scale drops, meat yields, and resources based on entity size and genetic vitality.

### ⚙️ Dynamic GameRule Registration & Auto-Translation Engine
- **On-Demand Registry Unfreezing:** Safely unfreezes `BuiltInRegistries.GAME_RULE` at runtime via `MappedRegistryAccessor`, allowing consumer mods and addons to register custom GameRules without startup race conditions or registry lockouts.
- **Automatic Translation Injection:** Dynamically generates human-readable translation keys and injects them into vanilla's `Language` map, eliminating missing string placeholders.
- **World Save Persistence:** Seamlessly intercepts `SavedData` serialization (`GameRuleMapMixin`), ensuring dynamic GameRule changes persist reliably in `level.dat` and `game_rules.dat`.
- **Automated Orphaned Rule Pruning:** Hooks `ServerLifecycleEvents.SERVER_STARTING` to detect and cleanly purge orphaned dynamic GameRules left behind when mods are uninstalled.
- **Math Conversion Helpers:** Built-in calculation utilities for percentages, permilles, and deciles (`getPct`, `getChance`, `getProb`, `getDecileFloat`).

### 🦅 High-Performance Swarm & Flocking AI (Cached Boids)
- **O(N) Leader-Follower Pattern:** Eliminates expensive O(N²) pairwise distance checks by computing and caching group metrics (`FlockState`) on the pack Leader, enabling smooth swarms of 100+ entities at a solid 20 TPS.
- **Biomechanical Steering:** Full Alignment, Cohesion, and Separation vector steering for realistic aerial murmurations (`AerialFlockingStrategy`) and terrestrial pack movement (`TerrestrialFlockingStrategy`).
- **Resilient Following:** Integrated water navigation penalty bypass and catch-up teleportation thresholds so pack members never get lost across chunk boundaries.

### 📡 The Hive Mind & Decoupled Signal Bus
- **Decoupled Social Signals:** High-throughput `SignalBus` enables mobs to emit and receive alerts, warnings, and mood updates across species without hard-coded class couplings.
- **Scope Isolation:** Targeted signal delivery (`Scope.SAME_SPECIES`, `Scope.HERD`, `Scope.GLOBAL`) with safe instance checks.
- **Lazy Freeze Architecture:** Solves mod initialization race conditions by locking the registry safely at server tick.

### 🖥️ Optional GUI Integration & Server ClassLoader Safety
- **Classloader-Isolated Config Screen Resolver:** `GuiHelper` dynamically discovers and invokes YetAnotherConfigLib (YACL) and Cloth Config via reflection and Knot ClassLoader isolation.
- **100% Dedicated Server Crash Immunity:** Completely prevents client GUI classes from loading on dedicated servers.
- **Standardized Creator Support:** Provides `DasikSupportHelper` to embed standardized creator support buttons across config screen category pages.

### 🛡️ Enterprise Stability & Zero-Allocation Hot Paths
- **Atomic File Persistence:** `ConfigHelper` manages JSON read/write using temporary file atomic swaps and automatic backup generation.
- **Zero-Allocation Rendering:** Zero-byte heap allocation architecture across high-frequency tick and render paths.

---

## 📊 Quick Reference & Mechanics Matrix

| Feature Dimension | Vanilla / Standard Mod | Dasik Library |
| :--- | :---: | :---: |
| **GameRule Registration** | Static build-time only | **Dynamic runtime unfreeze + auto-translation** |
| **Save File Persistence** | Only static vanilla rules | **Dynamic `SavedData` in `game_rules.dat`** |
| **Orphaned Rule Handling** | Corrupts saves or persists forever | **Automated startup pruning** |
| **Flocking AI Complexity** | O(N²) pairwise distance loops | **O(N) Leader-Cached `FlockState`** |
| **Animal Genetics Engine** | Flat vanilla breed inheritance | **Lineage tracking, mutations, scale linking & loot** |
| **Scale Attribute Range** | Rigid vanilla scaling | **Custom clamp `[0.1f, 3.0f]` with linked stats** |
| **Optional Config GUI** | Prone to server `NoClassDefFoundError` | **Reflection & Knot isolation (`GuiHelper`)** |

---

## 🚀 Developer Quick Start

### 1. Gradle Setup

```groovy
repositories {
    maven {
        name = "Modrinth"
        url = "https://api.modrinth.com/maven"
    }
}
dependencies {
    modImplementation "maven.modrinth:dasik-library:<latest-version>"
}
```

### 2. Registering a Dynamic GameRule

```java
import net.dasik.social.api.gamerule.DynamicGameRuleManager;

DynamicGameRuleManager.registerIntegerRule(
    "my_mod:drop_multiplier", 2, "Drop Multiplier", "Gameplay"
);
```

### 3. Binding Animal Scale to Health and Speed

```java
import net.dasik.social.api.genetics.SizeStatsBuilder;
import net.dasik.social.api.genetics.LinkMode;

SizeStatsBuilder.create("my_mod:beast_genetics")
    .linkAttribute(Attributes.MAX_HEALTH, LinkMode.PROPORTIONAL, 2.0f)
    .linkAttribute(Attributes.MOVEMENT_SPEED, LinkMode.INVERSE, 0.5f)
    .register();
```

### 4. Declaring the Dependency in fabric.mod.json

```json
"depends": {
  "fabricloader": ">=0.16.10",
  "minecraft": ">=26.1.2-",
  "fabric-api": "*",
  "dasik-library": ">=1.9.0"
}
```

---

## 🧩 Powered Projects

* 📜 [**Collapsible Game Rules**](https://www.curseforge.com/minecraft/mc-mods/collapsible-gamerules): Modernizes the Edit Game Rules screen into collapsible categories.
* 🐕 [**Better Dogs**](https://www.curseforge.com/minecraft/mc-mods/better-dogs): Complete canine behavioral overhaul powered by Dasik Library's AI flocking engine.
* 🌿 [**Natural Reproduction**](https://www.curseforge.com/minecraft/mc-mods/vo-natural-reproduction): 27-species wildlife breeding engine built on Dasik Animal Genetics.

> 🌟 *Explore my profile for more vanilla enhancements and game design overhauls.*

---

### 💬 Join My Discord

Looking for help, want to test early beta builds, or vote on upcoming features? Join my official Discord!

<p align="center">
<a href="https://discord.gg/EV99bgAFqb">
<img src="https://img.shields.io/badge/💬_Discord-Join_Community-5865F2?style=for-the-badge&logo=discord&logoColor=white" alt="Join Official Discord">
</a>
</p>

---

## ☕ Support

<p align="center">
<a href="https://ko-fi.com/dasikigaijin/tip"><img src="https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Ko-fi"></a>
<a href="https://sociabuzz.com/dasikigaijin/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge" alt="SocioBuzz"></a>
<a href="https://saweria.co/DasikIgaijinn"><img src="https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge" alt="Saweria"></a>
</p>

> **🇮🇩 Indonesian Users:** SocioBuzz and Saweria support local payment methods (Gopay, OVO, Dana, etc.)!

---

## 📜 Credits & Modpack Permissions

| Role / Property | Author / Link |
| :--- | :--- |
| **Creator / Author** | **Dasik** (Rifaditya) |
| **Community** | [Official Discord](https://discord.gg/EV99bgAFqb) |
| **Collection** | Universal Core Architecture |
| **License** | [GNU General Public License v3.0 (GPLv3)](https://www.gnu.org/licenses/gpl-3.0.html) |
| **Source Code** | [GitHub - Rifaditya/MC-DasikLibrary](https://github.com/Rifaditya/MC-DasikLibrary) |
| **Issue Tracker** | [GitHub Issues](https://github.com/Rifaditya/MC-DasikLibrary/issues) |
| **Wiki** | [GitHub Wiki](https://github.com/Rifaditya/MC-DasikLibrary/wiki) |

> **📦 Modpack Permissions:** You are fully welcome to include this mod in any modpack! The file must be downloaded through official channels (Modrinth or CurseForge). Re-uploading to mirrors is strictly prohibited.

---

<div align="center">

**Made with ❤️ for the Minecraft community**

*Part of the Universal Core Architecture*

</div>
