# Dynamic GameRules Manager

| Component | Class |
| :--- | :--- |
| **Manager Class** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **Key Namespace Format** | `modid:rule_name` |
| **Translation Injection** | `LanguageMixin` -> `GENERATED_TRANSLATIONS` Map |
| **Category Header Format** | `§lCategory Title (N rules)` |

---

## 🎲 Overview & Namespaced GameRules

`DynamicGameRuleManager` allows consumer mods to register infinite dynamic GameRules programmatically without adding manual entries into `en_us.json` language files.

### Key Features
1. **Automatic Translation Injection**: Automatically converts `modid:rule_name` into a human-readable title (e.g. `bd_enable_guard_mode` -> `BD Enable Guard Mode`) and injects it into client language tables via `LanguageMixin`.
2. **Category Header Bolding (`§l`)**: Category entries registered via `registerCategory` inject bold formatting (`§l`) to organize GameRule screens cleanly.
3. **Math Helper Converters**:
   - `getPct(Level level, GameRule<Integer> rule)` — Converts integer rule (`0-100`) to double (`/ 100.0`).
   - `getChance(Level level, GameRule<Integer> rule)` — Converts percentage integer rule to float (`/ 100.0f`).
   - `getProb(Level level, GameRule<Integer> rule)` — Converts permille integer rule (`0-1000`) to float (`/ 1000.0f`).
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — Converts decile integer rule to float (`/ 10.0f`).
   - `getIntVal(Level level, String key, int defaultValue)` — Queries integer GameRule value by string key.

---

## 💻 Developer Code Example

```java
// Register a dynamic boolean GameRule with description
GameRule<Boolean> ENABLE_GUARD = DynamicGameRuleManager.booleanRule(
    "betterdogs:bd_enable_guard_mode",
    GameRuleCategory.MOBS,
    true
).description("Enable wolf sentinel guard mode").register();

// Querying GameRule safely across client/server
boolean isGuardEnabled = DynamicGameRuleManager.getBoolean(level, ENABLE_GUARD);
```

---

## 🔗 Related Pages
* [[GameRule Codec & Serialization|GameRule-Codec-and-Serialization]]
* [[Client GameRule & GUI Helpers|Client-GameRule-and-GUI-Helpers]]
