# 过期属性清理与体型缩放

| 修复项 | 问题描述 | 修复版本 |
| :--- | :--- | :--- |
| **巨型实体 Bug** | 过期属性修饰符累加堆叠 | `1.8.11` |
| **缩放基准偏移** | `ADD_VALUE` 属性修饰符基准偏移数学计算 | `1.8.10` |

---

## 📐 缩放属性偏移数学

在 Minecraft 26.2 中，实体体型缩放依赖原生的 `minecraft:scale`。当为缩放应用 `ADD_VALUE` 属性修饰符时，修饰符数值 $M_{\text{scale}}$ 必须从目标缩放 $S$ 中减去 $1.0\text{f}$：

$$M_{\text{scale}} = S - 1.0\text{f}$$

$$\text{Final Scale} = \text{Base Scale } (1.0) + M_{\text{scale}} = 1.0 + (S - 1.0) = S$$

如果未减去 $1.0\text{f}$，将目标缩放设置为 $2.0$ 将会在基准 $1.0$ 上额外加上 $+2.0$，导致非预期的 $3.0\text{x}$（300%）巨型实体渲染。

---

## 🧹 `GeneticsEngine` 中的过期修饰符清理

为防止世界重新加载或繁殖更新时重复的属性修饰符叠加，`GeneticsEngine` 在应用新修饰符前会清理旧的 `genetics_` 修饰符：

```java
public static void applyGeneticsModifiers(LivingEntity entity) {
    AttributeInstance scaleInstance = entity.getAttribute(Attributes.SCALE);
    if (scaleInstance != null) {
        // Purge legacy/duplicate modifiers by identifier
        scaleInstance.getModifiers().stream()
            .filter(mod -> mod.id().getPath().startsWith("genetics_"))
            .toList()
            .forEach(scaleInstance::removeModifier);
    }
}
```

---

## 🔗 相关页面
* [[动物遗传学引擎|zh_cn-Animal-Genetics-Engine]]
* [[遗传学 API 与谱系|zh_cn-Genetics-API-and-Pedigree]]
