# 🎫 [BL-DASIK-023] Fix Ko-fi & Discord YACL Button No-Op on MC 26.3+ (Migrate ConfirmLinkScreen to URI Overload Across All & Future Anchors)

- **Ticket ID**: `[BL-DASIK-023]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Target Version**: `All Active Anchors (1.20.1, 1.21.1, 1.21.11, 26.1.2, 26.2, 26.3) & Future Anchors (26.x / 26.4+ / 2x.x+)`
- **Scope**: `ClientSupportScreenHelper.java` & `ClientSupportScreenHelperTest.java` across `MC-DasikLibrary`, `Dasik Library v26.1.2`, `Dasik Library v1.21.11`, `Dasik Library v1.21.1`, and `Dasik Library v1.20.1` (+ `mavenLocal` / 4-point archive sync on execution)
- **Status**: `📌 DEFERRED`
- **Target Repository/Root**: `MC-DasikLibrary` (`E:\Minecraft Project\MC-DasikLibrary`) & Multi-Era Anchor Folders (`Dasik Library v1.20.1`, `v1.21.1`, `v1.21.11`, `v26.1.2`)
- **Related Companion Ticket**: [`BL-HQS-007`](file:///E:/Minecraft%20Project/Vanilla%20Outsider%20Collections/Hyper%20Quality%20Screenshots/tickets/BL-HQS-007.md) (secondary direct fallback caller)
- **Governance Mandate**: [Minecraft Modern Sovereign Anchors Policy](file:///E:/Minecraft%20Project/.agents/rules/Minecraft%20Modern%20Sovereign%20Anchors%20Policy.md) §6 (`ConfirmLinkScreen.confirmLinkNow(Screen parent, URI uri)`), [Monetization & Creator Support Policy](file:///E:/Minecraft%20Project/.agents/rules/Monetization%20&%20Creator%20Support%20Policy.md), [Bytecode Testing & ClassLoader Safety Policy](file:///E:/Minecraft%20Project/.agents/rules/Bytecode%20Testing%20&%20ClassLoader%20Safety%20Policy.md)
- **Date Added**: `2026-11-19`

---

## 🎯 1. Goal & Definition of Done (The 4-Pillar Objective Architecture)

1. **North Star Objective**: Migrate `ClientSupportScreenHelper.openUrl(...)` from the deleted `ConfirmLinkScreen.confirmLinkNow(Screen, String)` overload to `ConfirmLinkScreen.confirmLinkNow(Screen, URI)` across all URI-capable active anchors (`1.21.1`, `1.21.11`, `26.1.2`, `26.2`, `26.3`) and future anchors (`26.x`, `26.4+`, `2x.x+`), harden `1.20.1`'s legacy `(String, Screen, boolean)` path with `URI.create(...)` syntax validation and safe `instanceof Screen` narrowing, and enforce a Pillar 5 bytecode contract test so clicking Ko-fi or Discord buttons in any YACL config screen reliably opens Minecraft's `ConfirmLinkScreen`.
2. **In-Game Experience (Before vs. After)**:
   - *Before*: Clicking `☕ Support Solo Dev on Ko-fi` or Discord buttons in any mod's YACL config screen on MC 26.3+ (e.g., `Better Dogs 5.7.21+26.3` running `dasik-library-1.13.0.jar`) does nothing in-game while logging `java.lang.NoSuchMethodError: 'void net.minecraft.client.gui.screens.ConfirmLinkScreen.confirmLinkNow(net.minecraft.client.gui.screens.Screen, java.lang.String)'` at `ClientSupportScreenHelper.java:58`.
   - *After*: Clicking Ko-fi or Discord config buttons on MC `1.20.1` through `26.3` and future `26.4+` / `2x.x+` opens Minecraft's native `ConfirmLinkScreen` for `https://ko-fi.com/dasikigaijin` or `https://discord.gg/EV99bgAFqb`, and returns cleanly to the YACL parent screen upon cancel or confirmation.
3. **🚫 Anti-Goals (Off-Limits / Do Not Touch)**:
   - ❌ Do NOT change public method signatures of `ClientSupportScreenHelper`, `DasikSupportHelper`, or `SocialLinks` (`openUrl`, `openKofi`, `openDiscord`, `createYaclButton`, `createDiscordYaclButton`) — consumer mods must remain 100% binary-compatible without recompilation.
   - ❌ Do NOT bypass `ConfirmLinkScreen` with direct `Blaze3D.openUri(...)`, `Util.getPlatform().openUri(...)`, or `java.awt.Desktop` calls — players must always receive Minecraft's standard external-link confirmation dialog.
   - ❌ Do NOT modify unrelated subsystems (`UniversalConfigScreen` field discovery, `DynamicGameRuleConfigSync`, `SignalBus`, genetics, or `DasikSupportHelper` chat command footers which already use `ClickEvent.OpenUrl(URI)`).
   - ❌ Do NOT leak `Screen`, `ConfirmLinkScreen`, or `Blaze3D` references into common classes (`DasikSupportHelper.class`, `SocialLinks.class`) — preserve `@Environment(EnvType.CLIENT)` isolation established by [`BL-DASIK-008`](./BL-DASIK-008.md).
