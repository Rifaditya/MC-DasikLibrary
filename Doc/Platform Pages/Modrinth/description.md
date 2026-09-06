<p align="center">
  <img src="https://raw.githubusercontent.com/Rifaditya/MC-DasikLibrary/master/Images/Dasik%20page%20banner.jpg" alt="Dasik Library Banner" width="85%">
</p>

<p align="center">
  <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
  <img src="https://img.shields.io/badge/Architecture-Universal_Core_Library-blueviolet?style=for-the-badge" alt="Universal Core Library">
  <img src="https://img.shields.io/badge/Environment-Client_&_Server-success?style=for-the-badge" alt="Client & Server">
  <img src="https://img.shields.io/badge/Language-Java_25-orange?style=for-the-badge&logo=java" alt="Java 25">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
  <img src="https://img.shields.io/badge/Minecraft-26.1.2+-brightgreen?style=for-the-badge" alt="Minecraft 26.1.2+">
</p>

# 🧠 Dasik Library

> **"The universal Hive Mind foundation powering autonomous animal genetics, dynamic GameRules, and high-performance flocking across Minecraft."**

---

## 📖 Introduction

Developing modern Minecraft Fabric mods often means facing recurring architectural roadblocks: registering dynamic GameRules on the fly without causing immutable registry lockouts, synchronizing animal genetics and scale attributes across world save cycles, orchestrating swarming and flocking AI without decimating server tick rates, and integrating optional client configuration GUIs without causing fatal `ClassNotFoundException` crashes on headless dedicated servers.

**Dasik Library** is the universal standalone foundation engineered to solve these challenges. It provides the high-performance architectural backbone powering every mod across the **Vanilla Outsider**, **Delayed Gratification**, and **Instant Gratification** collections. Designed with strict zero-allocation hot paths, Knot ClassLoader isolation, and modular APIs, it equips developers and players with an enterprise-grade framework for modern Minecraft.

> [!IMPORTANT]
> **This is a Standalone Universal Core Library / API.**
> * **Universal Single-JAR Standard:** Dasik Library builds a single universal cross-version JAR (`>=26.1.2-`, currently **`v1.8.35`**) using pure SemVer. It seamlessly loads across **Minecraft 26.1.2, 26.2, 26.3**, and future snapshots.
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
- **Automatic Translation Injection:** Dynamically generates human-readable translation keys and injects them directly into vanilla's `Language` map (`dasik$injectDynamicGameRuleTranslations`), eliminating missing string placeholders (`§l` bold category formatting included).
- **World Save Persistence:** Seamlessly intercepts `SavedData` serialization (`GameRuleMapMixin`), ensuring dynamic GameRule changes made via `/gamerule` persist reliably in `level.dat` and `game_rules.dat`.
- **Automated Orphaned Rule Pruning:** Hooks `ServerLifecycleEvents.SERVER_STARTING` to detect and cleanly purge orphaned dynamic GameRules left behind when mods are uninstalled, keeping world save files pristine.
- **Math Conversion Helpers:** Built-in calculation utilities for percentages, permilles, and deciles (`getPct`, `getChance`, `getProb`, `getDecileFloat`).

### 🦅 High-Performance Swarm & Flocking AI (Cached Boids)
- **$O(N)$ Leader-Follower Pattern:** Eliminates expensive $O(N^2)$ pairwise distance checks by computing and caching group metrics (`FlockState`) exclusively on the pack Leader, enabling smooth swarms of 100+ entities at a solid 20 TPS.
- **Biomechanical Steering:** Full Alignment, Cohesion, and Separation vector steering calculations for realistic aerial murmurations (`AerialFlockingStrategy`) and terrestrial pack movement (`TerrestrialFlockingStrategy`).
- **Resilient Following:** Integrated water navigation penalty bypass and catch-up teleportation thresholds to ensure pack members never get lost across chunk boundaries.

### 📡 The Hive Mind & Decoupled Signal Bus
- **Decoupled Social Signals:** High-throughput `SignalBus` enables mobs to emit and receive alerts, warnings, and mood updates across species without hard-coded class couplings.
- **Scope Isolation:** Targeted signal delivery (`Scope.SAME_SPECIES`, `Scope.HERD`, `Scope.GLOBAL`) with safe instance checks.
- **Lazy Freeze Architecture:** Solves mod initialization race conditions by allowing modules to register social events during mod init while locking the registry safely at server tick.

### 🖥️ Optional GUI Integration & Server ClassLoader Safety
- **Classloader-Isolated Config Screen Resolver:** `GuiHelper` dynamically discovers and invokes YetAnotherConfigLib (YACL) and Cloth Config via reflection and Knot ClassLoader isolation.
- **100% Dedicated Server Crash Immunity:** Completely prevents client GUI classes from loading on dedicated servers, eliminating server startup crashes.
- **Standardized Creator Support:** Provides `DasikSupportHelper` to embed standardized creator support buttons at index 0 across config screen category pages.

