# Référence Mixin & Points d'Injection

| Classe Mixin | Classe Cible | Méthode Cible | Rôle |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `getOrDefault` | Injection automatique des libellés de GameRules |
| `LivingEntityLootMixin` | `LivingEntity` | `dropFromLootTable` | Interception et modulation des butins génétiques |
| `MobGoalAccessor` | `Mob` | Accesseur de GoalSelector | Accès aux sélecteurs d'objectifs internes d'IA |
| `PathfinderMobMixin` | `PathfinderMob` | `tick` | Déclenchement de l'impulsion sociale |
| `ProfileTriggerMixin` | `LivingEntity` | Détection d'événements | Transition de profils de comportement |

---

## 🔗 Pages Liées
* [[Architecture et Structure des Paquets|fr_fr-Architecture-and-Package-Layout]]
* [[Modificateurs de Butin Génétique|fr_fr-Genetics-Loot-Modifiers]]
