# Codec и сериализация GameRules

| Тема | Спецификация |
| :--- | :--- |
| **Проблема сериализации** | Сбой `SavedDataStorage.encodeUnchecked` при сохранении мира |
| **Исправлено в релизе** | `1.8.15` |
| **Границы целых чисел** | `min = Integer.MIN_VALUE` (или `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ Исправление кодека целочисленных GameRule

В Minecraft 26.2 динамические правила сериализуются в `level.dat` / `game_rules.dat` через `SavedDataStorage.encodeUnchecked`. В старых версиях регистрация целочисленного правила без явной нижней границы приводила к жесткому ограничению `min = 0`, вызывая `IllegalStateException` при сохранении отрицательных значений или порогов по умолчанию.

### Реализация исправления в `DynamicGameRuleManager`

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

## 🔒 Безопасность пространств имен NBT

Всегда указывайте пространство имен в формате `modid:rule_name` (`[ERR-20260510-002]`). Правила без пространства имен не могут корректно сохраниться в файлах мира, что приводит к тихой потере конфигурации после перезапуска сервера.

---

## 🔗 Связанные страницы
* [[Менеджер динамических GameRules|ru_ru-Dynamic-GameRules-Manager]]
* [[Клиентские GameRules и помощники GUI|ru_ru-Client-GameRule-and-GUI-Helpers]]
