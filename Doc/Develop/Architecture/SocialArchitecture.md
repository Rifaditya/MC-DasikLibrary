# DasikLibrary Social Architecture

## Overview

The Social Architecture provides a "Hive Mind" for entities, allowing complex social interactions, event tracking, and persistent emotional states (moods).

## Core Components

### 1. GlobalSocialSystem ("The Pulse")

**Role**: The central ticker that drives all social logic.
**Key Behavior**:

- **Highlander Principle**: Ensures only ONE instance of the logic runs per tick, even if multiple mods bundle the library.
- **Lazy Freeze**: Locks the `SocialEventRegistry` on the first tick (Pulse 0) rather than during Mod Initialization. This prevents crashes when dependent mods try to register events after DasikLibrary has initialized.

### 2. SocialRegistry (O(1) Management)

**Role**: Efficient entity tracking and lookup.
**Architecture**:
- **Shard-based Storage**: Entities are distributed across concurrent shards to minimize lock contention.
- **Fast-Access Array**: Maintains a high-performance array of live entities for O(1) random selection during pulse ticks.
- **Weak References**: Uses `WeakReference` to ensure entities are automatically cleared when unloaded by Minecraft, preventing memory leaks.
- **Auto-Rebuild**: Automatically rebuilds the fast-access array when the registry version shifts.

### 3. SocialEventRegistry

**Role**: Holds all valid `SocialEvent` types.
**Behavior**:

- **Mutable Phase**: During Mod Initialization (`onInitialize`), mods can register new events.
- **Frozen Phase**: After the first game tick, the registry becomes immutable to ensure thread safety and stability.

## Usage Guide

### Registering a New Event

Do this in your `ModInitializer`.

```java
public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register early!
        SocialEventRegistry.register("mymod:event", new MyCustomEvent());
    }
}
```

### Scheduling an Event

Do this in your entity logic or AI Goals.

```java
SocialScheduler scheduler = entity.dasik$getScheduler();
if (scheduler != null) {
    scheduler.onSignalReceived(new Signal(entity, level, type, data));
}
```
