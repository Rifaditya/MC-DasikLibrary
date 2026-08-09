# Version Compatibility Matrix

| Minecraft Target | Library Version | Mod Version Guard | Dependency Spec | Support Status |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **Active Mainline** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | Backport / Stable |
| **Minecraft 1.21.x** | *Obsolete* | *N/A* | *Legacy 1.x* | **End of Life** |

> 📌 **Repository Source Disclaimer**: The documentation in this Wiki reflects the **current source code state in the repository**, which may include recent unreleased commits or developmental features ahead of public release builds on CurseForge and Modrinth.

---

## 🛡️ The 1 Jar 1 Version & Forward Compatibility Policy

Dasik Library implements the **1 Jar 1 Version Policy** combined with **Open-Ended Forward Compatibility**:

1. **Open-Ended Version Bounds (`"minecraft": ">=26.1.2-"`)**: The library specifies an open lower bound in `fabric.mod.json`, enabling Fabric Loader to accept the JAR on minor patch updates without hard-locking players out.
2. **Knot ClassLoader Safety (`ModVersionGuard`)**: During `onInitialize()`, `ModVersionGuard.checkClass` validates class presence using `Thread.currentThread().getContextClassLoader()` to catch unexpected API shifts early and display friendly error banners instead of silent JVM crashes.

```java
public final class ModVersionGuard {
    public static void checkClass(String modName, String requiredClassName) {
        try {
            Class.forName(requiredClassName, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("\n" +
                "=====================================================================\n" +
                " [" + modName + "] Minecraft API Mismatch!\n" +
                " A required Minecraft class or API was not found in your game version.\n" +
                "=====================================================================");
        }
    }
}
```

---

## 🚫 Version Identity Guard (No Legacy Conflation)

Under strict design guidelines (`[DIR-20260614-001]`), **Minecraft 26.x Annual Drop versions MUST NEVER be conflated with legacy 2024 1.21.x versions**:
* ❌ `26.2 (1.21.4)` — Strictly Banned.
* ✅ `Minecraft 26.2` — Standard sovereign annual drop notation.

---

## 🔗 Related Pages
* [[MC 26.2 Guide|Minecraft-26.2-Guide]]
* [[ModVersionGuard & Startup Safety|ModVersionGuard-and-Startup-Safety]]
