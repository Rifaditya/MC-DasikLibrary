# 动物遗传学引擎

| 属性 | 设定值 |
| :--- | :--- |
| **核心包** | `net.dasik.social.api.genetics` |
| **附加组件标识符** | `dasik-library:genetics` |
| **体型缩放范围** | `0.1x` 至 `3.0x` (默认: `0.5x` - `2.0x`) |
| **劣株阈值** | Scale $< 0.85x$ (或近亲繁殖) |
| **巨型阈值** | Scale $> 1.15x$ |
| **数据编解码器** | `EntityGenetics.CODEC` |

---

## 🧬 概述与 DNA 结构

**动物遗传学引擎** 基于 Fabric 的 `AttachmentType` 构建了与实体类型无关的持久化遗传系统。每个具备遗传特性的实体都存储一个 `EntityGenetics` 记录：

1. **`parent1Uuid` (`Optional<UUID>`)**：繁殖所产生的第一亲本 UUID。
2. **`parent2Uuid` (`Optional<UUID>`)**：繁殖所产生的第二亲本 UUID。
3. **`inbred` (`boolean`)**：标记亲本是否具有共同血统。
4. **`traitsRolled` (`boolean`)**：标记是否已完成初始随机属性抽取。
5. **`traits` (`Map<String, Float>`)**：动态特征数值（`scale`、`max_health`、`attack_damage`、`movement_speed`）。

```ascii
 ┌────────────────────────────────────────────────────────────────────────┐
 │                           EntityGenetics                               │
 ├───────────────────────────────────┬────────────────────────────────────┤
 │ parent1Uuid: Optional<UUID>       │ inbred: boolean                    │
 │ parent2Uuid: Optional<UUID>       │ traitsRolled: boolean              │
 │ traits: Map<String, Float>        │ Codec: EntityGenetics.CODEC        │
 └───────────────────────────────────┴────────────────────────────────────┘
```

---

## 📐 继承与变异数学

当两个实体繁殖时，子代的遗传数据由 `GeneticsEngine.calculateOffspringGenetics` 计算得出：

### 1. 体型继承公式
子代基准缩放比例 $S_{\text{offspring}}$ 由亲本均值加上三角高斯变异量 $\Delta_{\text{mutate}}$ 得出：

$$S_{\text{offspring}} = \operatorname{clamp}\left( \frac{S_{\text{parent1}} + S_{\text{parent2}}}{2} + \Delta_{\text{mutate}}, \, 0.1, \, 3.0 \right)$$

其中 $\Delta_{\text{mutate}}$ 从以 $\pm \text{mutationRate}$ 为边界的三角分布中采样。

### 2. 近亲繁殖惩罚公式
如果亲本具有共同血统（$F > 0$），则会施加近亲生命值/体型惩罚 $\text{Penalty}_{\text{inbreeding}}$：

$$\text{Penalty}_{\text{inbreeding}} = 1.0 - (F \times \text{penaltyFactor})$$

$$\text{Offspring Health} = \text{Base Health} \times \text{Penalty}_{\text{inbreeding}}$$

---

## 💻 开发者代码示例

```java
// Accessing genetics on an entity
EntityGenetics genetics = EntityGeneticsRegistry.getGenetics(livingEntity);
float scale = genetics.getScale();
boolean isRunt = scale < 0.75f;

// Applying genetics modifier
GeneticsEngine.applyGeneticsModifiers(livingEntity);
```

---

## 🔗 相关页面
* [[遗传学 API 与谱系|zh_cn-Genetics-API-and-Pedigree]]
* [[过期属性清理与体型缩放|zh_cn-Stale-Attribute-Purging-and-Scale]]
