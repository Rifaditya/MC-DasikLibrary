# Suivi de Leader et Comportement de Nuée

| Composant | Spécification |
| :--- | :--- |
| **Interface Membre** | `net.dasik.social.api.group.GroupMember` |
| **Objectif IA (Goal)** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **Types de Nuée** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Stratégies Boids** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ Présentation & Algorithme de Guidage

Dasik Library intègre une implémentation optimisée du modèle de **nuée Boids** (conçu par Craig Reynolds), adaptée aux contraintes de calcul spatial de Minecraft. Les entités implémentant `GroupMember` coordonnent leurs déplacements collectifs, le suivi d'un chef de meute et l'évitement mutuel.

### Formule de la Force de Direction Résultante
La force directrice totale $\vec{F}_{\text{steer}}$ appliquée à l'entité est donnée par :

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

Où :
* $\vec{V}_{\text{separation}}$ repousse les entités proches pour prévenir les collisions.
* $\vec{V}_{\text{cohesion}}$ attire l'entité vers le centre de masse du groupe.
* $\vec{V}_{\text{alignment}}$ aligne le vecteur de déplacement sur celui des voisins.
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ représentent les coefficients définis dans `GroupParameters`.

---

## ⚙️ Spécification de `GroupParameters`

```java
public record GroupParameters(
    float cohesionRadius, 
    float separationRadius, 
    float maxSpeed, 
    boolean canTeleport, 
    float teleportDistance,
    float startDistance,
    float stopDistance,
    float alignmentWeight,
    float cohesionWeight,
    float separationWeight
) {
    public static final GroupParameters DEFAULT_AERIAL = new GroupParameters(
        3.0f, 1.0f, 0.4f, true, 144.0f, 6.0f, 2.0f, 0.05f, 0.05f, 0.1f
    );
    public static final GroupParameters DEFAULT_TERRESTRIAL = new GroupParameters(
        5.0f, 1.5f, 1.2f, true, 144.0f, 6.0f, 2.0f, 0.0f, 0.0f, 0.0f
    );
}
```

---

## 💻 Exemple de Code Développeur

```java
public class CustomBirdEntity extends PathfinderMob implements GroupMember {
    private LivingEntity leader;

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new FollowLeaderGoal<>(this, GroupParameters.DEFAULT_AERIAL, 32.0D));
    }

    @Override public LivingEntity getLeader() { return this.leader; }
    @Override public void setLeader(LivingEntity leader) { this.leader = leader; }
    @Override public FlockType getFlockType() { return FlockType.AERIAL; }
}
```

---

## 🔗 Pages Liées
* [[Système Social d'Intelligence Collective|fr_fr-Hive-Mind-Social-System]]
* [[Architecture et Structure des Paquets|fr_fr-Architecture-and-Package-Layout]]
