# Arquitectura y Distribución de Paquetes

| Propiedad | Valor |
| :--- | :--- |
| **Paquete Raíz** | `net.dasik.social` |
| **Regla de Arquitectura** | 1 File, 1 Purpose (1 Archivo, 1 Propósito) |
| **Patrones de Diseño** | Fachada, Observador, Estrategia, Registro Fragmentado |

---

## 🏗️ Árbol Arquitectónico de Paquetes

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

## 🔗 Páginas Relacionadas
* [[Configuración de Desarrollador y Compilación|es_es-Developer-Setup-and-Building]]
* [[Referencia de Mixins y Puntos de Inyección|es_es-Mixin-Reference-and-Hooks]]
