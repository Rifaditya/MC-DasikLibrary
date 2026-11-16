# 🎫 [BL-DASIK-021] Transitive Vanilla-Client Server Compatibility: Forge 1.20.1 DisplayTest & DasikSupportHelper Fallback Text

- **Ticket ID**: `[BL-DASIK-021]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Target Version**: All Active Anchors (`1.20.1` Forge priority + `1.21.1`, `1.21.11`, `26.1.2`, `26.2`, `26.3`)
- **Scope**: `DasikLibraryNeoForge.java`, `META-INF/mods.toml`, `META-INF/neoforge.mods.toml` (`Dasik Library 1.20.1`), `DasikSupportHelper.java` (all anchors), `Maven Local` republish + `Sidedness/Handshake ContractTest`
- **Status**: `✅ RESOLVED`
- **Grilling Status**: `✅ PASSED` (Stage 2 Companion Genesis from `BL-BD-064` Forensic Audit)
- **Related Mod Ticket**: [`BL-BD-064`](file:///E:/Minecraft%20Project/Vanilla%20Outsider%20Collections/Better%20Dogs%20all%20version/tickets/BL-BD-064.md)
- **Governance Mandate**: [Server-Side Primacy & Vanilla Client Compatibility Policy](file:///E:/Minecraft%20Project/.agents/rules/Server-Side%20Primacy%20&%20Vanilla%20Client%20Compatibility%20Policy.md) (§4 Invariants C, D, E)

---

## 🎯 1. Problem Statement & Root Cause

Stage 1 forensic bytecode inspection during [`BL-BD-064`](file:///E:/Minecraft%20Project/Vanilla%20Outsider%20Collections/Better%20Dogs%20all%20version/tickets/BL-BD-064.md) revealed two transitive vanilla-client server compatibility defects in **Dasik Library**:

1. **Missing `DisplayTest` on Forge `1.20.1` (`BLOCKER` for All Consumer Mods)**:
   - Forge `1.20.1` (`net.neoforged.fancymodloader:core:47.2.2`, `ModContainer.<init>`) defaults `displayTest` to `"MATCH_VERSION"` when omitted from `META-INF/mods.toml`.
   - Both `Dasik Library v1.20.1` (`neoforge/src/main/resources/META-INF/mods.toml`, `neoforge/src/main/resources/META-INF/neoforge.mods.toml`, and `DasikLibraryNeoForge.java`) AND the published local Maven artifact (`dasik-library-neoforge-1.4.1+1.20.1-neoforge.jar`) omit `displayTest="IGNORE_SERVER_VERSION"` and `IExtensionPoint.DisplayTest` (`NetworkConstants.IGNORESERVERONLY`).
   - Because server-side mods on `1.20.1` Forge (including Better Dogs) require `dasik_library`, unmodded/vanilla clients are rejected during Forge handshake even after the consumer mod adds `DisplayTest`.
2. **Bare `Component.translatable(...)` in `DasikSupportHelper` Command Footers (`All Anchors`)**:
   - `DasikSupportHelper.getDiscordCommandFooter()`, `getKofiCommandFooter()`, and `getCommandFooter()` emit 6 bare `Component.translatable("dasiklibrary.support.*")` components (`KEY_DISCORD_CHAT_PROMPT`, `KEY_DISCORD_CHAT_LINK`, `KEY_DISCORD_CHAT_HOVER`, `KEY_CHAT_PROMPT`, `KEY_CHAT_LINK`, `KEY_CHAT_HOVER`) with no English fallback string.
   - Any server running a universal command (`ModCommandDispatcher` / `UniversalCommandRegistry` / `BetterDogsCommand`) sends raw `dasiklibrary.support.*` dotted keys to unmodded vanilla clients.

### 🚫 1.3 Anti-Goals (Creep Shield)
1. **Zero Public Signature Breaking Changes**: Keep all existing `DasikSupportHelper` method signatures (`getCommandFooter()`, `getDiscordCommandFooter()`, `getKofiCommandFooter()`, `appendCommandFooter(...)`) and `KEY_*` constants identical so consumer mods remain 100% binary-compatible.
2. **Zero Mandatory Client Network Channels**: Do not introduce required custom payloads, synced registries, or `SynchedEntityData` mutations in `DasikLibrary`.
3. **Zero Unrelated API Refactors**: Keep genetics, scheduler, `SignalBus`, and `UniversalConfigScreen` behavior untouched.

---

## 🔧 2. Fix Blueprint

1. **Forge `1.20.1` (`Dasik Library v1.20.1/Dasik Library 1.20.1`)**:
   - Register `ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (remote, isServer) -> true))` in `DasikLibraryNeoForge`.
   - Add `displayTest="IGNORE_SERVER_VERSION"` under `[[mods]]` in both `neoforge/src/main/resources/META-INF/mods.toml` and `neoforge/src/main/resources/META-INF/neoforge.mods.toml`.
   - Bump `1.20.1` version cleanly (`1.4.2+1.20.1` or aligned SemVer), republish to `mavenLocal()`, and update downstream `1.20.1` consumer bounds/builds if needed.
2. **`DasikSupportHelper.java` (`MC-DasikLibrary` & All Active Anchors `1.20.1`–`26.3`)**:
   - Convert `KEY_DISCORD_CHAT_PROMPT`, `KEY_DISCORD_CHAT_LINK`, `KEY_DISCORD_CHAT_HOVER`, `KEY_CHAT_PROMPT`, `KEY_CHAT_LINK`, and `KEY_CHAT_HOVER` in `getDiscordCommandFooter()` and `getKofiCommandFooter()` (and getter helpers) from `Component.translatable(key)` to `Component.translatableWithFallback(key, fallback)` matching `en_us.json`.

---

## 🧪 3. Verification & Negative Parity Criteria

- [ ] Forge `1.20.1` `DisplayTest` contract test verifies `displayTest="IGNORE_SERVER_VERSION"` in both `mods.toml` and `neoforge.mods.toml` plus `IExtensionPoint.DisplayTest` registration in `DasikLibraryNeoForge.java`.
- [ ] `DasikSupportHelperTest` asserts all footer components contain `TranslatableContents` with non-null English `getFallback()` strings (never raw keys when client lang JSON is absent).
- [ ] `./gradlew.bat test build publishToMavenLocal --no-daemon --console=plain` passes cleanly across updated `DasikLibrary` anchors with zero `SYNCED` registries or custom entity data slots.

---

## 🛡️ 4. Edge Cases & Blast Radius Analysis

| Dimension | Risk / Failure Mode | Blast Radius | Mitigation & Safeguard Built-In |
| :--- | :--- | :--- | :--- |
| **Vanilla Contraptions** | None | Zero | Handshake metadata & chat text fallback only. |
| **World Saves** | None | Zero | No NBT, codecs, or registry IDs modified. |
| **Redstone & Timing** | None | Zero | No tick/scheduler/world state changes. |
| **Multiplayer Exploits** | Vanilla clients kicked at `1.20.1` Forge handshake or seeing raw `dasiklibrary.support.*` keys | Critical (`1.20.1` Forge join block) | `IGNORESERVERONLY` + `displayTest="IGNORE_SERVER_VERSION"` + `translatableWithFallback`. |

---

## 5. Resolution & Multi-Anchor Execution Log (2026-11-16)

The ticket was fully executed and verified across all targeted Dasik Library anchors:

| Step | Anchor | Release Version | Scope Implemented | Verification & Tests | Distribution |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Step 1** | `MC 1.20.1` | **`1.4.2+1.20.1`** | Forge `DisplayTest` (`mods.toml`, `neoforge.mods.toml`, `DasikLibraryNeoForge.java`), `DasikSupportHelper` 6 fallback keys | `BUILD SUCCESSFUL in 19s`, all unit tests passed, `publishToMavenLocal` | 3-tier archive copy (`Archive Jar`, Release Hub, External Vault) |
| **Step 2** | `MC 1.21.1` | **`1.4.2+1.21.1`** | `DasikSupportHelper` 6 fallback keys | `BUILD SUCCESSFUL in 37s`, tests passed, `publishToMavenLocal` | 3-tier archive copy |
| **Step 3** | `MC 1.21.11` | **`1.4.2+1.21.11`** | `DasikSupportHelper` 6 fallback keys | `BUILD SUCCESSFUL in 42s`, tests passed, `publishToMavenLocal` | 3-tier archive copy |
| **Step 4** | `MC 26.1.2` & `MC-DasikLibrary` | `1.13.0` | `DasikSupportHelper` 6 fallback keys + unit test assertions | `BUILD SUCCESSFUL` across both targets | Modern in-dev alignment |

