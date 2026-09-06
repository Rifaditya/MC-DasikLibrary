# Mesin Genetika Hewan

| Properti | Nilai |
| :--- | :--- |
| **Paket Utama** | `net.dasik.social.api.genetics` |
| **ID Attachment** | `dasik-library:genetics` |
| **Rentang Skala Ukuran**| `0.1x` hingga `3.0x` (Standar: `0.5x` - `2.0x`) |
| **Ambang Kerdil (Runt)**| Skala $< 0.85x$ (atau inbreeding) |
| **Ambang Raksasa (Giant)**| Skala $> 1.15x$ |
| **Codec Data** | `EntityGenetics.CODEC` |

---

## 🧬 Gambaran Umum & Struktur DNA

**Mesin Genetika Hewan** menyediakan sistem genetika independen entitas dan persisten memanfaatkan `AttachmentType` Fabric. Setiap makhluk menyimpan rekaman `EntityGenetics`:

1. **`parent1Uuid` (`Optional<UUID>`)**: UUID induk pertama saat pembiakan.
2. **`parent2Uuid` (`Optional<UUID>`)**: UUID induk kedua saat pembiakan.
3. **`inbred` (`boolean`)**: Menandai apakah individu lahir dari perkawinan sedarah.
4. **`traitsRolled` (`boolean`)**: Menandai apakah pengundian sifat awal telah selesai.
5. **`traits` (`Map<String, Float>`)**: Nilai atribut dinamis (`scale`, `max_health`, `attack_damage`, `movement_speed`).

```ascii
 ┌────────────────────────────────────────────────────────────────────────┐
 │                           EntityGenetics                               │
 ├───────────────────────────────────┬────────────────────────────────────┤
 │ parent1Uuid: Optional<UUID>       │ inbred: boolean                    │
 │ parent2Uuid: Optional<UUID>       │ traitsRolled: boolean              │
 │ traits: Map<String, Float>        │ Codec: EntityGenetics.CODEC        │
 └───────────────────────────────────┴────────────────────────────────────┘
```

---

## 📐 Matematika Pewarisan & Mutasi

Saat dua hewan berkembang biak, genetika anak dihitung melalui `GeneticsEngine.calculateOffspringGenetics`:

### 1. Rumus Pewarisan Skala Ukuran
Skala dasar keturunan $S_{\text{offspring}}$ dihitung dari rata-rata skala kedua induk ditambah deviasi mutasi segitiga $\Delta_{\text{mutate}}$:

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

Di mana $\Delta_{\text{mutate}}$ diambil dari distribusi segitiga dalam rentang $\pm \text{mutationRate}$.

### 2. Rumus Penalti Perkawinan Sedarah (Inbreeding)
Jika terdeteksi hubungan kekerabatan ($F > 0$), penalti inbreeding $\text{Penalty}_{\text{inbreeding}}$ diterapkan pada kesehatan dan ukuran:

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 Contoh Kode Pengembang

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 Halaman Terkait
* [[API Genetika & Silsilah|id_id-Genetics-API-and-Pedigree]]
* [[Pembersihan Atribut Kedaluwarsa & Skala Ukuran|id_id-Stale-Attribute-Purging-and-Scale]]
