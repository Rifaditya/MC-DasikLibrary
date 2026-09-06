# 確率・数学ユーティリティ

| ツールクラス | クラス | 役割 |
| :--- | :--- | :--- |
| **高速乱数生成** | `net.dasik.social.util.FastRandom` | 非ブロッキング 64-bit XORSHIFT 乱数器 |
| **確率サンプリング** | `net.dasik.social.util.StochasticUtil` | デシル（`1/10`）、パーセント（`1/100`）、パーミル（`1/1000`） |
| **時間変換定数** | $20\text{ ticks} = 1\text{秒}$ | Minecraft 内部 Tick 基準の変換 |

---

## 🎲 `FastRandom` の XORSHIFT アルゴリズム

`FastRandom` は、`java.util.Random` のアトミック同期オーバーヘッドを排除し、極めて高速かつ優れた統計的均一性を持つ乱数を提供します：

$$x \leftarrow x \oplus (x \ll 13); \quad x \leftarrow x \oplus (x \gg 7); \quad x \leftarrow x \oplus (x \ll 17)$$

---

## 📊 `StochasticUtil` による直感的なサンプリング

`StochasticUtil` は整数の丸め誤差を防止し、精密な確率判定を実現します：

```java
// Roll probability out of 1000 (e.g. 5 permille = 0.5% chance)
if (StochasticUtil.rollPermille(random, 5)) {
    // Rare mutation triggered
}

// Roll percentage chance (0-100)
if (StochasticUtil.rollPercent(random, 25)) {
    // 25% chance branch
}
```

---

## 🔗 関連ページ
* [[動物遺伝学エンジン|ja_jp-Animal-Genetics-Engine]]
* [[群知能ソーシャルシステム|ja_jp-Hive-Mind-Social-System]]
