# Менеджер динамических GameRules

| Компонент | Класс |
| :--- | :--- |
| **Класс менеджера** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **Формат ключа правила** | `modid:rule_name` |
| **Инъекция переводов** | `LanguageMixin` -> Map `GENERATED_TRANSLATIONS` |
| **Формат заголовка категории** | `§lCategory Title (N rules)` |

---

## 🎲 Обзор и GameRules с пространствами имен

`DynamicGameRuleManager` позволяет модам регистрировать неограниченное количество динамических правил программно без необходимости вручную добавлять строки в файлы `en_us.json`.

### Ключевые особенности
1. **Автоматическая инъекция переводов**: Преобразует `modid:rule_name` в удобочитаемый заголовок (например, `bd_enable_guard_mode` -> `BD Enable Guard Mode`) и внедряет его в таблицу локализации через `LanguageMixin`.
2. **Жирные заголовки категорий (`§l`)**: Записи, зарегистрированные через `registerCategory`, форматируются жирным шрифтом (`§l`) для красивого разделения экрана правил.
3. **Математические преобразователи**:
   - `getPct(Level level, GameRule<Integer> rule)` — преобразует целое правило (`0-100`) в double (`/ 100.0`).
   - `getChance(Level level, GameRule<Integer> rule)` — преобразует проценты в float (`/ 100.0f`).
   - `getProb(Level level, GameRule<Integer> rule)` — преобразует промилле (`0-1000`) в float (`/ 1000.0f`).
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — преобразует децили в float (`/ 10.0f`).
   - `getIntVal(Level level, String key, int defaultValue)` — запрашивает целое значение по ключу.

---

## 💻 Пример кода для разработчиков

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

## 🔗 Связанные страницы
* [[Codec и сериализация GameRules|ru_ru-GameRule-Codec-and-Serialization]]
* [[Клиентские GameRules и помощники GUI|ru_ru-Client-GameRule-and-GUI-Helpers]]
