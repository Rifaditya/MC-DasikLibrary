# Stochastic & Math Utilities

| Utility Class | Purpose |
| :--- | :--- |
| `net.dasik.social.util.FastRandom` | Singleton zero-allocation `RandomSource` backed by `ThreadLocalRandom` |
| `net.dasik.social.util.StochasticUtil` | Permille, decile, and percentage probability rolls |
| `net.dasik.social.util.TimeUtil` | Day count calculations (`gameTime / 24000L`) and cycle distance math |

---

## 🎲 `FastRandom` (`RandomSource`)

`FastRandom` provides a thread-safe singleton `RandomSource` (`FastRandom.INSTANCE`) delegating to `ThreadLocalRandom`:

```java
FastRandom random = FastRandom.INSTANCE;
float val = random.nextFloat(); // [0.0f, 1.0f)
```

---

## ⏱️ `TimeUtil` Cycle Math

$$1\text{ Minecraft Day} = 24,000\text{ ticks}$$

```java
long currentDay = TimeUtil.getGameDay(level.getGameTime());
long ticksToTarget = TimeUtil.getCycleDistance(currentTime, targetTime, 24000L);
```

---

## 🔗 Related Pages
* [[Animal Genetics Engine|Animal-Genetics-Engine]]
* [[Dynamic GameRules Manager|Dynamic-GameRules-Manager]]
