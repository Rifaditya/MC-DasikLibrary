# 행동 프로필 및 조건

| 구성 요소 | 클래스 |
| :--- | :--- |
| **프로필 관리자** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **프로필 인터페이스** | `net.dasik.social.api.profile.BehaviorProfile` |
| **조건 인터페이스** | `net.dasik.social.api.profile.BehaviorCondition` |
| **기본 빌더** | `DefaultProfileBuilder` |

---

## 🎭 프로필 상태 머신

행동 프로필 시스템을 활용하면 엔티티의 AI Goal 목록을 환경 조건(경계 모드, 사냥 모드, 공황 도주 모드 등)에 따라 실시간으로 매끄럽게 전환할 수 있습니다.

```ascii
[ Idle / Wandering ]
         │
         ├── Trigger: Player Attacked ──► [ Sentinel Guard Mode ]
         │
         └── Trigger: Low Health ───────► [ Panic / Flee Mode ]
```

---

## 🔗 관련 페이지
* [[소셜 스케줄러 및 이벤트|ko_kr-Social-Scheduler-and-Events]]
* [[우두머리 추종 및 무리 형성(Boids)|ko_kr-Leader-Follower-and-Flocking]]
