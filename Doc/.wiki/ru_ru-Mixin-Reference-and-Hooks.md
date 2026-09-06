# Справочник Mixin и точки внедрения

| Класс Mixin | Целевой класс | Точка внедрения | Назначение |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `@Inject` на `loadFromJson` (`RETURN`) | Внедрение переводов динамических GameRules |
| `LivingEntityLootMixin` | `LivingEntity` | `@ModifyVariable` на `dropFromLootTable` | Перехват лута для генетического масштабирования |
| `MobGoalAccessor` | `Mob` | `@Accessor("goalSelector")` | Доступ к селектору целей для инъекции ИИ |
| `PathfinderMobMixin` | `PathfinderMob` | `@Inject` на `<init>` (`RETURN`) | Крючок регистрации в Hive Mind |
| `ProfileTriggerMixin` | `Entity` | `@Inject` на `teleportCrossDimension` (`RETURN`) | Срабатывание профиля при смене измерений |

---

## 🔍 Детальный анализ перехватчиков

### 1. `LanguageMixin`
Инжектирует динамически сгенерированные английские метки из `DynamicGameRuleManager` прямо в таблицы локализации игры без создания `.json` файлов.

### 2. `LivingEntityLootMixin`
Оборачивает потребителей лута во время `dropFromLootTable`, делегируя обработку `GeneticsLootRegistry` для изменения количества или типов предметов в зависимости от масштаба моба.

---

## 🔗 Связанные страницы
* [[Архитектура и структура пакетов|ru_ru-Architecture-and-Package-Layout]]
* [[Менеджер динамических GameRules|ru_ru-Dynamic-GameRules-Manager]]