4. **✅ Definition of Done (Binary Verification Checklist)**:
   - [ ] `ClientSupportScreenHelper.openUrl` calls `ConfirmLinkScreen.confirmLinkNow(parentScreen, uri)` using `java.net.URI` on `1.21.1`, `1.21.11`, `26.1.2`, and `MC-DasikLibrary` (`26.2`/`26.3`/`26.x`/`26.4+`).
   - [ ] `Dasik Library v1.20.1` keeps `ConfirmLinkScreen.confirmLinkNow(uri.toString(), screen, true)` with `URI.create(url)` validation and safe `parentScreen instanceof Screen` narrowing (zero raw `(Screen)` cast `ClassCastException`).
   - [ ] Malformed URI strings (e.g., `"not a valid uri ://[]"`, whitespace, null, empty) are caught cleanly without crashing the client or invoking `ConfirmLinkScreen`.
   - [ ] Pillar 5 bytecode inspection test in `ClientSupportScreenHelperTest` verifies `(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V` is referenced and `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V` is strictly absent on all `1.21.1+` and `26.x+` anchors.
   - [ ] All active anchors compile and pass `./gradlew.bat test build --no-daemon --console=plain` (plus `publishToMavenLocal` and 4-point distribution) with zero `NoSuchMethodError` on MC 26.3 live test.

---

## ❓ 2. Problem Statement & Player Impact

Live MC 26.3 testing with `vanilla-outsider-better-dogs-5.7.21+26.3.jar`, `dasik-library-1.13.0.jar`, and `yet_another_config_lib_v3-3.9.7+26.3-fabric.jar` produced the following stack trace when clicking the Ko-fi support button in the General Options config tab:

```text
[14:22:42] [Render thread/ERROR]: Failed to open confirmation screen for URL: https://ko-fi.com/dasikigaijin
java.lang.NoSuchMethodError: 'void net.minecraft.client.gui.screens.ConfirmLinkScreen.confirmLinkNow(net.minecraft.client.gui.screens.Screen, java.lang.String)'
    at knot//net.dasik.social.api.client.gui.ClientSupportScreenHelper.openUrl(ClientSupportScreenHelper.java:58)
    at knot//net.dasik.social.api.client.gui.ClientSupportScreenHelper.openKofi(ClientSupportScreenHelper.java:98)
    at knot//net.dasik.social.api.client.gui.ClientSupportScreenHelper.lambda$createYaclButton$0(ClientSupportScreenHelper.java:158)
    at knot//dev.isxander.yacl3.gui.controllers.ActionController$ActionControllerElement.executeAction(ActionController.java:84)
```

- **Root Cause (Mojang MC 26.2 → MC 26.3 API Removal)**: Verified against decompiled `ConfirmLinkScreen.java` (`26.1`, `26.2`, `26.3`, `26.4 snapshot 5`), Mojang deleted `confirmLinkNow(Screen, String)` and `confirmLinkNow(Screen, String, boolean)` in MC `26.3`, leaving only `confirmLinkNow(@Nullable Screen, URI)` and `confirmLinkNow(@Nullable Screen, URI, boolean)`.
- **Why Failure Is Silent**: `ClientSupportScreenHelper.openUrl` catches `Throwable` on line 59, which swallows `NoSuchMethodError` (a `java.lang.Error`), logs an error line, and presents zero visual feedback to the player.
- **YACL Compatibility Verified**: `javap` inspection of `yet_another_config_lib_v3-3.9.7+26.3-fabric.jar` (`dev.isxander.yacl3.api.ButtonOption$Builder`) confirmed `action(BiConsumer<YACLScreen, ButtonOption>)` is intact and invokes the callback cleanly — the defect is strictly the `ConfirmLinkScreen.confirmLinkNow` descriptor.
- **Blast Radius**: Affects **every** consumer mod relying on `DasikSupportHelper.createYaclButton()`, `createDiscordYaclButton()`, `UniversalConfigScreen`, or `SocialLinks.openKofi` / `openDiscord` / `openUrl` on MC `26.3` and future `26.4+` / `2x.x+` releases.

