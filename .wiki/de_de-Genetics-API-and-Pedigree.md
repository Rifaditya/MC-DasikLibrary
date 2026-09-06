# Genetik-API & Stammbaumsystem

| Fassadenklasse | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **Unterstützte Attribute** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **Prüftiefe der Verwandtschaft**| Bis zu 3 Generationen (Eltern, Großeltern) |
| **Inzuchtrisikobereich** | `0%` (Unverwandt) bis `100%` (Vollgeschwister / Selbe Entität) |

---

## 🛠️ `DasikAnimalGeneticsAPI` Fassade

`DasikAnimalGeneticsAPI` stellt statische Hilfsmethoden für Consumer-Mods bereit, um Genetik unkompliziert abzufragen, zu modifizieren oder zurückzusetzen, ohne direkt NBT-Tags manipulieren zu müssen.

### Übersicht der API-Methoden

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

## 🌳 Stammbaum & Inzuchtrisikovorhersage

Der Stammbaumanalysator prüft rekursiv Eltern-UUIDs auf Übereinstimmungen bei den Vorfahren:

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

## 💻 Entwickler-Codebeispiel

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 Verwandte Seiten
* [[Tiergenetik-Engine|de_de-Animal-Genetics-Engine]]
* [[Genetik-Beutemodifikatoren|de_de-Genetics-Loot-Modifiers]]
