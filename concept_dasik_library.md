# Dasik Library - Concept Document

## Identity

**Name:** Dasik Library  
**modid:** `dasik-library`  
**Package:** `net.dasik.social`  
**License:** GNU GPL v3  

---

## Purpose

Shared "Hive Mind" social AI engine for Vanilla Outsider mods. Provides:

- Unified entity behavior scheduling
- Leader-follower dynamics across species
- Cross-mod signal broadcasting
- O(1) random entity selection for pulse ticks

---

## Core Components

### 1. SocialEntity (Interface)

Implemented by entity mixins. Prefix: `dasik$`

```java
interface SocialEntity {
    long dasik$getDNA();
    String dasik$getSpeciesId();
    LivingEntity dasik$asEntity();
    float dasik$getSocialScale();
    EntitySocialScheduler dasik$getScheduler();
}
```

### 2. GlobalSocialSystem

**Highlander Rule:** Only ONE pulse per game tick, regardless of how many mods exist.

```java
AtomicLong LAST_TICK; // Prevents duplicate execution
ENGINE_VERSION = 100; // For future compatibility
```

### 3. SocialRegistry

WeakHashMap-based tracking. Auto-cleanup on entity unload.

### 4. EntitySocialScheduler

Per-entity dual-track scheduler:

- **Mood Track** (HIGH priority): Interruptive behaviors
- **Ambient Track** (LOW priority): Background behaviors

### 5. SocialEvent (Interface)

Contract for all behavior events.

---

## Signal System

```java
GlobalSocialSystem.broadcastSignal(SignalType.THUNDER, source, Scope.PUBLIC);
```

| Signal | Description |
|--------|-------------|
| `OWNER_EATING` | Owner consuming food |
| `THUNDER` | Thunder event |
| `DANGER` | Threat detected |
| `SOCIAL_INVITE` | Play/interaction request |

---

## Consumer Integration

1. Add dependency in `fabric.mod.json`
2. Mixin implements `SocialEntity` with `dasik$` methods
3. Register events with `SocialEventRegistry.register()`
4. Call `SocialRegistry.registerEntity()` on entity init

---

## Version Compatibility

| Engine Version | API Changes |
|----------------|-------------|
| 100 (1.0.0) | Initial release |
| 160 (1.6.0) | AI Behavior Profile System |

---

## 6. AI Behavior Profile System (v1.6.0+)

Switchable behavior profiles for context-aware AI.

### Core Classes

```java
BehaviorProfile       // Holds goals + conditions
BehaviorProfileManager // Per-entity profile switcher
```

### Design Rules

> [!CAUTION]
> **GUIDE (DO NOT DELETE)**
>
> - Max **5 conditions** per profile
> - Conditions: Dimension, Biome, Time, State, Custom
> - Highest match count wins priority
> - **Event-driven switching** (NOT polling):
>   - Dimension change → auto-evaluate
>   - Biome change → auto-evaluate  
>   - State change → call `markDirty()`
> - See: `Doc/Develop/profile_guide.md`

### Trigger Events

| Trigger | Method |
|:--------|:-------|
| Dimension | Mixin into `changeDimension` |
| Biome | Check on chunk entry |
| State | Manual `markDirty()` |
| Manual | `setActiveProfile(id)` |

### 6. Math Utils

- **StochasticUtil**: Probability-based rounding for fractional multipliers. Used by Ore Amplifier to handle sub-100% generation rates.

---

## Consumer Mods

- **Bat Ecology** - Bat social behaviors
- **Better Dogs** - Wolf/dog pack dynamics
- **Ore Amplifier** - Stochastic generation scaling
