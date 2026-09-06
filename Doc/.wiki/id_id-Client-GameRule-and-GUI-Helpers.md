# GameRule Klien & Pembantu GUI

| Kelas Utilitas | Lingkungan | Peran |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | Klien / Server Terintegrasi | Membaca GameRule server terintegrasi dari thread klien |
| `ConfigHelper` | Common (Klien/Server) | Pemuatan, penyimpanan, dan pertukaran berkas atomik JSON |
| `GuiHelper` | Klien | Bantuan pembangunan layar antarmuka ModMenu dan Cloth Config |

---

## 🖥️ Pencegahan Crash pada Dedicated Server

Guna mencegah error pemuatan kelas (ClassLoader) saat berjalan di Dedicated Server, seluruh antarmuka GUI menerapkan pemisahan lingkungan tunda (`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`).

```ascii
                      DynamicGameRuleManager.getInt(level, ruleKey)
                                         │
                    ┌───────────────────┴───────────────────┐
                    ▼                                       ▼
          [ level instanceof ServerLevel ]       [ Client Environment ]
                    │                                       │
                    ▼                                       ▼
            Direct Level Lookup                   ClientGameRuleHelper
                                             (Queries Integrated Server)
```

---

## 📄 Pertukaran Atomik JSON pada `ConfigHelper`

`ConfigHelper` mengamankan data konfigurasi agar tidak rusak jika game tertutup mendadak:

1. Menulis data ke berkas sementara `config.json.tmp`.
2. Memvalidasi sintaks JSON dan ukuran berkas.
3. Membuat cadangan otomatis di `config.json.bak`.
4. Mengganti berkas secara atomik `config.json.tmp` -> `config.json` via `Files.move(..., StandardCopyOption.ATOMIC_MOVE)`.

---

## 🔗 Halaman Terkait
* [[Pengelola GameRules Dinamis|id_id-Dynamic-GameRules-Manager]]
* [[Profil Perilaku & Kondisi Pemicu|id_id-Behavior-Profiles-and-Conditions]]
