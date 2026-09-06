# 군집 지능 소셜 시스템

| 시스템 매개변수 | 값 |
| :--- | :--- |
| **메인 클래스** | `net.dasik.social.core.GlobalSocialSystem` |
| **레지스트리 클래스** | `net.dasik.social.core.SocialRegistry` |
| **펄스 주기** | `1 tick` (하이랜더 법칙) |
| **검색 시간 복잡도** | $O(1)$ 샤딩 HashMap |
| **메모리 정리 전략** | `isAlive()` 및 `isRemoved()`를 통한 자동 파지 |

---

## ⚡ 개요 및 하이랜더 법칙

**군집 지능 소셜 시스템(Hive Mind)**은 Dasik Library의 핵심 총괄 조율 엔진입니다. 수백 마리의 소셜 엔티티가 매 틱마다 개별적으로 무거운 공간 탐색을 수행하는 대신, Dasik Library는 단일 글로벌 펄스 코디네이터(`GlobalSocialSystem`)를 운영합니다.

### 👑 하이랜더 법칙 ("살아남는 자는 오직 하나뿐이다")
`GlobalSocialSystem`은 서버 틱당($20\text{ ticks} = 1\text{초}$) 정확히 **1회의 글로벌 펄스 사이클**만 실행되도록 강제합니다. 동일한 틱 내에서 여러 스레드나 서브시스템이 펄스를 호출하더라도 초과 호출은 완전히 무시됩니다:

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

## 🗂️ `SocialRegistry`의 아키텍처

`SocialRegistry`는 활성 소셜 엔티티를 종족 및 월드 UUID별로 그룹화하여 관리합니다:

* **$O(1)$ 신속 등록**: 엔티티는 `SocialRegistry.register(SocialEntity entity)`를 통해 등록됩니다.
* **자동 파지**: 청크 언로드 또는 엔티티 사망 시 펄스 루프 내에서 `entity.dasik$asEntity().isAlive()` 및 `isRemoved()`를 확인하여 만료된 참조를 자동 정리합니다.
* **종족별 샤딩**: 엔티티가 `dasik$getSpeciesId()`를 기준으로 샤딩되므로, 전체 월드를 탐색할 필요 없이 근처의 무리 구성원을 즉시 조회할 수 있습니다.

---

## 💻 개발자 코드 예제

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

## 🔗 관련 페이지
* [[소셜 스케줄러 및 이벤트|ko_kr-Social-Scheduler-and-Events]]
* [[아키텍처 및 패키지 구조|ko_kr-Architecture-and-Package-Layout]]
