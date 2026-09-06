# 動物遺伝学エンジン

| 項目 | 設定値 |
| :--- | :--- |
| **主パッケージ** | `net.dasik.social.api.genetics` |
| **アタッチメント ID** | `dasik-library:genetics` |
| **スケール設定範囲** | `0.1x` 〜 `3.0x`（標準: `0.5x` 〜 `2.0x`） |
| **未熟個体（Runt）しきい値** | スケール $< 0.85x$（または近親交配） |
| **巨大個体（Giant）しきい値** | スケール $> 1.15x$ |
| **データ Codec** | `EntityGenetics.CODEC` |

---

## 🧬 概要と DNA データ構造

**動物遺伝学エンジン** は、Fabric の `AttachmentType` を基盤とするエンティティ非依存の永続的遺伝システムです。各モブは `EntityGenetics` レコードを保持します：

1. **`parent1Uuid` (`Optional<UUID>`)**: 繁殖時の親 1 の UUID。
2. **`parent2Uuid` (`Optional<UUID>`)**: 繁殖時の親 2 の UUID。
3. **`inbred` (`boolean`)**: 近親交配によって誕生した個体であるかのフラグ。
4. **`traitsRolled` (`boolean`)**: 初期特性のランダム抽選が完了しているかのフラグ。
5. **`traits` (`Map<String, Float>`)**: 動的な能力値（`scale`, `max_health`, `attack_damage`, `movement_speed`）。

```ascii
 ┌────────────────────────────────────────────────────────────────────────┐
 │                           EntityGenetics                               │
 ├───────────────────────────────────┬────────────────────────────────────┤
 │ parent1Uuid: Optional<UUID>       │ inbred: boolean                    │
 │ parent2Uuid: Optional<UUID>       │ traitsRolled: boolean              │
 │ traits: Map<String, Float>        │ Codec: EntityGenetics.CODEC        │
 └───────────────────────────────────┴────────────────────────────────────┘
```

---

## 📐 遺伝と突然変異の計算式

2 体のモブが繁殖する際、子の遺伝情報は `GeneticsEngine.calculateOffspringGenetics` によって算出されます：

### 1. サイズスケーリングの継承計算式
子の基準スケール $S_{\text{offspring}}$ は、両親の平均値に三角分布に基づく変異量 $\Delta_{\text{mutate}}$ を加算した値となります：

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

ここで $\Delta_{\text{mutate}}$ は $\pm \text{mutationRate}$ の範囲で三角分布から抽出されます。

### 2. 近親交配ペナルティの計算式
血統の重複（$F > 0$）が検出された場合、体力や体格に近親交配ペナルティ $\text{Penalty}_{\text{inbreeding}}$ が課されます：

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 開発者向けコード例

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 関連ページ
* [[遺伝学 API と血統ツリー|ja_jp-Genetics-API-and-Pedigree]]
* [[古い属性のパージとスケール補正|ja_jp-Stale-Attribute-Purging-and-Scale]]
