<div align="center">

![Dasik Library Banner](https://cdn.modrinth.com/data/cached_images/151bae2f6d662a83f7158fee8f036d7974645859.jpeg)

</div>
<p align="center">
    <a href="https://modrinth.com/mod/fabric-api"><img src="https://img.shields.io/badge/Requires-Fabric_API-blue?style=for-the-badge&logo=fabric" alt="Requires Fabric API"></a>
    <img src="https://img.shields.io/badge/Language-Java-orange?style=for-the-badge&logo=java" alt="Java">
    <img src="https://img.shields.io/badge/License-LGPLv3-green?style=for-the-badge" alt="License">
</p>

# 🧠 Dasik Library

**No Backports:** This library targets **Minecraft 26.1+** (Snapshot 5). Older versions are unsupported.

> **This is a Core Library / API.**
> It provides shared functionality for my mods (Vanilla Outsider, Better Dogs, Bat Ecology). It does nothing on its own.

**Dasik Library** provides a unified **Hive Mind** architecture for complex social behaviors. Entities become aware of "Social Events" (Alerts, Moods, Requests) and participate in a global, optimized social scheduler.

---

## ✨ Features

### 📡 The Hive Mind (Global Signal System)

Entities don't just "tick"—they **pulse**.

- **Unified Scheduler**: One centralized system manages social interactions for all supported mods.
- **Performance Optimized**: Uses a "Highlander" pattern ensuring logic only runs once per tick, even if 100 mods use the library.

### 🐣 Universal Life Cycle

Why should only Villagers and Cows have babies? Dasik Library democratizes life itself.

- **Universal Aging**: Any entity can be patched to support baby variants with proper scaling and growth data.
- **Unified Breeding**: A centralized registry allows defining breeding items, cooldowns, and partners for any mob—even those Mojang forgot.
- **Growth Acceleration**: Feeding babies valid items speeds up their growth logic, universally handled.

### 🎭 Social Events Registry

A standardized way for entities to communicate.

- **Tracks**: Events are categorized (e.g., `Mood` (High Priority), `Ambient` (Low Priority)).
- **Lazy Freeze**: The registry respects mod loading order but locks down tight for stabilization once the world loads.

> [!NOTE]
> **Developer Note**:
> This library solves the "Mod Initialization Race Condition" using the **Lazy Freeze Pattern**. You can register events safely in your `onInitialize`, and the system won't lock until `ServerTick`.

### 🤝 Inter-Mod Compat

- **Better Dogs**: Uses Dasik to handle Pack mechanics and howling.
- **Bat Ecology**: Uses Dasik to coordinate Swarm leaders and crop dusting.

---

## 📦 Install

1. Install **[Fabric API](https://modrinth.com/mod/fabric-api)**.
2. Download `dasik-library-1.2.6.jar` and place it in your `mods` folder.
3. *Required by all Vanilla Outsider mods.*

---

## ☕ Support

If you enjoy the **Vanilla Outsider** ecosystem, consider fueling the next update!

[![Ko-fi](https://img.shields.io/badge/Ko--fi-Support%20Me-FF5E5B?style=for-the-badge&logo=ko-fi&logoColor=white)](https://ko-fi.com/dasikigaijin/tip)
[![SocioBuzz](https://img.shields.io/badge/SocioBuzz-Local_Support-7BB32E?style=for-the-badge)](https://sociabuzz.com/dasikigaijin/tribe)

> [!NOTE]
> **Indonesian Users:** SocioBuzz supports local payment methods (Gopay, OVO, Dana, etc.) if you want to support me without using PayPal/Ko-fi!

---

## 📜 Credits

| Role | Author |
| :--- | :--- |
| **Architect** | **Rifaditya** (Dasik) |
| **Collection** | Vanilla Outsider |
| **License** | LGPLv3 |

---

> [!IMPORTANT]
> **Modpack Permissions:** You are free to include this mod in modpacks, **provided the modpack is hosted on the same platform** (e.g. Modrinth).
>
> **Cross-platform distribution is not permitted.** If you download this mod from Modrinth, your modpack must also be published on Modrinth.

---

<div align="center">

**Made with ❤️ for the Minecraft community**

*Part of the Vanilla Outsider Collection*

</div>
