# API генетики и система родословной

| Класс фасада | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **Поддерживаемые атрибуты** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **Глубина анализа родства**| До 3 поколений (Родители, Бабушки/Дедушки) |
| **Диапазон риска инбридинга**| `0%` (Чужие) до `100%` (Родные братья/сестры или та же особь) |

---

## 🛠️ Фасад `DasikAnimalGeneticsAPI`

`DasikAnimalGeneticsAPI` предоставляет удобные статические методы для модов-потребителей, позволяя запрашивать, изменять, сбрасывать и оценивать генетику без ручных манипуляций с NBT-тегами.

### Обзор методов API

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

## 🌳 Родословная и прогнозирование риска инбридинга

Анализатор родословной рекурсивно сопоставляет UUID родителей предков:

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

## 💻 Пример кода для разработчиков

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 Связанные страницы
* [[Движок генетики животных|ru_ru-Animal-Genetics-Engine]]
* [[Модификаторы добычи генетики|ru_ru-Genetics-Loot-Modifiers]]
