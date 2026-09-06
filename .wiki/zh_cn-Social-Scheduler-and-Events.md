# 社交调度器与事件系统

| 组件 | 类 |
| :--- | :--- |
| **调度器引擎** | `net.dasik.social.core.EntitySocialScheduler` |
| **事件注册表** | `net.dasik.social.api.SocialEventRegistry` |
| **优先级别** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **信号模式** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

---

## 🔄 双轨调度模型

每个社交实体均持有一个 `EntitySocialScheduler`，负责并行处理双轨任务：

1. **Mood Track (情绪长轨/长期行为状态)**：以较低频率（例如每 20-100 Tick）评估，用于更新情绪基准线、攻击倾向或耐力。
2. **Ambient Track (环境短轨/短期战术信号)**：以高频率（每 1-5 Tick）评估，用于对突发信号、威胁或集群转向调整作出即时反应。

```ascii
                      ┌───────────────────────────────┐
                      │    EntitySocialScheduler      │
                      └───────────────┬───────────────┘
                                      │
              ┌───────────────────────┴───────────────────────┐
              ▼                                               ▼
   ┌─────────────────────┐                         ┌─────────────────────┐
   │     Mood Track      │                         │    Ambient Track    │
   │ (Low Frequency Ticks)│                         │(High Frequency Ticks)│
   │ - Pack Hierarchy   │                         │ - Obstacle Avoidance│
   │ - Hunger / Fatigue  │                         │ - Signal Response   │
   └─────────────────────┘                         └─────────────────────┘
```

---

## 📢 `SocialEventRegistry` 与事件实现

下游模组实现 `SocialEvent` 并在模组初始化时向 `SocialEventRegistry` 注册实例：

```java
public class HowlEvent implements SocialEvent {
    @Override public String getId() { return "betterdogs:howl"; }
    @Override public int getPriorityValue() { return 80; }
    @Override public String getTrackId() { return "pack_command"; }
    @Override public boolean canPreempt(SocialEvent other) { return other.getPriorityValue() < 80; }
    @Override public void onStart(TickContext context) {}
    @Override public boolean tick(TickContext context) { return false; }
    @Override public void onEnd(SocialEntity entity, EndReason reason) {}
}

// Register during mod initialization (frozen on first pulse)
SocialEventRegistry.register(new HowlEvent());
```

---

## 📊 优先级别矩阵

| Tier | 最大轨道数 | 分配策略 | 典型用途 |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | 即时覆盖，最高优先级 | 脱离危险、战斗求生 |
| `HIGH` | `8` | 抢占低/普通优先级任务 | 战术指令、狼群召唤 |
| `NORMAL` | `16` | 标准执行预算 | 社交互动、常规游荡 |
| `LOW` | `32` | 最大容量，低优先级 | 后台环境巡视 |

---

## 🔗 相关页面
* [[蜂巢思维社交系统|zh_cn-Hive-Mind-Social-System]]
* [[行为配置文件与触发条件|zh_cn-Behavior-Profiles-and-Conditions]]
