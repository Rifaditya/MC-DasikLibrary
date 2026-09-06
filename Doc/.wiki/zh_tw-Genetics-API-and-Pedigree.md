# 遺傳學 API 與系譜系統

| 門面類別 | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **支援屬性** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **親緣檢查深度** | 向上可達 3 代（父母、祖父母） |
| **近親繁殖風險範圍** | `0%` (無親緣) 至 `100%` (全同胞 / 自身) |

---

## 🛠️ `DasikAnimalGeneticsAPI` 門面

`DasikAnimalGeneticsAPI` 為下游模組提供高級靜態方法，用於查詢、更新、重置和評估實體遺傳學，無需直接操作 NBT 標籤或底層附加編解碼器。

### API 方法摘要

```java
public final class DasikAnimalGeneticsAPI {
    // Size & Scale
    public static float getScale(LivingEntity entity);
    public static void setScale(LivingEntity entity, float scale);
    public static boolean isRunt(LivingEntity entity);
    public static boolean isGiant(LivingEntity entity);

    // Kinship & Pedigree
    public static boolean isRelated(LivingEntity entity1, LivingEntity entity2);
    public static int predictInbreedingRiskPercent(LivingEntity parent1, LivingEntity parent2);

    // Trait Modifiers
    public static void setTrait(LivingEntity entity, String traitKey, float value);
    public static void modifyTrait(LivingEntity entity, String traitKey, float delta);
    public static void resetGenetics(LivingEntity entity);
}
```

---

## 🌳 系譜與近親繁殖風險預測

系譜分析器遞迴檢查祖先中的親本 UUID 匹配情況：

$$\text{Inbreeding Risk (\%)} = \begin{cases}
100\%, & \text{if Parent1 UUID} = \text{Parent2 UUID} \\
50\%, & \text{if Full Siblings (same Parent1 AND Parent2)} \\
25\%, & \text{if Half Siblings (sharing 1 Parent)} \\
12.5\%, & \text{if Cousin overlap (sharing Grandparents)} \\
0\%, & \text{if No pedigree overlap detected}
\end{cases}$$

```ascii
[ Parent 1 ]             [ Parent 2 ]
     │                        │
     ├───────────┬────────────┤
     ▼           ▼            ▼
[ Offspring1 ] [ Offspring2 ] 
     │               │
     └─── BREEDING ──┘
            │
            ▼
    Inbreeding Risk = 50% (Full Siblings)
```

---

## 💻 開發者代碼範例

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 相關頁面
* [[動物遺傳學引擎|zh_tw-Animal-Genetics-Engine]]
* [[遺傳學戰利品修飾符|zh_tw-Genetics-Loot-Modifiers]]
