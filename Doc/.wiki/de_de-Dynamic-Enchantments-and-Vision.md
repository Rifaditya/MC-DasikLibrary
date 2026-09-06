# Dynamische Verzauberungen & Sichtverfolgung

| Komponente | Klasse |
| :--- | :--- |
| **Verzauberungsmanager** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **Sichtprüfer (Vision Tracker)** | `net.dasik.social.api.vision.PlayerVisionTracker` |
| **Sichtabfragemethode** | `PlayerVisionTracker.canSee(ServerPlayer player, Entity target)` |
| **Listener-Registrierung** | `PlayerVisionTracker.registerListener(String modId, double radius)` |

---

## 👁️ Frustum-Raycasting mit `PlayerVisionTracker`

`PlayerVisionTracker` führt effiziente räumliche Raycasts durch, um zu prüfen, ob sich Entitäten im Sichtfeld eines Spielers befinden, ohne Serververzögerungen hervorzurufen.

```java
// Register listener for 16-block vision sweeps
PlayerVisionTracker.registerListener("mymod", 16.0D);

// Check if player can see target entity
boolean isVisible = PlayerVisionTracker.canSee(serverPlayer, targetEntity);
```

---

## ✨ Temporäre Verzauberungen mit `DynamicEnchantmentManager`

Ermöglicht es Mods, dynamische Verzauberungseffekte zur Laufzeit zu berechnen oder Rüstungen und Waffen temporär zu verzaubern, ohne persistente NBT-Tags zu verändern.

---

## 🔗 Verwandte Seiten
* [[Stochastik- & Mathe-Werkzeuge|de_de-Stochastic-and-Math-Utilities]]
* [[Architektur & Paketstruktur|de_de-Architecture-and-Package-Layout]]
