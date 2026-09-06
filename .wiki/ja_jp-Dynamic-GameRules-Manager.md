# 動的 GameRules マネージャー

| コンポーネント | クラス |
| :--- | :--- |
| **マネージャークラス** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **名前空間キー形式** | `modid:rule_name` |
| **翻訳自動注入** | `LanguageMixin` -> Map `GENERATED_TRANSLATIONS` |
| **太字カテゴリヘッダー** | `§lCategory Title (N rules)` |

---

## 🎲 概要と名前空間付き GameRules

`DynamicGameRuleManager` を利用することで、Mod は `en_us.json` などの言語ファイルを個別に記述することなく、プログラムコードから直接無制限に動的な GameRule を登録できます。

### 主な機能
1. **翻訳の自動注入**: `modid:rule_name` の識別子を自動的に判読可能な英文字列（例: `bd_enable_guard_mode` -> `BD Enable Guard Mode`）に整形し、`LanguageMixin` 経由で言語マップに注入します。
2. **太字カテゴリヘッダー（`§l`）**: `registerCategory` で作成されたカテゴリ名は、設定画面で視認しやすいよう太字スタイルが自動付与されます。
3. **便利な数値変換ヘルパー**:
   - `getPct(Level level, GameRule<Integer> rule)` — 整数値（`0-100`）を Double（`/ 100.0`）に変換。
   - `getChance(Level level, GameRule<Integer> rule)` — パーセント値を Float（`/ 100.0f`）に変換。
   - `getProb(Level level, GameRule<Integer> rule)` — パーミル値（`0-1000`）を Float（`/ 1000.0f`）に変換。
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — デシル値を Float（`/ 10.0f`）に変換。
   - `getIntVal(Level level, String key, int defaultValue)` — 文字列キーから直接整数値を取得。

---

## 💻 開発者向けコード例

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

## 🔗 関連ページ
* [[GameRule Codec とシリアライズ|ja_jp-GameRule-Codec-and-Serialization]]
* [[クライアント GameRule と GUI ヘルパー|ja_jp-Client-GameRule-and-GUI-Helpers]]
