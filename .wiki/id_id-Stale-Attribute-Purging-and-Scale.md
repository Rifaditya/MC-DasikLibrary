# Pembersihan Atribut Kedaluwarsa & Skala Ukuran

| Masalah | Strategi Pemecahan |
| :--- | :--- |
| **Sisa Modifier Atribut** | Pembersihan modifier berawalan `genetics_` saat kalkulasi ulang |
| **Offset Penambahan Ukuran** | Pengurangan basis `-1.0f` pada operasi `ADD_VALUE` |
| **Atribut Skala Target** | `EntityAttributes.SCALE` |

---

## 🧹 Pembersihan Otomatis Modifier Kedaluwarsa

Saat atribut entitas dihitung ulang (misalnya setelah tumbuh besar, bermutasi, atau diobati), modifier lama harus dihapus agar tidak terjadi penumpukan nilai yang tidak diinginkan:

```java
public static void purgeStaleModifiers(LivingEntity entity, Holder<Attribute> attribute) {
    AttributeInstance instance = entity.getAttribute(attribute);
    if (instance != null) {
        instance.getModifiers().stream()
            .filter(m -> m.id().getPath().startsWith("genetics_"))
            .toList()
            .forEach(instance::removeModifier);
    }
}
```

---

## 📏 Matematika Offset untuk `ADD_VALUE`

Di Minecraft, nilai bawaan untuk `SCALE` adalah $1.0$. Saat modifier menggunakan operasi `ADD_VALUE`, nilai tambahan harus mengimbangi basis $1.0$:

$$\text{Modifier Amount} = \text{TargetScale} - 1.0f$$

Jika skala yang diinginkan adalah $1.5x$, nilai modifier yang dimasukkan adalah $+0.5f$.

---

## 🔗 Halaman Terkait
* [[Mesin Genetika Hewan|id_id-Animal-Genetics-Engine]]
* [[API Genetika & Silsilah|id_id-Genetics-API-and-Pedigree]]
