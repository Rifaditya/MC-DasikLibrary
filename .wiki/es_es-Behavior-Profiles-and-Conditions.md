# Perfiles de Comportamiento y Condiciones

| Componente | Clase |
| :--- | :--- |
| **Gestor de Perfiles** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **Interfaz de Perfil** | `net.dasik.social.api.profile.BehaviorProfile` |
| **Interfaz de Condición** | `net.dasik.social.api.profile.BehaviorCondition` |
| **Marcador de Entidad** | `net.dasik.social.api.profile.ProfileAware` |

---

## 🎭 Descripción General y Máquinas de Estado Dinámicas

`BehaviorProfileManager` permite que las criaturas cambien dinámicamente de metas de IA según el entorno (noche, salud baja, liderazgo de manada, tormentas).

```ascii
[ LivingEntity Tick ]
         │
         ▼
┌─────────────────────────────┐
│    BehaviorProfileManager   │  ◄── Evaluate Conditions
└────────┬────────────┬───────┘
         │            │
         ▼            ▼
   [ Night Profile ] [ Combat Profile ]
```

---

## 💻 Ejemplo de Código para Desarrolladores

```java
// Create a profile using DefaultProfileBuilder
BehaviorProfile netherProfile = new DefaultProfileBuilder("nether_hunter")
    .priority(10)
    .condition(BehaviorCondition.inDimension(Level.NETHER))
    .goals(configurator -> configurator.add(2, new FollowLeaderGoal<>(mob, GroupParameters.DEFAULT_TERRESTRIAL, 16.0D)))
    .build();

// Register profile on manager instance
BehaviorProfileManager manager = new BehaviorProfileManager();
manager.registerProfile(netherProfile);
```

---

## 🔗 Páginas Relacionadas
* [[Programador Social y Eventos|es_es-Social-Scheduler-and-Events]]
* [[Arquitectura y Distribución de Paquetes|es_es-Architecture-and-Package-Layout]]
