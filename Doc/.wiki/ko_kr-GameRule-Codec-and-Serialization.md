# GameRule Codec 및 직렬화

| 문제 항목 | 상세 사양 |
| :--- | :--- |
| **직렬화 크래시** | 월드 저장 시 `SavedDataStorage.encodeUnchecked`에서 발생했던 오류 |
| **수정 버전** | `1.8.15` |
| **정수 허용 범위** | `min = Integer.MIN_VALUE` (또는 `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ 정수 GameRule Codec 수정 배경

Minecraft 26.2에서 동적 GameRule은 `SavedDataStorage.encodeUnchecked`를 거쳐 `level.dat` / `game_rules.dat`에 직렬화됩니다. 초기 구현에서는 명시적 하한선이 없는 정수 규칙의 최소값이 `min = 0`으로 강제되어, 음수값이나 기본값을 저장할 때 `IllegalStateException`이 발생했습니다.

### `DynamicGameRuleManager` 내 수정 코드

```java
// IntegerBuilder initialization fix in DynamicGameRuleManager
public class IntegerBuilder {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public GameRule<Integer> register() {
        int effectiveMin = Math.min(min, defaultValue);
        int effectiveMax = Math.max(max, defaultValue);
        // Uses Codec.INT.intRange(effectiveMin, effectiveMax) to prevent encodeUnchecked bounds check failure
        GameRule<Integer> rule = new GameRule<>(
            GameRuleType.INT, defaultValue, Codec.INT.intRange(effectiveMin, effectiveMax), 
            FeatureFlagSet.of(), category, visitor
        );
        return Registry.register(BuiltInRegistries.GAME_RULE, Identifier.parse(ruleName), rule);
    }
}
```

---

## 🔒 네임스페이스 무결성 보장

동적 규칙에는 항상 `modid:rule_name` 형식을 지정해야 합니다(`[ERR-20260510-002]`). 네임스페이스가 없는 키는 올바르게 저장되지 않아 서버 재시작 시 설정이 기본값으로 초기화될 수 있습니다.

---

## 🔗 관련 페이지
* [[동적 GameRules 관리자|ko_kr-Dynamic-GameRules-Manager]]
* [[클라이언트 GameRule 및 GUI 도우미|ko_kr-Client-GameRule-and-GUI-Helpers]]