### 🛡️ Enterprise Stability & Zero-Allocation Hot Paths
- **Zero-Dependency Version Guard:** `ModVersionGuard` verifies runtime Minecraft environment compatibility on startup and displays human-readable diagnostic advice if an incompatible API drop is detected.
- **Atomic File Persistence:** `ConfigHelper` manages JSON read/write operations using temporary file atomic swaps and automatic backup generation.
- **Zero-Allocation Rendering:** Zero-byte heap allocation architecture across high-frequency tick and render paths.

---

## 📊 Quick Reference & Mechanics Matrix

| Feature Dimension | Vanilla / Standard Mod Implementation | Dasik Library (`v1.8.35`) |
| :--- | :---: | :---: |
| **GameRule Registration** | Static build-time registration only | **Dynamic runtime unfreeze + auto-translation injection** |
| **Save File Persistence** | Only static vanilla rules saved | **Dynamic `SavedData` storage in `game_rules.dat`** |
| **Orphaned Rule Handling** | Corrupts saves or persists forever | **Automated startup pruning (`pruneOrphanedRules`)** |
| **Flocking AI Complexity** | $O(N^2)$ pairwise entity distance loops | **$O(N)$ Leader-Cached `FlockState` computation** |
| **Animal Genetics Engine** | Flat vanilla breed inheritance | **Lineage tracking, mutations, scale linking & loot scaling** |
| **Scale Attribute Range** | Rigid vanilla scaling | **Custom scaling clamp `[0.1f, 3.0f]` with linked stats** |
| **Optional Config GUI** | Prone to server `NoClassDefFoundError` | **Reflection & Knot isolation (`GuiHelper`) with 0 server risk** |
| **Version Safeguards** | Obscure Mixin crash traces | **`ModVersionGuard` Knot ClassLoader validation** |

---

## 🚀 Developer Quick Start & Implementation Guide

Integrating Dasik Library into your own Fabric mod takes only a few lines of code:

### 1. Gradle Setup
Add the Modrinth Maven repository and declare the dependency in your `build.gradle`:

```groovy
repositories {
    maven {
        name = "Modrinth"
        url = "https://api.modrinth.com/maven"
    }
}

dependencies {
    modImplementation "maven.modrinth:dasik-library:1.8.35"
}
```

### 2. Registering a Dynamic GameRule
Register a GameRule with automatic English translation and category bolding:

```java
import net.dasik.social.api.gamerule.DynamicGameRuleManager;

// Registers 'my_mod:drop_multiplier' with a default value of 2 under the 'Gameplay' category
DynamicGameRuleManager.registerIntegerRule(
    "my_mod:drop_multiplier",
    2,
    "Drop Multiplier",
    "Gameplay"
);
```

### 3. Binding Animal Scale to Health and Speed
Link physical size to combat and mobility attributes:

```java
import net.dasik.social.api.genetics.SizeStatsBuilder;
import net.dasik.social.api.genetics.LinkMode;
import net.minecraft.world.entity.ai.attributes.Attributes;

SizeStatsBuilder.create("my_mod:beast_genetics")
    .linkAttribute(Attributes.MAX_HEALTH, LinkMode.PROPORTIONAL, 2.0f)
    .linkAttribute(Attributes.MOVEMENT_SPEED, LinkMode.INVERSE, 0.5f)
    .register();
```

### 4. Dynamic Registry Scanning
Safely observe and handle new registry entries added by other mods:

```java
import net.dasik.social.api.registry.DynamicRegistryScanner;
import net.minecraft.core.registries.BuiltInRegistries;

DynamicRegistryScanner.subscribe(BuiltInRegistries.ITEM, item -> {
    // Process new modded items dynamically on startup and live reload
});
```

---

## ⚙️ Core Configuration & Save Architecture

> [!IMPORTANT]
> **💡 Config vs. In-Game GameRules:** The global configuration file only defines default values for newly created worlds. In existing worlds, change settings in-game via the **Edit Game Rules** UI screen or the `/gamerule` command.

Dasik Library manages dynamic GameRule states natively within your world save files:
* **World Storage:** Stored in `<world_save>/data/game_rules.dat` and `level.dat`.
* **Zero Residual Clutter:** When you remove a mod that registered dynamic rules, Dasik Library's startup pruning engine detects the absent namespace and unregisters orphaned entries, preventing console spam and bloated save files.

---

## 📖 In-Depth How-To & Integration Playbook

### 1. For Survival Players & Server Administrators
1. Download `dasik-library-1.8.35.jar` (or latest release).
2. Place the JAR directly into your `.minecraft/mods` folder along with **Fabric API**.
3. Launch Minecraft. Dasik Library operates silently and efficiently in the background, providing foundational services for your installed mods.
4. When configuring GameRules in-game via the **Edit Game Rules** screen or `/gamerule`, all rules registered by Dasik-powered mods are automatically translated, organized into categories, and saved directly to the world file.

