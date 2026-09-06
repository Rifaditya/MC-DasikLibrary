# 行为配置文件与触发条件

| 组件 | 类 |
| :--- | :--- |
| **配置文件管理器** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **配置文件接口** | `net.dasik.social.api.profile.BehaviorProfile` |
| **条件接口** | `net.dasik.social.api.profile.BehaviorCondition` |
| **接口标记** | `net.dasik.social.api.profile.ProfileAware` |

---

## 🎭 概述与动态行为状态机

`BehaviorProfileManager` 允许生物根据环境条件（例如夜间、低生命值、狼群领袖地位或天气）动态切换 AI 配置文件。

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

## 💻 开发者代码示例

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

## 🔗 相关页面
* [[社交调度器与事件|zh_cn-Social-Scheduler-and-Events]]
* [[架构与包结构设计|zh_cn-Architecture-and-Package-Layout]]
