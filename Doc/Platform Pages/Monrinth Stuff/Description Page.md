<div align="center">

![Dasik Library Banner](https://media.forgecdn.net/attachments/1513/813/dasik-page-banner-jpg.jpg)

</div>

<p align="center">
    <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java">
    <img src="https://img.shields.io/badge/License-LGPLv3-green?style=for-the-badge" alt="License">
</p>

# 🧠 Dasik Library

**Active Version Target:** Supported on **Minecraft 26.1.2+** (including 26.2+). Older versions are unsupported.

> [!IMPORTANT]
> **This is a Core Library / API.**
> It provides shared functionality for my mods (Vanilla Outsider, Instant Gratification, and Delayed Gratification collections). It does nothing on its own.

**Dasik Library** provides a unified **Hive Mind** architecture for complex social behaviors. Entities become aware of "Social Events" (Alerts, Moods, Requests), dynamic genetics, flocking algorithms, and participate in a global, optimized social scheduler.

---

## ✨ Features

### 📡 The Hive Mind (Global Signal System)

Entities don't just "tick"—they **pulse**.

- **Unified Scheduler**: One centralized system manages social interactions for all supported mods.
- **Performance Optimized**: Uses a "Highlander" pattern ensuring logic only runs once per tick, even if multiple mods consume the library.

### 🎭 Social Events Registry

A standardized way for entities to communicate across mods.

- **Tracks**: Events are categorized into priority tracks (e.g., `Mood` for high priority, `Ambient` for low priority).
- **Lazy Freeze**: Respects mod loading order while locking down tight for world save stability once the server initializes.

> [!NOTE]
> **Developer Note**:
> This library solves the "Mod Initialization Race Condition" using the **Lazy Freeze Pattern**. You can register events safely in your `onInitialize`, and the system won't lock until `ServerTick`.

### 🤝 Inter-Mod Compatibility

- **Better Dogs**: Handles pack dynamics, howling signals, and wolf leader-follower flocking.
- **MCA Female Gender Bridge**: Integrates genetics & breeding physics solvers.
- **Collapsible Game Rule Screen**: Powers dynamic GameRule registration and localization helpers.

### 🐺 Leader-Follower API (Cached Boids)

A high-performance flocking API in the Fabric ecosystem.
- **Cached Boids Pattern**: Supports swarms (100+ entities) with minimal CPU load by pre-calculating group data on the Leader.
- **Biomechanical Steering**: Alignment, Cohesion, and Separation steering for organic pack movement.
- **Entity Scaling & Genetics**: Mapped directly to vanilla scale attributes and dynamic GameRule limits.

### 🧬 Genetics & Breeding Engine

Entity-agnostic genetics attachment, triangular mutation rules, inbreeding penalties, and linked attribute scaling (health, speed, damage).

---

## 📦 Installation

1. Install **[Fabric API](https://modrinth.com/mod/fabric-api)**.
2. Download the latest release of **Dasik Library** matching your Minecraft version and place it in your `mods` folder.
3. *Required by all Vanilla Outsider, Instant Gratification, and Delayed Gratification mods.*

---

## ☕ Support

If you enjoy the ecosystem, consider fueling the next update!

<p align="center">
    <a href="https://ko-fi.com/dasikigaijin/tip"><img src="https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Ko-fi"></a>
    <a href="https://sociabuzz.com/dasikigaijin/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge" alt="SocioBuzz"></a>
    <a href="https://saweria.co/DasikIgaijinn"><img src="https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge" alt="Saweria"></a>
</p>

> [!NOTE]
> **Indonesian Users:** SocioBuzz and Saweria support local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!

---

## 📜 Credits

| Role | Author |
| :--- | :--- |
| **Creator** | **Dasik (Rifaditya)** |
| **Collection** | Vanilla Outsider / Instant Gratification / Delayed Gratification |
| **License** | LGPLv3 |

---

> [!IMPORTANT]
> **📦 Modpack Permissions:** You are free to include this mod in any modpack on any platform. However, the mod itself must be downloaded from its official distribution pages on **Modrinth** or **CurseForge**. Re-uploading or redistributing the mod jar file to third-party sites is strictly prohibited unless explicitly permitted by the creator.

---

<div align="center">

**Made with ❤️ for the Minecraft community**

*Part of the Vanilla Outsider Collection*

</div>
