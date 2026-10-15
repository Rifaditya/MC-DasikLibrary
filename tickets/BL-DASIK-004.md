# 🎫 [BL-DASIK-004] Adopt 5-Pillar Testing & Sidedness Verification Harness

- **Ticket ID**: `[BL-DASIK-004]`
- **Category**: `[TECH_DEBT]`
- **Priority**: `[MEDIUM]`
- **Scope**: `All Anchors` (`1.20.1`, `1.21.1`, `1.21.11`, `26.1`, `26.2`, `26.3`)
- **Status**: `📌 DEFERRED`
- **Target Repository / Root**: `MC-DasikLibrary`

---

## 🎯 1. Problem Statement & Player Impact
Currently, `MC-DasikLibrary` lacks the studio's mandatory 5-Pillar Testing Harness. Without automated property fuzzing, concurrency stress tests, Brigadier AST validation, headless GameTests, and bytecode sidedness contracts, code refactors and multi-era backports risk regressions, subtle multi-thread race conditions, saturation overflow crashes, and server-side `ClassNotFoundException` errors. Adopting the 5-Pillar Harness guarantees rock-solid runtime stability for players across all Minecraft version anchors.

## 🔬 2. Architectural Blueprint & Target Classes
- Test Package: `net.dasik.social.test` (in `src/test/java/`)
- Target Test Classes to Implement:
  1. `McDasiklibraryStressFuzzTest.java` (Pillar 1 - Chaos Property Fuzzing):
     - Executes 10,000 to 50,000 randomized iterations validating configuration bounds, arithmetic saturation, and `NaN`/`Infinity` guards.
  2. `McDasiklibraryConcurrencyLoadTest.java` (Pillar 2 - Concurrency Load):
     - Stresses mod state, GameRule caches, and handlers across 50 virtual worker threads to verify lock-free safety and zero race conditions.
  3. `McDasiklibraryCommandTest.java` (Pillar 3 - Brigadier AST & Permissions):
     - Headlessly validates Brigadier command tree syntax, node hierarchy, and permission level requirements without requiring an active graphical client.
  4. `McDasiklibraryGameTest.java` (Pillar 4 - Headless In-Engine GameTest):
     - Simulates in-world ticking, block/entity interactions, and GameRule state changes using Mojang's native GameTest framework.
  5. `McDasiklibrarySidednessContractTest.java` (Pillar 5 - Sidedness & License Hygiene):
     - Automated source/bytecode scanner asserting 0 `net.minecraft.client.*` imports in common code and verifying 100% single-line GPLv3 license headers.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- **MC 1.20.1 (Java 17)**: JUnit 5 test harness, legacy Fabric Loader test runners, Mojang mappings assertions.
- **MC 1.21.1 / 1.21.11 (Java 21)**: DataComponent validation, virtual thread testing via Java 21 `Executors.newVirtualThreadPerTaskExecutor()`.
- **MC 26.1 / 26.2 / 26.3+ Sovereign Lead (Java 25)**: Modern Loom 1.15+ test runner, Renderpearl side-safety isolation, saturation arithmetic bounds.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- Dedicated Server Headless Execution: zero client classes loaded on dedicated server or headless test runner; verifies Knot ClassLoader runtime safety.
- Numerical Saturation: extreme configuration values (`Integer.MAX_VALUE`, `Double.MAX_VALUE`, negative numbers) must never wrap around or cause crashes (Freedom Over Anti-Crash policy).
- Concurrent GameRule mutations during active world tick: thread-safe atomic references ensure state consistency without locking the main server tick loop.

## 🚀 5. Implementation Steps (Code-Level Plan)
1. Add JUnit 5 dependencies (`org.junit.jupiter:junit-jupiter:5.10.2`) and `test { useJUnitPlatform() }` to `build.gradle`.
2. Author `src/test/java/net/dasik/social/McDasiklibrarySidednessContractTest.java` (Pillar 5).
3. Author `src/test/java/net/dasik/social/McDasiklibraryStressFuzzTest.java` (Pillar 1).
4. Author `src/test/java/net/dasik/social/McDasiklibraryConcurrencyLoadTest.java` (Pillar 2).
5. Author `src/test/java/net/dasik/social/McDasiklibraryCommandTest.java` (Pillar 3) and `McDasiklibraryGameTest.java` (Pillar 4).
6. Execute `./gradlew test --no-daemon --console=plain` and ensure 100% green tests.

## 🏁 6. 5-Pillar Verification Checklist
- [ ] **Pillar 1 (Chaos Fuzzing)**: Chaos property fuzzing passes 10k-50k iterations.
- [ ] **Pillar 2 (Concurrency Load)**: Concurrency harness passes 50 virtual threads with zero race conditions.
- [ ] **Pillar 3 (Brigadier AST)**: Brigadier command tree AST and permissions validated headlessly.
- [ ] **Pillar 4 (GameTest)**: Native Mojang GameTests pass in headless server environment.
- [ ] **Pillar 5 (Sidedness & Hygiene)**: Sidedness contract asserts 0 client imports in common code; single-line GPLv3 headers verified.
- [ ] **Anchor Compilation**: Clean `./gradlew test` and `./gradlew build` across all targeted anchors.
