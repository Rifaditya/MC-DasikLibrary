<div align="center">

![Dasik Library Banner](https://media.forgecdn.net/attachments/1513/813/dasik-page-banner-jpg.jpg)

</div>
<p align="center">
    <a href="https://www.curseforge.com/minecraft/mc-mods/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java">
    <img src="https://img.shields.io/badge/License-LGPLv3-green?style=for-the-badge" alt="License">
</p>

# 🧠 Dasik Library

**No Backports:** This library targets **Minecraft 26.2+**. Older versions are unsupported.

<blockquote>
    <strong>This is a Core Library / API.</strong><br>
    It provides shared functionality for my mods (Vanilla Outsider, Instant Gratification, and Delayed Gratification collections). It does nothing on its own.
</blockquote>

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

<blockquote>
    <strong>Developer Note:</strong><br>
    This library solves the "Mod Initialization Race Condition" using the <strong>Lazy Freeze Pattern</strong>. You can register events safely in your <code>onInitialize</code>, and the system won't lock until <code>ServerTick</code>.
</blockquote>

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

1. Install **<a href="https://www.curseforge.com/minecraft/mc-mods/fabric-api">Fabric API</a>**.
2. Download <code>dasik-library-1.8.5.jar</code> (or latest) and place it in your <code>mods</code> folder.
3. <em>Required by all Vanilla Outsider, Instant Gratification, and Delayed Gratification mods.</em>

---

## ☕ Support

If you enjoy the ecosystem, consider fueling the next update!

<p align="center">
    <a href="https://ko-fi.com/dasikigaijin/tip"><img src="https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white" alt="Ko-fi"></a>
    <a href="https://sociabuzz.com/dasikigaijin/tribe"><img src="https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge" alt="SocioBuzz"></a>
    <a href="https://saweria.co/DasikIgaijinn"><img src="https://img.shields.io/badge/Saweria-Local_Support-FFA500?style=for-the-badge" alt="Saweria"></a>
</p>

<blockquote>
    <strong>Indonesian Users:</strong> SocioBuzz and Saweria support local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!
</blockquote>

---

## 📜 Credits

| Role | Author |
| :--- | :--- |
| **Creator** | **Dasik (Rifaditya)** |
| **Collection** | Vanilla Outsider / Instant Gratification / Delayed Gratification |
| **License** | LGPLv3 |

---

<blockquote>
    <strong>📦 Modpack Permissions:</strong><br>
    You are free to include this mod in any modpack on any platform. However, the mod itself must be downloaded from its official distribution pages on <strong>Modrinth</strong> or <strong>CurseForge</strong>. Re-uploading or redistributing the mod jar file to third-party sites is strictly prohibited unless explicitly permitted by the creator.
</blockquote>

---

<div align="center">

**Made with ❤️ for the Minecraft community**

*Part of the Vanilla Outsider Collection*

</div>
