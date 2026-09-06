# Enchantment Dinamis & Pelacak Penglihatan

| Komponen | Kelas |
| :--- | :--- |
| **Manajer Enchantment** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **Pelacak Penglihatan Pemain**| `net.dasik.social.util.PlayerVisionTracker` |
| **Pengidentifikasi Kunci** | `Identifier` dinamis |

---

## ✨ Gambaran Umum & Registrasi Dinamis

`DynamicEnchantmentManager` memungkinkan penambahan enchantment saat runtime tanpa memerlukan struktur datapack JSON yang rumit saat permainan diluncurkan.

```java
// Registering dynamic enchantment
DynamicEnchantmentManager.registerEnchantment(
    Identifier.parse("betterdogs:loyal_bond"),
    new DynamicEnchantmentConfig(...)
);
```

---

## 👁️ Pelacak Penglihatan Pemain (`PlayerVisionTracker`)

`PlayerVisionTracker` menyediakan kalkulasi raycast dan frustum yang efisien untuk mendeteksi apakah suatu entitas sedang dipandang langsung oleh pemain:

1. **Uji Kerucut Sudut Pandang**: Menghitung dot product antara arah hadap pemain dan posisi target.
2. **Raycast Rintangan**: Memastikan tidak ada blok padat yang menghalangi pandangan.

---

## 🔗 Halaman Terkait
* [[Utilitas Stokastik & Matematika|id_id-Stochastic-and-Math-Utilities]]
* [[Arsitektur & Tata Letak Paket|id_id-Architecture-and-Package-Layout]]
