# 동적 GameRules 관리자

| 구성 요소 | 클래스 |
| :--- | :--- |
| **관리자 클래스** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **네임스페이스 키 형식** | `modid:rule_name` |
| **번역 자동 주입** | `LanguageMixin` -> Map `GENERATED_TRANSLATIONS` |
| **굵은 범주 헤더** | `§lCategory Title (N rules)` |

---

## 🎲 개요 및 네임스페이스 GameRules

`DynamicGameRuleManager`를 통해 하위 모드는 `en_us.json` 파일에 번역 키를 일일이 수동 등록할 필요 없이 프로그램 코드에서 직접 무제한으로 동적 GameRule을 등록할 수 있습니다.

### 핵심 기능
1. **번역 자동 주입**: `modid:rule_name` 키를 판독 가능한 영문명(예: `bd_enable_guard_mode` -> `BD Enable Guard Mode`)으로 자동 변환하여 `LanguageMixin`을 통해 언어 맵에 주입합니다.
2. **굵은 범주 헤더(`§l`)**: `registerCategory`로 등록된 범주는 GUI 설정 화면에서 식별하기 쉽도록 자동으로 볼드 스타일이 적용됩니다.
3. **편리한 수학 변환 도우미**:
   - `getPct(Level level, GameRule<Integer> rule)` — 정수값(`0-100`)을 Double(`/ 100.0`)로 변환.
   - `getChance(Level level, GameRule<Integer> rule)` — 백분율을 Float(`/ 100.0f`)로 변환.
   - `getProb(Level level, GameRule<Integer> rule)` — 퍼밀값(`0-1000`)을 Float(`/ 1000.0f`)로 변환.
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — 십분위수를 Float(`/ 10.0f`)로 변환.
   - `getIntVal(Level level, String key, int defaultValue)` — 문자열 키로 정수값을 즉시 조회.

---

## 💻 개발자 코드 예제

```java
// Register a dynamic boolean GameRule with description
GameRule<Boolean> ENABLE_GUARD = DynamicGameRuleManager.booleanRule(
    "betterdogs:bd_enable_guard_mode",
    GameRuleCategory.MOBS,
    true
).description("Enable wolf sentinel guard mode").register();

// Querying GameRule safely across client/server
boolean isGuardEnabled = DynamicGameRuleManager.getBoolean(level, ENABLE_GUARD);
```

---

## 🔗 관련 페이지
* [[GameRule Codec 및 직렬화|ko_kr-GameRule-Codec-and-Serialization]]
* [[클라이언트 GameRule 및 GUI 도우미|ko_kr-Client-GameRule-and-GUI-Helpers]]
