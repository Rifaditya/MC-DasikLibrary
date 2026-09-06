# Modificateurs de Butin Génétique

| Composant | Classe |
| :--- | :--- |
| **Interface Modificatrice** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **Classe de Registre** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Classe de Point d'Injection Mixin** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **Méthode Cible** | `LivingEntity.dropFromLootTable` |

---

## 🥩 Présentation & Interception des Butins

`GeneticsLootRegistry` permet aux mods d'ajuster dynamiquement les butins abandonnés à la mort d'une entité en fonction de ses caractéristiques génétiques (les créatures géantes rapportent plus de viande, les chétifs moins de ressources, les mutations rares débloquent des drops spécifiques).

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

## 💻 Exemple de Code Développeur

Enregistrement d'un modificateur de butin pour les loups proportionnel à leur taille :

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

## 🔗 Pages Liées
* [[Moteur de Génétique Animale|fr_fr-Animal-Genetics-Engine]]
* [[Référence Mixin et Points d'Injection|fr_fr-Mixin-Reference-and-Hooks]]
