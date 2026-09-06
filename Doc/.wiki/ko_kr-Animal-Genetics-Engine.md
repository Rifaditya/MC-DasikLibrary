# 동물 유전학 엔진

| 속성 | 값 |
| :--- | :--- |
| **메인 패키지** | `net.dasik.social.api.genetics` |
| **어태치먼트 ID** | `dasik-library:genetics` |
| **크기 스케일 범위** | `0.1x` ~ `3.0x` (표준: `0.5x` - `2.0x`) |
| **왜소체(Runt) 임계값** | 스케일 $< 0.85x$ (또는 근친교배) |
| **거대체(Giant) 임계값** | 스케일 $> 1.15x$ |
| **데이터 Codec** | `EntityGenetics.CODEC` |

---

## 🧬 개요 및 DNA 데이터 구조

**동물 유전학 엔진**은 Fabric의 `AttachmentType`을 기반으로 엔티티 비종속적인 영속 유전 시스템을 제공합니다. 각 동물은 `EntityGenetics` 레코드를 저장합니다:

1. **`parent1Uuid` (`Optional<UUID>`)**: 번식 시 부모 1의 UUID.
2. **`parent2Uuid` (`Optional<UUID>`)**: 번식 시 부모 2의 UUID.
3. **`inbred` (`boolean`)**: 근친교배로 태어난 개체인지 여부.
4. **`traitsRolled` (`boolean`)**: 초기 특성 무작위 추첨 완료 여부.
5. **`traits` (`Map<String, Float>`)**: 동적 능력치(`scale`, `max_health`, `attack_damage`, `movement_speed`).

```ascii
 ┌────────────────────────────────────────────────────────────────────────┐
 │                           EntityGenetics                               │
 ├───────────────────────────────────┬────────────────────────────────────┤
 │ parent1Uuid: Optional<UUID>       │ inbred: boolean                    │
 │ parent2Uuid: Optional<UUID>       │ traitsRolled: boolean              │
 │ traits: Map<String, Float>        │ Codec: EntityGenetics.CODEC        │
 └───────────────────────────────────┴────────────────────────────────────┘
```

---

## 📐 유전 및 돌연변이 연산 공식

두 개체가 교배할 때 자손의 유전 특성은 `GeneticsEngine.calculateOffspringGenetics`에 의해 계산됩니다:

### 1. 크기 스케일 상속 공식
자손의 기본 크기 $S_{\text{offspring}}$는 양부모의 평균값에 삼각 분포 기반의 돌연변이 편차 $\Delta_{\text{mutate}}$를 더하여 산출합니다:

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

여기서 $\Delta_{\text{mutate}}$는 $\pm \text{mutationRate}$ 범위의 삼각 분포에서 추출됩니다.

### 2. 근친교배 페널티 공식
혈통 중복($F > 0$)이 감지되면 체력과 크기에 근친교배 페널티 $\text{Penalty}_{\text{inbreeding}}$가 적용됩니다:

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 개발자 코드 예제

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 관련 페이지
* [[유전학 API 및 가계도|ko_kr-Genetics-API-and-Pedigree]]
* [[만료된 속성 정리 및 크기 스케일|ko_kr-Stale-Attribute-Purging-and-Scale]]
