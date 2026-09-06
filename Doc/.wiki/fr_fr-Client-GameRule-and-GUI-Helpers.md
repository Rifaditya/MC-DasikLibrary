# GameRules Client et Assistants GUI

| Classe Utilitaire | Environnement | Fonction |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | Client / Serveur Intégré | Interroge les GameRules du serveur intégré depuis le thread client |
| `ConfigHelper` | Commun (Client/Serveur) | Chargement, sauvegarde et permutation atomique des fichiers JSON |
| `GuiHelper` | Client | Fonctions utilitaires d'interface pour ModMenu et Cloth Config |

---

## 🖥️ Prévention des Crashs sur Serveurs Dédiés

Afin de garantir un fonctionnement sans faille sur serveurs dédiés, tous les assistants d'interface client appliquent un chargement conditionnel différé (`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`).

```ascii
                      DynamicGameRuleManager.getInt(level, ruleKey)
                                         │
                    ┌───────────────────┴───────────────────┐
                    ▼                                       ▼
          [ level instanceof ServerLevel ]       [ Client Environment ]
                    │                                       │
                    ▼                                       ▼
            Direct Level Lookup                   ClientGameRuleHelper
                                             (Queries Integrated Server)
```

---

## 📄 Sauvegarde Atomique JSON dans `ConfigHelper`

`ConfigHelper` protège l'intégrité des données de configuration contre les corruptions en cas de coupure inopinée :

1. Écriture des données dans un fichier temporaire `config.json.tmp`.
2. Validation de la syntaxe JSON et de la taille du fichier.
3. Sauvegarde de sécurité dans `config.json.bak`.
4. Remplacement atomique `config.json.tmp` -> `config.json` via `Files.move(..., StandardCopyOption.ATOMIC_MOVE)`.

---

## 🔗 Pages Liées
* [[Gestionnaire Dynamique de GameRules|fr_fr-Dynamic-GameRules-Manager]]
* [[Profils de Comportement et Conditions|fr_fr-Behavior-Profiles-and-Conditions]]
