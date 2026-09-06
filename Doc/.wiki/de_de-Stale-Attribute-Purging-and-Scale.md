# Veraltete Attributsbereinigung & Skalierung

| Fehlerbehebung | Problemstellung | Release |
| :--- | :--- | :--- |
| **Riesen-Bug** | Anhäufung veralteter Attributsmodifikatoren | `1.8.11` |
| **Basis-Offset** | Basis-Offset-Mathematik für `ADD_VALUE`-Modifikatoren | `1.8.10` |

---

## 📐 Skalierungs-Offset-Mathematik

In Minecraft 26.2 nutzt die Größenskalierung das Vanilla-Attribut `minecraft:scale`. Beim Anwenden eines `ADD_VALUE`-Attributsmodifikators für die Skalierung MUSS der Modifikatorwert $M_{\text{scale}}$ den Betrag $1.0\text{f}$ von der Zielgröße $S$ abziehen:

$$M_{\text{scale}} = S - 1.0\text{f}$$

$$\text{Final Scale} = \text{Base Scale } (1.0) + M_{\text{scale}} = 1.0 + (S - 1.0) = S$$

Wird $1.0\text{f}$ nicht subtrahiert, führt eine Zielskalierung von $2.0$ zu einem Zuschlag von $+2.0$ auf die Basis $1.0$, was in einer fehlerhaft gerenderten Riesenkreatur mit $3.0\text{x}$ (300%) resultiert.

---

## 🧹 Modifikatorbereinigung in `GeneticsEngine`

Um das fehlerhafte Stapeln von Modifikatoren nach Welt-Neuladevorgängen zu verhindern, bereinigt `GeneticsEngine` ältere Modifikatoren mit dem Präfix `genetics_` vor dem Zuweisen neuer Werte:

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

## 🔗 Verwandte Seiten
* [[Tiergenetik-Engine|de_de-Animal-Genetics-Engine]]
* [[Genetik-API & Stammbaum|de_de-Genetics-API-and-Pedigree]]
