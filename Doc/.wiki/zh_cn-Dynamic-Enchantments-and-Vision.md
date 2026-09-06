# 动态附魔与视野追踪

| 组件 | 类 |
| :--- | :--- |
| **附魔管理器** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **视野追踪器** | `net.dasik.social.api.vision.PlayerVisionTracker` |
| **视野查询方法** | `PlayerVisionTracker.canSee(ServerPlayer player, Entity target)` |
| **监听器注册** | `PlayerVisionTracker.registerListener(String modId, double radius)` |

---

## 👁️ `PlayerVisionTracker` 视锥射线检测

`PlayerVisionTracker` 执行高效的空间扫描射线检测，用于判定实体是否处于玩家的视野范围内，而不会引发服务器卡顿。

```java
// Register listener for 16-block vision sweeps
PlayerVisionTracker.registerListener("mymod", 16.0D);

// Check if player can see target entity
boolean isVisible = PlayerVisionTracker.canSee(serverPlayer, targetEntity);
```

---

## ✨ `DynamicEnchantmentManager` 运行时附魔

允许模组评估动态附魔效果，或向实体护甲/武器注入临时运行时附魔等级，而无需修改持久化的 NBT 标签。

---

## 🔗 相关页面
* [[随机与数学实用工具|zh_cn-Stochastic-and-Math-Utilities]]
* [[架构与包结构设计|zh_cn-Architecture-and-Package-Layout]]
