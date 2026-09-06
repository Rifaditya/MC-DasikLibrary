# Minecraft 26.2+ Leitfaden

| Parameter | Spezifikation |
| :--- | :--- |
| **Zielversion von Minecraft** | `26.2` (und vorwärtskompatibel mit `26.x`) |
| **Fabric Loader Bedingung** | `>=0.18.4` |
| **Java-Umgebung** | JDK 25 |
| **Bibliotheksversion** | `1.8.15` |
| **Mod-ID** | `dasik-library` |
| **Mod-Name** | Dasik Library |
| **Lizenz** | LGPL-3.0 |

> 📌 **Hinweis zum Quellcode-Repository**: Die Dokumentation in diesem Wiki spiegelt den **aktuellen Stand des Quellcodes im Repository** wider, der möglicherweise kürzlich durchgeführte, noch nicht veröffentlichte Commits oder Entwicklungsfunktionen vor den öffentlichen Release-Builds auf CurseForge und Modrinth enthält.

---

## 🛠️ Übersicht & Installation

**Dasik Library** ist eine unverzichtbare Laufzeitabhängigkeit für Mods der Reihen *Vanilla Outsider*, *Instant Gratification* und *Delayed Gratification*. Sie stellt zentrales Tick-Scheduling für soziale KI, Tiergenetik, Boids-Vektorlenkungsmathematik und dynamische GameRule-Registrierung bereit.

### 📥 Installation für Spieler
1. Installieren Sie **Fabric Loader** (`0.18.4` oder neuer) für Minecraft `26.2`.
2. Vergewissern Sie sich, dass die **Fabric API** (`0.152.1+26.2` oder neuer) im Ordner `.minecraft/mods` installiert ist.
3. Laden Sie `dasik-library-1.8.15.jar` herunter und legen Sie die Datei zusammen mit abhängigen Mods (z. B. *Better Dogs*, *Natural Reproduction*) in Ihr `.minecraft/mods`-Verzeichnis.

### 💻 Abhängigkeit für Mod-Entwickler

Fügen Sie **Dasik Library** zu Ihrer `fabric.mod.json` hinzu:

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

## ⚙️ Wichtigste Architekturänderungen in 26.2+

1. **Mojang Sovereign Mappings**: Nutzt Mojang-Mappings (`level`, `ServerLevel`, `EntityTypes`). Alte Yarn-Methoden (`world`, `getWorld`) sind vollständig obsolet.
2. **Identifier API**: Nutzt `Identifier.fromNamespaceAndPath(namespace, path)` oder `Identifier.parse(string)`. Die veraltete Methode `Identifier.of()` wird nicht unterstützt.
3. **Offene Versionsgrenzen (`>=26.1.2-`)**: Gewährleistet, dass eine einzige JAR mit kommenden Minecraft `26.2+` Patches kompatibel bleibt, während `ModVersionGuard` die Sicherheit zur Laufzeit garantiert.

---

## 🔗 Verwandte Seiten
* [[Versionskompatibilität|de_de-Version-Compatibility]]
* [[ModVersionGuard & Start-Sicherheit|de_de-ModVersionGuard-and-Startup-Safety]]
* [[Entwickler-Setup & Build|de_de-Developer-Setup-and-Building]]
