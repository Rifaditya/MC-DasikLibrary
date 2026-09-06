# Versionskompatibilitätsmatrix

| Minecraft-Zielversion | Bibliotheksversion | Mod Version Guard | Abhängigkeitsspezifikation | Support-Status |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **Aktiver Hauptzweig** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | Backport / Stabil |
| **Minecraft 1.21.x** | *Veraltet* | *N/A* | *Legacy 1.x* | **Ende des Lebenszyklus (EOL)** |

> 📌 **Hinweis zum Quellcode-Repository**: Die Dokumentation in diesem Wiki spiegelt den **aktuellen Stand des Quellcodes im Repository** wider, der möglicherweise kürzlich durchgeführte, noch nicht veröffentlichte Commits oder Entwicklungsfunktionen vor den öffentlichen Release-Builds auf CurseForge und Modrinth enthält.

---

## 🛡️ Die 1 Jar 1 Version- und Vorwärtskompatibilitäts-Richtlinie

Dasik Library setzt die **1 Jar 1 Version-Richtlinie** in Verbindung mit **offener Vorwärtskompatibilität** um:

1. **Offene Versionsgrenzen (`"minecraft": ">=26.1.2-"`)**: Die Bibliothek spezifiziert eine offene Untergrenze in `fabric.mod.json`, sodass Fabric Loader die JAR bei kleineren Patch-Updates akzeptieren kann, ohne Spieler auszusperren.
2. **Knot ClassLoader-Sicherheit (`ModVersionGuard`)**: Während `onInitialize()` validiert `ModVersionGuard.checkClass` das Vorhandensein von Klassen über `Thread.currentThread().getContextClassLoader()`, um API-Brüche frühzeitig abzufangen und benutzerfreundliche Fehlermeldungen anstelle stummer JVM-Abstürze auszugeben.

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

## 🚫 Versionsidentitätsschutz (Keine Vermischung mit Altversionen)

Gemäß strengen Designvorgaben (`[DIR-20260614-001]`) **dürfen Minecraft 26.x Jahres-Releases NIEMALS mit alten 1.21.x-Versionen aus 2024 vermischt werden**:
* ❌ `26.2 (1.21.4)` — Streng Verboten.
* ✅ `Minecraft 26.2` — Standardsouveräne Jahres-Release-Notation.

---

## 🔗 Verwandte Seiten
* [[MC 26.2 Leitfaden|de_de-Minecraft-26.2-Guide]]
* [[ModVersionGuard & Start-Sicherheit|de_de-ModVersionGuard-and-Startup-Safety]]
