# Социальный планировщик и система событий

| Компонент | Класс |
| :--- | :--- |
| **Движок планировщика** | `net.dasik.social.core.EntitySocialScheduler` |
| **Реестр событий** | `net.dasik.social.api.SocialEventRegistry` |
| **Уровни приоритета** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **Типы сигналов** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

---

## 🔄 Двухколейная модель планирования

Каждая социальная сущность содержит `EntitySocialScheduler`, обрабатывающий задачи в двух независимых колеях:

1. **Mood Track (Долгосрочные поведенческие состояния)**: Оценивается на низкой частоте (например, каждые 20-100 тиков) для обновления эмоционального фона, уровня агрессии или выносливости.
2. **Ambient Track (Краткосрочные тактические сигналы)**: Оценивается на высокой частоте (каждые 1-5 тиков) для немедленной реакции на внешние раздражители, угрозы или стайные маневры.

```ascii
                      ┌───────────────────────────────┐
                      │    EntitySocialScheduler      │
                      └───────────────┬───────────────┘
                                      │
              ┌───────────────────────┴───────────────────────┐
              ▼                                               ▼
   ┌─────────────────────┐                         ┌─────────────────────┐
   │     Mood Track      │                         │    Ambient Track    │
   │ (Low Frequency Ticks)│                         │(High Frequency Ticks)│
   │ - Pack Hierarchy   │                         │ - Obstacle Avoidance│
   │ - Hunger / Fatigue  │                         │ - Signal Response   │
   └─────────────────────┘                         └─────────────────────┘
```

---

## 📢 `SocialEventRegistry` и реализация событий

Моды реализуют интерфейс `SocialEvent` и регистрируют экземпляры через `SocialEventRegistry` во время инициализации:

```java
public class HowlEvent implements SocialEvent {
    @Override public String getId() { return "betterdogs:howl"; }
    @Override public int getPriorityValue() { return 80; }
    @Override public String getTrackId() { return "pack_command"; }
    @Override public boolean canPreempt(SocialEvent other) { return other.getPriorityValue() < 80; }
    @Override public void onStart(TickContext context) {}
    @Override public boolean tick(TickContext context) { return false; }
    @Override public void onEnd(SocialEntity entity, EndReason reason) {}
}

// Register during mod initialization (frozen on first pulse)
SocialEventRegistry.register(new HowlEvent());
```

---

## 📊 Матрица приоритетов PriorityTier

| Уровень | Макс. число треков | Политика распределения | Типичное применение |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | Мгновенный перехват, высший приоритет | Бегство от опасности, бой |
| `HIGH` | `8` | Вытесняет обычные/низкие задачи | Тактические команды, зов стаи |
| `NORMAL` | `16` | Стандартный бюджет тиков | Социальные взаимодействия, прогулка |
| `LOW` | `32` | Максимальная емкость, фоновый приоритет | Фоновый осмотр окружения |

---

## 🔗 Связанные страницы
* [[Социальная система коллективного разума|ru_ru-Hive-Mind-Social-System]]
* [[Профили поведения и условия|ru_ru-Behavior-Profiles-and-Conditions]]
