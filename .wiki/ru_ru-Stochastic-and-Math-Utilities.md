# Стохастические и математические утилиты

| Класс утилиты | Назначение |
| :--- | :--- |
| `net.dasik.social.util.FastRandom` | Одиночный безаллокационный `RandomSource` на базе `ThreadLocalRandom` |
| `net.dasik.social.util.StochasticUtil` | Проверки вероятностей в промилле, децилях и процентах |
| `net.dasik.social.util.TimeUtil` | Расчет игровых дней (`gameTime / 24000L`) и циклов времени |

---

## 🎲 `FastRandom` (`RandomSource`)

`FastRandom` предоставляет потокобезопасный синглтон `RandomSource` (`FastRandom.INSTANCE`), работающий поверх `ThreadLocalRandom`:

```java
FastRandom random = FastRandom.INSTANCE;
float val = random.nextFloat(); // [0.0f, 1.0f)
```

---

## ⏱️ Математика циклов в `TimeUtil`

$$1\text{ Minecraft Day} = 24,000\text{ ticks}$$

```java
long currentDay = TimeUtil.getGameDay(level.getGameTime());
long ticksToTarget = TimeUtil.getCycleDistance(currentTime, targetTime, 24000L);
```

---

## 🔗 Связанные страницы
* [[Движок генетики животных|ru_ru-Animal-Genetics-Engine]]
* [[Менеджер динамических GameRules|ru_ru-Dynamic-GameRules-Manager]]
