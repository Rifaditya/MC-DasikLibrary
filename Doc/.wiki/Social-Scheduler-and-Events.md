# Social Scheduler & Event System

| Component | Class |
| :--- | :--- |
| **Scheduler Engine** | `net.dasik.social.core.EntitySocialScheduler` |
| **Event Registry** | `net.dasik.social.api.SocialEventRegistry` |
| **Priority Levels** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **Signal Modes** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

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

## 📢 `SocialEventRegistry` & Event Implementation

Mods implement `SocialEvent` and register instances with `SocialEventRegistry` during mod initialization:

```java
public class HowlEvent implements SocialEvent {
    @Override public String getId() { return "betterdogs:howl"; }
    @Override public int getPriorityValue() { return 80; }
    @Override public String getTrackId() { return "pack_command"; }
    @Override public boolean canPreempt(SocialEvent other) { return other.getPriorityValue() < 80; }
    @Override public void onStart(TickContext context) {}
    @Override public boolean tick(TickContext context) { return false; }
    @Override public void onEnd(SocialEntity entity, EndReason reason) {}
}

// Register during mod initialization (frozen on first pulse)
SocialEventRegistry.register(new HowlEvent());
```

---

## 📊 Priority Tier Matrix

| Tier | Max Tracks | Allocation Policy | Typical Usage |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | Immediate override, high priority | Hazard escape, combat survival |
| `HIGH` | `8` | Preempts low/normal tasks | Tactical commands, pack calls |
| `NORMAL` | `16` | Standard execution budget | Social interaction, idle movement |
| `LOW` | `32` | Max capacity, low priority | Background ambient inspection |

---

## 🔗 Related Pages
* [[Hive Mind Social System|Hive-Mind-Social-System]]
* [[Behavior Profiles & Conditions|Behavior-Profiles-and-Conditions]]
