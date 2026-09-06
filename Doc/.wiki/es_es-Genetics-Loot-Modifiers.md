# Modificadores de Botín Genético

| Componente | Clase |
| :--- | :--- |
| **Interfaz de Modificador** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **Clase de Registro** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Clase de Mixin** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **Método Objetivo** | `LivingEntity.dropFromLootTable` |

---

## 🥩 Descripción General e Intercepción de Botín

`GeneticsLootRegistry` permite modificar dinámicamente las recompensas de botín según la genética del animal (animales gigantes sueltan carne adicional, animales enanos sueltan menos objetos, o variantes raras sueltan botín especial).

```ascii
[ LivingEntity.dropFromLootTable ]
                │
                ▼
 ┌─────────────────────────────┐
 │    LivingEntityLootMixin    │  ◄── @ModifyVariable Consumer Hook
 └──────────────┬──────────────┘
                │
                ▼
 ┌─────────────────────────────┐
 │    GeneticsLootRegistry     │  ◄── Lookup Modifier for EntityType
 └──────────────┬──────────────┘
                │
                ▼
 ┌─────────────────────────────┐
 │    GeneticsLootModifier     │  ◄── Scale stack count / Swap item
 └─────────────────────────────┘
```

---

## 💻 Ejemplo de Código para Desarrolladores

Registro de un modificador de botín para lobos según su tamaño:

```java
GeneticsLootRegistry.register(EntityTypes.WOLF, (entity, genetics, stack, random) -> {
    float scale = DasikAnimalGeneticsAPI.getScale(entity);
    if (stack.is(Items.BONE)) {
        // Scale bone drop count proportionally to entity scale
        int newCount = Math.max(1, Math.round(stack.getCount() * scale));
        return new ItemStack(Items.BONE, newCount);
    }
    return stack;
});
```

---

## 🔗 Páginas Relacionadas
* [[Motor de Genética Animal|es_es-Animal-Genetics-Engine]]
* [[Referencia de Mixins y Puntos de Inyección|es_es-Mixin-Reference-and-Hooks]]
