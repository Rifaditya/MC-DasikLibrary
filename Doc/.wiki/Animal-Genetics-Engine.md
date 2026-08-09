# Animal Genetics Engine

| Property | Value |
| :--- | :--- |
| **Core Package** | `net.dasik.social.api.genetics` |
| **Attachment Identifier** | `dasik-library:genetics` |
| **Scale Bounds** | `0.1x` to `3.0x` (Default: `0.5x` - `2.0x`) |
| **Runt Threshold** | Scale $< 0.75x$ |
| **Giant Threshold** | Scale $> 1.45x$ |
| **Data Codec** | `EntityGenetics.CODEC` |

---

## 🧬 Overview & DNA Structure

The **Animal Genetics Engine** provides an entity-agnostic persistent genetics system backed by Fabric's `AttachmentType`. Every genetic entity stores:

1. **DNA Signature (`long`)**: Bit-masked 64-bit integer encoding physical traits, scale modifiers, and gene dominance.
2. **Pedigree Metadata**: Parent 1 UUID, Parent 2 UUID, and generation depth counter.
3. **Inbreeding Coefficient ($F$)**: Percentage value derived from pedigree overlaps.
4. **Dynamic Trait Modifiers**: Map of attribute keys to numerical modifiers (e.g. `max_health`, `movement_speed`).

```ascii
 ┌─────────────────────────────────────────────────────────────┐
 │                      EntityGenetics                         │
 ├──────────────────────────────┬──────────────────────────────┤
 │ DNA Signature: 64-bit Long   │ Inbreeding Coefficient: float│
 │ Parent 1 UUID: UUID          │ Generation Depth: int        │
 │ Parent 2 UUID: UUID          │ Dynamic Traits: Map<Str,Flt> │
 └──────────────────────────────┴──────────────────────────────┘
```

---

## 📐 Inheritance & Mutation Math

When two entities breed, offspring genetics are calculated via `GeneticsEngine.calculateOffspringGenetics`:

### 1. Scale Inheritance Formula
The offspring's baseline scale $S_{\text{offspring}}$ is derived from the mid-parent average plus a triangular Gaussian mutation delta $\Delta_{\text{mutate}}$:

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

Where $\Delta_{\text{mutate}}$ is sampled from a triangular distribution bounded by $\pm \text{mutationRate}$.

### 2. Inbreeding Penalty Formula
If parents share lineage ($F > 0$), an inbreeding health/scale penalty $\text{Penalty}_{\text{inbreeding}}$ is applied:

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 Developer Code Example

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 Related Pages
* [[Genetics API & Pedigree|Genetics-API-and-Pedigree]]
* [[Stale Attribute Purging & Scale|Stale-Attribute-Purging-and-Scale]]
