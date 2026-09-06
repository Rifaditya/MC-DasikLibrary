# Gestionnaire Dynamique de GameRules

| Composant | Classe |
| :--- | :--- |
| **Classe Gestionnaire** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **Format de Clé Namespacée** | `modid:rule_name` |
| **Injection de Traduction** | `LanguageMixin` -> Map `GENERATED_TRANSLATIONS` |
| **Format d'En-tête de Catégorie** | `§lCategory Title (N rules)` |

---

## 🎲 Présentation & GameRules Namespacées

`DynamicGameRuleManager` permet aux mods consommateurs d'enregistrer dynamiquement des règles de jeu (GameRules) sans devoir déclarer manuellement chaque clé dans un fichier `en_us.json`.

### Fonctionnalités Clés
1. **Injection Automatique de Traduction** : Convertit les identifiants techniques `modid:rule_name` en libellés lisibles (ex. `bd_enable_guard_mode` -> `BD Enable Guard Mode`) et les injecte via `LanguageMixin`.
2. **Titres de Catégories en Gras (`§l`)** : Les catégories déclarées avec `registerCategory` reçoivent automatiquement un préfixe stylisé pour une mise en valeur claire dans les interfaces graphiques.
3. **Méthodes Utilitaires Mathématiques** :
   - `getPct(Level level, GameRule<Integer> rule)` — Convertit un entier (`0-100`) en Double (`/ 100.0`).
   - `getChance(Level level, GameRule<Integer> rule)` — Convertit un pourcentage en Float (`/ 100.0f`).
   - `getProb(Level level, GameRule<Integer> rule)` — Convertit une valeur par mille (`0-1000`) en Float (`/ 1000.0f`).
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — Convertit un décile en Float (`/ 10.0f`).
   - `getIntVal(Level level, String key, int defaultValue)` — Récupère une valeur entière directement par sa clé textuelle.

---

## 💻 Exemple de Code Développeur

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

## 🔗 Pages Liées
* [[Codec et Sérialisation des GameRules|fr_fr-GameRule-Codec-and-Serialization]]
* [[GameRules Client et Assistants GUI|fr_fr-Client-GameRule-and-GUI-Helpers]]
