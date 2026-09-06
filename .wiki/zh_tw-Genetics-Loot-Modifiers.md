# 遺傳學戰利品修飾符

| 組件 | 類別 |
| :--- | :--- |
| **修飾符介面** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **註冊表類別** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Mixin 鉤子類別** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **目標方法** | `LivingEntity.dropFromLootTable` |

---

## 🥩 概述與掉落物攔截

`GeneticsLootRegistry` 允許模組根據生物遺傳特徵動態修改戰利品掉落物（例如，巨型動物掉落額外肉類、劣株掉落較少物品、突變變種掉落特殊材料）。

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

## 💻 開發者代碼範例

根據狼的體型縮放註冊掉落物修飾符：

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

## 🔗 相關頁面
* [[動物遺傳學引擎|zh_tw-Animal-Genetics-Engine]]
* [[Mixin 參考與注入鉤子|zh_tw-Mixin-Reference-and-Hooks]]
