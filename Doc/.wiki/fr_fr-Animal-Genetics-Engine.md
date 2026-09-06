# Moteur de Génétique Animale

| Caractéristique | Valeur |
| :--- | :--- |
| **Paquet Principal** | `net.dasik.social.api.genetics` |
| **ID d'Attachement** | `dasik-library:genetics` |
| **Plage de Taille** | `0.1x` à `3.0x` (Standard : `0.5x` - `2.0x`) |
| **Seuil Chétif (Runt)** | Taille $< 0.85x$ (ou consanguinité avérée) |
| **Seuil Géant (Giant)** | Taille $> 1.15x$ |
| **Codec de Données** | `EntityGenetics.CODEC` |

---

## 🧬 Présentation & Structure ADN

Le **Moteur de Génétique Animale** offre un système génétique universel et persistant via l'`AttachmentType` de Fabric. Chaque créature stocke un enregistrement `EntityGenetics` :

1. **`parent1Uuid` (`Optional<UUID>`)** : UUID du premier parent lors de la reproduction.
2. **`parent2Uuid` (`Optional<UUID>`)** : UUID du second parent lors de la reproduction.
3. **`inbred` (`boolean`)** : Indique si la créature est issue d'une reproduction consanguine.
4. **`traitsRolled` (`boolean`)** : Indique si le tirage aléatoire initial des traits a été effectué.
5. **`traits` (`Map<String, Float>`)** : Attributs dynamiques (`scale`, `max_health`, `attack_damage`, `movement_speed`).

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

## 📐 Mathématiques d'Hérédité & de Mutation

Lors de l'accouplement de deux créatures, la génétique de la progéniture est calculée par `GeneticsEngine.calculateOffspringGenetics` :

### 1. Formule d'Hérédité de la Taille
La taille de base du nouveau-né $S_{\text{offspring}}$ correspond à la moyenne des parents ajustée d'un écart de mutation triangulaire $\Delta_{\text{mutate}}$ :

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

Où $\Delta_{\text{mutate}}$ est tiré selon une loi triangulaire sur l'intervalle $\pm \text{mutationRate}$.

### 2. Formule de Pénalité de Consanguinité
En cas de consanguinité ($F > 0$), une pénalité sur la santé et la taille $\text{Penalty}_{\text{inbreeding}}$ est appliquée :

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 Exemple de Code Développeur

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 Pages Liées
* [[API Génétique et Arbre Généalogique|fr_fr-Genetics-API-and-Pedigree]]
* [[Nettoyage d'Attributs Obsolètes et Échelle|fr_fr-Stale-Attribute-Purging-and-Scale]]
