# 头领跟随与集群系统

| 组件 | 规格说明 |
| :--- | :--- |
| **成员接口** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **集群类型** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Boids 策略** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ 概述与转向数学

Dasik Library 实现了针对 Minecraft 寻路高度优化的通用 **Boids 集群数学**（Craig Reynolds 模型）。实现 `GroupMember` 的实体将自动协调群体运动、头领跟随以及空间分离。

### 转向力向量公式
组合转向力向量 $\vec{F}_{\text{steer}}$ 计算如下：

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

其中：
* $\vec{V}_{\text{separation}}$ 推开实体以防过度拥挤。
* $\vec{V}_{\text{cohesion}}$ 拉动实体朝向群体质心。
* $\vec{V}_{\text{alignment}}$ 使实体前进方向保持一致。
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ 为 `GroupParameters` 中的可配置权重。

---

## ⚙️ `GroupParameters` 规格

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

## 💻 开发者代码示例

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

## 🔗 相关页面
* [[蜂巢思维社交系统|zh_cn-Hive-Mind-Social-System]]
* [[架构与包结构设计|zh_cn-Architecture-and-Package-Layout]]
