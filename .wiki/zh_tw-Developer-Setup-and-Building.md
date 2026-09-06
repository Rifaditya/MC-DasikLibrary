# 開發者環境配置與構建

| 工具 | 推薦版本 |
| :--- | :--- |
| **JDK** | Java 25 (`E:/JDK25`) |
| **Gradle** | 9.3+ |
| **Fabric Loom** | `1.15.2` |
| **Minecraft Mappings** | Mojang Sovereign |

---

## 🛠️ 環境依賴與構建步驟

### 1. 克隆代碼倉庫
```bash
git clone https://github.com/Rifaditya/DasikLibrary-Rebuilt.git
cd DasikLibrary-Rebuilt
```

### 2. 配置 JDK 路徑
在 `gradle.properties` 中：
```properties
org.gradle.java.home=E:/JDK25
```

### 3. 編譯構建與本地 Maven 發布
構建發布版 JAR：
```bash
./gradlew build --no-daemon
```

發布至本地 Maven 緩存（`~/.m2/repository`）：
```bash
./gradlew publishToMavenLocal
```

---

## 🧪 無頭自動化測試

執行自動化 GameTest 與 JUnit 測試套件：
```bash
./gradlew test
```

---

## 🔗 相關頁面
* [[架構與套件結構設計|zh_tw-Architecture-and-Package-Layout]]
* [[下游 Mod 接入集成指南|zh_tw-Consumer-Mods-Integration-Guide]]
