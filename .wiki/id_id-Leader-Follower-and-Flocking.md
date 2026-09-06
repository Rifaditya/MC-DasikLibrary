# Pengikut Pemimpin & Flocking Boids

| Komponen | Spesifikasi |
| :--- | :--- |
| **Antarmuka Anggota** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **Tipe Kawanan** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Strategi Boids** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ Gambaran Umum & Matematika Pengemudian

Dasik Library menyertakan implementasi model **kawanan Boids** (oleh Craig Reynolds) yang telah dioptimalkan secara khusus untuk navigasi dalam dunia Minecraft. Entitas yang mengimplementasikan `GroupMember` mengoordinasikan pergerakan rombongan, mengikuti pemimpin, dan menjaga jarak formasi.

### Rumus Vektor Kemudi Gabungan
Gaya kemudi total $\vec{F}_{\text{steer}}$ yang diterapkan pada entitas dihitung sebagai:

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

Di mana:
* $\vec{V}_{\text{separation}}$ menolak entitas sekitar agar tidak bertabrakan.
* $\vec{V}_{\text{cohesion}}$ menarik entitas menuju pusat massa rombongan.
* $\vec{V}_{\text{alignment}}$ menyelaraskan arah hadap dengan tetangga kelompok.
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ adalah koefisien bobot dalam `GroupParameters`.

---

## ⚙️ Spesifikasi `GroupParameters`

```java
public record GroupParameters(
    float cohesionRadius, 
    float separationRadius, 
    float maxSpeed, 
    boolean canTeleport, 
    float teleportDistance,
    float startDistance,
    float stopDistance,
    float alignmentWeight,
    float cohesionWeight,
    float separationWeight
) {
    public static final GroupParameters DEFAULT_AERIAL = new GroupParameters(
        3.0f, 1.0f, 0.4f, true, 144.0f, 6.0f, 2.0f, 0.05f, 0.05f, 0.1f
    );
    public static final GroupParameters DEFAULT_TERRESTRIAL = new GroupParameters(
        5.0f, 1.5f, 1.2f, true, 144.0f, 6.0f, 2.0f, 0.0f, 0.0f, 0.0f
    );
}
```

---

## 💻 Contoh Kode Pengembang

```java
public class CustomBirdEntity extends PathfinderMob implements GroupMember {
    private LivingEntity leader;

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new FollowLeaderGoal<>(this, GroupParameters.DEFAULT_AERIAL, 32.0D));
    }

    @Override public LivingEntity getLeader() { return this.leader; }
    @Override public void setLeader(LivingEntity leader) { this.leader = leader; }
    @Override public FlockType getFlockType() { return FlockType.AERIAL; }
}
```

---

## 🔗 Halaman Terkait
* [[Sistem Sosial Hive Mind|id_id-Hive-Mind-Social-System]]
* [[Arsitektur & Tata Letak Paket|id_id-Architecture-and-Package-Layout]]
