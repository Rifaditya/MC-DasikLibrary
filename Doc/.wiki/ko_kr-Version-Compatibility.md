# 버전 호환성 매트릭스

| 대상 Minecraft 버전 | 라이브러리 버전 | Mod Version Guard | 종속성 지정 | 지원 상태 |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **활성 메인 브랜치** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | 백포트 / 안정 |
| **Minecraft 1.21.x** | *지원 중단* | *N/A* | *Legacy 1.x* | **수명 종료 (EOL)** |

> 📌 **소스 저장소 안내**: 본 위키의 문서는 CurseForge 및 Modrinth의 공개 릴리스 빌드에 앞서 반영된 최신 커밋 또는 개발 중인 기능이 포함될 수 있는 **현재 저장소의 소스 코드 상태**를 반영합니다.

---

## 🛡️ "1 Jar 1 Version" 정책 및 상위 호환성 원칙

Dasik Library는 **1 Jar 1 Version 정책**과 **개방형 상위 호환성**을 결합하여 구현합니다:

1. **개방형 버전 범위(`"minecraft": ">=26.1.2-"`)**: `fabric.mod.json`에 하한선만 지정함으로써 마이너 패치 업데이트 시 Fabric Loader가 플레이어를 차단하지 않고 JAR를 정상적으로 로드할 수 있도록 합니다.
2. **Knot ClassLoader 안전성 검증(`ModVersionGuard`)**: `onInitialize()` 단계에서 `ModVersionGuard.checkClass`가 `Thread.currentThread().getContextClassLoader()`를 통해 필수 클래스의 유무를 사전 점검하여, 조용한 크래시 대신 명확한 진단 메시지를 발생시킵니다.

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

## 🚫 버전 명칭 혼용 금지 규칙

엄격한 가이드라인 `[DIR-20260614-001]`에 따라, **Minecraft 26.x 연간 릴리스 명칭을 2024년의 구식 1.21.x 명칭과 절대 혼용해서는 안 됩니다**:
* ❌ `26.2 (1.21.4)` — 엄격히 금지됨.
* ✅ `Minecraft 26.2` — 공식 표준 연간 릴리스 표기.

---

## 🔗 관련 페이지
* [[MC 26.2 가이드|ko_kr-Minecraft-26.2-Guide]]
* [[ModVersionGuard 및 시작 안전성|ko_kr-ModVersionGuard-and-Startup-Safety]]
