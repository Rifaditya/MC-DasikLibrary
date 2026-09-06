# Seguidor de Líder e Revoada Boids

| Componente | Especificação |
| :--- | :--- |
| **Interface de Membro** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **Tipos de Revoada** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Estratégias Boids** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ Visão Geral e Vetores de Direcionamento Boids

A Dasik Library inclui uma implementação generalizada do **algoritmo de revoada Boids** (modelo de Craig Reynolds) altamente otimizada para navegação de entidades no Minecraft. Criaturas com `GroupMember` coordenam movimentos conjuntos, perseguição de líder e repulsão de aglomeração.

### Fórmula do Vetor de Força de Direcionamento
A força de navegação combinada $\vec{F}_{\text{steer}}$ é formulada como:

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

Onde:
* $\vec{V}_{\text{separation}}$ repele as entidades para evitar aglomerações.
* $\vec{V}_{\text{cohesion}}$ atrai as criaturas em direção ao centro do grupo.
* $\vec{V}_{\text{alignment}}$ equaliza a orientação de movimento.
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ são pesos configuráveis em `GroupParameters`.

---

## ⚙️ Especificação do `GroupParameters`

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

## 💻 Exemplo de Código para Desenvolvedores

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
* [[Sistema Social de Mente Coletiva|pt_br-Hive-Mind-Social-System]]
* [[Arquitetura e Estrutura de Pacotes|pt_br-Architecture-and-Package-Layout]]
