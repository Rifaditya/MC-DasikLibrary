# 클라이언트 GameRule 및 GUI 도우미

| 도우미 클래스 | 실행 환경 | 주요 역할 |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | 클라이언트 / 통합 서버 | 클라이언트 스레드에서 통합 서버의 GameRule을 안전하게 조회 |
| `ConfigHelper` | Common (클라이언트/서버 공용) | JSON 설정 파일의 로드, 저장 및 원자적 교체 |
| `GuiHelper` | 클라이언트 | ModMenu 및 Cloth Config 화면 구성을 위한 도우미 |

---

## 🖥️ 전용 서버(Dedicated Server) 크래시 방지

전용 서버 실행 시 발생할 수 있는 ClassLoader 예외를 완전히 차단하기 위해, 모든 GUI 도우미는 지연 로드 및 환경 분기 검사(`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`)를 적용합니다.

```ascii
                      DynamicGameRuleManager.getInt(level, ruleKey)
                                         │
                    ┌───────────────────┴───────────────────┐
                    ▼                                       ▼
          [ level instanceof ServerLevel ]       [ Client Environment ]
                    │                                       │
                    ▼                                       ▼
            Direct Level Lookup                   ClientGameRuleHelper
                                             (Queries Integrated Server)
```

---

## 📄 `ConfigHelper`의 원자적 파일 교체 보호

`ConfigHelper`는 예기치 않은 게임 강제 종료나 전원 차단 시 설정 파일의 손상을 방지합니다:

1. 임시 파일 `config.json.tmp`에 데이터 기록.
2. JSON 문법 및 파일 크기 검증.
3. `config.json.bak`에 안전 백업 복사본 생성.
4. `Files.move(..., StandardCopyOption.ATOMIC_MOVE)`를 통해 원자적으로 파일 교체.

---

## 🔗 관련 페이지
* [[동적 GameRules 관리자|ko_kr-Dynamic-GameRules-Manager]]
* [[행동 프로필 및 조건|ko_kr-Behavior-Profiles-and-Conditions]]
