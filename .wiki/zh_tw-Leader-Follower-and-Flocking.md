# 頭領跟隨與集群系統

| 組件 | 規格說明 |
| :--- | :--- |
| **成員介面** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **群聚類型** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Boids 策略** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ 概述與轉向數學

Dasik Library 實現了針對 Minecraft 尋路高度優化的通用 **Boids 群聚數學**（Craig Reynolds 模型）。實現 `GroupMember` 的實體將自動協調群體運動、頭領跟隨以及空間分離。

### 轉向力向量公式
組合轉向力向量 $\vec{F}_{\text{steer}}$ 計算如下：

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

其中：
* $\vec{V}_{\text{separation}}$ 推開實體以防過度擁擠。
* $\vec{V}_{\text{cohesion}}$ 拉動實體朝向群體質心。
* $\vec{V}_{\text{alignment}}$ 使實體前進方向保持一致。
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ 為 `GroupParameters` 中的可配置權重。

---

## ⚙️ `GroupParameters` 規格

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

## 💻 開發者代碼範例

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

## 🔗 相關頁面
* [[蜂巢思維社交系統|zh_tw-Hive-Mind-Social-System]]
* [[架構與套件結構設計|zh_tw-Architecture-and-Package-Layout]]
