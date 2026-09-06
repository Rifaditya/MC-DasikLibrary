# クライアント GameRule と GUI ヘルパー

| ヘルパークラス | 実行環境 | 役割 |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | クライアント／統合サーバー | クライアントスレッドから統合サーバーの GameRule を安全に照会 |
| `ConfigHelper` | Common（クライアント／サーバー両用） | JSON 設定ファイルのアトミック書き込み・自動バックアップ |
| `GuiHelper` | クライアント | ModMenu や Cloth Config 向けの画面構築補助 |

---

## 🖥️ 専用サーバーでのクラッシュ防止設計

専用サーバー（Dedicated Server）での動作時における ClassLoader 例外を防ぐため、すべての GUI 関連コンポーネントには遅延ロードおよび環境分岐チェック（`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`）が施されています。

```ascii
                      DynamicGameRuleManager.getInt(level, ruleKey)
                                         │
                    ┌───────────────────┴───────────────────┐
                    ▼                                       ▼
          [ level instanceof ServerLevel ]       [ Client Environment ]
                    │                                       │
                    ▼                                       ▼
            Direct Level Lookup                   ClientGameRuleHelper
                                             (Queries Integrated Server)
```

---

## 📄 `ConfigHelper` によるアトミック置換保護

`ConfigHelper` は、クラッシュや予期せぬ電源遮断による設定ファイルの破損を防ぎます：

1. 一時ファイル `config.json.tmp` にデータを書き込み。
2. JSON 構文とファイルサイズを検証。
3. `config.json.bak` に自動バックアップを作成。
4. `Files.move(..., StandardCopyOption.ATOMIC_MOVE)` によりアトミックにファイルを置換。

---

## 🔗 関連ページ
* [[動的 GameRules マネージャー|ja_jp-Dynamic-GameRules-Manager]]
* [[行動プロファイルと発動条件|ja_jp-Behavior-Profiles-and-Conditions]]
