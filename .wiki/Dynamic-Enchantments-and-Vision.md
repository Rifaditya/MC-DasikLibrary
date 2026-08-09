# Dynamic Enchantments & Vision Tracker

| Component | Class |
| :--- | :--- |
| **Enchantment Manager** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **Vision Tracker** | `net.dasik.social.api.vision.PlayerVisionTracker` |
| **Vision Query Method** | `PlayerVisionTracker.canSee(ServerPlayer player, Entity target)` |
| **Listener Registration** | `PlayerVisionTracker.registerListener(String modId, double radius)` |

---

## 👁️ Frustum Raycasting with `PlayerVisionTracker`

`PlayerVisionTracker` performs efficient spatial sweep raycasting to determine whether entities are within a player's line of sight without causing server lag.

```java
// Register listener for 16-block vision sweeps
PlayerVisionTracker.registerListener("mymod", 16.0D);

// Check if player can see target entity
boolean isVisible = PlayerVisionTracker.canSee(serverPlayer, targetEntity);
```

---

## ✨ Runtime Enchantments with `DynamicEnchantmentManager`

Allows mods to evaluate dynamic enchantment effects or inject temporary runtime enchantment levels onto entity armor/weapons without mutating persistent NBT tags.

---

## 🔗 Related Pages
* [[Stochastic & Math Utilities|Stochastic-and-Math-Utilities]]
* [[Architecture & Package Layout|Architecture-and-Package-Layout]]