---

## 🔬 3. Architectural Blueprint & Target Classes

1. **Modern Sovereign Universal Anchor (`MC-DasikLibrary`)**:
   - File: `MC-DasikLibrary/src/main/java/net/dasik/social/api/client/gui/ClientSupportScreenHelper.java` (line 58)
   - Import `java.net.URI;`
   - Parse and validate `URI uri = URI.create(url.trim());` before invoking `ConfirmLinkScreen.confirmLinkNow(parentScreen, uri);` (ensuring `http`/`https` or valid URI syntax inside the existing `try/catch` guard).
2. **Transitional & Sovereign Multi-Loader Anchors (`1.21.1`, `1.21.11`, `26.1.2`)**:
   - `Dasik Library v26.1.2/Dasik Library 26.1.2/common/src/main/java/net/dasik/social/api/client/gui/ClientSupportScreenHelper.java` (line 57)
   - `Dasik Library v1.21.11/Dasik Library 1.21.11/common/src/main/java/net/dasik/social/api/client/gui/ClientSupportScreenHelper.java` (line 57)
   - `Dasik Library v1.21.1/Dasik Library 1.21.1/common/src/main/java/net/dasik/social/api/client/gui/ClientSupportScreenHelper.java` (line 57)
   - Align all three to `ConfirmLinkScreen.confirmLinkNow(parentScreen, URI.create(url.trim()));` so no anchor compiles against the deprecated `(Screen, String)` overload.
3. **Legacy Anchor (`1.20.1`)**:
   - `Dasik Library v1.20.1/Dasik Library 1.20.1/common/src/main/java/net/dasik/social/api/client/gui/ClientSupportScreenHelper.java` (line 51)
   - Keep Mojang 1.20.1's `ConfirmLinkScreen.confirmLinkNow(uri.toString(), screen, true)` signature (no `URI` overload exists in 1.20.1), while adding `URI uri = URI.create(url.trim());` syntax validation and `Screen screen = parentScreen instanceof Screen s ? s : null;` safe narrowing to eliminate raw `(Screen) parentScreen` `ClassCastException` risks.
4. **Pillar 5 Bytecode Descriptor & Regression Test Suites**:
   - `ClientSupportScreenHelperTest.java` across all 5 anchor directories (`MC-DasikLibrary`, `v26.1.2`, `v1.21.11`, `v1.21.1`, `v1.20.1`).

---

## ⚓ 4. Multi-Era Anchor Parity Specifications

| Anchor | Runtime / Toolchain | `ConfirmLinkScreen` Signature Used | Parity Implementation Specification |
| :--- | :--- | :--- | :--- |
| **Anchor 1 (`MC 1.20.1`)** | Java 17 target (Fabric + Forge) | `confirmLinkNow(String, Screen, boolean)` | No `URI` overload in 1.20.1. Validate `URI.create(url.trim())`, safely narrow `parentScreen instanceof Screen s ? s : null`, invoke `confirmLinkNow(uri.toString(), screen, true)`. |
| **Anchor 2 (`MC 1.21.1`)** | Java 21 target (Fabric + NeoForge) | `confirmLinkNow(Screen, URI)` | Replace String overload on line 57 with `URI.create(url.trim())` (`confirmLinkNow(Screen, URI)` verified supported on 1.21.1). |
| **Anchor 3 (`MC 1.21.11`)** | Java 21 target (`fabric-loom-remap`, Fabric + NeoForge) | `confirmLinkNow(Screen, URI)` | Replace String overload on line 57 with `URI.create(url.trim())`. |
| **Anchor 4 (`MC 26.1.2`)** | Java 25 target (Fabric + NeoForge) | `confirmLinkNow(@Nullable Screen, URI)` | Replace String overload on line 57 with `URI.create(url.trim())` (verified in `26.1 decompile/.../ConfirmLinkScreen.java:105-107`). |
| **Anchor 5 (`MC 26.2`)** | Java 25 (`MC-DasikLibrary`, Loom 1.15.2) | `confirmLinkNow(@Nullable Screen, URI)` | Replace String overload on line 58 with `URI.create(url.trim())` (verified in `26.2 decompile/.../ConfirmLinkScreen.java:105-107`). |
| **Anchor 6 (`MC 26.3`)** | Java 25 (`MC-DasikLibrary` universal binary) | `confirmLinkNow(@Nullable Screen, URI)` | String overload deleted in `26.3 decompile/.../ConfirmLinkScreen.java:68-81`; `URI` overload eliminates `NoSuchMethodError` across all 26.3 consumer mods. |
| **Anchor 7 & Future (`MC 26.x`, `26.4+`, `2x.x+`, `3x.x+`)** | Java 25+ (Rolling Universal / Future Anchors) | `confirmLinkNow(@Nullable Screen, URI)` | Verified intact in `26.4 snapshot 5 decompile/.../ConfirmLinkScreen.java:79-81`. Future anchor scaffolds & templates must inherit `URI`-based `ClientSupportScreenHelper` plus the Pillar 5 bytecode descriptor check preventing regression to `(Screen, String)`. |

