# GameRule Codec とシリアライズ

| 課題項目 | 詳細仕様 |
| :--- | :--- |
| **シリアライズ不具合** | ワールド保存時 `SavedDataStorage.encodeUnchecked` で発生したクラッシュ |
| **修正バージョン** | `1.8.15` |
| **有効な整数範囲** | `min = Integer.MIN_VALUE`（または `Math.min(Integer.MIN_VALUE, defaultValue)`） |

---

## 🛠️ 整数 GameRule Codec の修正背景

Minecraft 26.2 において、動的 GameRule は `SavedDataStorage.encodeUnchecked` を経由して `level.dat` や `game_rules.dat` にシリアライズされます。初期の実装では、境界値を明示しない整数ルールの最小値が自動的に `min = 0` に制限されていたため、負の値や初期値の書き込み時に `IllegalStateException` が発生していました。

### `DynamicGameRuleManager` における修正コード

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

## 🔒 名前空間の整合性保護

動的ルールには必ず `modid:rule_name` 形式の名前空間を付与してください（`[ERR-20260510-002]`）。名前空間のないキーは保存時に正しく識別できず、サーバー再起動時に設定が初期化される恐れがあります。

---

## 🔗 関連ページ
* [[動的 GameRules マネージャー|ja_jp-Dynamic-GameRules-Manager]]
* [[クライアント GameRule と GUI ヘルパー|ja_jp-Client-GameRule-and-GUI-Helpers]]
