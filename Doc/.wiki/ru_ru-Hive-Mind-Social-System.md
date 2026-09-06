# Социальная система коллективного разума

| Системный параметр | Значение |
| :--- | :--- |
| **Основной класс** | `net.dasik.social.core.GlobalSocialSystem` |
| **Класс реестра** | `net.dasik.social.core.SocialRegistry` |
| **Частота импульса** | `1 tick` (Правило Горца) |
| **Сложность поиска** | $O(1)$ Шардированный HashMap |
| **Стратегия очистки памяти** | Автоматическая очистка по `isAlive()` и `isRemoved()` |

---

## ⚡ Обзор и Правило Горца (Highlander Rule)

**Социальная система коллективного разума** — это центральный импульсный движок Dasik Library. Вместо того чтобы сотни социальных мобов независимо запускали тяжелые пространственные запросы каждый тик, библиотека использует единый глобальный координатор импульсов (`GlobalSocialSystem`).

### 👑 Правило Горца ("Остаться должен только один")
`GlobalSocialSystem` гарантирует, что за один тик игрового сервера ($20\text{ ticks} = 1\text{s}$) выполняется строго **один глобальный импульсный цикл**. Если несколько потоков или подсистем пытаются инициировать тик в рамках одного игрового тика, `GlobalSocialSystem` игнорирует повторные вызовы:

$$\text{Global Pulse Execution} = \begin{cases} \text{Execute Ticks}, & \text{if } \text{currentTick} > \text{lastTick} \\ \text{Skip (No-Op)}, & \text{if } \text{currentTick} \le \text{lastTick} \end{cases}$$

```ascii
[ Server Level Tick ]
         │
         ▼
 ┌──────────────────────┐
 │ GlobalSocialSystem   │  ◄── Highlander Rule Guard (1 tick per server tick)
 └──────────┬───────────┘
            │
            ▼
 ┌──────────────────────┐
 │   SocialRegistry     │  ◄── O(1) Shard Lookup & Dead Entity Purge
 └──────────┬───────────┘
            │
      ┌─────┴────────────────┐
      ▼                      ▼
 ┌──────────────┐      ┌──────────────┐
 │ Entity 1     │      │ Entity N     │
 │ Mood Task    │      │ Mood Task    │
 └──────────────┘      └──────────────┘
```

---

## 🗂️ Архитектура `SocialRegistry`

`SocialRegistry` хранит активные социальные сущности, распределенные по видам и UUID миров:

* **$O(1)$ Регистрация**: Сущности регистрируются через `SocialRegistry.register(SocialEntity entity)`.
* **Автоматическая очистка**: Недействительные ссылки (выгруженные чанки, погибшие сущности) автоматически удаляются во время цикла через проверки `entity.dasik$asEntity().isAlive()` и `isRemoved()`.
* **Шардирование по видам**: Сущности шардируются по `dasik$getSpeciesId()`, обеспечивая сверхбыстрые локальные проверки близости для механик стаи без сканирования всех сущностей мира.

---

## 💻 Пример кода для разработчиков

```java
// Registering an entity to the Hive Mind
public class CustomSocialMob extends PathfinderMob implements SocialEntity {
    private final EntitySocialScheduler scheduler = new EntitySocialScheduler();

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            SocialRegistry.register(this);
            GlobalSocialSystem.pulse(serverLevel);
        }
    }

    @Override
    public SocialScheduler dasik$getScheduler() {
        return this.scheduler;
    }
}
```

---

## 🔗 Связанные страницы
* [[Социальный планировщик и события|ru_ru-Social-Scheduler-and-Events]]
* [[Архитектура и структура пакетов|ru_ru-Architecture-and-Package-Layout]]
