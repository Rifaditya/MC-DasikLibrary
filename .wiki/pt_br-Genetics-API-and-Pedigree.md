# API de Genética e Sistema de Pedigree

| Classe de Fachada | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **Atributos Suportados** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **Profundidade de Análise**| Até 3 gerações (Pais, Avós) |
| **Risco de Consanguinidade**| `0%` (Sem parentesco) a `100%` (Irmãos completos / Clone) |

---

## 🛠️ Fachada `DasikAnimalGeneticsAPI`

A `DasikAnimalGeneticsAPI` oferece métodos estáticos para que mods leiam, configurem, redefinam e analisem a genética das criaturas sem manipular tags NBT cruas.

### Resumo de Métodos da API

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

## 🌳 Árvore Genealógica e Predição de Consanguinidade

O analisador de pedigree investiga recursivamente coincidências nos UUIDs dos ancestrais:

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

## 💻 Exemplo de Código para Desenvolvedores

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 Páginas Relacionadas
* [[Motor de Genética Animal|pt_br-Animal-Genetics-Engine]]
* [[Modificadores de Saque Genético|pt_br-Genetics-Loot-Modifiers]]
