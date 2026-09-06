# API Genetika & Sistem Silsilah

| Kelas Fasad | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **Atribut yang Didukung** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **Kedalaman Analisis Silsilah** | Hingga 3 generasi (induk, kakek-nenek) |
| **Rentang Risiko Inbreeding** | `0%` (Tanpa hubungan) hingga `100%` (Saudara kandung / Entitas sama) |

---

## 🛠️ Fasad `DasikAnimalGeneticsAPI`

`DasikAnimalGeneticsAPI` menyediakan sekumpulan metode pembantu statis bagi mod konsumen untuk memodifikasi genetika tanpa perlu menyentuh NBT tingkat rendah.

### Rangkuman Metode API

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

## 🌳 Pohon Silsilah & Prediksi Risiko Inbreeding

Penganalisis silsilah memeriksa UUID leluhur secara rekursif:

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

## 💻 Contoh Kode Pengembang

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 Halaman Terkait
* [[Mesin Genetika Hewan|id_id-Animal-Genetics-Engine]]
* [[Modifikasi Loot Genetika|id_id-Genetics-Loot-Modifiers]]
