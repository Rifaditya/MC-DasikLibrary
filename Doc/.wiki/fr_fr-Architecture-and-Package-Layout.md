# Architecture & Structure des Paquets

---

## 🌳 Arborescence des Paquets Source

```ascii
net.dasik.social
├── ai
│   ├── goal
│   │   ├── FollowLeaderGoal.java
│   │   └── GroundWanderGoal.java
│   └── strategy
│       ├── AerialFlockingStrategy.java
│       └── TerrestrialFlockingStrategy.java
├── api
│   ├── enchantment
│   │   └── DynamicEnchantmentManager.java
│   ├── gamerule
│   │   └── DynamicGameRuleManager.java
│   ├── genetics
│   │   ├── DasikAnimalGeneticsAPI.java
│   │   ├── EntityGenetics.java
│   │   ├── GeneticsEngine.java
│   │   ├── GeneticsLootModifier.java
│   │   └── GeneticsLootRegistry.java
│   └── group
│       ├── FlockType.java
│       ├── GroupMember.java
│       └── GroupParameters.java
├── config
│   ├── ClientGameRuleHelper.java
│   └── ConfigHelper.java
├── core
│   ├── EntitySocialScheduler.java
│   ├── GlobalSocialSystem.java
│   ├── ModVersionGuard.java
│   └── SocialRegistry.java
├── mixin
│   ├── LanguageMixin.java
│   ├── LivingEntityLootMixin.java
│   ├── MobGoalAccessor.java
│   ├── PathfinderMobMixin.java
│   └── ProfileTriggerMixin.java
└── util
    ├── FastRandom.java
    ├── PlayerVisionTracker.java
    └── StochasticUtil.java
```

---

## 🔗 Pages Liées
* [[Système Social d'Intelligence Collective|fr_fr-Hive-Mind-Social-System]]
* [[Référence Mixin et Points d'Injection|fr_fr-Mixin-Reference-and-Hooks]]
