# Mixin リファレンスとフック

| Mixin クラス | 対象クラス | 対象メソッド | 機能説明 |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `getOrDefault` | 動的 GameRule 翻訳の自動注入 |
| `LivingEntityLootMixin` | `LivingEntity` | `dropFromLootTable` | 遺伝学に基づくドロップアイテムの改変 |
| `MobGoalAccessor` | `Mob` | GoalSelector アクセサ | 内部 AI Goal セレクタへのアクセス許可 |
| `PathfinderMobMixin` | `PathfinderMob` | `tick` | ソーシャルパルスサイクルの開始トリガー |
| `ProfileTriggerMixin` | `LivingEntity` | イベント検知メソッド | 行動プロファイルの切り替え判定 |

---

## 🔗 関連ページ
* [[アーキテクチャとパッケージ構成|ja_jp-Architecture-and-Package-Layout]]
* [[遺伝学ドロップ修飾子|ja_jp-Genetics-Loot-Modifiers]]
