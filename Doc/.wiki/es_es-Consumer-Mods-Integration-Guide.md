# Guía de Integración para Mods Consumidores

| Mod Consumidor | Aspectos Destacados de la Integración |
| :--- | :--- |
| **Better Dogs** | Dinámica de manada, órdenes con cuerno de guerra, escala genética |
| **Natural Reproduction** | Genealogía, riesgo de endogamia, estadísticas de escala física |
| **Collapsible Game Rule Screen** | Encabezados en negrita (`§l`), registro dinámico de GameRules |
| **Bat Ecology** | Estrategias de vuelo en espiral (`FlockType.AERIAL`) |
| **Ore Amplifier** | Generación estocástica de minerales (`StochasticUtil`) |

---

## 🛠️ Lista de Verificación Paso a Paso

1. **Declarar Dependencia**: Añada `"dasik-library": "*"` en `fabric.mod.json`.
2. **Implementar Interfaces**: Implemente `SocialEntity` o `GroupMember` en sus criaturas.
3. **Registrar GameRules**: Invoque `DynamicGameRuleManager.registerBoolean` / `registerInt` al inicializar.
4. **Usar Fachada de Genética**: Consulte y modifique estadísticas a través de `DasikAnimalGeneticsAPI`.

---

## 🔗 Páginas Relacionadas
* [[Configuración de Desarrollador y Compilación|es_es-Developer-Setup-and-Building]]
* [[Arquitectura y Distribución de Paquetes|es_es-Architecture-and-Package-Layout]]
