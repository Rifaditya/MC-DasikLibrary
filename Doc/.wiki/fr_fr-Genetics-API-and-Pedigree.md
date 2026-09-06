# API Génétique & Système Généalogique

| Classe Façade | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **Attributs Pris en Charge** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **Profondeur d'Analyse Généalogique** | Jusqu'à 3 générations (parents, grands-parents) |
| **Plage de Risque de Consanguinité** | `0%` (Indépendants) à `100%` (Frères/sœurs purs ou même entité) |

---

## 🛠️ Façade `DasikAnimalGeneticsAPI`

`DasikAnimalGeneticsAPI` offre une suite de méthodes utilitaires statiques pour permettre aux mods consommateurs de manipuler la génétique sans interagir manuellement avec les NBT ou les composants bas niveau.

### Synthèse des Méthodes de l'API

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

## 🌳 Arbre Généalogique & Prédiction de Consanguinité

L'analyseur généalogique inspecte récursivement les UUIDs des ascendants pour déterminer le chevauchement génétique :

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

## 💻 Exemple de Code Développeur

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 Pages Liées
* [[Moteur de Génétique Animale|fr_fr-Animal-Genetics-Engine]]
* [[Modificateurs de Butin Génétique|fr_fr-Genetics-Loot-Modifiers]]
