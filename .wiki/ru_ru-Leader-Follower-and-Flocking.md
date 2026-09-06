# Следование за лидером и стайное поведение

| Компонент | Спецификация |
| :--- | :--- |
| **Интерфейс участника** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **Типы стай** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Стратегии Boids** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ Обзор и математика руления Boids

Dasik Library реализует обобщенную модель **стайного поведения Boids** (модель Крейга Рейнольдса), оптимизированную для поиска пути в Minecraft. Сущности с интерфейсом `GroupMember` согласованно координируют движение, следуют за лидером и удерживают дистанцию.

### Формула суммарного вектора силы руления
Суммарная сила руления $\vec{F}_{\text{steer}}$ рассчитывается следующим образом:

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

Где:
* $\vec{V}_{\text{separation}}$ отталкивает сущностей друг от друга для избежания столкновений.
* $\vec{V}_{\text{cohesion}}$ притягивает сущность к центру масс группы.
* $\vec{V}_{\text{alignment}}$ выравнивает направление движения стаи.
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ — настраиваемые весовые коэффициенты в `GroupParameters`.

---

## ⚙️ Спецификация `GroupParameters`

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

## 💻 Пример кода для разработчиков

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

## 🔗 Связанные страницы
* [[Социальная система коллективного разума|ru_ru-Hive-Mind-Social-System]]
* [[Архитектура и структура пакетов|ru_ru-Architecture-and-Package-Layout]]
