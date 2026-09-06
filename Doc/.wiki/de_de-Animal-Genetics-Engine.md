# Tiergenetik-Engine

| Eigenschaft | Wert |
| :--- | :--- |
| **Hauptpaket** | `net.dasik.social.api.genetics` |
| **Attachment-ID** | `dasik-library:genetics` |
| **Skalierungsbereich** | `0.1x` bis `3.0x` (Standard: `0.5x` - `2.0x`) |
| **Kümmerling-Schwelle (Runt)**| Skalierung $< 0.85x$ (oder Inzucht) |
| **Riesen-Schwelle (Giant)** | Skalierung $> 1.15x$ |
| **Daten-Codec** | `EntityGenetics.CODEC` |

---

## 🧬 Übersicht & DNA-Struktur

Die **Tiergenetik-Engine** bietet ein entitätsunabhängiges, persistentes Genetiksystem auf Basis des Fabric `AttachmentType`. Jede Kreatur speichert einen `EntityGenetics`-Datensatz:

1. **`parent1Uuid` (`Optional<UUID>`)**: UUID des ersten Elternteils bei Fortpflanzung.
2. **`parent2Uuid` (`Optional<UUID>`)**: UUID des zweiten Elternteils bei Fortpflanzung.
3. **`inbred` (`boolean`)**: Kennzeichnet verwandtschaftliche Abstammung der Eltern.
4. **`traitsRolled` (`boolean`)**: Kennzeichnet den Abschluss der anfänglichen Zufallsauslosung.
5. **`traits` (`Map<String, Float>`)**: Dynamische Eigenschaftswerte (`scale`, `max_health`, `attack_damage`, `movement_speed`).

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

## 📐 Vererbungs- & Mutationsmathematik

Wenn zwei Kreaturen züchten, wird die Genetik der Nachkommen über `GeneticsEngine.calculateOffspringGenetics` berechnet:

### 1. Skalierungs-Vererbungsformel
Die Basisskalierung der Nachkommen $S_{\text{offspring}}$ ergibt sich aus dem Mittelwert der Eltern zuzüglich einer dreieckigen Gaußschen Mutationsabweichung $\Delta_{\text{mutate}}$:

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

Wobei $\Delta_{\text{mutate}}$ aus einer Dreiecksverteilung im Bereich $\pm \text{mutationRate}$ gezogen wird.

### 2. Inzucht-Strafabzugsformel
Besteht eine Verwandtschaft ($F > 0$), wird ein Inzuchtabzug auf Gesundheit und Größe $\text{Penalty}_{\text{inbreeding}}$ wirksam:

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 Entwickler-Codebeispiel

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 Verwandte Seiten
* [[Genetik-API & Stammbaum|de_de-Genetics-API-and-Pedigree]]
* [[Veraltete Attributsbereinigung & Skalierung|de_de-Stale-Attribute-Purging-and-Scale]]
