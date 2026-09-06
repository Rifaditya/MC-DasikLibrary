# 遊戲規則 Codec 與序列化

| 主題 | 規格說明 |
| :--- | :--- |
| **序列化失敗問題** | 世界保存期間 `SavedDataStorage.encodeUnchecked` 崩潰 |
| **修復版本** | `1.8.15` |
| **整數邊界** | `min = Integer.MIN_VALUE` (或 `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ 整數 GameRule Codec 修復

在 Minecraft 26.2 中，動態 GameRules 透過 `SavedDataStorage.encodeUnchecked` 被序列化寫入 `level.dat` / `game_rules.dat`。在庫的早期版本中，註冊整數 GameRule 若未明確指定下界，GameRule 編解碼器會預設強制嚴格的正數範圍（`min = 0`），在序列化負整數值或特定預設值時會拋出 `IllegalStateException` 崩潰。

### `DynamicGameRuleManager` 中的修復實現

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

## 🔒 NBT 命名空間安全性

請務必使用 `modid:rule_name` 格式為動態 GameRules 添加命名空間（`[ERR-20260510-002]`）。未添加命名空間的動態 GameRules 無法乾淨地序列化到原生世界的存檔數據中，會導致伺服器重啟時數據靜默丟失。

---

## 🔗 相關頁面
* [[動態遊戲規則管理器|zh_tw-Dynamic-GameRules-Manager]]
* [[客戶端遊戲規則與 GUI 助手|zh_tw-Client-GameRule-and-GUI-Helpers]]
