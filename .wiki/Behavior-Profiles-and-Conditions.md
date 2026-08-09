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
// Create a profile using DefaultProfileBuilder
BehaviorProfile netherProfile = new DefaultProfileBuilder("nether_hunter")
    .priority(10)
    .condition(BehaviorCondition.inDimension(Level.NETHER))
    .goals(configurator -> configurator.add(2, new FollowLeaderGoal<>(mob, GroupParameters.DEFAULT_TERRESTRIAL, 16.0D)))
    .build();

// Register profile on manager instance
BehaviorProfileManager manager = new BehaviorProfileManager();
manager.registerProfile(netherProfile);
```

---

## 🔗 Related Pages
* [[Social Scheduler & Events|Social-Scheduler-and-Events]]
* [[Architecture & Package Layout|Architecture-and-Package-Layout]]
