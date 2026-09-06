# 📌 Dasik Library Backlog

This file tracks planned features, technical refinements, performance optimizations, and deferred bug fixes for **Dasik Library**.

---

## 📊 Backlog Summary

| ID | Category | Title | Priority | Target Version | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `[BL-DASIK-001]` | `[FEATURE]` | Dynamic GameRule Unregistration & Orphaned Mod Registry Pruning | `[HIGH]` | `Universal (26.1.2+)` | `✅ RESOLVED` |

---

## 🏷 Legend & Status Tags
- **Categories**: `[FEATURE]`, `[REFINEMENT]`, `[BUGFIX]`, `[PERF]`, `[TECH_DEBT]`
- **Priorities**: `[HIGH]` (Critical logic fix/enhancement), `[MEDIUM]` (Quality of life / optimization), `[LOW]` (Minor polish)
- **Statuses**: `📌 DEFERRED` (Queued for future work), `🚧 IN_PROGRESS` (Active development), `✅ RESOLVED` (Implemented and verified)

---

## 📝 Detailed Backlog Entries

### [BL-DASIK-001] Dynamic GameRule Unregistration & Orphaned Mod Registry Pruning
- **Category**: `[FEATURE]`
- **Priority**: `[HIGH]`
- **Status**: `✅ RESOLVED`
- **Target Component(s)**: [DynamicGameRuleManager.java](src/main/java/net/dasik/social/api/gamerule/DynamicGameRuleManager.java), [LanguageMixin.java](src/main/java/net/dasik/social/mixin/LanguageMixin.java)
- **Date Added**: 2026-08-25

#### ❓ Problem / Context
When mods registering dynamic GameRules via `DynamicGameRuleManager` (such as **Agrarian Reform** and **Durability Multiplier**) are uninstalled or removed by a player, their registered GameRules remain lingering in Minecraft's `BuiltInRegistries.GAME_RULE`, internal manager lookup maps (`DYNAMIC_RULES`, `GENERATED_TRANSLATIONS`), client language tables, and world save data (`level.dat` GameRules).

Because there is currently no unregistration or pruning lifecycle, these orphaned GameRules do nothing at runtime while cluttering registries, `/gamerule` command tab-completion, and GUI configuration menus.

#### 💡 Proposed Solution & Technical Specifications
Implement dynamic unregistration and orphaned mod registry pruning in `DynamicGameRuleManager`:

1. **Explicit Unregistration API**:
   - `unregister(String ruleName)` / `unregister(Identifier id)`: Purges dynamic GameRules and generated translation keys from `DYNAMIC_RULES` and `GENERATED_TRANSLATIONS`.
   - `unregisterModRules(String modId)`: Unregisters all dynamic GameRules associated with a given namespace or prefixed ore identifier.

2. **Automated Mod Lifecycle Pruning Sweep**:
   - On server lifecycle startup (`ServerLifecycleEvents.SERVER_STARTING`), sweep registered dynamic GameRules and evaluate active mod IDs via `FabricLoader.getInstance().isModLoaded(namespace)`.
   - Automatically prune and unregister GameRules belonging to uninstalled mods.

```java
public static boolean unregister(Identifier id) {
    if (id == null) return false;
    String ruleName = id.toString();
    GameRule<?> removed = DYNAMIC_RULES.remove(ruleName);
    String translationKey = Util.makeDescriptionId("gamerule", id);
    GENERATED_TRANSLATIONS.remove(translationKey);
    GENERATED_TRANSLATIONS.remove(translationKey + ".description");
    if (removed != null && LOGGER.isDebugEnabled()) {
        LOGGER.debug("DynamicGameRuleManager: Unregistered dynamic GameRule '{}'", ruleName);
    }
    return removed != null;
}

public static int pruneOrphanedRules() {
    FabricLoader loader = FabricLoader.getInstance();
    if (loader == null) return 0;
    int prunedCount = 0;
    for (String ruleName : DYNAMIC_RULES.keySet()) {
        String modId = extractModId(ruleName);
        if (modId != null && !loader.isModLoaded(modId)) {
            if (unregister(ruleName)) {
                prunedCount++;
            }
        }
    }
    return prunedCount;
}
```

#### 🎯 Acceptance Criteria
- [x] Dynamic GameRules belonging to uninstalled mods can be explicitly or automatically unregistered.
- [x] Internal maps (`DYNAMIC_RULES`, `GENERATED_TRANSLATIONS`) cleanly remove keys for uninstalled mods.
- [x] Automated lifecycle sweep (`ServerLifecycleEvents.SERVER_STARTING`) prunes orphaned rules on server startup.
- [x] Headless unit tests assert single unregistration, bulk namespace unregistration, prefix parsing, and null/boundary safety.
