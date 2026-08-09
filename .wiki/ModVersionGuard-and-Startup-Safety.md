# ModVersionGuard & Startup Safety

| Class | `net.dasik.social.util.ModVersionGuard` |
| :--- | :--- |
| **ClassLoader Policy** | Explicit `Thread.currentThread().getContextClassLoader()` |
| **Banned Pattern** | Default `Class.forName(name)` without ClassLoader |
| **Entrypoint Target** | `DasikLibraryMod.onInitialize()` |

---

## 🛡️ Knot ClassLoader Resolution Law

In Fabric modding, default `Class.forName(String className)` uses the system ClassLoader, which fails under Fabric Knot ClassLoader during early mod initialization, throwing a false `ClassNotFoundException` even when target classes are present.

`ModVersionGuard` passes `Thread.currentThread().getContextClassLoader()` explicitly:

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
                " Try updating your Minecraft version or download a matching build.\n" +
                "=====================================================================");
        }
    }
}
```

---

## 🔗 Related Pages
* [[Version Compatibility|Version-Compatibility]]
* [[MC 26.2 Guide|Minecraft-26.2-Guide]]
