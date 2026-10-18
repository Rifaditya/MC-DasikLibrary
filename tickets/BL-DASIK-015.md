# 🎫 [BL-DASIK-015] Public Headless Testing & VirtualPlayerBot API (`net.dasik.social.api.test`)

- **Ticket ID**: `[BL-DASIK-015]`
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Scope**: `All Anchors` (`MC-DasikLibrary` 26.x+, `Dasik Library v1.21.11`, `Dasik Library v1.21.1`, `Dasik Library v1.20.1`)
- **Status**: `📌 DEFERRED`
- **Date Added**: `2026-10-18`
- **Target Repository / Root**: `MC-DasikLibrary`

---

## 🎯 1. Problem Statement & Player Impact
Downstream studio mods require a zero-boilerplate, cross-version API in `DasikLibrary` to execute headless `GameTestServer` player simulations (`VirtualPlayerBot`), force-audit Mixin transformations (`MixinEnvironment.getCurrentEnvironment().audit()`), and run 1-line bytecode/sidedness contract assertions (`DasikContractVerifier.verifyModContract(MOD_ID)`) without duplicating test utilities across 30+ mod repositories.

---

## 🔬 2. Architectural Blueprint & Target Classes
- **Target Package**: `net.dasik.social.api.test`
- **Classes to Create**:
  1. `net.dasik.social.api.test.DasikContractVerifier`:
     - `public static void verifyModContract(String modId, Path projectRoot)`: Executes Pillar 5 bytecode constant-pool sidedness checks, `ModVersionGuard` absence verification, `fabric.mod.json` entrypoint resolution, optional GUI dependency isolation (`modmenu` / `yacl`), and GameRule lowercase regex validation.
  2. `net.dasik.social.api.test.VirtualPlayerBot`:
     - Wraps `GameTestHelper` and a real `ServerPlayer` connected to a headless Netty `EmbeddedChannel` + `Connection(PacketFlow.SERVERBOUND)`.
     - Methods: `spawn(GameTestHelper, BlockPos, GameType)`, `equipMainHand(ItemStack)`, `lookAt(Vec3)`, `attack(Entity)`, `interactEntity(Entity, InteractionHand)`, `useBlock(BlockPos)`, `executeCommand(String)`, `drainOutboundPackets()`, and `auditAllMixins()`.

---

## ⚓ 3. Multi-Era Anchor Parity Specifications
- **MC 26.x+ (`MC-DasikLibrary`)**: Uses `GameTestHelper.makeMockServerPlayer`, `PermissionSet`, and `ServerTickRateManager`.
- **MC 1.21.1 / 1.21.11**: Uses `CommonListenerCookie.createInitial` and `ServerPlayer` 4-arg constructor.
- **MC 1.20.1**: Uses `new ServerPlayer(server, level, profile)` and `new Connection(PacketFlow.SERVERBOUND)`.

---

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Dedicated Server & Production Classpath Safety**: `VirtualPlayerBot` and `DasikContractVerifier` must not pull JUnit (`org.junit.jupiter.*`) into production `main` runtime imports; use standard Java `AssertionError` / `IllegalStateException` so production JARs have zero test-library runtime dependencies.

---

## 🚀 5. Implementation Steps (Code-Level Plan)
1. Implement `DasikContractVerifier.java` and `VirtualPlayerBot.java` in `MC-DasikLibrary` (`1.12.0`).
2. Add headless JUnit 5 tests in `src/test/java/net/dasik/social/api/test/`.
3. Port to `Dasik Library v1.21.11`, `Dasik Library v1.21.1`, and `Dasik Library v1.20.1` in lockstep and publish to `mavenLocal()`.

---

## 🏁 6. 5-Pillar Verification Checklist
- [ ] **Pillar 1 (Chaos Fuzzing)**: Fuzz `DasikContractVerifier` descriptor & constant-pool parser with malformed `.class` byte arrays.
- [ ] **Pillar 2 (Concurrency)**: Verify thread-safe packet queue draining on `VirtualPlayerBot`.
- [ ] **Pillar 3 (Brigadier Commands)**: Verify `VirtualPlayerBot.executeCommand` dispatches against live `CommandDispatcher`.
- [ ] **Pillar 4 (GameTest Engine)**: Verify `VirtualPlayerBot.auditAllMixins()` and player interaction helpers inside `GameTestServer`.
- [ ] **Pillar 5 (Sidedness Contract)**: Zero `net.minecraft.client.*` references in `net.dasik.social.api.test.*`.