---

## 🛡️ 5. Edge Cases, Collisions & Failure Modes

| Dimension | Risk / Failure Mode | Blast Radius | Mitigation & Safeguard Built-In |
| :--- | :--- | :--- | :--- |
| **Malformed / Invalid URI Strings** | `URI.create(url)` throws `IllegalArgumentException` on spaces, control chars, or invalid syntax | Low (caller misuse) | Parse inside `try { ... } catch (Throwable t)` (or explicit `IllegalArgumentException` guard) so bad URLs log cleanly without crashing the client. |
| **Null / Blank URLs** | Null, empty, or whitespace-only `url` | Zero | Early `url == null \|\| url.isBlank()` check logs warning and returns before `URI.create`. |
| **Untyped `parentScreen` (`1.20.1`)** | `openUrl(Object, String)` passing non-`Screen` object triggers `ClassCastException` on `(Screen) parentScreen` | Low (`1.20.1` only) | Replace raw cast with `parentScreen instanceof Screen s ? s : null` matching `1.21.1+` / `26.x`. |
| **Dedicated Server Environment** | Calling `openUrl`/`openKofi`/`openDiscord` on server | Critical if unguarded | Preserve environment guard (`EnvType.CLIENT` / `Services.PLATFORM.isClient()`) returning early before referencing `ConfirmLinkScreen` + keep `@Environment(EnvType.CLIENT)`. |
| **Headless JUnit Runners** | `FabricLoader` / `Minecraft.getInstance()` unavailable in unit tests | Test reliability | Guard environment probe and `ConfirmLinkScreen` call so headless tests execute safely without `ExceptionInInitializerError` or NPE. |
| **Vanilla Contraptions / World Saves / Redstone / Exploits** | None | Zero | Client GUI link-confirmation screen helper only; zero world, entity, packet, or tick mutations. |

---

## 🚀 6. Implementation Steps (Code-Level Plan)

1. **Primary Sovereign Lead (`MC-DasikLibrary`)**:
   - Update `ClientSupportScreenHelper.java` to import `java.net.URI`, parse `URI uri = URI.create(url.trim());`, and call `ConfirmLinkScreen.confirmLinkNow(parentScreen, uri);`.
   - Extend `ClientSupportScreenHelperTest.java` with malformed-URI negative tests and a Pillar 5 `.class` constant-pool/bytecode assertion proving `ClientSupportScreenHelper.class` references `(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V` and contains zero `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V` descriptors.
2. **Active URI-Capable Anchors (`Dasik Library v26.1.2`, `v1.21.11`, `v1.21.1`)**:
   - Sync `ClientSupportScreenHelper.java` (`openUrl`) to `ConfirmLinkScreen.confirmLinkNow(parentScreen, URI.create(url.trim()))` and add matching Pillar 5 bytecode descriptor + malformed-URI negative tests to `ClientSupportScreenHelperTest.java`.
3. **Legacy Anchor (`Dasik Library v1.20.1`)**:
   - Add `URI.create(url.trim())` syntax validation and `Screen screen = parentScreen instanceof Screen s ? s : null;` guard while retaining `ConfirmLinkScreen.confirmLinkNow(uri.toString(), screen, true)`, plus negative tests in `ClientSupportScreenHelperTest.java`.
4. **Version Calibration (Single-Ticket Release Policy)**:
   - Bump `mod_version` by 1 patch step in each updated anchor's `gradle.properties` (e.g., `1.13.0` → `1.13.1` in `MC-DasikLibrary`, and matching patch bumps on `v26.1.2`, `v1.21.11`, `v1.21.1`, `v1.20.1`).
5. **5-Pillar Verification, Local Maven & 4-Point Distribution**:
   - Run `./gradlew.bat test build publishToMavenLocal --no-daemon --console=plain` across all 5 anchor projects (with `--refresh-dependencies` if needed), verifying archive JAR deployment to all target Modrinth profiles (including `26.3`), Central Archive, Release Hub archives, and External Vault.
6. **Live Client Smoke Verification**:
   - Launch MC 26.3 Modrinth profile with Better Dogs `5.7.21+26.3` and click both Ko-fi and Discord buttons in YACL config screen; confirm `ConfirmLinkScreen` opens cleanly with zero `NoSuchMethodError` in `latest.log`.
