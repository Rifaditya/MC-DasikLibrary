# 隨機與數學實用工具

| 實用工具類別 | 用途 |
| :--- | :--- |
| `net.dasik.social.util.FastRandom` | 基於 `ThreadLocalRandom` 的單例零分配 `RandomSource` |
| `net.dasik.social.util.StochasticUtil` | 千分比、十分位與百分比概率檢定 |
| `net.dasik.social.util.TimeUtil` | 遊戲天數計算（`gameTime / 24000L`）與週期距離數學 |

---

## 🎲 `FastRandom` (`RandomSource`)

`FastRandom` 提供了執行緒安全的單例 `RandomSource`（`FastRandom.INSTANCE`），底層委託給 `ThreadLocalRandom`：

```java
FastRandom random = FastRandom.INSTANCE;
float val = random.nextFloat(); // [0.0f, 1.0f)
```

---

## ⏱️ `TimeUtil` 週期數學

$$1\text{ Minecraft Day} = 24,000\text{ ticks}$$

```java
long currentDay = TimeUtil.getGameDay(level.getGameTime());
long ticksToTarget = TimeUtil.getCycleDistance(currentTime, targetTime, 24000L);
```

---

## 🔗 相關頁面
* [[動物遺傳學引擎|zh_tw-Animal-Genetics-Engine]]
* [[動態遊戲規則管理器|zh_tw-Dynamic-GameRules-Manager]]
