# Entwickler-Setup & Build

| Werkzeug | Empfohlene Version |
| :--- | :--- |
| **JDK** | Java 25 (`E:/JDK25`) |
| **Gradle** | 9.3+ |
| **Fabric Loom** | `1.15.2` |
| **Minecraft Mappings** | Mojang Sovereign |

---

## 🛠️ Umgebungsvoraussetzungen & Build-Prozess

### 1. Repository klonen
```bash
git clone https://github.com/Rifaditya/DasikLibrary-Rebuilt.git
cd DasikLibrary-Rebuilt
```

### 2. JDK-Pfad einrichten
In der Datei `gradle.properties`:
```properties
org.gradle.java.home=E:/JDK25
```

### 3. Kompilieren & Lokale Maven-Veröffentlichung
Release-JAR erstellen:
```bash
./gradlew build --no-daemon
```

Im lokalen Maven-Cache (`~/.m2/repository`) veröffentlichen:
```bash
./gradlew publishToMavenLocal
```

---

## 🧪 Headless-Automatisierungstests

Ausführen der automatisierten GameTest- und JUnit-Testsuiten:
```bash
./gradlew test
```

---

## 🔗 Verwandte Seiten
* [[Architektur & Paketstruktur|de_de-Architecture-and-Package-Layout]]
* [[Integrationsleitfaden für nachgelagerte Mods|de_de-Consumer-Mods-Integration-Guide]]
