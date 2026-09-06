# 蜂巢思维社交系统

| 系统参数 | 设定值 |
| :--- | :--- |
| **核心类** | `net.dasik.social.core.GlobalSocialSystem` |
| **注册表类** | `net.dasik.social.core.SocialRegistry` |
| **脉冲频率** | `1 tick` (Highlander 法则) |
| **查找时间复杂度** | $O(1)$ 分片式 HashMap |
| **内存清理策略** | 自动依据 `isAlive()` 与 `isRemoved()` 清理 |

---

## ⚡ 概述与 Highlander 法则

**蜂巢思维社交系统** 是 Dasik Library 的中央脉冲心脏。传统模式下数百个社交实体各自每 Tick 执行沉重的空间射线查询，而 Dasik Library 采用单一全局脉冲协调器（`GlobalSocialSystem`）。

### 👑 Highlander 法则（"独尊法则"）
`GlobalSocialSystem` 强制每个服务器游戏 Tick（$20\text{ ticks} = 1\text{s}$）仅运行**一次全局脉冲周期**。如果多个线程或子系统在同一个 Tick 内尝试触发 Tick，`GlobalSocialSystem` 将自动忽略重复调用：

$$\text{Global Pulse Execution} = \begin{cases} \text{Execute Ticks}, & \text{if } \text{currentTick} > \text{lastTick} \\ \text{Skip (No-Op)}, & \text{if } \text{currentTick} \le \text{lastTick} \end{cases}$$

```ascii
[ Server Level Tick ]
         │
         ▼
 ┌──────────────────────┐
 │ GlobalSocialSystem   │  ◄── Highlander Rule Guard (1 tick per server tick)
 └──────────┬───────────┘
            │
            ▼
 ┌──────────────────────┐
 │   SocialRegistry     │  ◄── O(1) Shard Lookup & Dead Entity Purge
 └──────────┬───────────┘
            │
      ┌─────┴────────────────┐
      ▼                      ▼
 ┌──────────────┐      ┌──────────────┐
 │ Entity 1     │      │ Entity N     │
 │ Mood Task    │      │ Mood Task    │
 └──────────────┘      └──────────────┘
```

---

## 🗂️ `SocialRegistry` 架构

`SocialRegistry` 维护当前活动的社交实体，按物种与世界 UUID 分类存储：

* **$O(1)$ 注册**：实体通过 `SocialRegistry.register(SocialEntity entity)` 进行注册。
* **自动清理**：在脉冲周期中，失效的引用（未加载区块、已死亡实体）会通过 `entity.dasik$asEntity().isAlive()` 和 `isRemoved()` 被自动清除。
* **物种分片**：实体按照 `dasik$getSpeciesId()` 进行分片存储，为狼群机制与鸟群集群提供极速的局部邻近查询，无需遍历世界中的所有实体。

---

## 💻 开发者代码示例

```java
// Registering an entity to the Hive Mind
public class CustomSocialMob extends PathfinderMob implements SocialEntity {
    private final EntitySocialScheduler scheduler = new EntitySocialScheduler();

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            SocialRegistry.register(this);
            GlobalSocialSystem.pulse(serverLevel);
        }
    }

    @Override
    public SocialScheduler dasik$getScheduler() {
        return this.scheduler;
    }
}
```

---

## 🔗 相关页面
* [[社交调度器与事件|zh_cn-Social-Scheduler-and-Events]]
* [[架构与包结构设计|zh_cn-Architecture-and-Package-Layout]]
