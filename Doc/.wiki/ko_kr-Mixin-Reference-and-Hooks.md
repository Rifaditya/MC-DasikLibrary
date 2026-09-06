# Mixin 참조 및 주입 훅

| Mixin 클래스 | 대상 클래스 | 대상 메서드 | 상세 기능 |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `getOrDefault` | 동적 GameRule 번역명 자동 주입 |
| `LivingEntityLootMixin` | `LivingEntity` | `dropFromLootTable` | 유전학 기반 전리품 아이템 변조 |
| `MobGoalAccessor` | `Mob` | GoalSelector 접근자 | 내부 AI GoalSelector 접근 허용 |
| `PathfinderMobMixin` | `PathfinderMob` | `tick` | 소셜 펄스 사이클 시작 훅 |
| `ProfileTriggerMixin` | `LivingEntity` | 이벤트 감지 메서드 | 행동 프로필 전환 감지 |

---

## 🔗 관련 페이지
* [[아키텍처 및 패키지 구조|ko_kr-Architecture-and-Package-Layout]]
* [[유전학 전리품 수정자|ko_kr-Genetics-Loot-Modifiers]]
