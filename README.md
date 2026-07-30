# Dasik Library

**Social AI & Infrastructure Engine for Minecraft Mods**

A shared Fabric library providing the "Hive Mind" social behavior system, Genetics Engine, Boids Flocking, and Dynamic GameRule localization for Vanilla Outsider, Instant Gratification, and Delayed Gratification mods.

---

## Philosophy

**"Thin Mod, Fat Library" & "One Brain, Many Minds"**

Instead of each mod duplicating social AI and infrastructure code, Dasik Library provides:

- **GlobalSocialSystem**: Single pulse engine using Highlander Rule (only one tick per game tick).
- **SocialRegistry**: Shard-based O(1) entity tracking with automated memory management.
- **EntitySocialScheduler**: Per-entity dual-track scheduler (Mood & Ambient).
- **Current Version**: `1.8.5` (Minecraft 26.2+)
- **Leader-Follower API**: Generalized flocking and swarming with modular strategies (Aerial/Terrestrial).
- **DynamicGameRuleManager**: Generate infinite GameRules dynamically with automatic runtime English translation injection.
- **Genetics & Breeding Engine**: Entity-agnostic genetics attachment, inbreeding verification, and linked attribute scaling.
- **ModVersionGuard**: Standalone, Knot-safe ClassLoader startup guard for open-ended version bounds (`"minecraft": ">=26.2-"`).

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

- **Better Dogs**: Wolf/dog pack dynamics, howling, and leader-follower flocking.
- **MCA Female Gender Bridge**: Genetics & physics solver integration.
- **Collapsible Game Rule Screen**: Category grouping and dynamic GameRule registration.
- **Bat Ecology**: Bat social behaviors & murmuration.
- **Ore Amplifier**: Stochastic generation scaling.

---

## License

GNU Lesser General Public License v3.0 (LGPL-3.0)

---

## Links

- [GitHub Repository](https://github.com/Rifaditya/DasikLibrary-Rebuilt)
- [Modrinth](https://modrinth.com/mod/dasik-library)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
