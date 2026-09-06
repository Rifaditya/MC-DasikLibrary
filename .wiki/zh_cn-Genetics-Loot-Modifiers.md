# 遗传学战利品修饰符

| 组件 | 类 |
| :--- | :--- |
| **修饰符接口** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **注册表类** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Mixin 钩子类** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **目标方法** | `LivingEntity.dropFromLootTable` |

---

## 🥩 概述与掉落物拦截

`GeneticsLootRegistry` 允许模组根据生物遗传特征动态修改战利品掉落物（例如，巨型动物掉落额外肉类、劣株掉落较少物品、突变变种掉落特殊材料）。

```ascii
[ LivingEntity.dropFromLootTable ]
                │
                ▼
 ┌─────────────────────────────┐
 │    LivingEntityLootMixin    │  ◄── @ModifyVariable Consumer Hook
 └──────────────┬──────────────┘
                │
                ▼
 ┌─────────────────────────────┐
 │    GeneticsLootRegistry     │  ◄── Lookup Modifier for EntityType
 └──────────────┬──────────────┘
                │
                ▼
 ┌─────────────────────────────┐
 │    GeneticsLootModifier     │  ◄── Scale stack count / Swap item
 └─────────────────────────────┘
```

---

## 💻 开发者代码示例

根据狼的体型缩放注册掉落物修饰符：

```java
GeneticsLootRegistry.register(EntityTypes.WOLF, (entity, genetics, stack, random) -> {
    float scale = DasikAnimalGeneticsAPI.getScale(entity);
    if (stack.is(Items.BONE)) {
        // Scale bone drop count proportionally to entity scale
        int newCount = Math.max(1, Math.round(stack.getCount() * scale));
        return new ItemStack(Items.BONE, newCount);
    }
    return stack;
});
```

---

## 🔗 相关页面
* [[动物遗传学引擎|zh_cn-Animal-Genetics-Engine]]
* [[Mixin 参考与注入钩子|zh_cn-Mixin-Reference-and-Hooks]]
