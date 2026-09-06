# Anführer-Gefolge & Schwarmbildung

| Komponente | Spezifikation |
| :--- | :--- |
| **Mitglieds-Interface** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **Schwarmtypen** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Boids-Strategien** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ Übersicht & Lenkungsmathematik

Dasik Library implementiert ein verallgemeinertes **Boids-Schwarmbildungsmodell** (nach Craig Reynolds), das speziell für die Wegfindung in Minecraft optimiert wurde. Entitäten mit `GroupMember` koordinieren Gruppenbewegungen, Anführerfolge und räumlichen Abstand.

### Formel des resultierenden Lenkvektors
Die summierte Lenkkraft $\vec{F}_{\text{steer}}$ errechnet sich wie folgt:

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

Wobei:
* $\vec{V}_{\text{separation}}$ Mobs abstößt, um Zusammenstöße zu vermeiden.
* $\vec{V}_{\text{cohesion}}$ Mobs zum gemeinsamen Schwerpunkt der Gruppe zieht.
* $\vec{V}_{\text{alignment}}$ die Ausrichtung und Flug-/Laufrichtung angleicht.
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ konfigurierbare Gewichtungen in `GroupParameters` sind.

---

## ⚙️ `GroupParameters` Spezifikation

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

## 💻 Entwickler-Codebeispiel

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

## 🔗 Verwandte Seiten
* [[Schwarmintelligenz-Sozialsystem|de_de-Hive-Mind-Social-System]]
* [[Architektur & Paketstruktur|de_de-Architecture-and-Package-Layout]]
