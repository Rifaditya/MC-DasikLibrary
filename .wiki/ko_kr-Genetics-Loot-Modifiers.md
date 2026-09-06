# 유전학 전리품 수정자

| 구성 요소 | 클래스 |
| :--- | :--- |
| **수정자 인터페이스** | `net.dasik.social.api.genetics.GeneticsLootModifier` |
| **레지스트리 클래스** | `net.dasik.social.api.genetics.GeneticsLootRegistry` |
| **Mixin 주입 클래스** | `net.dasik.social.mixin.LivingEntityLootMixin` |
| **대상 메서드** | `LivingEntity.dropFromLootTable` |

---

## 🥩 개요 및 전리품 인터셉트

`GeneticsLootRegistry`를 사용하면 엔티티 사망 시 드롭되는 전리품을 유전적 특성에 따라 동적으로 변경할 수 있습니다(거대 개체는 고기를 더 많이 드롭하고, 왜소체는 전리품이 줄어들며, 희귀 돌연변이는 특수 아이템을 드롭합니다).

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

## 💻 개발자 코드 예제

늑대의 크기 스케일에 비례하여 뼈 드롭 수량을 조절하는 수정자 등록 예제:

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

## 🔗 관련 페이지
* [[동물 유전학 엔진|ko_kr-Animal-Genetics-Engine]]
* [[Mixin 참조 및 주입 훅|ko_kr-Mixin-Reference-and-Hooks]]
