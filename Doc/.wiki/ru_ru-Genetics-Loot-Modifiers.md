# Модификаторы добычи генетики

| Компонент | Класс |
| :--- | :--- |
| **Интерфейс модификатора**| `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **Класс реестра** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Класс Mixin-перехватчика**| `net.dasik.social.mixin.LivingEntityLootMixin` |
| **Целевой метод** | `LivingEntity.dropFromLootTable` |

---

## 🥩 Обзор и перехват добычи

`GeneticsLootRegistry` позволяет модам динамически изменять лут при гибели мобов в зависимости от генетики (например, гиганты дают больше мяса, недомерки дают меньше ресурсов, а мутанты сбрасывают редкие предметы).

```ascii
[ LivingEntity.dropFromLootTable ]
                │
                ▼
 ┌─────────────────────────────┐
 │    LivingEntityLootMixin    │  ◄── @ModifyVariable Consumer Hook
 └──────────────┬──────────────┘
                │
                ▼
 ┌─────────────────────────────┐
 │    GeneticsLootRegistry     │  ◄── Lookup Modifier for EntityType
 └──────────────┬──────────────┘
                │
                ▼
 ┌─────────────────────────────┐
 │    GeneticsLootModifier     │  ◄── Scale stack count / Swap item
 └─────────────────────────────┘
```

---

## 💻 Пример кода для разработчиков

Регистрация модификатора добычи для волков на основе физического масштаба:

```java
GeneticsLootRegistry.register(EntityTypes.WOLF, (entity, genetics, stack, random) -> {
    float scale = DasikAnimalGeneticsAPI.getScale(entity);
    if (stack.is(Items.BONE)) {
        // Scale bone drop count proportionally to entity scale
        int newCount = Math.max(1, Math.round(stack.getCount() * scale));
        return new ItemStack(Items.BONE, newCount);
    }
    return stack;
});
```

---

## 🔗 Связанные страницы
* [[Движок генетики животных|ru_ru-Animal-Genetics-Engine]]
* [[Справочник Mixin и точки внедрения|ru_ru-Mixin-Reference-and-Hooks]]
