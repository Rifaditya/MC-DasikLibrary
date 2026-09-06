# 下游 Mod 接入集成指南

| 下游 Mod | 整合亮點 |
| :--- | :--- |
| **Better Dogs** | 狼群動態、戰術號角指令、劣株/巨型遺傳縮放 |
| **Natural Reproduction** | 親緣系譜檢查、近親繁殖風險預測、動物體型屬性 |
| **Collapsible Game Rule Screen** | 分類標題格式化（`§l`）、動態 GameRule 註冊 |
| **Bat Ecology** | 空中群聚旋轉策略（`FlockType.AERIAL`） |
| **Ore Amplifier** | 隨機生成縮放（`StochasticUtil`） |

---

## 🛠️ 逐步接入清單

1. **添加依賴**：在 `fabric.mod.json` 中宣告 `"dasik-library": "*"`。
2. **實現介面**：在目標生物實體上實現 `SocialEntity` / `GroupMember`。
3. **註冊動態遊戲規則**：在模組初始化時調用 `DynamicGameRuleManager.registerBoolean` / `registerInt`。
4. **調用遺傳學門面**：透過 `DasikAnimalGeneticsAPI` 查詢體型屬性與系譜。

---

## 🔗 相關頁面
* [[開發者環境配置與構建|zh_tw-Developer-Setup-and-Building]]
* [[架構與套件結構設計|zh_tw-Architecture-and-Package-Layout]]
