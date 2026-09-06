# 아키텍처 및 패키지 구조

---

## 🌳 패키지 트리 계층 구조

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

## 🔗 관련 페이지
* [[군집 지능 소셜 시스템|ko_kr-Hive-Mind-Social-System]]
* [[Mixin 참조 및 주입 훅|ko_kr-Mixin-Reference-and-Hooks]]
