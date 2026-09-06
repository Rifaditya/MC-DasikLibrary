# Профили поведения и условия

| Компонент | Класс |
| :--- | :--- |
| **Менеджер профилей** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **Интерфейс профиля** | `net.dasik.social.api.profile.BehaviorProfile` |
| **Интерфейс условия** | `net.dasik.social.api.profile.BehaviorCondition` |
| **Маркер сущности** | `net.dasik.social.api.profile.ProfileAware` |

---

## 🎭 Обзор и конечные автоматы динамического поведения

`BehaviorProfileManager` позволяет мобам динамически переключать наборы целей ИИ при изменении условий окружающей среды (ночное время, низкое здоровье, статус лидера стаи, гроза).

```ascii
[ LivingEntity Tick ]
         │
         ▼
┌─────────────────────────────┐
│    BehaviorProfileManager   │  ◄── Evaluate Conditions
└────────┬────────────┬───────┘
         │            │
         ▼            ▼
   [ Night Profile ] [ Combat Profile ]
```

---

## 💻 Пример кода для разработчиков

```java
// Create a profile using DefaultProfileBuilder
BehaviorProfile netherProfile = new DefaultProfileBuilder("nether_hunter")
    .priority(10)
    .condition(BehaviorCondition.inDimension(Level.NETHER))
    .goals(configurator -> configurator.add(2, new FollowLeaderGoal<>(mob, GroupParameters.DEFAULT_TERRESTRIAL, 16.0D)))
    .build();

// Register profile on manager instance
BehaviorProfileManager manager = new BehaviorProfileManager();
manager.registerProfile(netherProfile);
```

---

## 🔗 Связанные страницы
* [[Социальный планировщик и события|ru_ru-Social-Scheduler-and-Events]]
* [[Архитектура и структура пакетов|ru_ru-Architecture-and-Package-Layout]]
