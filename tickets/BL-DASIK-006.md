# 🎫 [BL-DASIK-006] Universal Brigadier Command Framework Multi-Era Anchor Parity Port

- **Ticket ID**: `[BL-DASIK-006]`
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Target Version**: `1.2.0+1.21.11`, `1.2.0+1.21.1`, `1.2.0+1.20.1`
- **Scope**: `Multi-Era Shared APIs & Commands` (`net.dasik.social.api.command.*`)
- **Status**: `🚧 IN_PROGRESS`
- **Master Workspace Reference**: [`BL-WS-015`](file:///e:/Minecraft%20Project/tickets/BL-WS-015.md)

---

## 🎯 1. Problem Statement & Player Impact
Following the successful implementation and release of the Universal Brigadier Command Framework on the Modern Sovereign Lead (`1.10.0` / MC 26.2+), the three remaining studio version anchors (`MC 1.21.11`, `MC 1.21.1`, and `MC 1.20.1`) remain on legacy `1.1.0+mc` builds lacking centralized command orchestration. Downstream mods in older anchors still resort to repetitive manual Brigadier code and disjointed formatting. 

Under the **Multi-Era Anchor Parity Law** ("1 Update Across All Anchors"), this ticket brings full 1:1 functional parity to `MC 1.21.11`, `MC 1.21.1`, and `MC 1.20.1` by porting the `net.dasik.social.api.command.*` suite with era-adapted bindings for `GameRules.Key<?>` vs `GameRule<?>`.

## 🔬 2. Architectural Blueprint & Target Classes
- **Target Directories**:
  - `Dasik Library v1.21.11/Dasik Library 1.21.11/src/main/java/net/dasik/social/api/command/`
  - `Dasik Library v1.21.1/Dasik Library 1.21.1/src/main/java/net/dasik/social/api/command/`
  - `Dasik Library v1.20.1/Dasik Library 1.20.1/src/main/java/net/dasik/social/api/command/`
- **Classes**:
  1. `CommandFeedbackFormatter.java`: Static component formatter providing gold mod prefixes (`§6[ModName]§r `), success, warning, error, aqua values, and clickable Discord/Ko-fi support footers via `DasikSupportHelper`.
  2. `GameRuleCommandBridge.java`: Two-way reactive synchronizer bridging Brigadier arguments, `DynamicGameRuleManager` keys, and disk config JSON.
     - *MC 1.21.11 Adaptation*: Binds `GameRule<?>`, `GameRule<Boolean>`, `GameRule<Integer>` to `level.getGameRules().get(rule)` and `set(rule, val, server)`.
     - *MC 1.21.1 & 1.20.1 Adaptation*: Binds `GameRules.Key<GameRules.BooleanValue>` and `GameRules.Key<GameRules.IntegerValue>` to `level.getGameRules().getRule(key).get()` and `set(val, server)`.
     - *Common Fallback*: Supports standalone in-memory/config-only key-value pairs without hard GameRule requirements.
  3. `UniversalCommandRegistry.java`: Fluent Brigadier builder constructing complete command suites (`help`, `status`, `get`, `set`, `reset`, `reload`) using `CommandSourceStack`, `Commands.literal`, `Commands.argument`, and permission checks (`source -> source.hasPermission(0)` / `source -> source.hasPermission(2)`).
  4. `ModCommandDispatcher.java`: Fabric listener hooking `CommandRegistrationCallback.EVENT.register` to dispatch mod suites on server start.
  5. `DasikLibraryMod.java`: Hook `ModCommandDispatcher.init()` into `onInitialize()`.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- **MC 1.21.11**: Java 21, Fabric Loom 1.15.5, Mojang mappings (`officialMojangMappings()`), `GameRule<?>`, `CommandSourceStack`.
- **MC 1.21.1**: Java 21, Fabric Loom 1.10.2, Mojang mappings (`officialMojangMappings()`), `GameRules.Key<?>`, `CommandSourceStack`.
- **MC 1.20.1**: Java 17, Fabric Loom 1.10.2, Mojang mappings (`officialMojangMappings()`), `GameRules.Key<?>`, `CommandSourceStack`.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Console & Command Blocks**: `source.getEntity() == null` handled gracefully without NPEs.
- **Freedom Over Anti-Crash**: Saturated numeric bounds with non-blocking warnings via `CommandFeedbackFormatter.warning(...)`.
- **Side Safety & Knot ClassLoader**: Strict zero client imports (`net.minecraft.client.*`) in common packages; safe on dedicated servers.

## 🚀 5. Implementation Steps
1. **Anchor 1.21.11**: Pre-bump `gradle.properties` to `1.2.0+1.21.11`, port classes & tests, compile `./gradlew test build remapJar`, 4-point deploy, update `RELEASE_QUEUE.md` & `CHANGELOG.md`, remote git push.
2. **Anchor 1.21.1**: Pre-bump `gradle.properties` to `1.2.0+1.21.1`, port classes & tests with `GameRules.Key<?>`, compile `./gradlew test build remapJar`, 4-point deploy, update `RELEASE_QUEUE.md` & `CHANGELOG.md`, remote git push.
3. **Anchor 1.20.1**: Pre-bump `gradle.properties` to `1.2.0+1.20.1`, port classes & tests with Java 17 compatibility, compile `./gradlew test build remapJar`, 4-point deploy, update `RELEASE_QUEUE.md` & `CHANGELOG.md`, remote git push.
4. **Governance & Boards Sync**: Clean sweep backlog entries, sync `MASTER_BACKLOG_BOARD.md` and `MASTER_ANCHOR_BOARD.md`, align `MASTER_RELEASE_QUEUE.md`.

## 🏁 6. 5-Pillar Verification Checklist
- [ ] **Pillar 1 (Chaos Fuzzing)**: Saturated integer and double fuzzing across all 3 subproject test suites.
- [ ] **Pillar 2 (Concurrency Load)**: Virtual thread concurrent bridge access test in all 3 subprojects.
- [ ] **Pillar 3 (Brigadier AST)**: Tab completion and AST builder tests in all 3 subprojects.
- [ ] **Pillar 4 (GameTest / Headless)**: Headless execution tests passing in all 3 subprojects.
- [ ] **Pillar 5 (Sidedness & Hygiene Contract)**: 0 client imports in common code; single-line GPLv3 header on all files.
- [ ] **Compilation & Build**: Clean `./gradlew test build remapJar` producing `dasik-library-1.2.0+1.21.11.jar`, `dasik-library-1.2.0+1.21.1.jar`, and `dasik-library-1.2.0+1.20.1.jar`.
