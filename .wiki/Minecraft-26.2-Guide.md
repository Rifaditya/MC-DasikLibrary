# Minecraft 26.2+ Guide

| Parameter | Specification |
| :--- | :--- |
| **Target Minecraft Version** | `26.2` (and forward-compatible `26.x`) |
| **Fabric Loader Constraint** | `>=0.18.4` |
| **Java Environment** | JDK 25 |
| **Library Version** | `1.8.15` |
| **Mod ID** | `dasik-library` |
| **Mod Name** | Dasik Library |
| **License** | LGPL-3.0 |

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🛠️ Overview & Installation

**Dasik Library** is an essential runtime dependency for mods in the *Vanilla Outsider*, *Instant Gratification*, and *Delayed Gratification* collections. It delivers central social AI tick scheduling, animal genetics, Boids vector steering math, and dynamic GameRule registration.

### 📥 Player Installation
1. Install **Fabric Loader** (`0.18.4` or higher) for Minecraft `26.2`.
2. Ensure **Fabric API** (`0.152.1+26.2` or higher) is installed in your `.minecraft/mods` folder.
3. Download `dasik-library-1.8.15.jar` and place it into your `.minecraft/mods` directory alongside dependent consumer mods (e.g. *Better Dogs*, *Natural Reproduction*).

### 💻 Mod Developer Dependency

Add **Dasik Library** to your `fabric.mod.json`:

```json
{
  "schemaVersion": 1,
  "id": "my_consumer_mod",
  "version": "1.0.0+26.2",
  "name": "My Consumer Mod",
  "depends": {
    "fabricloader": ">=0.18.4",
    "minecraft": ">=26.1.2-",
    "dasik-library": "*"
  }
}
```

In `gradle.properties`:

```properties
dasik_library_version=1.8.15
```

In `build.gradle`:

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_library_version}"
}
```

---

## ⚙️ Core 26.2+ Architecture Changes

1. **Mojang Sovereign Mappings**: Uses Mojang mappings (`level`, `ServerLevel`, `EntityTypes`). Yarn-mapped methods (`world`, `getWorld`) are strictly obsolete.
2. **Identifier API**: Uses `Identifier.fromNamespaceAndPath(namespace, path)` or `Identifier.parse(string)` for resource locations. Legacy `Identifier.of()` is unsupported.
3. **Open-Ended Bounds (`>=26.1.2-`)**: Ensures a single build JAR remains forward-compatible across Minecraft `26.2+` drops while using `ModVersionGuard` for runtime safety.

---

## 🔗 Related Pages
* [[Version Compatibility|Version-Compatibility]]
* [[ModVersionGuard & Startup Safety|ModVersionGuard-and-Startup-Safety]]
* [[Developer Setup & Building|Developer-Setup-and-Building]]
