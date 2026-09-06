# Modificadores de Saque Genético

| Componente | Classe |
| :--- | :--- |
| **Interface do Modificador** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **Classe de Registro** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Classe de Mixin** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **Método Alvo** | `LivingEntity.dropFromLootTable` |

---

## 🥩 Visão Geral e Interceptação de Saque

O `GeneticsLootRegistry` permite que mods alterem dinamicamente o saque de entidades com base na genética (ex: animais gigantes gerando mais carne, nanicos gerando menos drops, ou variantes raras gerando itens exclusivos).

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

## 💻 Exemplo de Código para Desenvolvedores

Registrando um modificador de saque para lobos proporcional à sua escala física:

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
* [[Motor de Genética Animal|pt_br-Animal-Genetics-Engine]]
* [[Referência de Mixin e Pontos de Injeção|pt_br-Mixin-Reference-and-Hooks]]
