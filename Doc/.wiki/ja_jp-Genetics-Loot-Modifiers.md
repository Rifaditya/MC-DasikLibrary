# 遺伝学ドロップ修飾子

| コンポーネント | クラス |
| :--- | :--- |
| **修飾子インターフェース** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **レジストリクラス** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Mixin フッククラス** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **対象メソッド** | `LivingEntity.dropFromLootTable` |

---

## 🥩 概要とドロップアイテムの介入

`GeneticsLootRegistry` を利用することで、Mod はモブ死亡時のドロップ品を遺伝的特徴に基づいて動的に変更できます（巨大な個体は多くの肉を落とし、未熟個体はドロップ量が減少し、突然変異体はレア素材をドロップします）。

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

## 💻 開発者向けコード例

オオカミの体格スケールに応じて骨のドロップ数を変化させる修飾子の登録：

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

## 🔗 関連ページ
* [[動物遺伝学エンジン|ja_jp-Animal-Genetics-Engine]]
* [[Mixin リファレンスとフック|ja_jp-Mixin-Reference-and-Hooks]]
