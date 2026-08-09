# Dynamic Enchantments & Vision Tracker

| Component | Class |
| :--- | :--- |
| **Enchantment Manager** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **Vision Tracker** | `net.dasik.social.api.vision.PlayerVisionTracker` |
| **Frustum Query Method** | `PlayerVisionTracker.isLookingAt(Player, Entity, float maxAngleDegrees)` |

---

## 👁️ Frustum Raycasting with `PlayerVisionTracker`

`PlayerVisionTracker` calculates whether a player is actively looking at a target entity within a cone of vision defined by angle $\theta$:

$$\cos(\theta) = \frac{\vec{D}_{\text{player}} \cdot (\vec{P}_{\text{entity}} - \vec{P}_{\text{player}})}{\|\vec{D}_{\text{player}}\| \|\vec{P}_{\text{entity}} - \vec{P}_{\text{player}}\|}$$

If $\cos(\theta) \ge \cos(\text{maxAngleDegrees})$, the entity is within the player's direct line of sight.

```java
// Check if player is looking at mob within 15 degree cone up to 16 blocks
boolean isLooking = PlayerVisionTracker.isLookingAt(player, mob, 15.0f, 16.0D);
```

---

## ✨ Runtime Enchantments with `DynamicEnchantmentManager`

Allows mods to evaluate dynamic enchantment effects or inject temporary runtime enchantment levels onto entity armor/weapons without mutating persistent NBT tags.

---

## 🔗 Related Pages
* [[Stochastic & Math Utilities|Stochastic-and-Math-Utilities]]
* [[Architecture & Package Layout|Architecture-and-Package-Layout]]
