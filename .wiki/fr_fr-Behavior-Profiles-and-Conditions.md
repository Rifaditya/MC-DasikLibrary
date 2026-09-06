# Profils de Comportement & Conditions

| Composant | Classe |
| :--- | :--- |
| **Gestionnaire de Profils** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **Interface de Profil** | `net.dasik.social.api.profile.BehaviorProfile` |
| **Interface de Condition** | `net.dasik.social.api.profile.BehaviorCondition` |
| **Constructeur par Défaut** | `DefaultProfileBuilder` |

---

## 🎭 Machine à États des Profils

Les profils de comportement permettent de basculer dynamiquement l'ensemble des objectifs d'IA d'une créature en fonction de son environnement (mode garde, mode chasse, mode panique) sans réinitialiser manuellement les listes de goals.

```ascii
[ Idle / Wandering ]
         │
         ├── Trigger: Player Attacked ──► [ Sentinel Guard Mode ]
         │
         └── Trigger: Low Health ───────► [ Panic / Flee Mode ]
```

---

## 🔗 Pages Liées
* [[Planificateur Social et Événements|fr_fr-Social-Scheduler-and-Events]]
* [[Suivi de Leader et Comportement de Nuée|fr_fr-Leader-Follower-and-Flocking]]
