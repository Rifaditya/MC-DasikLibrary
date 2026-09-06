# Stochastik- & Mathe-Werkzeuge

| Hilfsklasse | Zweck |
| :--- | :--- |
| `net.dasik.social.util.FastRandom` | Allokationsfreier Singleton-`RandomSource` auf Basis von `ThreadLocalRandom` |
| `net.dasik.social.util.StochasticUtil` | Wahrscheinlichkeitsabfragen in Promille, Dezilen und Prozenten |
| `net.dasik.social.util.TimeUtil` | Spieltagsberechnungen (`gameTime / 24000L`) und Zyklusabstände |

---

## 🎲 `FastRandom` (`RandomSource`)

`FastRandom` bietet eine threadsichere Singleton-Instanz (`FastRandom.INSTANCE`), die Aufrufe an `ThreadLocalRandom` weiterleitet:

```java
FastRandom random = FastRandom.INSTANCE;
float val = random.nextFloat(); // [0.0f, 1.0f)
```

---

## ⏱️ Zyklusmathematik in `TimeUtil`

$$1\text{ Minecraft Day} = 24,000\text{ ticks}$$

```java
long currentDay = TimeUtil.getGameDay(level.getGameTime());
long ticksToTarget = TimeUtil.getCycleDistance(currentTime, targetTime, 24000L);
```

---

## 🔗 Verwandte Seiten
* [[Tiergenetik-Engine|de_de-Animal-Genetics-Engine]]
* [[Dynamischer GameRules-Manager|de_de-Dynamic-GameRules-Manager]]
