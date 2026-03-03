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
- O(1) shard-based entity registry for high-performance selection

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
    SocialScheduler dasik$getScheduler();
}
```

### 2. GlobalSocialSystem

**Highlander Rule:** Only ONE pulse per game tick, regardless of how many mods exist.

```java
AtomicLong LAST_TICK; // Prevents duplicate execution
ENGINE_VERSION = 261; // Aligned with MC 26.1
```

### 3. SocialRegistry

Shard-based `WeakReference` tracking. O(1) access and selection. Auto-rebuilds on registry mutation.

### 4. Entity Social Scheduler

Per-entity dual-track scheduler:

- **Mood Track** (HIGH priority): Interruptive behaviors
- **Ambient Track** (LOW priority): Background behaviors

### 5. SocialEvent (Interface)

Contract for all behavior events.

---

## Signal System

Signals are represented as Java 25 `record` types for thread-safe immutability.

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
4. Call `SocialRegistry.register()` on entity initialization

---

## Version Compatibility

| Engine Version | Mod Version | API Changes |
|----------------|-------------|-------------|
| 100 | 1.0.0 | Initial release |
| 160 | 1.6.0 | AI Behavior Profile System |
| 261 | 1.6.9+build.13 | Java 25 Records, O(1) Shard Registry, Snapshot 10 Support |

---

## 6. AI Behavior Profile System (v1.6.0+)

Switchable behavior profiles for context-aware AI.

### Core Classes

```java
BehaviorProfile       // Holds goals + conditions
BehaviorProfileManager // Per-entity profile switcher
```

### Trigger Events

| Trigger | Method |
|:--------|:-------|
| Dimension | Mixin into `changeDimension` |
| Biome | Check on chunk entry |
| State | Manual `markDirty()` |
| Manual | `setActiveProfile(id)` |

---

## 7. Dynamic GameRule Manager (v1.6.9+)

Allows seamless creation of GameRules with automatic English translation injection.
- `registerInteger` / `registerBoolean`: Returns standard GameRules while caching human-readable translations.
- `LanguageMixin`: Injects cached translations directly into Minecraft's `Language` map.
- `DynamicGameRuleManager`: Centralized registry for non-static gamerules.

---

## Consumer Mods

- **Bat Ecology** - Bat social behaviors
- **Better Dogs** - Wolf/dog pack dynamics
- **Ore Amplifier** - Stochastic generation scaling
