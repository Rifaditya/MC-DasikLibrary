# 社交調度器與事件系統

| 組件 | 類別 |
| :--- | :--- |
| **調度器引擎** | `net.dasik.social.core.EntitySocialScheduler` |
| **事件註冊表** | `net.dasik.social.api.SocialEventRegistry` |
| **優先級別** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **信號模式** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

---

## 🔄 雙軌調度模型

每個社交實體均持有一個 `EntitySocialScheduler`，負責並行處理雙軌任務：

1. **Mood Track (情緒長軌/長期行為狀態)**：以較低頻率（例如每 20-100 Tick）評估，用於更新情緒基準線、攻擊傾向或耐力。
2. **Ambient Track (環境短軌/短期戰術信號)**：以高頻率（每 1-5 Tick）評估，用於對突發信號、威脅或群聚轉向調整作出即時反應。

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

## 📢 `SocialEventRegistry` 與事件實現

下游模組實現 `SocialEvent` 並在模組初始化時向 `SocialEventRegistry` 註冊實例：

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

## 📊 優先級別矩陣

| Tier | 最大軌道數 | 分配策略 | 典型用途 |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | 即時覆蓋，最高優先級 | 脫離危險、戰鬥求生 |
| `HIGH` | `8` | 搶佔低/普通優先級任務 | 戰術指令、狼群召喚 |
| `NORMAL` | `16` | 標準執行預算 | 社交互動、常規遊蕩 |
| `LOW` | `32` | 最大容量，低優先級 | 後台環境巡視 |

---

## 🔗 相關頁面
* [[蜂巢思維社交系統|zh_tw-Hive-Mind-Social-System]]
* [[行為設定檔與觸發條件|zh_tw-Behavior-Profiles-and-Conditions]]
