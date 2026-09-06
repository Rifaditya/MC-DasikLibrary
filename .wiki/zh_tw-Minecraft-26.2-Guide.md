# Minecraft 26.2+ 使用指南

| 參數 | 規格說明 |
| :--- | :--- |
| **目標 Minecraft 版本** | `26.2` (及向前相容的 `26.x`) |
| **Fabric Loader 約束** | `>=0.18.4` |
| **Java 環境** | JDK 25 |
| **庫版本** | `1.8.15` |
| **Mod ID** | `dasik-library` |
| **Mod 名稱** | Dasik Library |
| **開源授權** | LGPL-3.0 |

> 📌 **代碼倉庫原始碼聲明**：本 Wiki 中的文檔反映的是**當前代碼倉庫的原始碼狀態**，可能包含領先於 CurseForge 與 Modrinth 公開發布版本的最新未發布提交或開發中特性。

---

## 🛠️ 概述與安裝指南

**Dasik Library** 是 *Vanilla Outsider*、*Instant Gratification* 與 *Delayed Gratification* 系列模組的核心前置依賴庫。它提供了中央社交 AI 的 Tick 調度、動物遺傳學、Boids 向量轉向數學以及動態 GameRule 註冊。

### 📥 玩家安裝步驟
1. 為 Minecraft `26.2` 安裝 **Fabric Loader**（`0.18.4` 或更高版本）。
2. 確保將 **Fabric API**（`0.152.1+26.2` 或更高版本）放入 `.minecraft/mods` 資料夾。
3. 下載 `dasik-library-1.8.15.jar` 並放入 `.minecraft/mods` 目錄，與依賴它的下游模組（如 *Better Dogs*、*Natural Reproduction*）一同加載。

### 💻 模組開發者依賴配置

在 `fabric.mod.json` 中宣告 **Dasik Library** 依賴：

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

## ⚙️ 核心 26.2+ 架構變化

1. **Mojang Sovereign Mappings**：全面使用 Mojang 原生映射（`level`、`ServerLevel`、`EntityTypes`）。舊版 Yarn 映射方法（`world`、`getWorld`）已徹底廢棄。
2. **Identifier API**：使用 `Identifier.fromNamespaceAndPath(namespace, path)` 或 `Identifier.parse(string)` 構建資源標識符。舊版 `Identifier.of()` 不受支援。
3. **開放式版本下界 (`>=26.1.2-`)**：確保單個編譯生成的 JAR 檔案在 Minecraft `26.2+` 的後續小版本中保持向前相容，同時配合 `ModVersionGuard` 確保運行時類別加載安全。

---

## 🔗 相關頁面
* [[版本相容性|zh_tw-Version-Compatibility]]
* [[ModVersionGuard 與啟動安全|zh_tw-ModVersionGuard-and-Startup-Safety]]
* [[開發者環境配置與構建|zh_tw-Developer-Setup-and-Building]]
