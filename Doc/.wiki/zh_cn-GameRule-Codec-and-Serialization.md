# 游戏规则 Codec 与序列化

| 主题 | 规格说明 |
| :--- | :--- |
| **序列化失败问题** | 世界保存期间 `SavedDataStorage.encodeUnchecked` 崩溃 |
| **修复版本** | `1.8.15` |
| **整数边界** | `min = Integer.MIN_VALUE` (或 `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ 整数 GameRule Codec 修复

在 Minecraft 26.2 中，动态 GameRules 通过 `SavedDataStorage.encodeUnchecked` 被序列化写入 `level.dat` / `game_rules.dat`。在库的早期版本中，注册整数 GameRule 若未明确指定下界，GameRule 编解码器会默认强制严格的正数范围（`min = 0`），在序列化负整数值或特定默认值时会抛出 `IllegalStateException` 崩溃。

### `DynamicGameRuleManager` 中的修复实现

```java
// IntegerBuilder initialization fix in DynamicGameRuleManager
public class IntegerBuilder {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public GameRule<Integer> register() {
        int effectiveMin = Math.min(min, defaultValue);
        int effectiveMax = Math.max(max, defaultValue);
        // Uses Codec.INT.intRange(effectiveMin, effectiveMax) to prevent encodeUnchecked bounds check failure
        GameRule<Integer> rule = new GameRule<>(
            GameRuleType.INT, defaultValue, Codec.INT.intRange(effectiveMin, effectiveMax), 
            FeatureFlagSet.of(), category, visitor
        );
        return Registry.register(BuiltInRegistries.GAME_RULE, Identifier.parse(ruleName), rule);
    }
}
```

---

## 🔒 NBT 命名空间安全性

请务必使用 `modid:rule_name` 格式为动态 GameRules 添加命名空间（`[ERR-20260510-002]`）。未添加命名空间的动态 GameRules 无法干净地序列化到原生世界的存档数据中，会导致服务器重启时数据静默丢失。

---

## 🔗 相关页面
* [[动态游戏规则管理器|zh_cn-Dynamic-GameRules-Manager]]
* [[客户端游戏规则与 GUI 助手|zh_cn-Client-GameRule-and-GUI-Helpers]]
