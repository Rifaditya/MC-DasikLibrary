# Клиентские GameRules и помощники GUI

| Класс помощника | Среда выполнения | Назначение |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | Клиент / Встроенный сервер | Запрос значений GameRules в потоке клиента |
| `ConfigHelper` | Common (Клиент/Сервер) | Атомарная загрузка, сохранение и бэкап конфигурации JSON |
| `GuiHelper` | Клиент | Помощники экранов для ModMenu / Cloth Config |

---

## 🖥️ Безопасность выделенного сервера от сбоев загрузки классов

Чтобы обеспечить 100% совместимость с выделенным сервером и избежать вылетов из-за обращения к классам клиента, все GUI-помощники защищены ленивой проверкой (`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`).

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

## 📄 Атомарная замена JSON в `ConfigHelper`

`ConfigHelper` гарантирует надежное сохранение файлов конфигурации без риска повреждения при выключении питания:

1. Записывает новые данные во временный файл `config.json.tmp`.
2. Проверяет структуру JSON и размер файла.
3. Создает резервную копию `config.json.bak`.
4. Выполняет атомарную замену `config.json.tmp` -> `config.json` через `Files.move(..., StandardCopyOption.ATOMIC_MOVE)`.

---

## 🔗 Связанные страницы
* [[Менеджер динамических GameRules|ru_ru-Dynamic-GameRules-Manager]]
* [[Профили поведения и условия|ru_ru-Behavior-Profiles-and-Conditions]]
