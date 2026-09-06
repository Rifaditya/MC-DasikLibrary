# 위키 메인 포털 - Dasik Library

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

**Dasik Library** 공식 기술 문서에 오신 것을 환영합니다. 본 라이브러리는 Fabric Minecraft 모드를 위한 군집 지능 소셜 AI(Hive Mind), 동물 유전학 엔진, Boids 무리 조향 벡터 계산, 그리고 동적 GameRule 인프라를 제공하는 공용 코어 프레임워크입니다.

> 📌 **소스 저장소 안내**: 본 위키의 문서는 CurseForge 및 Modrinth의 공개 릴리스 빌드에 앞서 반영된 최신 커밋 또는 개발 중인 기능이 포함될 수 있는 **현재 저장소의 소스 코드 상태**를 반영합니다.

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 아키텍처 설계 철학

Dasik Library는 두 가지 핵심 설계 원칙을 기반으로 구축되었습니다:

1. **"Thin Mod, Fat Library" (가벼운 모드, 강력한 라이브러리)**: 개별 소비자 모드는 엔티티 고유 콘텐츠 및 등록에 집중합니다. 복잡한 수학 연산, 틱 스케줄링, 유전적 상속, Boids 조향 벡터 계산, NBT 직렬화 등은 Dasik Library에 일원화하여 관리합니다.
2. **"One Brain, Many Minds" (하나의 두뇌, 수많은 지능)**: `GlobalSocialSystem`을 통한 중앙 집중식 펄스 실행으로 **하이랜더 법칙**(게임 틱당 엄격히 1회의 글로벌 틱 사이클)을 강제하여 서버 부하를 최소화하면서 수천 개의 활성 엔티티를 조율합니다.

---

## 📦 마인크래프트 버전 디렉터리

* [[MC 26.2 가이드|ko_kr-Minecraft-26.2-Guide]] — Minecraft 26.2+ 설치 및 설정 가이드.
* [[버전 호환성 매트릭스|ko_kr-Version-Compatibility]] — 멀티 버전 수명 주기 매트릭스(`>=26.1.2-` ~ `26.2+`), Knot ClassLoader 안전성 및 버전 보호 규칙.

---

## 🎮 핵심 시스템 및 메커니즘 매트릭스

전체 기술 메커니즘 및 상세 구성 가이드:

* [[군집 지능 소셜 시스템|ko_kr-Hive-Mind-Social-System]] — 단일 펄스 구동 엔진, 하이랜더 법칙, $O(1)$ 샤딩 `SocialRegistry`, 틱 예산 관리.
* [[소셜 스케줄러 및 이벤트|ko_kr-Social-Scheduler-and-Events]] — `EntitySocialScheduler`, Mood/Ambient 듀얼 트랙 실행, `PriorityTier`, `SocialEventRegistry`.
* [[동물 유전학 엔진|ko_kr-Animal-Genetics-Engine]] — `EntityGenetics` 어태치먼트, DNA Long 값 인코딩, 크기 스케일링(`0.1x` ~ `3.0x`), 왜소체(Runt) 판별 및 상속 수학.
* [[유전학 API 및 가계도|ko_kr-Genetics-API-and-Pedigree]] — `DasikAnimalGeneticsAPI` 파사드, 혈통 분석 및 친족 판별, 근친교배 위험도 예측, 동적 특성 수정자.
* [[유전학 전리품 수정자|ko_kr-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`, `GeneticsLootRegistry`, `LivingEntityLootMixin`을 통한 드롭 아이템 변조 훅.
* [[우두머리 추종 및 무리 형성(Boids)|ko_kr-Leader-Follower-and-Flocking]] — `GroupMember`, `FollowLeaderGoal`, 공중 및 지상 Boids 알고리즘, `GroupParameters` 가중치.
* [[동적 GameRules 관리자|ko_kr-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`, 런타임 동적 등록, 영어 번역 자동 주입, 굵은 범주 헤더(`§l`), 수학 헬퍼.
* [[GameRule Codec 및 직렬화|ko_kr-GameRule-Codec-and-Serialization]] — 정수형 GameRule 경계값 검증(`Integer.MIN_VALUE`), `SavedDataStorage.encodeUnchecked` 크래시 방지.
* [[클라이언트 GameRule 및 GUI 도우미|ko_kr-Client-GameRule-and-GUI-Helpers]] — `ClientGameRuleHelper` 쿼리, `GuiHelper`, `ConfigHelper`를 통한 JSON 원자적 교체.
* [[동적 마법부여 및 시야 추적|ko_kr-Dynamic-Enchantments-and-Vision]] — `DynamicEnchantmentManager`를 통한 동적 주입 및 `PlayerVisionTracker` 기반 시야 절두체 판정.
* [[확률 및 수학 유틸리티|ko_kr-Stochastic-and-Math-Utilities]] — `FastRandom` XORSHIFT 알고리즘, `StochasticUtil` 퍼밀/십분위수 샘플링, 틱-초 환산($20\text{ ticks} = 1\text{초}$).
* [[만료된 속성 정리 및 크기 스케일|ko_kr-Stale-Attribute-Purging-and-Scale]] — 속성 수정자 제거 규칙, `ADD_VALUE` `-1.0f` 베이스 오프셋 계산, `genetics_` 접두사 안전성.
* [[ModVersionGuard 및 시작 안전성|ko_kr-ModVersionGuard-and-Startup-Safety]] — Knot ClassLoader 사전 검증(`Thread.currentThread().getContextClassLoader()`)을 통한 버전 불일치 조기 감지.

---

## 💻 개발자 및 기술 참조 문서

* [[개발자 환경 설정 및 빌드|ko_kr-Developer-Setup-and-Building]] — JDK 25 요구사항, Gradle 9.3+, `./gradlew build --no-daemon`, `./gradlew test`.
* [[아키텍처 및 패키지 구조|ko_kr-Architecture-and-Package-Layout]] — 전체 ASCII 패키지 트리(`ai`, `api`, `config`, `core`, `mixin`, `util`) 및 스레드 안전성 모델.
* [[Mixin 참조 및 주입 훅|ko_kr-Mixin-Reference-and-Hooks]] — 상세 Mixin 주입 테이블(`LanguageMixin`, `LivingEntityLootMixin`, `MobGoalAccessor`, `PathfinderMobMixin`, `ProfileTriggerMixin`).
* [[행동 프로필 및 조건|ko_kr-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`, `BehaviorProfile`, `BehaviorCondition`, `ProfileAware`, `DefaultProfileBuilder`.
* [[하위 모드 연동 및 통합 가이드|ko_kr-Consumer-Mods-Integration-Guide]] — 공식 하위 모드(*Better Dogs*, *Natural Reproduction*, *Collapsible Game Rule Screen*, *Bat Ecology*, *Ore Amplifier*) 연동 지침 및 예제.

---

## 🔗 외부 링크

* [[GitHub 저장소|Home]]
* [Modrinth 프로젝트 페이지](https://modrinth.com/mod/dasik-library)
* [CurseForge 프로젝트 페이지](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
