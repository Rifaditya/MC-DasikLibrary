# アーキテクチャとパッケージ構成

---

## 🌳 パッケージツリー構造

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

## 🔗 関連ページ
* [[群知能ソーシャルシステム|ja_jp-Hive-Mind-Social-System]]
* [[Mixin リファレンスとフック|ja_jp-Mixin-Reference-and-Hooks]]