### 2. For Modpack Creators
1. **Universal Multi-Version Support:** You do not need separate builds of Dasik Library for minor Minecraft patches—one build handles MC 26.1.2, 26.2, and 26.3+.
2. **Server-Safe:** Include Dasik Library in both client and server modpacks without fear of client GUI crashes.
3. **Clean World Upgrades:** When updating modpacks and removing deprecated mods, the library automatically purges orphaned GameRules on the next server launch.

### 3. For Mod Developers
1. Declare `dasik-library` in your `fabric.mod.json`:
   ```json
   "depends": {
     "fabricloader": ">=0.16.10",
     "minecraft": ">=26.1.2-",
     "fabric-api": "*",
     "dasik-library": ">=1.8.15"
   }
   ```
2. Leverage `DynamicGameRuleManager` to avoid writing repetitive GameRule boilerplate and translation files.
3. Use `GuiHelper` to supply optional YACL or Cloth Config GUIs that open seamlessly via ModMenu while preserving 100% server safety.

---

## 🧩 Recommended Sister Mods & Powered Projects

Dasik Library serves as the engine for numerous popular mods. Explore these projects powered directly by the library:

* 📜 [**Collapsible Game Rules**](https://modrinth.com/mod/collapsible-gamerules): Modernizes the Edit Game Rules screen into collapsible categories with real-time search, leveraging Dasik Library's dynamic metadata.
* 🐕 [**Better Dogs**](https://modrinth.com/mod/better-dogs): Complete canine behavioral overhaul featuring personality intelligence, pack murmuration, and vehicle riding powered by Dasik Library's AI flocking engine.
* 🌿 [**Natural Reproduction**](https://modrinth.com/mod/vo-natural-reproduction): Autonomous 27-species wildlife breeding and genetics engine built directly on Dasik Animal Genetics.

> 🌟 *Explore my profile for more vanilla enhancements, performance utilities, and game design overhauls.*

---

## ☕ Support

If you appreciate this library and the mods it powers, consider fueling future updates!

<p align="center">
  <a href="https://ko-fi.com/dasikigaijin/tip"><img src="https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Ko-fi"></a>
  <a href="https://sociabuzz.com/dasikigaijin/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge" alt="SocioBuzz"></a>
  <a href="https://saweria.co/DasikIgaijinn"><img src="https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge" alt="Saweria"></a>
</p>

> [!NOTE]
> **🇮🇩 Indonesian Users:** SocioBuzz and Saweria support local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!

> [!TIP]
> **Dedicated Server Hosting Partner:**
> Looking for a high-performance server to host your community or play with friends? Check out **BisectHosting** for 1-click modpack installations, automated backups, and 24/7 dedicated customer support. Use promo code **`Dasik`** for 25% off your first month!

---

## 📜 Credits & Modpack Permissions

| Property | Information |
| :--- | :--- |
| **Creator / Author** | **Dasik** (Rifaditya) |
| **Collection** | Universal Core Architecture |
| **License** | [GNU General Public License v3.0 (GPLv3)](https://www.gnu.org/licenses/gpl-3.0.html) |
| **Source Code** | [GitHub - Rifaditya/MC-DasikLibrary](https://github.com/Rifaditya/MC-DasikLibrary) |
| **Issue Tracker** | [GitHub Issues](https://github.com/Rifaditya/MC-DasikLibrary/issues) |
| **Documentation / Wiki** | [GitHub Wiki](https://github.com/Rifaditya/MC-DasikLibrary/wiki) |

> [!IMPORTANT]
> **📦 Modpack Permissions & Distribution:**<br>
> You are fully welcome to include this mod in any modpack on any platform! However, the mod file must be downloaded directly through official distribution channels (**Modrinth** or **CurseForge**). Re-uploading, mirroring, or redistributing the original mod JAR to third-party mirror sites, scraper portals, or unauthorized launchers is strictly prohibited.
> <br><br>
> **⚖️ License & Fork Guidelines (No Zero-Change Re-uploads):**<br>
> This project is open-source under the **GNU GPLv3**. You are fully encouraged to inspect the code, learn from it, and fork the repository to create genuine modifications, substantial feature expansions, or community ports—provided your project remains open-source under GPLv3 with proper attribution.<br>
> **However, straight 1:1 re-uploads, clone forks with no meaningful functional changes, or re-publishing identical builds under different project names (e.g. to farm downloads or rewards) are strictly forbidden.**

---

<p align="center">
  <strong>Made with ❤️ for the Minecraft community</strong><br>
  <em>Part of the Universal Core Architecture</em>
</p>
