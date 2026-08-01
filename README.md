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
- **Current Version**: `1.8.9` (Minecraft 26.1.2+)
- **DasikAnimalGeneticsAPI**: High-level universal genetics facade providing Size-Stats & Scale (`0.5x` - `2.0x`), Kinship & Pedigree family tree risk prediction, and Dynamic Trait Modifiers & Stat Reset across all animal mods.
- **Leader-Follower API**: Generalized flocking and swarming with modular strategies (Aerial/Terrestrial).
- **DynamicGameRuleManager**: Generate infinite GameRules dynamically with automatic runtime English translation injection.
- **Genetics & Breeding Engine**: Entity-agnostic genetics attachment, inbreeding verification, and linked attribute scaling.
- **ModVersionGuard**: Standalone, Knot-safe ClassLoader startup guard for open-ended version bounds (`"minecraft": ">=26.1.2-"`).

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

// Inside your entity's registerGoals
this.goalSelector.addGoal(5, new FollowLeaderGoal<>(this, GroupParameters.DEFAULT_TERRESTRIAL, 32.0D));
```

### Animal Genetics API (`DasikAnimalGeneticsAPI`)

```java
// Size-Stats & Scale API
float scale = DasikAnimalGeneticsAPI.getScale(entity); // Clamped [0.5f, 2.0f]
DasikAnimalGeneticsAPI.setScale(entity, 1.5f);
boolean isRunt = DasikAnimalGeneticsAPI.isRunt(entity);

// Kinship & Pedigree API
boolean isRelated = DasikAnimalGeneticsAPI.isRelated(animal1, animal2);
int inbreedingRisk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(parent1, parent2);

// Dynamic Trait Modifiers & Stat Reset API
DasikAnimalGeneticsAPI.setTrait(entity, "max_health", 5.0f);
DasikAnimalGeneticsAPI.modifyTrait(entity, "max_health", 3.0f);
DasikAnimalGeneticsAPI.resetGenetics(entity);
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
