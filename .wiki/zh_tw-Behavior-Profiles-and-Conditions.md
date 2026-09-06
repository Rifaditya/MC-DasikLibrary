# 行為設定檔與觸發條件

| 組件 | 類別 |
| :--- | :--- |
| **設定檔管理器** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **設定檔介面** | `net.dasik.social.api.profile.BehaviorProfile` |
| **條件介面** | `net.dasik.social.api.profile.BehaviorCondition` |
| **介面標記** | `net.dasik.social.api.profile.ProfileAware` |

---

## 🎭 概述與動態行為狀態機

`BehaviorProfileManager` 允許生物根據環境條件（例如夜間、低生命值、狼群領袖地位或天氣）動態切換 AI 設定檔。

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

## 💻 開發者代碼範例

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

## 🔗 相關頁面
* [[社交調度器與事件|zh_tw-Social-Scheduler-and-Events]]
* [[架構與套件結構設計|zh_tw-Architecture-and-Package-Layout]]
