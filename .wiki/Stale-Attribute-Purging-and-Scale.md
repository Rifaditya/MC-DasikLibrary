# Stale Attribute Purging & Scale Math

| Bug Fix | Issue | Release |
| :--- | :--- | :--- |
| **Giant Scale Bug** | Stale attribute modifier accumulation | `1.8.11` |
| **Base Scale Offset** | `ADD_VALUE` attribute modifier base offset math | `1.8.10` |

---

## 📐 Scale Attribute Offset Math

In Minecraft 26.2, entity scale uses vanilla `minecraft:scale`. When applying an `ADD_VALUE` attribute modifier for scale, the modifier value $M_{\text{scale}}$ MUST subtract $1.0\text{f}$ from the target scale $S$:

$$M_{\text{scale}} = S - 1.0\text{f}$$

$$\text{Final Scale} = \text{Base Scale } (1.0) + M_{\text{scale}} = 1.0 + (S - 1.0) = S$$

If $1.0\text{f}$ is not subtracted, setting target scale to $2.0$ applies $+2.0$ onto base $1.0$, rendering a $3.0\text{x}$ (300%) unintended giant entity.

---

## 🧹 Stale Modifier Purging in `GeneticsEngine`

To prevent duplicate attribute modifiers from stacking over world reloads or breeding updates, `GeneticsEngine` purges legacy `genetics_` modifiers before applying new ones:

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

## 🔗 Related Pages
* [[Animal Genetics Engine|Animal-Genetics-Engine]]
* [[Genetics API & Pedigree|Genetics-API-and-Pedigree]]
