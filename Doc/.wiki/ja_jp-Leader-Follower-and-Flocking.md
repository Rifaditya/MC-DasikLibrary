# リーダー追従＆群れ形成アルゴリズム

| コンポーネント | 仕様 |
| :--- | :--- |
| **グループメンバー IF** | `net.dasik.social.api.group.GroupMember` |
| **AI Goal クラス** | `net.dasik.social.ai.goal.FollowLeaderGoal` |
| **群れタイプ** | `FlockType.TERRESTRIAL`, `FlockType.AERIAL` |
| **Boids 戦略** | `TerrestrialFlockingStrategy`, `AerialFlockingStrategy` |

---

## 🕊️ 概要と操舵ベクトルの計算

Dasik Library には、Craig Reynolds 氏提唱の **Boids 群れ制御アルゴリズム** を Minecraft の空間制約向けに高度に最適化した実装が組み込まれています。`GroupMember` を実装したエンティティは、群れ全体の整列、リーダー追従、衝突回避を協調して行います。

### 合成操舵ベクトルの計算式
エンティティに加えられる操舵ベクトル $\vec{F}_{\text{steer}}$ は次式で算出されます：

$$\vec{F}_{\text{steer}} = (w_{\text{sep}} \cdot \vec{V}_{\text{separation}}) + (w_{\text{coh}} \cdot \vec{V}_{\text{cohesion}}) + (w_{\text{ali}} \cdot \vec{V}_{\text{alignment}})$$

ここで：
* $\vec{V}_{\text{separation}}$ は個体間の衝突を防ぐ反発ベクトル。
* $\vec{V}_{\text{cohesion}}$ は群れの中心へ引き寄せる結合ベクトル。
* $\vec{V}_{\text{alignment}}$ は周囲の個体の進行方向に合わせる整列ベクトル。
* $w_{\text{sep}}, w_{\text{coh}}, w_{\text{ali}}$ は `GroupParameters` で定義された重み係数。

---

## ⚙️ `GroupParameters` の定義

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

## 💻 開発者向けコード例

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

## 🔗 関連ページ
* [[群知能ソーシャルシステム|ja_jp-Hive-Mind-Social-System]]
* [[アーキテクチャとパッケージ構成|ja_jp-Architecture-and-Package-Layout]]
