# GameRule Codec & Serialization

| Topic | Specification |
| :--- | :--- |
| **Serialization Failure** | `SavedDataStorage.encodeUnchecked` crash during world save |
| **Fix Release** | `1.8.15` |
| **Integer Bounds** | `min = Integer.MIN_VALUE` (or `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ The Integer GameRule Codec Fix

In Minecraft 26.2, dynamic GameRules are serialized into `level.dat` / `game_rules.dat` via `SavedDataStorage.encodeUnchecked`. In earlier library versions, integer GameRules registered without explicit minimum bounds caused the GameRule codec to enforce strict positive bounds (`min = 0`), throwing an `IllegalStateException` when serializing negative integer values or default thresholds.

### The Fix in `DynamicGameRuleManager`

```java
// IntegerBuilder initialization fix in DynamicGameRuleManager
public static GameRules.Key<GameRules.IntValue> registerInt(String name, GameRules.Category category, int defaultValue) {
    int minBound = Math.min(Integer.MIN_VALUE, defaultValue);
    // Prevents encodeUnchecked bounds check failure during world save
    GameRules.Type<GameRules.IntValue> type = GameRules.Type.createIntRule(defaultValue, minBound, Integer.MAX_VALUE);
    return GameRules.register(name, category, type);
}
```

---

## 🔒 NBT Namespace Safety

Always namespace dynamic GameRules with `modid:rule_name` (`[ERR-20260510-002]`). Unnamespaced dynamic GameRules fail to serialize cleanly into vanilla level saved data, causing silent data loss upon server restart.

---

## 🔗 Related Pages
* [[Dynamic GameRules Manager|Dynamic-GameRules-Manager]]
* [[Client GameRule & GUI Helpers|Client-GameRule-and-GUI-Helpers]]
