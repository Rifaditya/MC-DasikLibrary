# Profil Perilaku & Kondisi Pemicu

| Komponen | Kelas |
| :--- | :--- |
| **Manajer Profil** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **Antarmuka Profil** | `net.dasik.social.api.profile.BehaviorProfile` |
| **Antarmuka Kondisi** | `net.dasik.social.api.profile.BehaviorCondition` |
| **Pembangun Standar** | `DefaultProfileBuilder` |

---

## 🎭 State Machine Profil

Sistem profil perilaku memungkinkan peralihan sekumpulan AI Goal secara dinamis tergantung pada kondisi lingkungan (mode siaga, mode berburu, mode panik) tanpa perlu mendaftarkan ulang AI secara manual.

```ascii
[ Idle / Wandering ]
         │
         ├── Trigger: Player Attacked ──► [ Sentinel Guard Mode ]
         │
         └── Trigger: Low Health ───────► [ Panic / Flee Mode ]
```

---

## 🔗 Halaman Terkait
* [[Penjadwal Sosial & Event|id_id-Social-Scheduler-and-Events]]
* [[Pengikut Pemimpin & Flocking Boids|id_id-Leader-Follower-and-Flocking]]
