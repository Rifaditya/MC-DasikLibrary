# Dynamischer GameRules-Manager

| Komponente | Klasse |
| :--- | :--- |
| **Manager-Klasse** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **Namespace-Schlüsselformat**| `modid:rule_name` |
| **Übersetzungsinjektion** | `LanguageMixin` -> Map `GENERATED_TRANSLATIONS` |
| **Kategorie-Headerformat** | `§lCategory Title (N rules)` |

---

## 🎲 Übersicht & Namenbehaftete GameRules

`DynamicGameRuleManager` erlaubt es Consumer-Mods, unbegrenzt dynamische GameRules programmatisch zu registrieren, ohne Einträge manuell in `en_us.json`-Dateien eintragen zu müssen.

### Wichtigste Funktionen
1. **Automatische Übersetzungsinjektion**: Konvertiert `modid:rule_name` in lesbaren Klartext (z. B. `bd_enable_guard_mode` -> `BD Enable Guard Mode`) und speist ihn über `LanguageMixin` direkt in die Sprachentabelle ein.
2. **Kategorieüberschriften in Fettschrift (`§l`)**: Über `registerCategory` registrierte Kategorien werden für eine saubere GUI-Darstellung automatisch fett hervorgehoben.
3. **Mathematische Hilfskonverter**:
   - `getPct(Level level, GameRule<Integer> rule)` — Wandelt Integer-Werte (`0-100`) in Double um (`/ 100.0`).
   - `getChance(Level level, GameRule<Integer> rule)` — Wandelt Prozentwerte in Float um (`/ 100.0f`).
   - `getProb(Level level, GameRule<Integer> rule)` — Wandelt Promillewerte (`0-1000`) in Float um (`/ 1000.0f`).
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — Wandelt Dezilwerte in Float um (`/ 10.0f`).
   - `getIntVal(Level level, String key, int defaultValue)` — Fragt Integer-Werte über ihren String-Schlüssel ab.

---

## 💻 Entwickler-Codebeispiel

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

## 🔗 Verwandte Seiten
* [[GameRule-Codec & Serialisierung|de_de-GameRule-Codec-and-Serialization]]
* [[Client-GameRules & GUI-Helfer|de_de-Client-GameRule-and-GUI-Helpers]]
