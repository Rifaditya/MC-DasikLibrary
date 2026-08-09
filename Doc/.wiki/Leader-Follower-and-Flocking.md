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
    int maxGroupSize,
    double searchRadius,
    double followSpeed,
    double stopDistance,
    double startDistance,
    float separationWeight,
    float cohesionWeight,
    float alignmentWeight,
    boolean allowLeaderReassignment,
    FlockType flockType
) {
    public static final GroupParameters DEFAULT_TERRESTRIAL = new GroupParameters(
        8, 16.0D, 1.25D, 3.0D, 6.0D, 1.5f, 1.0f, 0.5f, true, FlockType.TERRESTRIAL
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
