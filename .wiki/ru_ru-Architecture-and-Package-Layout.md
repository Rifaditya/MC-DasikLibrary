# Архитектура и структура пакетов

| Свойство | Значение |
| :--- | :--- |
| **Корневой пакет** | `net.dasik.social` |
| **Архитектурный принцип** | 1 File, 1 Purpose (1 файл — 1 назначение) |
| **Шаблоны проектирования** | Фасад, Наблюдатель, Стратегия, Шардированный реестр |

---

## 🏗️ Дерево пакетов архитектуры

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

## 🔗 Связанные страницы
* [[Настройка среды разработчика и сборка|ru_ru-Developer-Setup-and-Building]]
* [[Справочник Mixin и точки внедрения|ru_ru-Mixin-Reference-and-Hooks]]
