# 動態遊戲規則管理器

| 組件 | 類別 |
| :--- | :--- |
| **管理器類別** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **鍵命名空間格式** | `modid:rule_name` |
| **翻譯注入** | `LanguageMixin` -> `GENERATED_TRANSLATIONS` Map |
| **分類標題格式** | `§lCategory Title (N rules)` |

---

## 🎲 概述與帶命名空間的 GameRules

`DynamicGameRuleManager` 允許下游模組以編程方式動態註冊無限數量的 GameRules，無需在 `en_us.json` 語言文件中手動添加條目。

### 核心特性
1. **自動翻譯注入**：自動將 `modid:rule_name` 轉換為可讀的標題（例如 `bd_enable_guard_mode` -> `BD Enable Guard Mode`），並透過 `LanguageMixin` 注入客戶端語言表。
2. **分類標題加粗 (`§l`)**：透過 `registerCategory` 註冊的分類條目會自動注入加粗格式（`§l`），使 GameRule 介面清晰分組。
3. **數學輔助轉換器**：
   - `getPct(Level level, GameRule<Integer> rule)` — 將整數規則（`0-100`）轉換為 double（`/ 100.0`）。
   - `getChance(Level level, GameRule<Integer> rule)` — 將百分比整數規則轉換為 float（`/ 100.0f`）。
   - `getProb(Level level, GameRule<Integer> rule)` — 將千分比整數規則（`0-1000`）轉換為 float（`/ 1000.0f`）。
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — 將十分位整數規則轉換為 float（`/ 10.0f`）。
   - `getIntVal(Level level, String key, int defaultValue)` — 透過字串鍵查詢整數 GameRule 數值。

---

## 💻 開發者代碼範例

```java
// Register a dynamic boolean GameRule with description
GameRule<Boolean> ENABLE_GUARD = DynamicGameRuleManager.booleanRule(
    "betterdogs:bd_enable_guard_mode",
    GameRuleCategory.MOBS,
    true
).description("Enable wolf sentinel guard mode").register();

// Querying GameRule safely across client/server
boolean isGuardEnabled = DynamicGameRuleManager.getBoolean(level, ENABLE_GUARD);
```

---

## 🔗 相關頁面
* [[遊戲規則 Codec 與序列化|zh_tw-GameRule-Codec-and-Serialization]]
* [[客戶端遊戲規則與 GUI 助手|zh_tw-Client-GameRule-and-GUI-Helpers]]
