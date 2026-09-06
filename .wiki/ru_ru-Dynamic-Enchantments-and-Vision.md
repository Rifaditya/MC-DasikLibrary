# Динамические зачарования и трекер поля зрения

| Компонент | Класс |
| :--- | :--- |
| **Менеджер зачарований** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **Трекер поля зрения** | `net.dasik.social.api.vision.PlayerVisionTracker` |
| **Метод проверки видимости** | `PlayerVisionTracker.canSee(ServerPlayer player, Entity target)` |
| **Регистрация слушателя** | `PlayerVisionTracker.registerListener(String modId, double radius)` |

---

## 👁️ Лучевой поиск во фрустуме с `PlayerVisionTracker`

`PlayerVisionTracker` производит эффективный пространственный рейкастинг для определения видимости сущности игроком без создания спайков лагов на сервере.

```java
// Register listener for 16-block vision sweeps
PlayerVisionTracker.registerListener("mymod", 16.0D);

// Check if player can see target entity
boolean isVisible = PlayerVisionTracker.canSee(serverPlayer, targetEntity);
```

---

## ✨ Временные зачарования с `DynamicEnchantmentManager`

Позволяет модам вычислять динамические эффекты зачарований или наделять броню/оружие временными уровнями зачарований без изменения постоянных NBT-тегов предметов.

---

## 🔗 Связанные страницы
* [[Стохастические и математические утилиты|ru_ru-Stochastic-and-Math-Utilities]]
* [[Архитектура и структура пакетов|ru_ru-Architecture-and-Package-Layout]]
