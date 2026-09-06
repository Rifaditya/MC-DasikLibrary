# 동적 마법부여 및 시야 추적

| 구성 요소 | 클래스 |
| :--- | :--- |
| **마법부여 관리자** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **플레이어 시야 추적기** | `net.dasik.social.util.PlayerVisionTracker` |
| **식별자 형식** | 동적 `Identifier` |

---

## ✨ 개요 및 런타임 마법부여 등록

`DynamicEnchantmentManager`를 사용하면 부팅 시 복잡한 JSON 데이터팩을 작성할 필요 없이 런타임에 직접 마법부여를 등록하고 관리할 수 있습니다.

```java
// Registering dynamic enchantment
DynamicEnchantmentManager.registerEnchantment(
    Identifier.parse("betterdogs:loyal_bond"),
    new DynamicEnchantmentConfig(...)
);
```

---

## 👁️ 플레이어 시야 추적 (`PlayerVisionTracker`)

`PlayerVisionTracker`는 플레이어가 엔티티를 정면으로 바라보고 있는지 신속하게 판단하는 절두체(Frustum) 및 레이캐스트 연산을 지원합니다:

1. **시야각 원뿔 검사**: 플레이어의 시선 방향 벡터와 대상 방향 간의 내적을 계산하여 시야 내 포함 여부를 판정.
2. **장애물 레이캐스트 검사**: 시선 사이에 불투명 블록이 가로막고 있지 않은지 검증.

---

## 🔗 관련 페이지
* [[확률 및 수학 유틸리티|ko_kr-Stochastic-and-Math-Utilities]]
* [[아키텍처 및 패키지 구조|ko_kr-Architecture-and-Package-Layout]]
