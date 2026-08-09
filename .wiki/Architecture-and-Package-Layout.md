# Architecture & Package Layout

| Property | Value |
| :--- | :--- |
| **Root Package** | `net.dasik.social` |
| **Architectural Law** | 1 File, 1 Purpose |
| **Design Pattern** | Facade, Observer, Strategy, Sharded Registry |

---

## 🏗️ Package Architecture Tree

```ascii
net.dasik.social/
├── DasikLibraryMod.java             ◄── Main Fabric Entrypoint
├── ai/
│   └── goal/
│       └── FollowLeaderGoal.java    ◄── AI Boids Flocking Goal
├── api/
│   ├── PriorityTier.java
│   ├── Scope.java
│   ├── SignalType.java
│   ├── SocialEntity.java            ◄── Core Interface for Social Mobs
│   ├── SocialEventRegistry.java
│   ├── config/                      ◄── Atomic Config & GUI Helpers
│   ├── enchantment/                 ◄── Runtime Enchantment Manager
│   ├── gamerule/                    ◄── Dynamic Namespaced GameRules
│   ├── genetics/                    ◄── Genetics Facade, Pedigree & Loot
│   ├── group/                       ◄── Flocking Strategies & Parameters
│   ├── profile/                     ◄── Behavior Profiles & Conditions
│   ├── projectile/                  ◄── Projectile Helper
│   └── vision/                      ◄── Player Vision Frustum Tracker
├── config/
│   └── PerformanceConfig.java
├── core/                            ◄── Hive Mind Engine & Schedulers
│   ├── EntitySocialScheduler.java
│   ├── GlobalSocialSystem.java
│   └── SocialRegistry.java
├── mixin/                           ◄── Fabric Mixin Interceptors
│   ├── LanguageMixin.java
│   ├── LivingEntityLootMixin.java
│   ├── MobGoalAccessor.java
│   ├── PathfinderMobMixin.java
│   └── ProfileTriggerMixin.java
└── util/                            ◄── Fast Math, PRNG & Version Guard
    ├── FastRandom.java
    ├── ModVersionGuard.java
    ├── ObjectPool.java
    ├── StochasticUtil.java
    └── TimeUtil.java
```

---

## 🔗 Related Pages
* [[Developer Setup & Building|Developer-Setup-and-Building]]
* [[Mixin Reference & Hooks|Mixin-Reference-and-Hooks]]
