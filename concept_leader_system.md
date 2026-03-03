# Concept: Modular Leader-Follower System

## Objective

Migrate and generalize the Leader-Follower system from `Bat Ecology` to `DasikLibrary` to allow any entity to easily implement swarming, schooling, or group behavior.

## Design Principle: Zero-Edit Extensibility
>
> [!IMPORTANT]
> **Mandate**: All systems must allow consuming mods to inject custom behavior (Strategy Pattern, Registry, or Events) **without** modifying the library source code.
>
> - **Configuration**: Logic parameters (speed, radius) must be overridable per entity.
> - **Logic**: Core behavior (Movement, Target Selection) must be interchangeable via Interfaces.

## Core Components

### 1. API (`net.dasik.social.api.group`)

- **`GroupMember<T extends LivingEntity>`**: Interface for entities that can join a group.
  - `getLeader()`: Returns current leader.
  - `hasLeader()`: Boolean check.
  - `setLeader(T leader)`: Updates leader.
  - `getGroupSize()`: Returns perceived size of the group.
  - `getFlockType()`: Returns `FlockType` (AERIAL/TERRESTRIAL).

### 2. Group Management (`net.dasik.social.core.group`)

- **`GroupManager`**: A static or singleton manager that handles:
  - **Discovery**: Finding nearby compatible members (using Generics).
  - **Leader Election**: Deterministic election (e.g., UUID sort, strength, longevity).
  - **Caching**: Performance optimization (reusing the chunk-based logic from Bat Ecology).

### 3. AI Goals (`net.dasik.social.ai.goal`)

- **`FollowLeaderGoal<T>`**: A generic Goal that:
  - Checks for a leader via `GroupManager`.
  - **Delegates Movement**: Calls `mob.getFlockingStrategy().execute(mob, leader)`.
  - This ensures the Goal itself never needs modification for new movement types.

### 4. Customization (`net.dasik.social.api.group.strategy`)

- **`FlockingStrategy`**: Interface for defining HOW an entity moves.
  - Default Implementations provided:
    - `Strategies.AERIAL`: 3D Velocity control (Bats).
    - `Strategies.TERRESTRIAL`: Pathfinding (Wolves).
  - **Customizable**: Mods can implement their own `MyCustomStrategy` (e.g., Teleporting, Strafing) without editing the library.

- **`GroupParameters`**:
  - Defines `cohesionRadius`, `separationRadius`, `maxSpeed`.
  - Overridable by the entity.

## Implementation Details

### Boid Logic (Generalization)

The `applyFollowerBehavior` from `Bat Ecology` will be adapted into a utility or a strategy pattern in `FollowLeaderGoal`.

- **Cohesion**: Stick to leader.
- **Separation**: Don't crowd (dynamic radius).
- **Environment Awareness**: Floor/Ceiling avoidance (configurable).

### Integration

- **Bat Ecology**: Will be refactored to implement `GroupMember<Bat>` and usage of `FollowLeaderGoal`.
- **Future Mods**: Can implement `GroupMember` and add `FollowLeaderGoal` to initGoals.

## Verification

- **Parity**: Bat behavior must remain identical (or improved) after migration.
- **Performance**: Determine if `GroupManager` incurs overhead.
- **Scalability**: Verify it works for other entities (e.g., Fish, Wolves) conceptually.
