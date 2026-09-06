# Purga de Atributos Obsoletos e Escala

| Correção | Causa do Erro | Versão |
| :--- | :--- | :--- |
| **Bug dos Gigantes** | Acúmulo de modificadores de atributo obsoletos | `1.8.11` |
| **Deslocamento Base**| Matemática de compensação de base `ADD_VALUE` | `1.8.10` |

---

## 📐 Matemática de Deslocamento do Atributo de Escala

No Minecraft 26.2, a alteração de tamanho utiliza o atributo nativo `minecraft:scale`. Ao aplicar um modificador `ADD_VALUE` para redimensionamento, o valor do modificador $M_{\text{scale}}$ DEVE subtrair $1.0\text{f}$ da escala desejada $S$:

$$M_{\text{scale}} = S - 1.0\text{f}$$

$$\text{Final Scale} = \text{Base Scale } (1.0) + M_{\text{scale}} = 1.0 + (S - 1.0) = S$$

Se $1.0\text{f}$ não for subtraído, definir o tamanho para $2.0$ aplicará $+2.0$ sobre a base $1.0$, gerando acidentalmente um gigante com escala $3.0\text{x}$ (300%).

---

## 🧹 Purga de Modificadores Obsoletos no `GeneticsEngine`

Para evitar empilhamento incorreto ao reiniciar o mundo, o `GeneticsEngine` purga modificadores antigos iniciados com `genetics_` antes de registrar novos:

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
* [[Motor de Genética Animal|pt_br-Animal-Genetics-Engine]]
* [[API de Genética e Pedigree|pt_br-Genetics-API-and-Pedigree]]
