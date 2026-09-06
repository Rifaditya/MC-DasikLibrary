# API de Genética y Sistema de Pedigrí

| Clase de Fachada | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **Atributos Soportados** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **Profundidad de Árbol** | Hasta 3 generaciones (Padres, Abuelos) |
| **Rango de Riesgo de Endogamia**| `0%` (Sin relación) a `100%` (Hermanos completos / Mismo espécimen) |

---

## 🛠️ Fachada `DasikAnimalGeneticsAPI`

`DasikAnimalGeneticsAPI` proporciona métodos estáticos para que los mods consulten, actualicen, restablezcan y evalúen la genética sin necesidad de manipular etiquetas NBT manualmente.

### Resumen de Métodos de la API

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

## 🌳 Predicción de Riesgo de Endogamia y Pedigrí

El analizador genealógico comprueba recursivamente las coincidencias de UUID de progenitores:

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

## 💻 Ejemplo de Código para Desarrolladores

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 Páginas Relacionadas
* [[Motor de Genética Animal|es_es-Animal-Genetics-Engine]]
* [[Modificadores de Botín Genético|es_es-Genetics-Loot-Modifiers]]
