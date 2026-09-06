# Encantamientos Dinámicos y Rastreador de Visión

| Componente | Clase |
| :--- | :--- |
| **Gestor de Encantamientos** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **Rastreador de Visión** | `net.dasik.social.api.vision.PlayerVisionTracker` |
| **Método de Consulta de Visión** | `PlayerVisionTracker.canSee(ServerPlayer player, Entity target)` |
| **Registro de Oyente** | `PlayerVisionTracker.registerListener(String modId, double radius)` |

---

## 👁️ Detección de Línea de Visión con `PlayerVisionTracker`

`PlayerVisionTracker` realiza barridos espaciales eficientes para determinar si una entidad se encuentra dentro del campo visual de un jugador sin generar lag en el servidor.

```java
// Register listener for 16-block vision sweeps
PlayerVisionTracker.registerListener("mymod", 16.0D);

// Check if player can see target entity
boolean isVisible = PlayerVisionTracker.canSee(serverPlayer, targetEntity);
```

---

## ✨ Encantamientos Temporales con `DynamicEnchantmentManager`

Permite a los mods evaluar efectos dinámicos o inyectar niveles de encantamiento temporales en el equipo de las entidades sin alterar las etiquetas NBT persistentes.

---

## 🔗 Páginas Relacionadas
* [[Utilidades Estocásticas y Matemáticas|es_es-Stochastic-and-Math-Utilities]]
* [[Arquitectura y Distribución de Paquetes|es_es-Architecture-and-Package-Layout]]
