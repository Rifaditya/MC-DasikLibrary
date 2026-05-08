# Dasik Library

**Social AI Engine for Minecraft Mods**

A shared Fabric library providing the "Hive Mind" social behavior system for Vanilla Outsider mods.

---

## Philosophy

**"One Brain, Many Minds"**

Instead of each mod duplicating social AI code, Dasik Library provides:

- **GlobalSocialSystem** - Single pulse engine using Highlander Rule (only one tick per game tick)
- **SocialRegistry** - Shard-based O(1) entity tracking with automated memory management
- **EntitySocialScheduler** - Per-entity dual-track scheduler (Mood & Ambient)
- **Current Version**: `1.6.9+build.23` (Snapshot 26.1)
- **Leader-Follower API** - Generalized flocking and swarming with modular strategies (Aerial/Terrestrial)
- **DynamicGameRuleManager** - Generate infinite GameRules dynamically with automatic runtime English translation injection.

---

## For Mod Developers

### Dependency

```json
// fabric.mod.json
"depends": {
    "dasik-library": "*"
}
```

### Interface Implementation

Your entity mixin implements `SocialEntity`:

```java
public interface SocialEntity {
    long dasik$getDNA();
    String dasik$getSpeciesId();
    LivingEntity dasik$asEntity();
    float dasik$getSocialScale();
    @Nullable SocialScheduler dasik$getScheduler();
}
```

### Registering Events

```java
SocialEventRegistry.register("mymod:howl", new HowlEvent());
```

### Leader-Follower Configuration

To support group flocking/swarming (aerial or terrestrial), implement `GroupMember` on your entity and add `FollowLeaderGoal` to your AI goals:

```java
public interface GroupMember {
    @Nullable LivingEntity getLeader();
    void setLeader(@Nullable LivingEntity leader);
    int getGroupSize();
    FlockType getFlockType(); // AERIAL or TERRESTRIAL
}
```

```java
// Inside your entity's registerGoals
this.goalSelector.addGoal(5, new FollowLeaderGoal<>(this, GroupParameters.DEFAULT_TERRESTRIAL, 32.0D));
```

---

## Consumer Mods

- **Bat Ecology** - Bat social behaviors
- **Better Dogs** - Wolf/dog pack dynamics
- **Ore Amplifier** - Stochastic generation scaling

---

## License

GNU General Public License v3.0

---

## Links

- [GitHub Repository](https://github.com/DasikIgaijinn/DasikLibrary)
- [Modrinth](https://modrinth.com/mod/dasik-library)
