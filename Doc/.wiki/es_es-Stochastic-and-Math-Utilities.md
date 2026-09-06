# Utilidades Estocásticas y Matemáticas

| Clase de Utilidad | Propósito |
| :--- | :--- |
| `net.dasik.social.util.FastRandom` | `RandomSource` singleton de asignación cero basado en `ThreadLocalRandom` |
| `net.dasik.social.util.StochasticUtil` | Cálculos de probabilidad por mil, deciles y porcentajes |
| `net.dasik.social.util.TimeUtil` | Cálculo de días de juego (`gameTime / 24000L`) y distancias de ciclo |

---

## 🎲 `FastRandom` (`RandomSource`)

`FastRandom` ofrece una instancia compartida y segura para hilos (`FastRandom.INSTANCE`) que delega en `ThreadLocalRandom`:

```java
FastRandom random = FastRandom.INSTANCE;
float val = random.nextFloat(); // [0.0f, 1.0f)
```

---

## ⏱️ Matemáticas de Ciclos de Tiempo en `TimeUtil`

$$1\text{ Minecraft Day} = 24,000\text{ ticks}$$

```java
long currentDay = TimeUtil.getGameDay(level.getGameTime());
long ticksToTarget = TimeUtil.getCycleDistance(currentTime, targetTime, 24000L);
```

---

## 🔗 Páginas Relacionadas
* [[Motor de Genética Animal|es_es-Animal-Genetics-Engine]]
* [[Gestor de GameRules Dinámicas|es_es-Dynamic-GameRules-Manager]]
