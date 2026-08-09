# Genetics API & Pedigree System

| Facade Class | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **Supported Attributes** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **Kinship Check Depth** | Up to 3 generations (Parents, Grandparents) |
| **Inbreeding Risk Range** | `0%` (Unrelated) to `100%` (Full Siblings / Self) |

---

## 🛠️ `DasikAnimalGeneticsAPI` Facade

`DasikAnimalGeneticsAPI` provides high-level static methods for consumer mods to query, update, reset, and evaluate entity genetics without directly manipulating NBT tags or raw attachment codecs.

### API Method Summary

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

## 🌳 Pedigree & Inbreeding Risk Prediction

The pedigree analyzer recursively checks parent UUID matches across ancestors:

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

## 💻 Developer Code Example

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 Related Pages
* [[Animal Genetics Engine|Animal-Genetics-Engine]]
* [[Genetics Loot Modifiers|Genetics-Loot-Modifiers]]
