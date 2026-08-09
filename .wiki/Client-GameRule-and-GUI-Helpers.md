# Client GameRule & GUI Helpers

| Helper Class | Environment | Purpose |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | Client Singleplayer / Integrated Server | Queries integrated server GameRules on client thread |
| `ConfigHelper` | Common (Client/Server) | Atomic JSON config loading, saving, and backup swaps |
| `GuiHelper` | Client | Optional ModMenu / Cloth Config screen helpers |

---

## 🖥️ Dedicated Server Classloading Safety

To allow 100% server-side compatibility without dedicated server crashes, client-side GUI and integrated server helpers use lazy classloading checks (`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`).

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

## 📄 Atomic JSON Swaps in `ConfigHelper`

`ConfigHelper` provides robust JSON file persistence with atomic swap logic:

1. Writes new configuration data to temporary file `config.json.tmp`.
2. Validates JSON structure and file size.
3. Creates automatic backup `config.json.bak`.
4. Performs atomic file swap `config.json.tmp` -> `config.json` via `Files.move(..., StandardCopyOption.ATOMIC_MOVE)`.

---

## 🔗 Related Pages
* [[Dynamic GameRules Manager|Dynamic-GameRules-Manager]]
* [[Behavior Profiles & Conditions|Behavior-Profiles-and-Conditions]]
