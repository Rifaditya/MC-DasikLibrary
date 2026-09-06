# ソーシャルスケジューラ＆イベントシステム

| コンポーネント | クラス |
| :--- | :--- |
| **スケジューラエンジン** | `net.dasik.social.core.EntitySocialScheduler` |
| **イベントレジストリ** | `net.dasik.social.api.SocialEventRegistry` |
| **優先度階層** | `PriorityTier`（`CRITICAL`, `HIGH`, `NORMAL`, `LOW`） |
| **シグナル種別** | `SignalType`（`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`） |

---

## 🔄 デュアルトラック・スケジューリングモデル

各ソーシャルエンティティは独自の `EntitySocialScheduler` を持ち、2 本の並行トラックでタスクを評価・実行します：

1. **Mood トラック（長期心理・状態変化）**: 低頻度（例: 20〜100 Tick ごと）で実行され、群れの序列、空腹度、スタミナなどを管理します。
2. **Ambient トラック（即時戦術・シグナル反応）**: 高頻度（1〜5 Tick ごと）で実行され、危機回避や群れのフォーメーション修正など環境変化に即座に反応します。

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

## 📢 `SocialEventRegistry` とイベント実装

各 Mod は `SocialEvent` インターフェースを実装し、Mod の初期化時に登録します：

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

## 📊 優先度階層マトリクス

| 優先度 | 最大トラック数 | 割り込み方針 | 主な用途 |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | 最優先・即座に割り込み | 致命的危機からの脱出、戦闘開始 |
| `HIGH` | `8` | 通常／低優先度タスクを中断 | 群れの遠吠え命令、戦術的シグナル |
| `NORMAL` | `16` | 標準的な実行枠 | 個体間の交流、散歩、遊び |
| `LOW` | `32` | 最大容量・バックグラウンド | 周囲環境の低速スキャン |

---

## 🔗 関連ページ
* [[群知能ソーシャルシステム|ja_jp-Hive-Mind-Social-System]]
* [[行動プロファイルと発動条件|ja_jp-Behavior-Profiles-and-Conditions]]
