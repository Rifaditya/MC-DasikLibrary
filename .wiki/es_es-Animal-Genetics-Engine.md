# Motor de Genética Animal

| Propiedad | Valor |
| :--- | :--- |
| **Paquete Principal** | `net.dasik.social.api.genetics` |
| **Identificador de Adjunto** | `dasik-library:genetics` |
| **Rango de Escala** | `0.1x` a `3.0x` (Predeterminado: `0.5x` - `2.0x`) |
| **Umbral de Raquítico** | Escala $< 0.85x$ (o consanguíneo) |
| **Umbral de Gigante** | Escala $> 1.15x$ |
| **Codec de Datos** | `EntityGenetics.CODEC` |

---

## 🧬 Descripción General y Estructura del ADN

El **Motor de Genética Animal** proporciona un sistema de herencia persistente e independiente del tipo de entidad, impulsado por `AttachmentType` de Fabric. Cada criatura almacena un registro `EntityGenetics`:

1. **`parent1Uuid` (`Optional<UUID>`)**: UUID del primer progenitor si nació por crianza.
2. **`parent2Uuid` (`Optional<UUID>`)**: UUID del segundo progenitor si nació por crianza.
3. **`inbred` (`boolean`)**: Indica si los progenitores compartían linaje común.
4. **`traitsRolled` (`boolean`)**: Indica si se generaron las estadísticas iniciales al azar.
5. **`traits` (`Map<String, Float>`)**: Valores dinámicos de rasgos (`scale`, `max_health`, `attack_damage`, `movement_speed`).

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

## 📐 Matemáticas de Herencia y Mutación

Cuando dos entidades se reproducen, la genética de la cría se calcula mediante `GeneticsEngine.calculateOffspringGenetics`:

### 1. Fórmula de Herencia de Escala
La escala base de la cría $S_{\text{offspring}}$ se obtiene del promedio de los progenitores más una desviación gaussiana triangular de mutación $\Delta_{\text{mutate}}$:

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

Donde $\Delta_{\text{mutate}}$ se muestrea de una distribución triangular en el rango $\pm \text{mutationRate}$.

### 2. Fórmula de Penalización por Endogamia
Si los progenitores comparten linaje ($F > 0$), se aplica una penalización de salud y escala $\text{Penalty}_{\text{inbreeding}}$:

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 Ejemplo de Código para Desarrolladores

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 Páginas Relacionadas
* [[API de Genética y Pedigrí|es_es-Genetics-API-and-Pedigree]]
* [[Purga de Atributos Obsoletos y Escala|es_es-Stale-Attribute-Purging-and-Scale]]
