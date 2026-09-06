# Очистка устаревших атрибутов и масштабирование

| Исправление | Проблема | Релиз |
| :--- | :--- | :--- |
| **Баг сущностей-гигантов** | Накопление устаревших модификаторов атрибутов | `1.8.11` |
| **Смещение базового масштаба**| Математика смещения базы `ADD_VALUE` | `1.8.10` |

---

## 📐 Математика смещения атрибута масштаба

В Minecraft 26.2 масштабирование сущностей использует ванильный атрибут `minecraft:scale`. При добавлении модификатора `ADD_VALUE` для масштаба, значение модификатора $M_{\text{scale}}$ ОБЯЗАНО вычитать $1.0\text{f}$ из целевого масштаба $S$:

$$M_{\text{scale}} = S - 1.0\text{f}$$

$$\text{Final Scale} = \text{Base Scale } (1.0) + M_{\text{scale}} = 1.0 + (S - 1.0) = S$$

Если не вычесть $1.0\text{f}$, установка целевого масштаба $2.0$ прибавит $+2.0$ к базовой единице, ошибочно порождая моба размером $3.0\text{x}$ (300%).

---

## 🧹 Очистка устаревших модификаторов в `GeneticsEngine`

Для предотвращения дублирования модификаторов при перезагрузке мира `GeneticsEngine` очищает старые модификаторы `genetics_` перед установкой новых:

```java
public static void applyGeneticsModifiers(LivingEntity entity) {
    AttributeInstance scaleInstance = entity.getAttribute(Attributes.SCALE);
    if (scaleInstance != null) {
        // Purge legacy/duplicate modifiers by identifier
        scaleInstance.getModifiers().stream()
            .filter(mod -> mod.id().getPath().startsWith("genetics_"))
            .toList()
            .forEach(scaleInstance::removeModifier);
    }
}
```

---

## 🔗 Связанные страницы
* [[Движок генетики животных|ru_ru-Animal-Genetics-Engine]]
* [[API генетики и родословная|ru_ru-Genetics-API-and-Pedigree]]
