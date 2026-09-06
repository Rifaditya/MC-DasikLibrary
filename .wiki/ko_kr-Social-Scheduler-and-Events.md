# 소셜 스케줄러 및 이벤트 시스템

| 구성 요소 | 클래스 |
| :--- | :--- |
| **스케줄러 엔진** | `net.dasik.social.core.EntitySocialScheduler` |
| **이벤트 레지스트리** | `net.dasik.social.api.SocialEventRegistry` |
| **우선순위 계층** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **신호 유형** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

---

## 🔄 듀얼 트랙 스케줄링 모델

각 소셜 엔티티는 개별 `EntitySocialScheduler`를 보유하며 두 개의 병렬 트랙에서 작업을 평가합니다:

1. **Mood 트랙 (장기 심리 및 상태)**: 저주기(예: 20~100틱마다)로 실행되어 무리 서열, 허기, 피로도 등을 갱신합니다.
2. **Ambient 트랙 (즉각 전술 및 환경 반응)**: 고주기(1~5틱마다)로 실행되어 자극, 위험 회피, 대형 유지 등에 즉각 대응합니다.

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

## 📢 `SocialEventRegistry` 및 이벤트 구현

모드는 `SocialEvent` 인터페이스를 구현하고 초기화 시점에 등록합니다:

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

## 📊 우선순위 계층 매트릭스

| 계층 | 최대 트랙 수 | 선점 정책 | 일반적 용도 |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | 즉각 선점, 절대적 최우선 | 치명적 위험 회피, 전투 돌입 |
| `HIGH` | `8` | 일반/낮은 작업 중단 | 무리 포효 명령, 전술 신호 |
| `NORMAL` | `16` | 표준 실행 예산 | 개체 간 상호작용, 배회, 놀이 |
| `LOW` | `32` | 최대 수용량, 백그라운드 | 주변 환경의 수동적 관찰 |

---

## 🔗 관련 페이지
* [[군집 지능 소셜 시스템|ko_kr-Hive-Mind-Social-System]]
* [[행동 프로필 및 조건|ko_kr-Behavior-Profiles-and-Conditions]]
