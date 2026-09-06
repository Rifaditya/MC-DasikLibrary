# Seguidor de Líder y Sistema de Bandadas

| Componente | Especificación |
| :--- | :--- |
| **Interfaz de Miembro** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **Tipos de Bandada** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Estrategias Boids** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ Descripción General y Matemáticas de Dirección Boids

Dasik Library implementa un modelo generalizado de **comportamiento de bandadas Boids** (modelo de Craig Reynolds) optimizado para el motor de navegación de Minecraft. Las entidades con `GroupMember` coordinan desplazamientos grupales, seguimiento de líder y dispersión espacial.

### Fórmula del Vector de Dirección
El vector combinado de fuerza de dirección $\vec{F}_{\text{steer}}$ se calcula como:

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

Donde:
* $\vec{V}_{\text{separation}}$ dispersa a las entidades para evitar apiñamiento.
* $\vec{V}_{\text{cohesion}}$ atrae a los miembros hacia el centro de masa de la bandada.
* $\vec{V}_{\text{alignment}}$ alinea las direcciones de movimiento.
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ son ponderaciones configurables en `GroupParameters`.

---

## ⚙️ Especificación de `GroupParameters`

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

## 💻 Ejemplo de Código para Desarrolladores

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

## 🔗 Páginas Relacionadas
* [[Sistema Social de Mente Colmena|es_es-Hive-Mind-Social-System]]
* [[Arquitectura y Distribución de Paquetes|es_es-Architecture-and-Package-Layout]]
