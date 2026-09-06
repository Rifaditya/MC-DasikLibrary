# 客戶端遊戲規則與 GUI 助手

| 助手類別 | 適用環境 | 用途 |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | 客戶端單人遊戲 / 整合伺服器 | 在客戶端執行緒查詢整合伺服器的 GameRules |
| `ConfigHelper` | Common (客戶端/伺服器) | 原子性 JSON 配置加載、保存與備份替換 |
| `GuiHelper` | 客戶端 | 可選 ModMenu / Cloth Config 介面助手 |

---

## 🖥️ 專用伺服器類別加載安全

為確保 100% 伺服器端相容性且絕不導致專用伺服器因調用客戶端類別崩潰，客戶端 GUI 與整合伺服器助手全面採用延遲類別加載檢查（`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`）。

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

## 📄 `ConfigHelper` 中的原子性 JSON 替換

`ConfigHelper` 提供了帶有原子替換邏輯的高可靠性 JSON 檔案持久化機制：

1. 將新配置數據寫入臨時檔案 `config.json.tmp`。
2. 驗證 JSON 結構完整性與檔案大小。
3. 自動建立備份檔案 `config.json.bak`。
4. 透過 `Files.move(..., StandardCopyOption.ATOMIC_MOVE)` 執行原子檔案替換 `config.json.tmp` -> `config.json`。

---

## 🔗 相關頁面
* [[動態遊戲規則管理器|zh_tw-Dynamic-GameRules-Manager]]
* [[行為設定檔與觸發條件|zh_tw-Behavior-Profiles-and-Conditions]]