7. **Companion Ticket Separation**:
   - Execute [`BL-HQS-007`](file:///E:/Minecraft%20Project/Vanilla%20Outsider%20Collections/Hyper%20Quality%20Screenshots/tickets/BL-HQS-007.md) in its own separate atomic sprint per the Single-Ticket Release Policy.

---

## 🏁 7. 5-Pillar Verification Checklist

- [ ] **Pillar 1 (Chaos Property Fuzzing)**: Fuzz `ClientSupportScreenHelper.openUrl(null, fuzzedUrl)` and `openUrl(arbitraryObject, fuzzedUrl)` across 10,000+ randomized valid/malformed/unicode/whitespace/control-char strings (`assertDoesNotThrow`).
- [ ] **Pillar 2 (Concurrency Load)**: Stress `openUrl`, `openKofi`, `openDiscord`, `createYaclButton`, and `createDiscordYaclButton` across 50 concurrent threads (`Executors.newVirtualThreadPerTaskExecutor()` on Java 21+/25, fixed thread pool on Java 17) with zero race conditions or uncaught errors.
- [ ] **Pillar 3 (Command AST & Footer Integrity)**: Confirm `DasikSupportHelper.getCommandFooter()`, `getKofiCommandFooter()`, and `getDiscordCommandFooter()` retain `ClickEvent.OpenUrl(URI)` and English fallback strings without regression from [`BL-DASIK-021`](./BL-DASIK-021.md).
- [ ] **Pillar 4 (Negative Parity & Lifecycle Safety)**: Verify null/blank URLs, malformed URIs, untyped non-`Screen` parents, and non-client environment paths return early without crashing or leaking state.
- [ ] **Pillar 5 (Bytecode Descriptor, Sidedness & License Hygiene)**: Assert `ClientSupportScreenHelper.class` contains `(Lnet/minecraft/client/gui/screens/Screen;Ljava/net/URI;)V` and ZERO `(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;)V` on `1.21.1` / `1.21.11` / `26.1.2` / `MC-DasikLibrary`; confirm `DasikSupportHelper.class` and `SocialLinks.class` retain ZERO `Screen`/`ConfirmLinkScreen` bytecode references; verify single-line GNU GPLv3 headers on all touched Java files and clean `./gradlew.bat test build --no-daemon --console=plain` across all 5 anchor builds.

---

## 🛡️ 8. Autonomous Goal Dependability & Self-Healing Contract

- **Verified On-Disk Targets & Toolchains**:
  - `E:\Minecraft Project\MC-DasikLibrary` (`gradle.properties`: `minecraft_version=26.2`, `mod_version=1.13.0`, `org.gradle.java.home=C:/Program Files/Eclipse Adoptium/jdk-25.0.5.8-hotspot`, Java 25, Fabric Loom `1.15.2`).
  - `E:\Minecraft Project\Dasik Library v26.1.2\Dasik Library 26.1.2` (`minecraft_version=26.1.2`, `mod_version=1.13.0+26.1.2`, `org.gradle.java.home=E:/JDK25`, Fabric + NeoForge).
  - `E:\Minecraft Project\Dasik Library v1.21.11\Dasik Library 1.21.11` (`minecraft_version=1.21.11`, `mod_version=1.4.2+1.21.11`, `org.gradle.java.home=E:/JDK25`, `fabric-loom-remap`, Fabric + NeoForge).
  - `E:\Minecraft Project\Dasik Library v1.21.1\Dasik Library 1.21.1` (`minecraft_version=1.21.1`, `mod_version=1.4.2+1.21.1`, `org.gradle.java.home=E:/JDK25`, Fabric + NeoForge).
  - `E:\Minecraft Project\Dasik Library v1.20.1\Dasik Library 1.20.1` (`minecraft_version=1.20.1`, `mod_version=1.4.2+1.20.1`, `org.gradle.java.home=E:/JDK25`, Fabric + Forge).
- **Windows Process Hygiene**: Always run Gradle with `--no-daemon --console=plain` and use PowerShell `;` instead of `&&`.
- **Self-Healing Compile Budget**: Up to 1 localized retry per anchor if `Services.PLATFORM` vs `FabricLoader` or Loom cache nuances differ across `1.20.1`–`26.x`; escalate via `enter_plan_mode` if a second attempt fails or if any anchor unexpectedly lacks the `(Screen, URI)` overload.
- **Continuation Re-Spawn**: If multi-anchor builds or Gradle downloads time out transiently, re-anchor from the last green subproject without collapsing remaining anchors into a partial fix.
