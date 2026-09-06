# 行動プロファイルと発動条件

| コンポーネント | クラス |
| :--- | :--- |
| **プロファイルマネージャー** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **プロファイル IF** | `net.dasik.social.api.profile.BehaviorProfile` |
| **発動条件 IF** | `net.dasik.social.api.profile.BehaviorCondition` |
| **標準ビルダー** | `DefaultProfileBuilder` |

---

## 🎭 プロファイル状態マシン

行動プロファイルシステムにより、エンティティの AI Goal 群を状況（警戒警備モード、狩猟モード、パニック逃亡モードなど）に応じて動的に切り替えることができます。

```ascii
[ Idle / Wandering ]
         │
         ├── Trigger: Player Attacked ──► [ Sentinel Guard Mode ]
         │
         └── Trigger: Low Health ───────► [ Panic / Flee Mode ]
```

---

## 🔗 関連ページ
* [[ソーシャルスケジューラ＆イベント|ja_jp-Social-Scheduler-and-Events]]
* [[リーダー追従＆群れ形成アルゴリズム|ja_jp-Leader-Follower-and-Flocking]]
