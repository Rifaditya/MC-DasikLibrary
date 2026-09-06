# Minecraft 26.2+ 使用指南

| 参数 | 规格说明 |
| :--- | :--- |
| **目标 Minecraft 版本** | `26.2` (及向前兼容的 `26.x`) |
| **Fabric Loader 约束** | `>=0.18.4` |
| **Java 环境** | JDK 25 |
| **库版本** | `1.8.15` |
| **Mod ID** | `dasik-library` |
| **Mod 名称** | Dasik Library |
| **开源协议** | LGPL-3.0 |

> 📌 **代码仓库源码声明**：本 Wiki 中的文档反映的是**当前代码仓库的源码状态**，可能包含领先于 CurseForge 与 Modrinth 公开发布版本的最新未发布提交或开发中特性。

---

## 🛠️ 概述与安装指南

**Dasik Library** 是 *Vanilla Outsider*、*Instant Gratification* 与 *Delayed Gratification* 系列模组的核心前置依赖库。它提供了中央社交 AI 的 Tick 调度、动物遗传学、Boids 向量转向数学以及动态 GameRule 注册。

### 📥 玩家安装步骤
1. 为 Minecraft `26.2` 安装 **Fabric Loader**（`0.18.4` 或更高版本）。
2. 确保将 **Fabric API**（`0.152.1+26.2` 或更高版本）放入 `.minecraft/mods` 文件夹。
3. 下载 `dasik-library-1.8.15.jar` 并放入 `.minecraft/mods` 目录，与依赖它的下游模组（如 *Better Dogs*、*Natural Reproduction*）一同加载。

### 💻 模组开发者依赖配置

在 `fabric.mod.json` 中声明 **Dasik Library** 依赖：

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

在 `gradle.properties` 中：

```properties
dasik_library_version=1.8.15
```

在 `build.gradle` 中：

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_library_version}"
}
```

---

## ⚙️ 核心 26.2+ 架构变化

1. **Mojang Sovereign Mappings**：全面使用 Mojang 原生映射（`level`、`ServerLevel`、`EntityTypes`）。旧版 Yarn 映射方法（`world`、`getWorld`）已彻底废弃。
2. **Identifier API**：使用 `Identifier.fromNamespaceAndPath(namespace, path)` 或 `Identifier.parse(string)` 构建资源标识符。旧版 `Identifier.of()` 不受支持。
3. **开放式版本下界 (`>=26.1.2-`)**：确保单个编译生成的 JAR 文件在 Minecraft `26.2+` 的后续小版本中保持向前兼容，同时配合 `ModVersionGuard` 确保运行时类加载安全。

---

## 🔗 相关页面
* [[版本兼容性|zh_cn-Version-Compatibility]]
* [[ModVersionGuard 与启动安全|zh_cn-ModVersionGuard-and-Startup-Safety]]
* [[开发者环境配置与构建|zh_cn-Developer-Setup-and-Building]]
