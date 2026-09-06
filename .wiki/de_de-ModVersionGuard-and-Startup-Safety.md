# ModVersionGuard & Start-Sicherheit

| Klasse | `net.dasik.social.util.ModVersionGuard` |
| :--- | :--- |
| **ClassLoader-Richtlinie** | Explizite Übergabe von `Thread.currentThread().getContextClassLoader()` |
| **Verbotenes Muster** | Standard `Class.forName(name)` ohne Angabe des ClassLoaders |
| **Aufrufpunkt** | `DasikLibraryMod.onInitialize()` |

---

## 🛡️ Klassenauflösung unter Knot ClassLoader

In Fabric-Mods nutzt der Standardaufruf `Class.forName(String className)` den System-ClassLoader. Dies schlägt unter dem Knot ClassLoader in frühen Initialisierungsphasen fehl und wirft fälschlicherweise eine `ClassNotFoundException`, obwohl die Klassen existieren.

`ModVersionGuard` übergibt explizit `Thread.currentThread().getContextClassLoader()`:

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

## 🔗 Verwandte Seiten
* [[Versionskompatibilität|de_de-Version-Compatibility]]
* [[MC 26.2 Leitfaden|de_de-Minecraft-26.2-Guide]]
