# GameRule-Codec & Serialisierung

| Thema | Spezifikation |
| :--- | :--- |
| **Serialisierungsfehler** | Absturz in `SavedDataStorage.encodeUnchecked` beim Welt-Speichern |
| **Behoben in Release** | `1.8.15` |
| **Ganzzahlgrenzen** | `min = Integer.MIN_VALUE` (oder `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ Der Integer-GameRule-Codec-Fix

In Minecraft 26.2 werden dynamische GameRules über `SavedDataStorage.encodeUnchecked` in `level.dat` / `game_rules.dat` serialisiert. In frühen Versionen führte die Registrierung von Integer-Regeln ohne explizite Untergrenze dazu, dass der Codec `min = 0` erzwang, was beim Speichern negativer Zahlen oder Standardwerten zu einer `IllegalStateException` führte.

### Die Lösung in `DynamicGameRuleManager`

```java
// IntegerBuilder initialization fix in DynamicGameRuleManager
public class IntegerBuilder {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public GameRule<Integer> register() {
        int effectiveMin = Math.min(min, defaultValue);
        int effectiveMax = Math.max(max, defaultValue);
        // Uses Codec.INT.intRange(effectiveMin, effectiveMax) to prevent encodeUnchecked bounds check failure
        GameRule<Integer> rule = new GameRule<>(
            GameRuleType.INT, defaultValue, Codec.INT.intRange(effectiveMin, effectiveMax), 
            FeatureFlagSet.of(), category, visitor
        );
        return Registry.register(BuiltInRegistries.GAME_RULE, Identifier.parse(ruleName), rule);
    }
}
```

---

## 🔒 NBT-Namespace-Sicherheit

Verwenden Sie bei dynamischen Regeln immer das Format `modid:rule_name` (`[ERR-20260510-002]`). Regeln ohne Namespace können nicht sauber in die Weltdaten serialisiert werden, was zu stillem Konfigurationsverlust beim Server-Neustart führt.

---

## 🔗 Verwandte Seiten
* [[Dynamischer GameRules-Manager|de_de-Dynamic-GameRules-Manager]]
* [[Client-GameRules & GUI-Helfer|de_de-Client-GameRule-and-GUI-Helpers]]
