# 动态游戏规则管理器

| 组件 | 类 |
| :--- | :--- |
| **管理器类** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **键命名空间格式** | `modid:rule_name` |
| **翻译注入** | `LanguageMixin` -> `GENERATED_TRANSLATIONS` Map |
| **分类标题格式** | `§lCategory Title (N rules)` |

---

## 🎲 概述与带命名空间的 GameRules

`DynamicGameRuleManager` 允许下游模组以编程方式动态注册无限数量的 GameRules，无需在 `en_us.json` 语言文件中手动添加条目。

### 核心特性
1. **自动翻译注入**：自动将 `modid:rule_name` 转换为可读的标题（例如 `bd_enable_guard_mode` -> `BD Enable Guard Mode`），并通过 `LanguageMixin` 注入客户端语言表。
2. **分类标题加粗 (`§l`)**：通过 `registerCategory` 注册的分类条目会自动注入加粗格式（`§l`），使 GameRule 界面清晰分组。
3. **数学辅助转换器**：
   - `getPct(Level level, GameRule<Integer> rule)` — 将整数规则（`0-100`）转换为 double（`/ 100.0`）。
   - `getChance(Level level, GameRule<Integer> rule)` — 将百分比整数规则转换为 float（`/ 100.0f`）。
   - `getProb(Level level, GameRule<Integer> rule)` — 将千分比整数规则（`0-1000`）转换为 float（`/ 1000.0f`）。
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — 将十分位整数规则转换为 float（`/ 10.0f`）。
   - `getIntVal(Level level, String key, int defaultValue)` — 通过字符串键查询整数 GameRule 数值。

---

## 💻 开发者代码示例

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

## 🔗 相关页面
* [[游戏规则 Codec 与序列化|zh_cn-GameRule-Codec-and-Serialization]]
* [[客户端游戏规则与 GUI 助手|zh_cn-Client-GameRule-and-GUI-Helpers]]
