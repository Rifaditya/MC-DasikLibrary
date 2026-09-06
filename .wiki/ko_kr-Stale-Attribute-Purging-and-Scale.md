# 만료된 속성 정리 및 크기 스케일

| 문제 항목 | 해결 지침 |
| :--- | :--- |
| **잔여 속성 수정자** | 재계산 시 `genetics_` 접두사를 가진 모든 수정자 제거 |
| **크기 추가 오프셋** | `ADD_VALUE` 연산 시 `-1.0f` 베이스 오프셋 보정 |
| **대상 크기 속성** | `EntityAttributes.SCALE` |

---

## 🧹 만료된 수정자 자동 정리

동물의 성장, 돌연변이, 치료 등으로 능력치를 재계산할 때 이전에 부여된 수정자를 제거하지 않으면 수치가 중복으로 누적되는 오류가 발생합니다:

```java
public static void purgeStaleModifiers(LivingEntity entity, Holder<Attribute> attribute) {
    AttributeInstance instance = entity.getAttribute(attribute);
    if (instance != null) {
        instance.getModifiers().stream()
            .filter(m -> m.id().getPath().startsWith("genetics_"))
            .toList()
            .forEach(instance::removeModifier);
    }
}
```

---

## 📏 `ADD_VALUE` 연산의 베이스 오프셋

Minecraft에서 속성 `SCALE`의 기본값은 $1.0$입니다. 따라서 `ADD_VALUE` 연산을 적용할 때 기준값 $1.0$을 보정해 주어야 합니다:

$$\text{Modifier Amount} = \text{TargetScale} - 1.0f$$

예를 들어 목표 크기가 $1.5x$라면 부여해야 하는 수정자 수치는 $+0.5f$가 됩니다.

---

## 🔗 관련 페이지
* [[동물 유전학 엔진|ko_kr-Animal-Genetics-Engine]]
* [[유전학 API 및 가계도|ko_kr-Genetics-API-and-Pedigree]]
