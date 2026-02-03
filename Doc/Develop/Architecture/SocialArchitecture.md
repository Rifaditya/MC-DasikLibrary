# DasikLibrary Social Architecture

## Overview

The Social Architecture provides a "Hive Mind" for entities, allowing complex social interactions, event tracking, and persistent emotional states (moods).

## Core Components

### 1. GlobalSocialSystem ("The Pulse")

**Role**: The central ticker that drives all social logic.
**Key Behavior**:

- **Highlander Principle**: Ensures only ONE instance of the logic runs per tick, even if multiple mods bundle the library.
- **Lazy Freeze**: Locks the `SocialEventRegistry` on the first tick (Pulse 0) rather than during Mod Initialization. This prevents crashes when dependent mods try to register events after DasikLibrary has initialized.
  - *See [PAT-20260202-001] Lazy Freeze Registry*

### 2. SocialEventRegistry

**Role**: Holds all valid `SocialEvent` types.
**Behavior**:

- **Mutable Phase**: During Mod Initialization (`onInitialize`), mods can register new events.
- **Frozen Phase**: After the first game tick, the registry becomes immutable to ensure thread safety and stability.
- **Error Handling**: Attempting to register after the freeze throws a `IllegalStateException`.

## Usage Guide

### Registering a New Event

Do this in your `ModInitializer`.

```java
public class MyMod implements ModInitializer {
    @Override
    public void onInitialize() {
        // Register early!
        SocialEventRegistry.register(new MyCustomEvent());
    }
}
```

### Scheduling an Event

Do this in your entity logic or AI Goals.

```java
SocialScheduler scheduler = entity.dasik$getScheduler();
// "main" is the track ID (e.g., mood, action)
scheduler.schedule("main", new MyCustomEvent());
```
