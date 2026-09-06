# Gestor de GameRules Dinámicas

| Componente | Clase |
| :--- | :--- |
| **Clase del Gestor** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **Formato de Clave con Espacio de Nombres** | `modid:rule_name` |
| **Inyección de Traducción** | `LanguageMixin` -> Mapa `GENERATED_TRANSLATIONS` |
| **Formato de Título de Categoría** | `§lCategory Title (N rules)` |

---

## 🎲 Descripción General y GameRules con Espacios de Nombres

`DynamicGameRuleManager` permite a los mods registrar un número ilimitado de GameRules programáticamente sin necesidad de agregar entradas manuales en los archivos `en_us.json`.

### Características Principales
1. **Inyección Automática de Traducciones**: Convierte automáticamente `modid:rule_name` en un título legible (por ejemplo, `bd_enable_guard_mode` -> `BD Enable Guard Mode`) y lo inyecta en la tabla de idiomas del cliente vía `LanguageMixin`.
2. **Encabezados de Categoría en Negrita (`§l`)**: Las categorías registradas con `registerCategory` reciben formato en negrita para una presentación limpia en pantalla.
3. **Convertidores Matemáticos de Ayuda**:
   - `getPct(Level level, GameRule<Integer> rule)` — Convierte un valor entero (`0-100`) a double (`/ 100.0`).
   - `getChance(Level level, GameRule<Integer> rule)` — Convierte un porcentaje a float (`/ 100.0f`).
   - `getProb(Level level, GameRule<Integer> rule)` — Convierte un valor por mil (`0-1000`) a float (`/ 1000.0f`).
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — Convierte un valor decil a float (`/ 10.0f`).
   - `getIntVal(Level level, String key, int defaultValue)` — Consulta el valor entero mediante clave textual.

---

## 💻 Ejemplo de Código para Desarrolladores

```java
// Register a dynamic boolean GameRule with description
GameRule<Boolean> ENABLE_GUARD = DynamicGameRuleManager.booleanRule(
    "betterdogs:bd_enable_guard_mode",
    GameRuleCategory.MOBS,
    true
).description("Enable wolf sentinel guard mode").register();

// Querying GameRule safely across client/server
boolean isGuardEnabled = DynamicGameRuleManager.getBoolean(level, ENABLE_GUARD);
```

---

## 🔗 Páginas Relacionadas
* [[Codec y Serialización de GameRules|es_es-GameRule-Codec-and-Serialization]]
* [[GameRules del Cliente y Ayudantes de GUI|es_es-Client-GameRule-and-GUI-Helpers]]
