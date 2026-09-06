# 개발자 환경 설정 및 빌드

| 도구 | 권장 및 필수 버전 |
| :--- | :--- |
| **Java Development Kit (JDK)** | JDK 25 |
| **Gradle** | `9.3+` |
| **Fabric Loom** | `1.11+` |

---

## 🚀 로컬 빌드 명령어

저장소를 복제하고 로컬에서 빌드하는 방법:

```bash
# Clone the repository
git clone https://github.com/Rifaditya/MC-DasikLibrary.git
cd MC-DasikLibrary

# Build without daemon
./gradlew build --no-daemon

# Run automated tests
./gradlew test
```

---

## 🔗 관련 페이지
* [[아키텍처 및 패키지 구조|ko_kr-Architecture-and-Package-Layout]]
* [[MC 26.2 가이드|ko_kr-Minecraft-26.2-Guide]]
