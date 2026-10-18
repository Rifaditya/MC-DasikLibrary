# 🎫 [BL-DASIK-008] Dedicated Server Sidedness & Synthetic Lambda Isolation (`DasikSupportHelper` & `SocialLinks`)

- **Ticket ID**: `[BL-DASIK-008]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Target Version**: `All Anchors` (`1.11.3` / `26.2+`, `1.3.1+1.21.11`, `1.3.1+1.21.1`, `1.3.1+1.20.1`)
- **Scope**: `Client Side-Safety & ClassLoader Isolation` (`net.dasik.social.api.config.DasikSupportHelper`, `net.dasik.social.api.SocialLinks`, `net.dasik.social.api.client.gui.*`)
- **Status**: `✅ RESOLVED`

---

## 🎯 1. Problem Statement & Player Impact
`DasikSupportHelper` and `SocialLinks` are common classes loaded on Dedicated Servers whenever server commands execute (`ModCommandDispatcher` -> `UniversalCommandRegistry` -> `CommandFeedbackFormatter.appendSupportFooters` -> `DasikSupportHelper.getDiscordCommandFooter` -> `SocialLinks.getDiscordUri`). However, both classes directly import `net.minecraft.client.gui.screens.Screen` and `net.minecraft.client.gui.screens.ConfirmLinkScreen`.

Crucially, in `DasikSupportHelper.java` lines 220 and 286, lambdas inside `@Environment(EnvType.CLIENT)` methods:
```java
BiConsumer<Object, Object> biAction = (screen, opt) -> openKofi((Screen) screen);
```
compile into synthetic methods `lambda$createYaclButton$0` in `DasikSupportHelper.class` without `RuntimeVisibleAnnotations` (`@Environment(EnvType.CLIENT)` is NOT propagated by `javac` onto synthetic lambda methods). On Dedicated Servers, Fabric Loader's `ClassTweaker` strips `openKofi(Screen)`, leaving unstripped synthetic lambda bytecode referencing stripped methods and client-only `Screen` classes, violating Pillar 5 Sidedness (`Library API Governance & Side-Safety Policy.md`) and risking dedicated server startup crashes.

## 🔬 2. Architectural Blueprint & Target Classes
- **Client Helper Extraction**:
  - Create a dedicated client-only helper class `net.dasik.social.api.client.gui.ClientSupportScreenHelper` marked with `@Environment(EnvType.CLIENT)`.
  - Move all direct references to `Screen`, `ConfirmLinkScreen`, `Minecraft.getInstance()`, and YACL screen action lambdas out of `DasikSupportHelper` and `SocialLinks` into `ClientSupportScreenHelper`.
- **Common Class Sanitization**:
  - `DasikSupportHelper.java`: Purge all imports of `net.minecraft.client.*`. In `createYaclButton` and `createDiscordYaclButton`, delegate screen construction to `ClientSupportScreenHelper` via dynamic reflection or client-isolated bridges. Remove dead private method `findMethod(Class<?>, String)`.
  - `SocialLinks.java`: Purge all imports of `net.minecraft.client.gui.screens.Screen` and `ConfirmLinkScreen`. Move `openUri(Screen, URI)` to `ClientSupportScreenHelper.openUri(Screen, URI)`.
- **Bytecode Constant-Pool Verification**:
  - Run `javap -p -v` on compiled common classes to assert zero entries containing `net/minecraft/client/gui/screens/Screen`.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- **MC 26.2+ Sovereign Lead**: Java 25, clean client-isolated bridge.
- **MC 1.21.11 / 1.21.1**: Java 21, Yarn/Mojmap mappings, client screen isolation.
- **MC 1.20.1**: Java 17, `Screen` isolation, Yarn/Mojmap mappings.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Headless Dedicated Server**: Common command execution must never trigger classloading of `ClientSupportScreenHelper` or `Screen`.
- **Missing YACL / ModMenu**: Client methods safely return dummy objects or no-ops without throwing `ClassNotFoundException`.

## 🚀 5. Implementation Steps
1. Create `net.dasik.social.api.client.gui.ClientSupportScreenHelper` with `@Environment(EnvType.CLIENT)` housing `openKofi(Screen)`, `openDiscord(Screen)`, and YACL `BiConsumer` button action builders.
2. Refactor `DasikSupportHelper.java` and `SocialLinks.java` to remove client imports and delegate GUI opening.
3. Remove dead `findMethod` reflection helper.
4. Add JUnit / headless test verifying zero client references in common bytecode.
5. Backport across `1.21.11`, `1.21.1`, and `1.20.1`.

## 🏁 6. 5-Pillar Verification Checklist
- [x] **Pillar 1 (Bytecode Scan)**: `javap -c -p build/classes/java/main/net/dasik/social/api/config/DasikSupportHelper.class` contains 0 references to `net/minecraft/client/gui/screens/Screen`.
- [x] **Pillar 2 (Synthetic Lambda Hygiene)**: Zero synthetic lambda methods in common classes reference stripped client classes.
- [x] **Pillar 3 (Common Tests)**: `./gradlew test --no-daemon --console=plain` passes all command sidedness tests.
- [x] **Pillar 4 (Knot Headless Simulation)**: Command feedback formatter executes safely without initializing client graphics.
- [x] **Pillar 5 (Multi-Era Parity)**: Sidedness contract holds across all 4 version anchors.
