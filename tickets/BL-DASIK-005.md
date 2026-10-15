# 🎫 [BL-DASIK-005] Universal Brigadier Command Framework Implementation

- **Ticket ID**: `[BL-DASIK-005]`
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Target Version**: `1.10.0` (Universal Sovereign Lead / MC 26.2+)
- **Scope**: `Shared APIs & Commands` (`net.dasik.social.api.command.*`)
- **Status**: `🚧 IN_PROGRESS`
- **Master Workspace Reference**: [`BL-WS-003`](file:///e:/Minecraft%20Project/tickets/BL-WS-003.md)

---

## 🎯 1. Problem Statement & Player Impact
Every mod across the studio currently implements 150–200 lines of repetitive Brigadier command boilerplate, custom feedback formatters, permission checks, and GameRule reflection to expose standard in-game commands (`/<modid> help`, `status`, `get`, `set`, `reset`, `reload`). This duplicates effort, clutters codebases, and produces inconsistent in-game feedback formatting. Implementing a centralized, high-performance universal command framework in `DasikLibrary` allows downstream mods to register complete, standardized Brigadier command suites with full GameRule 2-way sync in under 10 lines of code.

## 🔬 2. Architectural Blueprint & Target Classes
- **Package**: `net.dasik.social.api.command`
- **Classes**:
  1. `CommandFeedbackFormatter.java`: Static formatting utility providing standardized gold mod prefixes (`§6[ModName]§r `), green success (`§a`), yellow warning (`§e`), red error (`§c`), aqua values (`§b`), and clickable Discord/Ko-fi help footers via `DasikSupportHelper`.
  2. `GameRuleCommandBridge.java`: Two-way reactive synchronizer between Brigadier arguments, `DynamicGameRuleManager` keys, and disk config JSON. Handles integer, boolean, double, and string keys with saturated arithmetic guards.
  3. `UniversalCommandRegistry.java`: Fluent Brigadier tree builder automatically constructing standard subcommands:
     - `/<root>` -> Welcome & syntax pointer
     - `/<root> help` -> Color-coded command cheat sheet (requires `Commands.hasPermission(Commands.LEVEL_ALL)`)
     - `/<root> status` -> Summary of active rules & overrides (requires `Commands.hasPermission(Commands.LEVEL_ALL)`)
     - `/<root> get <key>` -> Tab-completed key query (requires `Commands.hasPermission(Commands.LEVEL_ALL)`)
     - `/<root> set <key> <val>` -> Tab-completed mutation (requires `Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)`)
     - `/<root> reset` -> Reverts all rules to defaults (requires `Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)`)
     - `/<root> reload` -> Reloads config JSON & syncs GameRules (requires `Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)`)
  4. `ModCommandDispatcher.java`: Central listener hooking `CommandRegistrationCallback.EVENT.register` to dispatch all registered mod command suites on server initialization.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- **MC 26.2+ Sovereign Lead**: Java 25, `Commands.hasPermission(Commands.LEVEL_ALL)` / `Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)`, `CommandSourceStack`, `Identifier`, `Component`.
- **Universal Forward Compatibility**: Exposed as pure `@DasikApiStatus.Public` interfaces ensuring seamless compatibility across `26.1.2`, `26.2`, `26.3+`, and clean backport adaptors for legacy anchors (`1.20.1`, `1.21.1`, `1.21.11`).

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Console / RCON / Command Blocks**: `source.getEntity() == null` handled gracefully with zero `NullPointerException`s when broadcasting success/error messages.
- **Freedom Over Anti-Crash & Saturated Bounds**: Full ranges accepted (`Integer.MIN_VALUE` to `Integer.MAX_VALUE`, `Double.MIN_VALUE` to `Double.MAX_VALUE`). Saturated inputs trigger non-blocking warnings via `CommandFeedbackFormatter.warning(...)` without throwing unhandled exceptions.
- **Thread Safety & Sidedness**: `ConcurrentHashMap` for command bridge mappings. Strict common isolation: zero `net.minecraft.client.*` imports.

## 🚀 5. Implementation Steps
1. Pre-step SemVer bump to `1.10.0` in `gradle.properties`.
2. Author `CommandFeedbackFormatter.java`, `GameRuleCommandBridge.java`, `UniversalCommandRegistry.java`, and `ModCommandDispatcher.java`.
3. Hook `ModCommandDispatcher` into `DasikLibraryMod.java`.
4. Add localization strings in `src/main/resources/assets/dasiklibrary/lang/en_us.json`.
5. Author comprehensive 5-Pillar test suite in `src/test/java/net/dasik/social/api/command/`.

## 🏁 6. 5-Pillar Verification Checklist
- [x] **Pillar 1 (Chaos Fuzzing)**: 20k iterations of saturated integer and double fuzzing in `GameRuleCommandBridgeTest`.
- [x] **Pillar 2 (Concurrency Load)**: 50 concurrent virtual threads reading and mutating bridges in `CommandConcurrencyTest`.
- [x] **Pillar 3 (Brigadier AST)**: Complete parse tree and tab-completion validation in `UniversalCommandRegistryTest`.
- [x] **Pillar 4 (GameTest / Headless Integration)**: Simulated `CommandSourceStack` execution assertions.
- [x] **Pillar 5 (Sidedness & Hygiene Contract)**: 0 client imports in common package; single-line GPLv3 header on 100% of files.
- [x] **Compilation & Build**: Clean `./gradlew test build` producing `dasik-library-1.10.0.jar`.
