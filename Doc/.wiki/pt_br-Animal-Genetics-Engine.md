# Motor de Genética Animal

| Propriedade | Valor |
| :--- | :--- |
| **Pacote Principal** | `net.dasik.social.api.genetics` |
| **Identificador do Anexo** | `dasik-library:genetics` |
| **Limites de Escala** | `0.1x` a `3.0x` (Padrão: `0.5x` - `2.0x`) |
| **Limiar de Nanismo (Runt)** | Escala $< 0.85x$ (ou consanguíneo) |
| **Limiar de Gigante (Giant)**| Escala $> 1.15x$ |
| **Codec de Dados** | `EntityGenetics.CODEC` |

---

## 🧬 Visão Geral e Estrutura do DNA

O **Motor de Genética Animal** fornece um sistema de atributos hereditários persistente via `AttachmentType` do Fabric. Cada entidade genética preserva um registro `EntityGenetics`:

1. **`parent1Uuid` (`Optional<UUID>`)**: UUID do primeiro genitor se nascido por reprodução.
2. **`parent2Uuid` (`Optional<UUID>`)**: UUID do segundo genitor se nascido por reprodução.
3. **`inbred` (`boolean`)**: Flag que indica consanguinidade parental.
4. **`traitsRolled` (`boolean`)**: Flag indicando sorteio inicial de traços.
5. **`traits` (`Map<String, Float>`)**: Valores dinâmicos de traços (`scale`, `max_health`, `attack_damage`, `movement_speed`).

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

## 📐 Matemática de Herança e Mutações

Ao cruzar dois animais, os traços dos filhotes são calculados via `GeneticsEngine.calculateOffspringGenetics`:

### 1. Fórmula de Herança da Escala
A escala base da prole $S_{\text{offspring}}$ deriva da média parental mais uma perturbação gaussiana triangular de mutação $\Delta_{\text{mutate}}$:

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

Onde $\Delta_{\text{mutate}}$ é amostrado de uma distribuição triangular nos limites $\pm \text{mutationRate}$.

### 2. Fórmula de Penalidade de Consanguinidade
Se houver parentesco compartilhado ($F > 0$), aplica-se penalidade de vida e porte $\text{Penalty}_{\text{inbreeding}}$:

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 Exemplo de Código para Desenvolvedores

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
* [[API de Genética e Pedigree|pt_br-Genetics-API-and-Pedigree]]
* [[Purga de Atributos Obsoletos e Escala|pt_br-Stale-Attribute-Purging-and-Scale]]
