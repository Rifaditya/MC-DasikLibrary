# Leader-Follower API

The DasikLibrary Leader-Follower API provides a modular, "zero-edit" extensible framework for swarming, flocking, and pack behaviors. It completely abstracts away the complex math of boids algorithms and terrestrial pathfinding into simple interfaces.

## 1. Core Interfaces

To make an entity use the system, it must implement `GroupMember<T>` (where `T` is the entity type, usually `Mob`):

```java
public interface GroupMember<T extends LivingEntity> {
    T getLeader();
    boolean hasLeader();
    void setLeader(@Nullable T leader);
    int getGroupSize();
    FlockType getFlockType(); // Flocking logic to use (AERIAL or TERRESTRIAL)
}
```

## 2. Flocking Strategies

The `FlockingStrategy` interface dictates *how* the `GroupMember` follows the leader.
The library ships with two default strategies in the `Strategies` class:

- **`Strategies.AERIAL`**: 3D Boids algorithm using `Vec3` delta movement manipulation for flying mobs (Bats, Phantoms). Supports block collision avoidance (floor/ceiling pushback), cohesion, and separation.
- **`Strategies.TERRESTRIAL`**: 2D ground pathfinding via Minecraft's `PathNavigation`. Best suited for wolves, pigs, or ground mobs.

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

## 4. The `FollowLeaderGoal`

A vanilla Minecraft `Goal` that ties the system together. Consumer mods only need to instantiate this goal and add it to their `goalSelector`.

```java
// Example for a ground mob:
this.goalSelector.addGoal(5, new FollowLeaderGoal<>(this, GroupParameters.DEFAULT_TERRESTRIAL, 32.0D));
```

The goal automatically polls the `GroupManager` periodically (to save performance rather than checking every tick) and delegates execution to your defined `FlockingStrategy`.
