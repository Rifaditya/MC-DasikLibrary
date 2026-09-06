# Penjadwal Sosial & Sistem Event

| Komponen | Kelas |
| :--- | :--- |
| **Mesin Penjadwal** | `net.dasik.social.core.EntitySocialScheduler` |
| **Registri Event** | `net.dasik.social.api.SocialEventRegistry` |
| **Tingkatan Prioritas** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **Tipe Sinyal** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

---

## 🔄 Model Penjadwalan Dua Jalur

Setiap entitas sosial memiliki `EntitySocialScheduler` pribadi yang mengeksekusi tugas pada dua jalur paralel:

1. **Jalur Mood (Kondisi Jangka Panjang)**: Dievaluasi pada frekuensi rendah (misal tiap 20-100 tick) untuk memperbarui suasana hati, hierarki kawanan, rasa lapar, atau keletihan.
2. **Jalur Ambient (Reaksi Taktis Seketika)**: Dievaluasi pada frekuensi tinggi (tiap 1-5 tick) untuk bereaksi langsung terhadap stimulus lingkungan, bahaya mendadak, atau penyesuaian posisi kawanan.

```ascii
                      ┌───────────────────────────────┐
                      │    EntitySocialScheduler      │
                      └───────────────┬───────────────┘
                                       │
               ┌───────────────────────┴───────────────────────┐
               ▼                                               ▼
    ┌─────────────────────┐                         ┌─────────────────────┐
    │     Mood Track      │                         │    Ambient Track    │
    │ (Low Frequency Ticks)│                         │(High Frequency Ticks)│
    │ - Pack Hierarchy   │                         │ - Obstacle Avoidance│
    │ - Hunger / Fatigue  │                         │ - Signal Response   │
    └─────────────────────┘                         └─────────────────────┘
```

---

## 📢 `SocialEventRegistry` & Implementasi Event

Mod mengimplementasikan antarmuka `SocialEvent` dan mendaftarkannya saat inisialisasi:

```java
public class HowlEvent implements SocialEvent {
    @Override public String getId() { return "betterdogs:howl"; }
    @Override public int getPriorityValue() { return 80; }
    @Override public String getTrackId() { return "pack_command"; }
    @Override public boolean canPreempt(SocialEvent other) { return other.getPriorityValue() < 80; }
    @Override public void onStart(TickContext context) {}
    @Override public boolean tick(TickContext context) { return false; }
    @Override public void onEnd(SocialEntity entity, EndReason reason) {}
}

// Register during mod initialization (frozen on first pulse)
SocialEventRegistry.register(new HowlEvent());
```

---

## 📊 Matriks Tingkatan Prioritas

| Tingkatan | Jalur Maks | Kebijakan Interupsi | Penggunaan Tipikal |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | Interupsi langsung, prioritas mutlak | Menyelamatkan diri dari bahaya, pertempuran |
| `HIGH` | `8` | Menghentikan tugas normal/rendah | Perintah kawanan, sinyal taktis |
| `NORMAL` | `16` | Alokasi anggaran standar | Interaksi sosial, bermain, berkeliaran |
| `LOW` | `32` | Kapasitas terbesar, latar belakang | Pengamatan pasif terhadap lingkungan |

---

## 🔗 Halaman Terkait
* [[Sistem Sosial Hive Mind|id_id-Hive-Mind-Social-System]]
* [[Profil Perilaku & Kondisi Pemicu|id_id-Behavior-Profiles-and-Conditions]]
