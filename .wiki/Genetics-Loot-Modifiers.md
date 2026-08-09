# Genetics Loot Modifiers

| Component | Class |
| :--- | :--- |
| **Modifier Interface** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **Registry Class** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Mixin Hook Class** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **Target Method** | `LivingEntity.dropFromLootTable` |

---

## 🥩 Overview & Loot Interception

`GeneticsLootRegistry` allows mods to dynamically modify mob loot drops based on entity genetics (e.g., giant animals dropping extra meat, runts dropping less loot, or mutated variants dropping special items).

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

## 💻 Developer Code Example

Registering a loot modifier for wolves based on physical scale:

```java
GeneticsLootRegistry.register(EntityTypes.WOLF, (entity, originalStack, genetics) -> {
    float scale = genetics.getScale();
    if (originalStack.is(Items.BONE)) {
        // Scale bone drop count proportionally to entity scale
        int newCount = Math.max(1, Math.round(originalStack.getCount() * scale));
        return new ItemStack(Items.BONE, newCount);
    }
    return originalStack;
});
```

---

## 🔗 Related Pages
* [[Animal Genetics Engine|Animal-Genetics-Engine]]
* [[Mixin Reference & Hooks|Mixin-Reference-and-Hooks]]
