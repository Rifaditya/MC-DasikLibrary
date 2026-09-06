# Referensi Mixin & Kait Injeksi

| Kelas Mixin | Kelas Target | Metode Target | Tujuan |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `getOrDefault` | Injeksi otomatis nama GameRule dinamis |
| `LivingEntityLootMixin` | `LivingEntity` | `dropFromLootTable` | Intersepsi jarahan berdasarkan genetika |
| `MobGoalAccessor` | `Mob` | Aksesor GoalSelector | Akses ke selektor AI Goal internal |
| `PathfinderMobMixin` | `PathfinderMob` | `tick` | Pemicu siklus denyut nadi sosial |
| `ProfileTriggerMixin` | `LivingEntity` | Deteksi Event | Pengalihan profil perilaku |

---

## 🔗 Halaman Terkait
* [[Arsitektur & Tata Letak Paket|id_id-Architecture-and-Package-Layout]]
* [[Modifikasi Loot Genetika|id_id-Genetics-Loot-Modifiers]]
