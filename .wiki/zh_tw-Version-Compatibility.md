# 版本相容性矩陣

| 目標 Minecraft 版本 | 庫版本 | Mod Version Guard | 依賴範圍 | 支援狀態 |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **主線活躍支援** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | 向後移植 / 穩定 |
| **Minecraft 1.21.x** | *已過時* | *N/A* | *Legacy 1.x* | **生命週期結束 (EOL)** |

> 📌 **代碼倉庫原始碼聲明**：本 Wiki 中的文檔反映的是**當前代碼倉庫的原始碼狀態**，可能包含領先於 CurseForge 與 Modrinth 公開發布版本的最新未發布提交或開發中特性。

---

## 🛡️ 1 Jar 1 Version 與向前相容策略

Dasik Library 推行 **1 Jar 1 Version 政策**，並結合**開放式向前相容性**：

1. **開放式版本下界 (`"minecraft": ">=26.1.2-"`)**：在 `fabric.mod.json` 中指定開放式下界，允許 Fabric Loader 在後續補丁更新中加載該 JAR，避免因硬編碼版本號將玩家拒之門外。
2. **Knot ClassLoader 安全檢查 (`ModVersionGuard`)**：在 `onInitialize()` 階段，`ModVersionGuard.checkClass` 透過 `Thread.currentThread().getContextClassLoader()` 主動驗證類別是否存在，以便在 API 發生重大變化時第一時間拋出友好的錯誤橫幅，避免 JVM 靜默崩潰。

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

## 🚫 版本標識守衛（禁止混淆歷史版本）

根據嚴格的設計規範（`[DIR-20260614-001]`），**Minecraft 26.x 年度版本絕不能與 2024 年的舊版 1.21.x 混為一談**：
* ❌ `26.2 (1.21.4)` — 嚴格禁止。
* ✅ `Minecraft 26.2` — 標準主權年度版本記法。

---

## 🔗 相關頁面
* [[MC 26.2 指南|zh_tw-Minecraft-26.2-Guide]]
* [[ModVersionGuard 與啟動安全|zh_tw-ModVersionGuard-and-Startup-Safety]]
