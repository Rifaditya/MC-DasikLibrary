# Perfis de Comportamento e Condições

| Componente | Classe |
| :--- | :--- |
| **Gerenciador de Perfis** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **Interface de Perfil** | `net.dasik.social.api.profile.BehaviorProfile` |
| **Interface de Condição** | `net.dasik.social.api.profile.BehaviorCondition` |
| **Marcador de Entidade** | `net.dasik.social.api.profile.ProfileAware` |

---

## 🎭 Visão Geral e Máquinas de Estado Comportamentais

O `BehaviorProfileManager` possibilita que criaturas alterem dinamicamente seus objetivos de IA de acordo com condições ambientais (noite, pouca vida, liderança de bando ou clima).

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

## 💻 Exemplo de Código para Desenvolvedores

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
* [[Agendador Social e Eventos|pt_br-Social-Scheduler-and-Events]]
* [[Arquitetura e Estrutura de Pacotes|pt_br-Architecture-and-Package-Layout]]
