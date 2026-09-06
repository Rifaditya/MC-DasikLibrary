# 動的エンチャント＆視界トラッカー

| コンポーネント | クラス |
| :--- | :--- |
| **動的エンチャント管理** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **プレイヤー視界追跡** | `net.dasik.social.util.PlayerVisionTracker` |
| **エンチャント識別子** | 動的 `Identifier` |

---

## ✨ 概要と動的エンチャント登録

`DynamicEnchantmentManager` により、起動時に大量の JSON データパックを記述することなく、Java コードから直接エンチャントを登録・管理できます。

```java
// Registering dynamic enchantment
DynamicEnchantmentManager.registerEnchantment(
    Identifier.parse("betterdogs:loyal_bond"),
    new DynamicEnchantmentConfig(...)
);
```

---

## 👁️ 視界判定ユーティリティ（`PlayerVisionTracker`）

`PlayerVisionTracker` は、プレイヤーがエンティティを直接目視しているかを高速に判定するフラスタム・レイキャスト計算を提供します：

1. **視野角コーンテスト**: プレイヤーの視線ベクトルとターゲット方向の内積を計算し、視野範囲内に収まっているかを判定。
2. **遮蔽物レイキャスト判定**: 視線間に不透過ブロックが存在しないかを検証。

---

## 🔗 関連ページ
* [[確率・数学ユーティリティ|ja_jp-Stochastic-and-Math-Utilities]]
* [[アーキテクチャとパッケージ構成|ja_jp-Architecture-and-Package-Layout]]
