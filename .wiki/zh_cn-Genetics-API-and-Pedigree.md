# 遗传学 API 与系谱系统

| 门面类 | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **支持属性** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **亲缘检查深度** | 向上可达 3 代（父母、祖父母） |
| **近亲繁殖风险范围** | `0%` (无亲缘) 至 `100%` (全同胞 / 自身) |

---

## 🛠️ `DasikAnimalGeneticsAPI` 门面

`DasikAnimalGeneticsAPI` 为下游模组提供高级静态方法，用于查询、更新、重置和评估实体遗传学，无需直接操作 NBT 标签或底层附加编解码器。

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

## 🌳 系谱与近亲繁殖风险预测

系谱分析器递归检查祖先中的亲本 UUID 匹配情况：

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

## 💻 开发者代码示例

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 相关页面
* [[动物遗传学引擎|zh_cn-Animal-Genetics-Engine]]
* [[遗传学战利品修饰符|zh_cn-Genetics-Loot-Modifiers]]
