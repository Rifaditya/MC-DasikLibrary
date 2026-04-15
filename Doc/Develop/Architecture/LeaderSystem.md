# Leader-Follower API

The DasikLibrary Leader-Follower API provides a modular, "zero-edit" extensible framework for swarming, flocking, and pack behaviors. It completely abstracts away the complex math of boids algorithms and terrestrial pathfinding into simple interfaces.

## 1. Core Interfaces

To make an entity use the system, it must implement `GroupMember<T>` (where `T` is the entity type, usually `Mob`):

```java
public interface GroupMember {
    LivingEntity getLeader();
    boolean hasLeader();
    void setLeader(@Nullable LivingEntity leader);
    int getGroupSize();
    FlockType getFlockType(); // Flocking logic to use (AERIAL or TERRESTRIAL)
    
    @Nullable
    default FlockState getFlockState() { return null; }
    default void setFlockState(@Nullable FlockState state) { }
}
```

### 1.1 `FlockState` (The Cached Boids Pattern)

To prevent $O(N^2)$ calculations where every follower scans every peer independently, DasikLibrary uses a **Cached Boids Pattern**. The Leader stores a `FlockState` object containing:
- **Center of Mass (CoM)**: The average position of all entities in the swarm.
- **Average Velocity**: The average heading and speed of the swarm.

Followers read this state in $O(1)$ time, allowing for massive flocks with minimal server overhead. The state is refreshed every 20 ticks by the `GroupManager`.

## 2. Flocking Strategies

The `FlockingStrategy` interface dictates *how* the `GroupMember` follows the leader.
The library ships with two default strategies in the `Strategies` class:

- **`Strategies.AERIAL`**: Advanced 3D Boids algorithm (Alignment, Cohesion, Separation). It uses the `FlockState` for efficient group-wide steering and includes predictive block collision avoidance.
- **`Strategies.TERRESTRIAL`**: Smooth 2D ground pathfinding. Uses Distance-Based Linear Interpolation (Lerp) to scale speed dynamically as entities approach the leader, preventing visual jitter and "pile-ups."

Other mods can implement their own `FlockingStrategy` if custom logic is required, and assign it inside their respective AI goals.

### `GroupParameters`

Both strategies consume a `GroupParameters` record, providing configuration for:

- `cohesionRadius`: At what distance to pull the entity towards the leader.
- `separationRadius`: At what distance to push the entity away from siblings to avoid crowding.
- `maxSpeed`: The maximum movement speed applied.

## 3. The `GroupManager`

`GroupManager.findAndSetLeader(member, searchRadius)` handles the stateless discovery and election of leaders.

- **Discovery**: Uses an AABB bounding box search to find other `GroupMember` entities of the same class.
- **Election**: Deterministically elects the entity with the "lowest" String UUID representation to govern the group.
- **Chaining Prevention**: If the elected leader follows *another* leader, the member chains upward to maintain a single core leader per pack.
- **State Computation**: Periodically (`computeFlockState`) aggregates spatial data from all active followers into the Leader's `FlockState`.

## 4. The `FollowLeaderGoal`

A vanilla Minecraft `Goal` that ties the system together. Consumer mods only need to instantiate this goal and add it to their `goalSelector`.

```java
// Example for a ground mob:
this.goalSelector.addGoal(5, new FollowLeaderGoal<>(this, GroupParameters.DEFAULT_TERRESTRIAL, 32.0D));
```

The goal automatically polls the `GroupManager` periodically and delegates execution to your defined `FlockingStrategy`.

### 4.1 Performance & Snapshot 26.1 Support

- **Tick Staggering**: `FollowLeaderGoal` uses an entity-ID-based offset (`mob.getId() % 10`) for path recalculations. This guarantees that a swarm of 50 entities spreads its AI load across 10 game ticks rather than spiking the server on a single tick.
- **Waypoint Attributes (Snapshot 26.1)**: The system natively respects the new vanilla attributes:
    - `Attributes.WAYPOINT_TRANSMIT_RANGE`: Determines the Leader's area of influence.
    - `Attributes.WAYPOINT_RECEIVE_RANGE`: Determines the Follower's detection sensitivity.
- **Backward Compatibility**: If these attributes are missing (e.g., on older Minecraft versions or unmodified entities), the system gracefully falls back to the `searchRadius` provided in the goal constructor.
