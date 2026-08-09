# Stochastic & Math Utilities

| Utility Class | Purpose |
| :--- | :--- |
| `net.dasik.social.util.FastRandom` | High-speed zero-allocation XORSHIFT random generator |
| `net.dasik.social.util.StochasticUtil` | Permille, decile, and percentage probability rolls |
| `net.dasik.social.util.TimeUtil` | Tick-to-second ($20\text{ ticks} = 1\text{s}$) conversions |

---

## 🎲 `FastRandom` (XORSHIFT PRNG)

`FastRandom` provides lightweight pseudo-random generation to avoid synchronization overhead:

$$x \leftarrow x \oplus (x \ll 13), \quad x \leftarrow x \oplus (x \gg 17), \quad x \leftarrow x \oplus (x \ll 5)$$

```java
FastRandom random = new FastRandom(seed);
float val = random.nextFloat(); // [0.0f, 1.0f)
```

---

## ⏱️ `TimeUtil` Conversions

$$1\text{ second} = 20\text{ ticks}, \quad 1\text{ minute} = 1,200\text{ ticks}$$

```java
int ticks = TimeUtil.toTicks(5.5f); // Returns 110 ticks
float seconds = TimeUtil.toSeconds(200); // Returns 10.0 seconds
```

---

## 🔗 Related Pages
* [[Animal Genetics Engine|Animal-Genetics-Engine]]
* [[Dynamic GameRules Manager|Dynamic-GameRules-Manager]]
