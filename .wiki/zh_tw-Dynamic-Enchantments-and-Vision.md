# 動態附魔與視野追蹤

| 組件 | 類別 |
| :--- | :--- |
| **附魔管理器** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **視野追蹤器** | `net.dasik.social.api.vision.PlayerVisionTracker` |
| **視野查詢方法** | `PlayerVisionTracker.canSee(ServerPlayer player, Entity target)` |
| **監聽器註冊** | `PlayerVisionTracker.registerListener(String modId, double radius)` |

---

## 👁️ `PlayerVisionTracker` 視錐射線檢測

`PlayerVisionTracker` 執行高效的空間掃描射線檢測，用於判定實體是否處於玩家的視野範圍內，而不會引發伺服器卡頓。

```java
// Register listener for 16-block vision sweeps
PlayerVisionTracker.registerListener("mymod", 16.0D);

// Check if player can see target entity
boolean isVisible = PlayerVisionTracker.canSee(serverPlayer, targetEntity);
```

---

## ✨ `DynamicEnchantmentManager` 運行時附魔

允許模組評估動態附魔效果，或向實體護甲/武器注入臨時運行時附魔等級，而無需修改持久化的 NBT 標籤。

---

## 🔗 相關頁面
* [[隨機與數學實用工具|zh_tw-Stochastic-and-Math-Utilities]]
* [[架構與套件結構設計|zh_tw-Architecture-and-Package-Layout]]
