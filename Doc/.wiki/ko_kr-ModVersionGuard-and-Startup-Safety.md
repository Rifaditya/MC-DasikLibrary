# ModVersionGuard 및 시작 안전성

| 구성 요소 | 클래스 |
| :--- | :--- |
| **버전 가드** | `net.dasik.social.core.ModVersionGuard` |
| **필수 ClassLoader** | `Thread.currentThread().getContextClassLoader()` |
| **보호 기능** | 부팅 시 API 불일치로 인한 조용한 비정상 종료 방지 |

---

## 🛡️ `ModVersionGuard`의 역할과 작동 방식

Fabric Loader의 Knot 환경에서는 명시적 ClassLoader 없이 `Class.forName(name)`을 호출할 경우 클래스 로딩에 실패하거나 누락을 감지하지 못할 수 있습니다. `ModVersionGuard`는 현재 스레드의 컨텍스트 ClassLoader를 강제 참조하여 필수 API의 존재를 초기화 단계에서 선제적으로 검증합니다.

```java
public final class ModVersionGuard {
    public static void checkClass(String modName, String requiredClassName) {
        try {
            Class.forName(requiredClassName, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("\n" +
                "=====================================================================\n" +
                " [" + modName + "] Minecraft API Mismatch!\n" +
                " A required Minecraft class or API was not found in your game version.\n" +
                " Try updating your Minecraft version or download a matching build.\n" +
                "=====================================================================");
        }
    }
}
```

---

## 🔗 관련 페이지
* [[버전 호환성 매트릭스|ko_kr-Version-Compatibility]]
* [[MC 26.2 가이드|ko_kr-Minecraft-26.2-Guide]]
