# Hive Mind Social System

| System Parameter | Value |
| :--- | :--- |
| **Core Class** | `net.dasik.social.core.GlobalSocialSystem` |
| **Registry Class** | `net.dasik.social.core.SocialRegistry` |
| **Pulse Frequency** | `1 tick` (Highlander Rule) |
| **Lookup Time Complexity** | $O(1)$ Shard-based HashMap |
| **Memory Cleanup Strategy** | Automatic `isAlive()` & `isRemoved()` purging |

---

## ⚡ Overview & Highlander Rule

The **Hive Mind Social System** is the central pulse engine of Dasik Library. Rather than having hundreds of social entities each executing separate heavy spatial queries every tick, Dasik Library uses a single global pulse coordinator (`GlobalSocialSystem`).

### 👑 The Highlander Rule ("There Can Only Be One")
`GlobalSocialSystem` enforces that only **one global pulse cycle** runs per server game tick ($20\text{ ticks} = 1\text{s}$). If multiple threads or sub-systems attempt to trigger a tick within the same game tick, `GlobalSocialSystem` ignores duplicate calls:

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

## 🗂️ `SocialRegistry` Architecture

`SocialRegistry` maintains active social entities categorized by species and world UUIDs:

* **$O(1)$ Registration**: Entities register via `SocialRegistry.register(SocialEntity entity)`.
* **Automatic Purging**: Stale references (unloaded chunks, dead entities) are automatically purged during the pulse cycle using `entity.dasik$asEntity().isAlive()` and `isRemoved()`.
* **Species Sharding**: Entities are sharded by `dasik$getSpeciesId()`, enabling fast localized proximity queries for pack mechanics and flocking without scanning all world entities.

---

## 💻 Developer Code Example

```java
// Registering an entity to the Hive Mind
public class CustomSocialMob extends PathfinderMob implements SocialEntity {
    private final EntitySocialScheduler scheduler = new EntitySocialScheduler();

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            SocialRegistry.register(this);
            GlobalSocialSystem.tick(this.level());
        }
    }

    @Override
    public SocialScheduler dasik$getScheduler() {
        return this.scheduler;
    }
}
```

---

## 🔗 Related Pages
* [[Social Scheduler & Events|Social-Scheduler-and-Events]]
* [[Architecture & Package Layout|Architecture-and-Package-Layout]]
