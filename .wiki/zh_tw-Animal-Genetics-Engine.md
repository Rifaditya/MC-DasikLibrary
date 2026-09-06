# 動物遺傳學引擎

| 屬性 | 設定值 |
| :--- | :--- |
| **核心套件** | `net.dasik.social.api.genetics` |
| **附加組件標識符** | `dasik-library:genetics` |
| **體型縮放範圍** | `0.1x` 至 `3.0x` (預設: `0.5x` - `2.0x`) |
| **劣株閾值** | Scale $< 0.85x$ (或近親繁殖) |
| **巨型閾值** | Scale $> 1.15x$ |
| **數據編解碼器** | `EntityGenetics.CODEC` |

---

## 🧬 概述與 DNA 結構

**動物遺傳學引擎** 基於 Fabric 的 `AttachmentType` 構建了與實體類型無關的持久化遺傳系統。每個具備遺傳特性的實體都存儲一個 `EntityGenetics` 記錄：

1. **`parent1Uuid` (`Optional<UUID>`)**：繁殖所產生的第一親本 UUID。
2. **`parent2Uuid` (`Optional<UUID>`)**：繁殖所產生的第二親本 UUID。
3. **`inbred` (`boolean`)**：標記親本是否具有共同血統。
4. **`traitsRolled` (`boolean`)**：標記是否已完成初始隨機屬性抽取。
5. **`traits` (`Map<String, Float>`)**：動態特徵數值（`scale`、`max_health`、`attack_damage`、`movement_speed`）。

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

## 📐 繼承與變異數學

當兩個實體繁殖時，子代的遺傳數據由 `GeneticsEngine.calculateOffspringGenetics` 計算得出：

### 1. 體型繼承公式
子代基準縮放比例 $S_{\text{offspring}}$ 由親本均值加上三角高斯變異量 $\Delta_{\text{mutate}}$ 得出：

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

其中 $\Delta_{\text{mutate}}$ 從以 $\pm \text{mutationRate}$ 為邊界的三角分佈中採樣。

### 2. 近親繁殖懲罰公式
如果親本具有共同血統（$F > 0$），則會施加近親生命值/體型懲罰 $\text{Penalty}_{\text{inbreeding}}$：

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 開發者代碼範例

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 相關頁面
* [[遺傳學 API 與譜系|zh_tw-Genetics-API-and-Pedigree]]
* [[過期屬性清理與體型縮放|zh_tw-Stale-Attribute-Purging-and-Scale]]
