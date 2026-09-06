# 随机与数学实用工具

| 实用工具类 | 用途 |
| :--- | :--- |
| `net.dasik.social.util.FastRandom` | 基于 `ThreadLocalRandom` 的单例零分配 `RandomSource` |
| `net.dasik.social.util.StochasticUtil` | 千分比、十分位与百分比概率检定 |
| `net.dasik.social.util.TimeUtil` | 游戏天数计算（`gameTime / 24000L`）与周期距离数学 |

---

## 🎲 `FastRandom` (`RandomSource`)

`FastRandom` 提供了线程安全的单例 `RandomSource`（`FastRandom.INSTANCE`），底层委托给 `ThreadLocalRandom`：

```java
FastRandom random = FastRandom.INSTANCE;
float val = random.nextFloat(); // [0.0f, 1.0f)
```

---

## ⏱️ `TimeUtil` 周期数学

$$1\text{ Minecraft Day} = 24,000\text{ ticks}$$

```java
long currentDay = TimeUtil.getGameDay(level.getGameTime());
long ticksToTarget = TimeUtil.getCycleDistance(currentTime, targetTime, 24000L);
```

---

## 🔗 相关页面
* [[动物遗传学引擎|zh_cn-Animal-Genetics-Engine]]
* [[动态游戏规则管理器|zh_cn-Dynamic-GameRules-Manager]]
