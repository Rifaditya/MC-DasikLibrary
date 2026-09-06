# Minecraft 26.2+ 가이드

| 매개변수 | 상세 사양 |
| :--- | :--- |
| **대상 Minecraft 버전** | `26.2` (향후 `26.x` 버전과 상위 호환) |
| **Fabric Loader 요구조건** | `>=0.18.4` |
| **Java 환경** | JDK 25 |
| **라이브러리 버전** | `1.8.15` |
| **모드 ID** | `dasik-library` |
| **모드 이름** | Dasik Library |
| **라이선스** | LGPL-3.0 |

> 📌 **소스 저장소 안내**: 본 위키의 문서는 CurseForge 및 Modrinth의 공개 릴리스 빌드에 앞서 반영된 최신 커밋 또는 개발 중인 기능이 포함될 수 있는 **현재 저장소의 소스 코드 상태**를 반영합니다.

---

## 🛠️ 개요 및 설치 방법

**Dasik Library**는 *Vanilla Outsider*, *Instant Gratification*, *Delayed Gratification* 시리즈 모드의 필수 런타임 종속성입니다. 소셜 AI의 통합 틱 스케줄링, 동물 유전학, Boids 무리 제어 벡터 연산, 동적 GameRule 등록을 제공합니다.

### 📥 플레이어 설치 안내
1. Minecraft `26.2`용 **Fabric Loader**(`0.18.4` 이상)를 설치합니다.
2. `.minecraft/mods` 폴더에 **Fabric API**(`0.152.1+26.2` 이상)가 설치되어 있는지 확인합니다.
3. `dasik-library-1.8.15.jar`를 다운로드하여 연동 모드(*Better Dogs*, *Natural Reproduction* 등)와 함께 `.minecraft/mods` 디렉터리에 넣습니다.

### 💻 모드 개발자 종속성 구성

`fabric.mod.json`에 **Dasik Library**를 추가합니다:

```json
{
  "schemaVersion": 1,
  "id": "my_consumer_mod",
  "version": "1.0.0+26.2",
  "name": "My Consumer Mod",
  "depends": {
    "fabricloader": ">=0.18.4",
    "minecraft": ">=26.1.2-",
    "dasik-library": "*"
  }
}
```

`gradle.properties`에 추가:

```properties
dasik_library_version=1.8.15
```

`build.gradle`에 추가:

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_library_version}"
}
```

---

## ⚙️ 26.2+ 주요 아키텍처 변경점

1. **Mojang Sovereign 매핑**: Mojang 공식 매핑(`level`, `ServerLevel`, `EntityTypes`)을 전면 채택했습니다. 기존 Yarn 명칭(`world`, `getWorld`)은 완전히 제거되었습니다.
2. **Identifier API**: `Identifier.fromNamespaceAndPath(namespace, path)` 또는 `Identifier.parse(string)`를 사용합니다. 구식 `Identifier.of()`는 지원되지 않습니다.
3. **개방형 버전 경계(`>=26.1.2-`)**: 단일 JAR 바이너리가 향후 Minecraft `26.2+` 패치 업데이트와 안정적으로 호환되도록 보장하며, `ModVersionGuard`가 런타임 안전성을 보호합니다.

---

## 🔗 관련 페이지
* [[버전 호환성 매트릭스|ko_kr-Version-Compatibility]]
* [[ModVersionGuard 및 시작 안전성|ko_kr-ModVersionGuard-and-Startup-Safety]]
* [[개발자 환경 설정 및 빌드|ko_kr-Developer-Setup-and-Building]]
