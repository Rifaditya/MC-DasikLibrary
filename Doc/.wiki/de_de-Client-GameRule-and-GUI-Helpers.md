# Client-GameRules & GUI-Helfer

| Hilfsklasse | Umgebung | Zweck |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | Client / Integrierter Server | Fragt GameRules des integrierten Servers im Client-Thread ab |
| `ConfigHelper` | Common (Client/Server) | Atomares Laden, Speichern und Backup-Austausch von JSON-Dateien |
| `GuiHelper` | Client | Hilfsklassen für ModMenu- und Cloth Config-Menüs |

---

## 🖥️ Schutz vor ClassLoader-Abstürzen auf dedizierten Servern

Um 100%ige Serverkompatibilität ohne Abstürze auf dedizierten Servern zu garantieren, nutzen Client-GUI-Komponenten verzögerte Klassenladeprüfungen (`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`).

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

## 📄 Atomare JSON-Ersetzungen in `ConfigHelper`

`ConfigHelper` bietet robusten Schutz vor beschädigten Konfigurationen bei Abstürzen oder Stromausfall:

1. Schreibt neue Daten in eine temporäre Datei `config.json.tmp`.
2. Validiert die JSON-Syntax und die Dateigröße.
3. Erstellt automatisch ein Backup `config.json.bak`.
4. Vollzieht den atomaren Dateitausch `config.json.tmp` -> `config.json` via `Files.move(..., StandardCopyOption.ATOMIC_MOVE)`.

---

## 🔗 Verwandte Seiten
* [[Dynamischer GameRules-Manager|de_de-Dynamic-GameRules-Manager]]
* [[Verhaltensprofile & Bedingungen|de_de-Behavior-Profiles-and-Conditions]]
