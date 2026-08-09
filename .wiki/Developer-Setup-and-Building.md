# Developer Setup & Building

| Tool | Recommended Version |
| :--- | :--- |
| **JDK** | Java 25 (`E:/JDK25`) |
| **Gradle** | 9.3+ |
| **Fabric Loom** | `1.15.2` |
| **Minecraft Mappings** | Mojang Sovereign |

---

## 🛠️ Environment Prerequisites & Building

### 1. Clone Repository
```bash
git clone https://github.com/Rifaditya/DasikLibrary-Rebuilt.git
cd DasikLibrary-Rebuilt
```

### 2. Configure JDK Path
In `gradle.properties`:
```properties
org.gradle.java.home=E:/JDK25
```

### 3. Build & Local Maven Publish
Build release JAR:
```bash
./gradlew build --no-daemon
```

Publish to local maven cache (`~/.m2/repository`):
```bash
./gradlew publishToMavenLocal
```

---

## 🧪 Headless Automated Testing

Execute automated GameTest & JUnit test suites:
```bash
./gradlew test
```

---

## 🔗 Related Pages
* [[Architecture & Package Layout|Architecture-and-Package-Layout]]
* [[Consumer Mods Integration Guide|Consumer-Mods-Integration-Guide]]
