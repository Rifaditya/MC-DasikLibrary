# 架构与包结构设计

| 属性 | 设定值 |
| :--- | :--- |
| **根包** | `net.dasik.social` |
| **架构法则** | 1 File, 1 Purpose |
| **设计模式** | Facade, Observer, Strategy, Sharded Registry |

---

## 🏗️ 包架构目录树

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

## 🔗 相关页面
* [[开发者环境配置与构建|zh_cn-Developer-Setup-and-Building]]
* [[Mixin 参考与注入钩子|zh_cn-Mixin-Reference-and-Hooks]]
