# Social Scheduler & Event System

| Component | Class |
| :--- | :--- |
| **Scheduler Engine** | `net.dasik.social.core.EntitySocialScheduler` |
| **Event Registry** | `net.dasik.social.api.SocialEventRegistry` |
| **Priority Levels** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`, `BACKGROUND`) |
| **Signal Modes** | `SignalType` (`EMERGENCY`, `SOCIAL`, `AMBIENT`, `TACTICAL`) |

---

## 🔄 Dual-Track Scheduling Model

Every social entity carries an `EntitySocialScheduler` which processes dual-track tasks:

1. **Mood Track (Long-Term Behavioral States)**: Evaluated at lower frequencies (e.g. every 20-100 ticks) to update emotional baseline, aggression levels, or stamina.
2. **Ambient Track (Short-Term Tactical Signals)**: Evaluated at high frequencies (every 1-5 ticks) for immediate reaction to signals, threats, or flocking adjustments.

```ascii
                      ┌───────────────────────────────┐
                      │    EntitySocialScheduler      │
                      └───────────────┬───────────────┘
                                      │
              ┌───────────────────────┴───────────────────────┐
              ▼                                               ▼
   ┌─────────────────────┐                         ┌─────────────────────┐
   │     Mood Track      │                         │    Ambient Track    │
   │ (Low Frequency Ticks)│                         │(High Frequency Ticks)│
   │ - Pack Hierarchy   │                         │ - Obstacle Avoidance│
   │ - Hunger / Fatigue  │                         │ - Signal Response   │
   └─────────────────────┘                         └─────────────────────┘
```

---

## 📢 `SocialEventRegistry` & Signal Dispatch

Mods can broadcast social signals to surrounding entities using `SocialEventRegistry`:

```java
// Registering a social event handler
SocialEventRegistry.register("betterdogs:howl", (source, target, signalType, context) -> {
    if (signalType == SignalType.TACTICAL) {
        target.getNavigation().moveTo(source.getX(), source.getY(), source.getZ(), 1.25D);
    }
});

// Broadcasting an event
SocialEventRegistry.broadcast(
    sourceEntity, 
    32.0D, // Radius in blocks
    SignalType.TACTICAL, 
    PriorityTier.HIGH, 
    "betterdogs:howl"
);
```

---

## 📊 Priority Tier Matrix

| Tier | Priority Value | Execution Policy | Typical Usage |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `0` | Immediate override, bypasses cooldowns | Combat survival, hazard escape |
| `HIGH` | `1` | Preempts normal goals | Pack call response, horn commands |
| `NORMAL` | `2` | Standard priority | Social greeting, idle flocking |
| `LOW` | `3` | Yields to active goals | Ambient inspection |
| `BACKGROUND` | `4` | Runs when idle | Background mood updating |

---

## 🔗 Related Pages
* [[Hive Mind Social System|Hive-Mind-Social-System]]
* [[Behavior Profiles & Conditions|Behavior-Profiles-and-Conditions]]
