# Purga de Atributos Obsoletos y Escala

| Corrección | Descripción del Problema | Versión |
| :--- | :--- | :--- |
| **Error de Gigantes** | Acumulación de modificadores obsoletos | `1.8.11` |
| **Desplazamiento Base** | Desplazamiento base de modificador `ADD_VALUE` | `1.8.10` |

---

## 📐 Matemáticas del Desplazamiento del Atributo de Escala

En Minecraft 26.2, la escala de las entidades utiliza el atributo nativo `minecraft:scale`. Al aplicar un modificador `ADD_VALUE` para cambiar el tamaño, el valor $M_{\text{scale}}$ DEBE restar $1.0\text{f}$ a la escala objetivo $S$:

$$M_{\text{scale}} = S - 1.0\text{f}$$

$$\text{Final Scale} = \text{Base Scale } (1.0) + M_{\text{scale}} = 1.0 + (S - 1.0) = S$$

Si no se resta $1.0\text{f}$, establecer una escala de $2.0$ sumará $+2.0$ sobre la base $1.0$, resultando en una criatura erróneamente gigante de $3.0\text{x}$ (300%).

---

## 🧹 Purga de Modificadores en `GeneticsEngine`

Para evitar la duplicación de modificadores tras recargar mundos, `GeneticsEngine` elimina los modificadores previos con prefijo `genetics_` antes de asignar nuevos:

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

## 🔗 Páginas Relacionadas
* [[Motor de Genética Animal|es_es-Animal-Genetics-Engine]]
* [[API de Genética y Pedigrí|es_es-Genetics-API-and-Pedigree]]
