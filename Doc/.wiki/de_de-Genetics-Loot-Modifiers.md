# Genetik-Beutemodifikatoren

| Komponente | Klasse |
| :--- | :--- |
| **Modifikator-Interface** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **Registry-Klasse** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Mixin-Hook-Klasse** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **Zielmethode** | `LivingEntity.dropFromLootTable` |

---

## 🥩 Übersicht & Beuteabfang

`GeneticsLootRegistry` erlaubt es Mods, die Beute getöteter Mobs anhand genetischer Merkmale dynamisch anzupassen (Riesentiere lassen mehr Fleisch fallen, Kümmerlinge weniger Beute, mutierte Formen besondere Items).

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

## 💻 Entwickler-Codebeispiel

Registrieren eines Beutemodifikators für Wölfe basierend auf ihrer Körpergröße:

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

## 🔗 Verwandte Seiten
* [[Tiergenetik-Engine|de_de-Animal-Genetics-Engine]]
* [[Mixin-Referenz & Hooks|de_de-Mixin-Reference-and-Hooks]]
