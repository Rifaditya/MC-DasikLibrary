# Sistem Sosial Hive Mind

| Parameter Sistem | Nilai |
| :--- | :--- |
| **Kelas Utama** | `net.dasik.social.core.GlobalSocialSystem` |
| **Kelas Registri** | `net.dasik.social.core.SocialRegistry` |
| **Frekuensi Denyut Nadi** | `1 tick` (Aturan Highlander) |
| **Kompleksitas Pencarian** | $O(1)$ Hash Map Ter-shard |
| **Strategi Pembersihan Memori** | Pembersihan otomatis via `isAlive()` dan `isRemoved()` |

---

## ⚡ Gambaran Umum & Aturan Highlander

**Sistem Sosial Hive Mind** adalah inti orkestrasi Dasik Library. Alih-alih membiarkan ratusan entitas sosial menjalankan pemindaian spasial berat secara terpisah setiap tick, Dasik Library menggunakan satu koordinator denyut nadi global (`GlobalSocialSystem`).

### 👑 Aturan Highlander ("Hanya Boleh Ada Satu")
`GlobalSocialSystem` memastikan bahwa hanya tepat **satu siklus denyut nadi global** yang dieksekusi per tick server ($20\text{ ticks} = 1\text{s}$). Jika beberapa thread atau subsistem memanggil denyut nadi pada nomor tick yang sama, panggilan berlebih akan diabaikan:

$$\text{Global Pulse Execution} = \begin{cases} \text{Execute Ticks}, & \text{if } \text{currentTick} > \text{lastTick} \\ \text{Skip (No-Op)}, & \text{if } \text{currentTick} \le \text{lastTick} \end{cases}$$

```ascii
[ Server Level Tick ]
         │
         ▼
 ┌──────────────────────┐
 │ GlobalSocialSystem   │  ◄── Highlander Rule Guard (1 tick per server tick)
 └──────────┬───────────┘
            │
            ▼
 ┌──────────────────────┐
 │   SocialRegistry     │  ◄── O(1) Shard Lookup & Dead Entity Purge
 └──────────┬───────────┘
            │
      ┌─────┴────────────────┐
      ▼                      ▼
 ┌──────────────┐      ┌──────────────┐
 │ Entity 1     │      │ Entity N     │
 │ Mood Task    │      │ Mood Task    │
 └──────────────┘      └──────────────┘
```

---

## 🗂️ Arsitektur `SocialRegistry`

`SocialRegistry` mengelompokkan entitas sosial aktif berdasarkan spesies dan UUID dunia:

* **Pendaftaran $O(1)$**: Entitas mendaftar melalui `SocialRegistry.register(SocialEntity entity)`.
* **Pembersihan Otomatis**: Referensi entitas mati atau chunk yang dibongkar akan dibersihkan secara berkala pada siklus denyut nadi melalui pengecekan `entity.dasik$asEntity().isAlive()` dan `isRemoved()`.
* **Sharding Berdasarkan Spesies**: Entitas diindeks menurut `dasik$getSpeciesId()`, memungkinkan pencarian tetangga terdekat secara instan untuk kawanan tanpa memindai seluruh dunia.

---

## 💻 Contoh Kode Pengembang

```java
// Registering an entity to the Hive Mind
public class CustomSocialMob extends PathfinderMob implements SocialEntity {
    private final EntitySocialScheduler scheduler = new EntitySocialScheduler();

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            SocialRegistry.register(this);
            GlobalSocialSystem.pulse(serverLevel);
        }
    }

    @Override
    public SocialScheduler dasik$getScheduler() {
        return this.scheduler;
    }
}
```

---

## 🔗 Halaman Terkait
* [[Penjadwal Sosial & Event|id_id-Social-Scheduler-and-Events]]
* [[Arsitektur & Tata Letak Paket|id_id-Architecture-and-Package-Layout]]
