# Behavior Profiles & Conditions

| Component | Class |
| :--- | :--- |
| **Profile Manager** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **Profile Interface** | `net.dasik.social.api.profile.BehaviorProfile` |
| **Condition Interface** | `net.dasik.social.api.profile.BehaviorCondition` |
| **Interface Marker** | `net.dasik.social.api.profile.ProfileAware` |

---

## 🎭 Overview & Dynamic Behavior State Machines

`BehaviorProfileManager` enables mobs to switch dynamic AI profiles based on environment conditions (e.g. night time, low health, pack leadership, or weather).

```ascii
[ LivingEntity Tick ]
         │
         ▼
┌─────────────────────────────┐
│    BehaviorProfileManager   │  ◄── Evaluate Conditions
└────────┬────────────┬───────┘
         │            │
         ▼            ▼
   [ Night Profile ] [ Combat Profile ]
```

---

## 💻 Developer Code Example

```java
BehaviorProfile nightProfile = DefaultProfileBuilder.create("night_hunter")
    .when(BehaviorCondition.isNight())
    .withSpeedMultiplier(1.35f)
    .build();

BehaviorProfileManager.register(EntityTypes.WOLF, nightProfile);
```

---

## 🔗 Related Pages
* [[Social Scheduler & Events|Social-Scheduler-and-Events]]
* [[Architecture & Package Layout|Architecture-and-Package-Layout]]
