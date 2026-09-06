# Modifikasi Loot Genetika

| Komponen | Kelas |
| :--- | :--- |
| **Antarmuka Modifier** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **Kelas Registri** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Kelas Kait Mixin** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **Metode Target** | `LivingEntity.dropFromLootTable` |

---

## 🥩 Gambaran Umum & Intersepsi Loot

`GeneticsLootRegistry` memungkinkan mod untuk menyesuaikan barang jarahan (loot) saat entitas mati secara dinamis berdasarkan ciri genetiknya (hewan raksasa menjatuhkan lebih banyak daging, hewan kerdil menjatuhkan lebih sedikit hasil, bentuk mutasi menjatuhkan item langka).

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

## 💻 Contoh Kode Pengembang

Mendaftarkan modifikasi drop tulang serigala sebanding dengan ukuran tubuhnya:

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

## 🔗 Halaman Terkait
* [[Mesin Genetika Hewan|id_id-Animal-Genetics-Engine]]
* [[Referensi Mixin & Kait Injeksi|id_id-Mixin-Reference-and-Hooks]]
