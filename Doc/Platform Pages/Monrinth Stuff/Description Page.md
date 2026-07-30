<div align="center">

![Dasik Library Banner](https://media.forgecdn.net/attachments/1513/813/dasik-page-banner-jpg.jpg)

</div>
<p align="center">
    <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java">
    <img src="https://img.shields.io/badge/License-LGPLv3-green?style=for-the-badge" alt="License">
</p>

# 🧠 Dasik Library

**No Backports:** This library targets **Minecraft 26.2+**. Older versions are unsupported.

> **This is a Core Library / API.**
> It provides shared functionality for my mods (Vanilla Outsider, Instant Gratification, and Delayed Gratification collections). It does nothing on its own.

**Dasik Library** provides a unified **Hive Mind** architecture for complex social behaviors. Entities become aware of "Social Events" (Alerts, Moods, Requests) and participate in a global, optimized social scheduler.

---

## ✨ Features

### 📡 The Hive Mind (Global Signal System)

Entities don't just "tick"—they **pulse**.

- **Unified Scheduler**: One centralized system manages social interactions for all supported mods.
- **Performance Optimized**: Uses a "Highlander" pattern ensuring logic only runs once per tick, even if 100 mods use the library.

### 🎭 Social Events Registry

A standardized way for entities to communicate.

- **Tracks**: Events are categorized (e.g., `Mood` (High Priority), `Ambient` (Low Priority)).
- **Lazy Freeze**: The registry respects mod loading order but locks down tight for stabilization once the world loads.

> [!NOTE]
> **Developer Note**:
> This library solves the "Mod Initialization Race Condition" using the **Lazy Freeze Pattern**. You can register events safely in your `onInitialize`, and the system won't lock until `ServerTick`.

### 🤝 Inter-Mod Compat

- **Better Dogs**: Uses Dasik to handle Pack mechanics, howling, and wild wolf leader-follower logic.
- **MCA Female Gender Bridge**: Integrates genetics & breeding physics solvers.
- **Collapsible Game Rule Screen**: Utilizes dynamic GameRule registration and localization helpers.

### 🐺 Leader-Follower API (Cached Boids)

The most advanced flocking API in the Fabric ecosystem.
- **Cached Boids Pattern**: Supports massive swarms (100+ entities) with minimal CPU usage by pre-calculating group data on the Leader.
- **Biomechanical Logic**: Alignment, Cohesion, and Separation steering for organic movement.
- **Minecraft 26.2+ Native**: Fully utilizes native 26.2+ entity attributes and dynamic GameRule registration.

---

## 📦 Install

1. Install **[Fabric API](https://modrinth.com/mod/fabric-api)**.
2. Download `dasik-library-1.8.5.jar` (or latest) and place it in your `mods` folder.
3. *Required by all Vanilla Outsider, Instant Gratification, and Delayed Gratification mods.*

---

## ☕ Support

If you enjoy the ecosystem, consider fueling the next update!

[![Ko-fi](https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/dasikigaijin/tip)
[![SocioBuzz](https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge)](https://sociabuzz.com/dasikigaijin/tribe)
[![Saweria](https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge)](https://saweria.co/DasikIgaijinn)

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
