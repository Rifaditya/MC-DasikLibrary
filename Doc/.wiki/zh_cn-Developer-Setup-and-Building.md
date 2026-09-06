# 开发者环境配置与构建

| 工具 | 推荐版本 |
| :--- | :--- |
| **JDK** | Java 25 (`E:/JDK25`) |
| **Gradle** | 9.3+ |
| **Fabric Loom** | `1.15.2` |
| **Minecraft Mappings** | Mojang Sovereign |

---

## 🛠️ 环境依赖与构建步骤

### 1. 克隆代码仓库
```bash
git clone https://github.com/Rifaditya/DasikLibrary-Rebuilt.git
cd DasikLibrary-Rebuilt
```

### 2. 配置 JDK 路径
在 `gradle.properties` 中：
```properties
org.gradle.java.home=E:/JDK25
```

### 3. 编译构建与本地 Maven 发布
构建发布版 JAR：
```bash
./gradlew build --no-daemon
```

发布至本地 Maven 缓存（`~/.m2/repository`）：
```bash
./gradlew publishToMavenLocal
```

---

## 🧪 无头自动化测试

执行自动化 GameTest 与 JUnit 测试套件：
```bash
./gradlew test
```

---

## 🔗 相关页面
* [[架构与包结构设计|zh_cn-Architecture-and-Package-Layout]]
* [[下游 Mod 接入集成指南|zh_cn-Consumer-Mods-Integration-Guide]]
