# Encantamentos Dinâmicos e Rastreador de Visão

| Componente | Classe |
| :--- | :--- |
| **Gerenciador de Encantamentos** | `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **Rastreador de Visão** | `net.dasik.social.api.vision.PlayerVisionTracker` |
| **Método de Consulta de Visão** | `PlayerVisionTracker.canSee(ServerPlayer player, Entity target)` |
| **Registro de Observador** | `PlayerVisionTracker.registerListener(String modId, double radius)` |

---

## 👁️ Raycasting de Frustum com `PlayerVisionTracker`

O `PlayerVisionTracker` faz varreduras espaciais otimizadas para verificar se uma entidade está no campo visual de um jogador sem sobrecarregar o servidor.

```java
// Register listener for 16-block vision sweeps
PlayerVisionTracker.registerListener("mymod", 16.0D);

// Check if player can see target entity
boolean isVisible = PlayerVisionTracker.canSee(serverPlayer, targetEntity);
```

---

## ✨ Encantamentos Temporários com `DynamicEnchantmentManager`

Permite que mods apliquem bônus dinâmicos ou injetem níveis temporários de encantamentos nas armaduras e armas sem gravar tags NBT persistentes nos itens.

---

## 🔗 Páginas Relacionadas
* [[Utilitários Estocásticos e Matemáticos|pt_br-Stochastic-and-Math-Utilities]]
* [[Arquitetura e Estrutura de Pacotes|pt_br-Architecture-and-Package-Layout]]
