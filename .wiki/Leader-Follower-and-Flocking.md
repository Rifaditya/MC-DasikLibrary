# Leader Follower & Flocking System

| Component | Specification |
| :--- | :--- |
| **Member Interface** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **Flock Types** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Boids Strategies** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ Overview & Steering Math

Dasik Library implements generalized **Boids Flocking Math** (Craig Reynolds model) optimized for Minecraft pathfinding. Entities implementing `GroupMember` automatically coordinate group movement, leader following, and spatial separation.

### Steering Vector Formula
The combined steering force vector $\vec{F}_{\text{steer}}$ is computed as:

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

Where:
* $\vec{V}_{\text{separation}}$ pushes entities apart to prevent crowding.
* $\vec{V}_{\text{cohesion}}$ pulls entities toward the group center of mass.
* $\vec{V}_{\text{alignment}}$ aligns entity headings.
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ are configurable weights in `GroupParameters`.

---

## ⚙️ `GroupParameters` Specification

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

## 💻 Developer Code Example

Adding flocking AI to a custom entity:

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

## 🔗 Related Pages
* [[Hive Mind Social System|Hive-Mind-Social-System]]
* [[Architecture & Package Layout|Architecture-and-Package-Layout]]
