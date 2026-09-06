# 유전학 API 및 가계도 시스템

| 파사드 클래스 | `net.dasik.social.api.genetics.DasikAnimalGeneticsAPI` |
| :--- | :--- |
| **지원 속성** | `scale`, `max_health`, `movement_speed`, `attack_damage` |
| **가계도 탐색 깊이** | 최대 3세대 (부모, 조부모) |
| **근친 위험도 범위** | `0%` (무관) ~ `100%` (동일 개체 또는 동복 남매) |

---

## 🛠️ `DasikAnimalGeneticsAPI` 파사드

`DasikAnimalGeneticsAPI`는 하위 모드가 저수준 NBT나 컴포넌트를 직접 조작하지 않고도 유전 정보를 안전하게 제어할 수 있는 정적 헬퍼 함수들을 제공합니다.

### API 메서드 요약

```java
public final class DasikAnimalGeneticsAPI {
    // Size & Scale
    public static float getScale(LivingEntity entity);
    public static void setScale(LivingEntity entity, float scale);
    public static boolean isRunt(LivingEntity entity);
    public static boolean isGiant(LivingEntity entity);

    // Kinship & Pedigree
    public static boolean isRelated(LivingEntity entity1, LivingEntity entity2);
    public static int predictInbreedingRiskPercent(LivingEntity parent1, LivingEntity parent2);

    // Trait Modifiers
    public static void setTrait(LivingEntity entity, String traitKey, float value);
    public static void modifyTrait(LivingEntity entity, String traitKey, float delta);
    public static void resetGenetics(LivingEntity entity);
}
```

---

## 🌳 가계도 분석 및 근친 위험도 예측

가계도 분석기는 조상의 UUID를 재귀적으로 대조하여 중복률을 계산합니다:

$$\text{Inbreeding Risk (\%)} = \begin{cases}
100\%, & \text{if Parent1 UUID} = \text{Parent2 UUID} \\
50\%, & \text{if Full Siblings (same Parent1 AND Parent2)} \\
25\%, & \text{if Half Siblings (sharing 1 Parent)} \\
12.5\%, & \text{if Cousin overlap (sharing Grandparents)} \\
0\%, & \text{if No pedigree overlap detected}
\end{cases}$$

```ascii
[ Parent 1 ]             [ Parent 2 ]
     │                        │
     ├───────────┬────────────┤
     ▼           ▼            ▼
[ Offspring1 ] [ Offspring2 ] 
     │               │
     └─── BREEDING ──┘
            │
            ▼
    Inbreeding Risk = 50% (Full Siblings)
```

---

## 💻 개발자 코드 예제

```java
// Check inbreeding risk before allowing breeding
int risk = DasikAnimalGeneticsAPI.predictInbreedingRiskPercent(dog1, dog2);
if (risk > 25) {
    // Trigger inbreeding warning particles or prevent breeding
}
```

---

## 🔗 관련 페이지
* [[동물 유전학 엔진|ko_kr-Animal-Genetics-Engine]]
* [[유전학 전리품 수정자|ko_kr-Genetics-Loot-Modifiers]]
