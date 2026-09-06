# Pengelola GameRules Dinamis

| Komponen | Kelas |
| :--- | :--- |
| **Kelas Manajer** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **Format Kunci Namespace**| `modid:rule_name` |
| **Injeksi Terjemahan** | `LanguageMixin` -> Map `GENERATED_TRANSLATIONS` |
| **Format Judul Kategori** | `§lCategory Title (N rules)` |

---

## 🎲 Gambaran Umum & GameRules Bernamespace

`DynamicGameRuleManager` memungkinkan mod konsumen mendaftarkan GameRule dinamis secara terprogram tanpa perlu mendeklarasikan kunci terjemahan satu per satu di dalam `en_us.json`.

### Fitur Unggulan
1. **Injeksi Terjemahan Otomatis**: Mengonversi `modid:rule_name` menjadi teks terbaca (misal `bd_enable_guard_mode` -> `BD Enable Guard Mode`) dan menginjeksinya langsung via `LanguageMixin`.
2. **Tajuk Kategori Tebal (`§l`)**: Kategori yang didaftarkan dengan `registerCategory` otomatis diformat tebal agar tampak rapi di layar opsi.
3. **Fungsi Pembantu Matematika**:
   - `getPct(Level level, GameRule<Integer> rule)` — Mengonversi integer (`0-100`) ke Double (`/ 100.0`).
   - `getChance(Level level, GameRule<Integer> rule)` — Mengonversi persentase ke Float (`/ 100.0f`).
   - `getProb(Level level, GameRule<Integer> rule)` — Mengonversi permil (`0-1000`) ke Float (`/ 1000.0f`).
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — Mengonversi nilai desil ke Float (`/ 10.0f`).
   - `getIntVal(Level level, String key, int defaultValue)` — Mengambil nilai integer langsung berdasarkan kunci teks.

---

## 💻 Contoh Kode Pengembang

```java
// Register a dynamic boolean GameRule with description
GameRule<Boolean> ENABLE_GUARD = DynamicGameRuleManager.booleanRule(
    "betterdogs:bd_enable_guard_mode",
    GameRuleCategory.MOBS,
    true
).description("Enable wolf sentinel guard mode").register();

// Querying GameRule safely across client/server
boolean isGuardEnabled = DynamicGameRuleManager.getBoolean(level, ENABLE_GUARD);
```

---

## 🔗 Halaman Terkait
* [[Codec GameRule & Serialisasi|id_id-GameRule-Codec-and-Serialization]]
* [[GameRule Klien & Pembantu GUI|id_id-Client-GameRule-and-GUI-Helpers]]
