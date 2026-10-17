# 🎫 [BL-DASIK-007] Zero-Boilerplate Universal YACL Config Screen Generator (`UniversalConfigScreen`)

- **Ticket ID**: `[BL-DASIK-007]`
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Target Version**: `1.11.0` (Universal Sovereign Lead / MC 26.2+)
- **Scope**: `Client GUI & Shared APIs` (`net.dasik.social.api.config.ConfigOption`, `net.dasik.social.api.client.gui.*`)
- **Status**: `✅ RESOLVED`
- **Master Workspace Reference**: [`BL-WS-011`](file:///e:/Minecraft%20Project/tickets/BL-WS-011.md)

---

## 🎯 1. Problem Statement & Player Impact
Every mod supporting client configuration GUIs currently writes a dedicated `YaclScreenHelper.java` containing 80–580 lines of repetitive category, group, slider, and option-builder boilerplate. This creates redundant code across dozens of mods (~40+ mods in the workspace) and risks server classloading crashes if deferred reflection is improperly implemented. Players see inconsistent slider increments and confusing option layouts. Implementing a centralized, zero-boilerplate universal YACL screen generator in `DasikLibrary` allows downstream mods to register complete, beautiful, categorized YACL v3 config screens in ONE line of code (`UniversalConfigScreen.createFactory(MyModConfig.class)`).

## 🔬 2. Architectural Blueprint & Target Classes
- **Common Annotation**:
  - `net.dasik.social.api.config.ConfigOption`: Side-safe runtime annotation for config fields with metadata:
    - `category`: Category name key or ID (default `"general"`).
    - `group`: Sub-group name key (default `""`).
    - `nameKey`: Localization key for option name (default `""` -> auto-infers `config.<modId>.<fieldName>`).
    - `descriptionKey`: Localization key for option description/tooltip (default `""` -> auto-infers `config.<modId>.<fieldName>.description`).
    - `min`, `max`, `step`: Numerical bounds and step increments.
    - `slider`: Boolean flag rendering slider instead of numeric field when bounds are finite.
    - `order`: Relative ordering priority within category/group.
    - `syncGameRule`: Boolean flag to auto-sync changes to in-game DynamicGameRules in singleplayer.
    - `gameRuleName`: Explicit GameRule identifier if different from `<modId>:<fieldName>`.
- **Client GUI Subsystem** (`net.dasik.social.api.client.gui.*`, marked with `@Environment(EnvType.CLIENT)`):
  - `YaclSafetyBridge.java`: Knot ClassLoader safe reflection wrapper that detects YACL availability (`dev.isxander.yacl3`), checks mod loader presence (`yet_another_config_lib_v3` / `yet-another-config-lib`), and safely invokes YACL builder primitives without compile-time hard dependencies.
  - `UniversalConfigScreen.java`: Dynamic reflection/annotation-based YACL v3 screen generator that:
    - Introspects static config classes or object instances for `@ConfigOption` fields.
    - Organizes options into ordered `ConfigCategory` and `OptionGroup` structures.
    - Automatically attaches `DasikSupportHelper.createYaclButton()` and `createDiscordYaclButton()` creator buttons to every category.
    - Binds save actions to custom `Runnable` or parameterless `save()` methods.
    - Provides `createFactory(...)` returning `ConfigScreenFactory<?>` for 1-line ModMenu integration.
  - `DynamicGameRuleConfigSync.java`: Bi-directional binding helper that updates live singleplayer GameRules when GUI options change.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- **MC 26.2+ Sovereign Lead**: Java 25, YACL v3 dynamic reflection, `Component.translatable`, client-only GUI isolation.
- **Universal Forward Compatibility**: Exposed as pure `@DasikApiStatus.Public` interfaces ensuring seamless compatibility across `26.1.2`, `26.2`, `26.3+`, and clean backport adaptors for legacy anchors (`1.20.1`, `1.21.1`, `1.21.11` tracked in `BL-WS-022`).

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Dedicated Server Startup Safety**: `@ConfigOption` has ZERO client imports and ZERO `@Environment(EnvType.CLIENT)` so common config classes can be annotated on dedicated servers safely. All GUI builder classes (`UniversalConfigScreen`, `YaclSafetyBridge`, `DynamicGameRuleConfigSync`) are strictly isolated behind `@Environment(EnvType.CLIENT)` and deferred reflection in `GuiHelper`.
- **Missing YACL / ModMenu**: Returns `parent -> null` dummy factory gracefully without throwing `ClassNotFoundException` or `NoClassDefFoundError`.
- **Freedom Over Anti-Crash & Saturated Bounds**: Numerical options without finite bounds default to unbounded numeric fields (`Double.NEGATIVE_INFINITY` to `Double.POSITIVE_INFINITY`). Reflection field errors log warnings and skip gracefully without crashing the game.

## 🚀 5. Implementation Steps
1. Author side-safe `@ConfigOption` annotation in `net.dasik.social.api.config.ConfigOption`.
2. Author `YaclSafetyBridge.java`, `DynamicGameRuleConfigSync.java`, and `UniversalConfigScreen.java` in `net.dasik.social.api.client.gui`.
3. Update `GuiHelper.java` with convenient `createScreenFactory(...)` helper methods.
4. Author comprehensive 5-Pillar test suite in `src/test/java/net/dasik/social/api/client/gui/`.
5. Pre-step SemVer bump to `1.11.0` in `gradle.properties`, compile, verify test suite, and distribute.

## 🏁 6. 5-Pillar Verification Checklist
- [ ] **Pillar 1 (Chaos Fuzzing)**: 10,000 iterations of extreme bounds, NaN/infinite numbers, and null instances in `UniversalConfigScreenTest`.
- [ ] **Pillar 2 (Concurrency Load)**: 50 concurrent virtual threads generating screens simultaneously.
- [ ] **Pillar 3 (Annotation AST & Field Introspection)**: Headless assertion of field discovery, category ordering, and type resolution.
- [ ] **Pillar 4 (DynamicGameRule Sync Integration)**: Simulation of live singleplayer GameRule synchronization.
- [ ] **Pillar 5 (Sidedness & Hygiene Contract)**: 0 client imports in `@ConfigOption`; single-line GPLv3 header on 100% of files.
- [ ] **Compilation & Build**: Clean `./gradlew test build` producing `dasik-library-1.11.0.jar`.
