# 우두머리 추종 및 무리 형성(Boids)

| 구성 요소 | 상세 사양 |
| :--- | :--- |
| **그룹 구성원 IF** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal 클래스** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **무리 유형** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Boids 전략** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ 개요 및 조향 벡터 연산

Dasik Library는 Craig Reynolds의 **Boids 무리 형성 모델**을 Minecraft의 공간 환경에 맞춰 고도로 최적화하여 구현했습니다. `GroupMember`를 구현한 엔티티는 리더 추종, 무리 내 거리 유지 및 충돌 방지를 유기적으로 수행합니다.

### 합성 조향력 벡터 공식
개체에 작용하는 합성 조향력 $\vec{F}_{\text{steer}}$는 다음과 같이 계산됩니다:

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

여기서:
* $\vec{V}_{\text{separation}}$은 개체 간 충돌을 방지하는 반발 벡터입니다.
* $\vec{V}_{\text{cohesion}}$은 무리의 중심점으로 당겨주는 응집 벡터입니다.
* $\vec{V}_{\text{alignment}}$는 주변 개체의 진행 방향과 일치시키는 정렬 벡터입니다.
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$는 `GroupParameters`에 정의된 가중치 계수입니다.

---

## ⚙️ `GroupParameters` 사양

```java
public record GroupParameters(
    float cohesionRadius, 
    float separationRadius, 
    float maxSpeed, 
    boolean canTeleport, 
    float teleportDistance,
    float startDistance,
    float stopDistance,
    float alignmentWeight,
    float cohesionWeight,
    float separationWeight
) {
    public static final GroupParameters DEFAULT_AERIAL = new GroupParameters(
        3.0f, 1.0f, 0.4f, true, 144.0f, 6.0f, 2.0f, 0.05f, 0.05f, 0.1f
    );
    public static final GroupParameters DEFAULT_TERRESTRIAL = new GroupParameters(
        5.0f, 1.5f, 1.2f, true, 144.0f, 6.0f, 2.0f, 0.0f, 0.0f, 0.0f
    );
}
```

---

## 💻 개발자 코드 예제

```java
public class CustomBirdEntity extends PathfinderMob implements GroupMember {
    private LivingEntity leader;

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new FollowLeaderGoal<>(this, GroupParameters.DEFAULT_AERIAL, 32.0D));
    }

    @Override public LivingEntity getLeader() { return this.leader; }
    @Override public void setLeader(LivingEntity leader) { this.leader = leader; }
    @Override public FlockType getFlockType() { return FlockType.AERIAL; }
}
```

---

## 🔗 관련 페이지
* [[군집 지능 소셜 시스템|ko_kr-Hive-Mind-Social-System]]
* [[아키텍처 및 패키지 구조|ko_kr-Architecture-and-Package-Layout]]
