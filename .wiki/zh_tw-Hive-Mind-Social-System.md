# 蜂巢思維社交系統

| 系統參數 | 設定值 |
| :--- | :--- |
| **核心類別** | `net.dasik.social.core.GlobalSocialSystem` |
| **註冊表類別** | `net.dasik.social.core.SocialRegistry` |
| **脈衝頻率** | `1 tick` (Highlander 法則) |
| **查找時間複雜度** | $O(1)$ 分片式 HashMap |
| **內存清理策略** | 自動依據 `isAlive()` 與 `isRemoved()` 清理 |

---

## ⚡ 概述與 Highlander 法則

**蜂巢思維社交系統** 是 Dasik Library 的中央脈衝心臟。傳統模式下數百個社交實體各自每 Tick 執行沈重的空間射線查詢，而 Dasik Library 採用單一全局脈衝協調器（`GlobalSocialSystem`）。

### 👑 Highlander 法則（"獨尊法則"）
`GlobalSocialSystem` 強制每個伺服器遊戲 Tick（$20\text{ ticks} = 1\text{s}$）僅運行**一次全局脈衝週期**。如果多個執行緒或子系統在同一個 Tick 內嘗試觸發 Tick，`GlobalSocialSystem` 將自動忽略重複調用：

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

## 🗂️ `SocialRegistry` 架構

`SocialRegistry` 維護當前活動的社交實體，按物種與世界 UUID 分類存儲：

* **$O(1)$ 註冊**：實體透過 `SocialRegistry.register(SocialEntity entity)` 進行註冊。
* **自動清理**：在脈衝週期中，失效的引用（未加載區塊、已死亡實體）會透過 `entity.dasik$asEntity().isAlive()` 和 `isRemoved()` 被自動清除。
* **物種分片**：實體按照 `dasik$getSpeciesId()` 進行分片存儲，為狼群機制與鳥群群聚提供極速的局部鄰近查詢，無需遍歷世界中的所有實體。

---

## 💻 開發者代碼範例

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

## 🔗 相關頁面
* [[社交調度器與事件|zh_tw-Social-Scheduler-and-Events]]
* [[架構與套件結構設計|zh_tw-Architecture-and-Package-Layout]]
