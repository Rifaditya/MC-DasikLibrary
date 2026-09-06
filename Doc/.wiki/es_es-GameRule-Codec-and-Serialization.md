# Codec y Serialización de GameRules

| Tema | Especificación |
| :--- | :--- |
| **Problema de Serialización** | Cierre inesperado en `SavedDataStorage.encodeUnchecked` al guardar |
| **Versión de Corrección** | `1.8.15` |
| **Límites de Enteros** | `min = Integer.MIN_VALUE` (o `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ Corrección del Codec de GameRule para Enteros

En Minecraft 26.2, las GameRules dinámicas se serializan en `level.dat` / `game_rules.dat` a través de `SavedDataStorage.encodeUnchecked`. En versiones anteriores, registrar reglas sin un límite mínimo explícito hacía que el codec impusiera `min = 0`, lanzando un error `IllegalStateException` al guardar valores negativos.

### Solución en `DynamicGameRuleManager`

```java
// IntegerBuilder initialization fix in DynamicGameRuleManager
public class IntegerBuilder {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public GameRule<Integer> register() {
        int effectiveMin = Math.min(min, defaultValue);
        int effectiveMax = Math.max(max, defaultValue);
        // Uses Codec.INT.intRange(effectiveMin, effectiveMax) to prevent encodeUnchecked bounds check failure
        GameRule<Integer> rule = new GameRule<>(
            GameRuleType.INT, defaultValue, Codec.INT.intRange(effectiveMin, effectiveMax), 
            FeatureFlagSet.of(), category, visitor
        );
        return Registry.register(BuiltInRegistries.GAME_RULE, Identifier.parse(ruleName), rule);
    }
}
```

---

## 🔒 Seguridad de Nombres de Espacio NBT

Utilice siempre el formato `modid:rule_name` (`[ERR-20260510-002]`). Las reglas sin espacio de nombres no se serializan correctamente en los datos de nivel, lo que causa pérdida silenciosa de configuración tras reiniciar el servidor.

---

## 🔗 Páginas Relacionadas
* [[Gestor de GameRules Dinámicas|es_es-Dynamic-GameRules-Manager]]
* [[GameRules del Cliente y Ayudantes de GUI|es_es-Client-GameRule-and-GUI-Helpers]]
