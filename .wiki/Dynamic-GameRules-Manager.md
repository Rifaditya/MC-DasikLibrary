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
   - `getPct(level, ruleKey)` — Converts `0-100` int rule to float `[0.0, 1.0]`.
   - `getChance(level, ruleKey)` — Evaluates permille `0-1000` against random float.
   - `getProb(level, ruleKey)` — Evaluates percentage `0-100` against random float.

---

## 💻 Developer Code Example

```java
// Register a custom category header
GameRules.Key<GameRules.BooleanValue> ENABLE_GUARD;

ENABLE_GUARD = DynamicGameRuleManager.registerBoolean(
    "betterdogs:bd_enable_guard_mode",
    GameRules.Category.MOBS,
    true
);

// Querying GameRule safely across client/server
boolean isGuardEnabled = DynamicGameRuleManager.getBoolean(level, "betterdogs:bd_enable_guard_mode");
```

---

## 🔗 Related Pages
* [[GameRule Codec & Serialization|GameRule-Codec-and-Serialization]]
* [[Client GameRule & GUI Helpers|Client-GameRule-and-GUI-Helpers]]
