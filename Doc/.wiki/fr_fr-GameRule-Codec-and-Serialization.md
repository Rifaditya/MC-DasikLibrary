# Codec et Sérialisation des GameRules

| Sujet | Spécification |
| :--- | :--- |
| **Problème de Sérialisation** | Crash lors de `SavedDataStorage.encodeUnchecked` à la sauvegarde |
| **Résolu dans la Version** | `1.8.15` |
| **Bornes Entières Définies** | `min = Integer.MIN_VALUE` (ou `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ Le Correctif du Codec de GameRules Entières

Sous Minecraft 26.2, les GameRules dynamiques sont sérialisées dans les fichiers `level.dat` / `game_rules.dat` via `SavedDataStorage.encodeUnchecked`. Dans les premières versions, l'enregistrement d'une règle entière sans spécification explicite des bornes imposait un minimum par défaut de `min = 0`, provoquant une exception `IllegalStateException` lors de la persistance de valeurs négatives ou de clés réinitialisées.

### Implémentation dans `DynamicGameRuleManager`

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

## 🔒 Intégrité des Noms avec Namespace

Utilisez systématiquement la structure `modid:rule_name` (`[ERR-20260510-002]`). Les règles non namespacées ne peuvent pas être correctement sérialisées et risquent d'être réinitialisées silencieusement au redémarrage du serveur.

---

## 🔗 Pages Liées
* [[Gestionnaire Dynamique de GameRules|fr_fr-Dynamic-GameRules-Manager]]
* [[GameRules Client et Assistants GUI|fr_fr-Client-GameRule-and-GUI-Helpers]]
