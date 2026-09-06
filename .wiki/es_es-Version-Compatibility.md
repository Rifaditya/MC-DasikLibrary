# Matriz de Compatibilidad de Versiones

| Objetivo de Minecraft | Versión de Biblioteca | Mod Version Guard | Especificación de Dependencia | Estado de Soporte |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **Línea Principal Activa** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | Retrocompatibilidad / Estable |
| **Minecraft 1.21.x** | *Obsoleto* | *N/A* | *Legacy 1.x* | **Fin de Ciclo de Vida (EOL)** |

> 📌 **Aviso sobre el Código Fuente del Repositorio**: La documentación de esta Wiki refleja el **estado actual del código fuente en el repositorio**, que puede incluir confirmaciones recientes no publicadas o características en desarrollo antes de las compilaciones públicas en CurseForge y Modrinth.

---

## 🛡️ Política 1 Jar 1 Version y Compatibilidad hacia Adelante

Dasik Library implementa la **Política 1 Jar 1 Version** combinada con **compatibilidad abierta hacia adelante**:

1. **Límites Abiertos de Versión (`"minecraft": ">=26.1.2-"`)**: La biblioteca especifica un límite inferior abierto en `fabric.mod.json`, permitiendo que Fabric Loader acepte el JAR en parches menores sin bloquear a los jugadores.
2. **Seguridad con Knot ClassLoader (`ModVersionGuard`)**: Durante `onInitialize()`, el método `ModVersionGuard.checkClass` valida la existencia de clases mediante `Thread.currentThread().getContextClassLoader()` para detectar discrepancias de API y emitir mensajes claros en lugar de fallos silenciosos de la JVM.

```java
public final class ModVersionGuard {
    public static void checkClass(String modName, String requiredClassName) {
        try {
            Class.forName(requiredClassName, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("\n" +
                "=====================================================================\n" +
                " [" + modName + "] Minecraft API Mismatch!\n" +
                " A required Minecraft class or API was not found in your game version.\n" +
                " Try updating your Minecraft version or download a matching build.\n" +
                "=====================================================================");
        }
    }
}
```

---

## 🚫 Identidad de Versión (Prohibición de Mezcla con Versiones Antiguas)

Bajo estrictas pautas de diseño (`[DIR-20260614-001]`), **las versiones anuales de Minecraft 26.x NUNCA deben confundirse con las versiones antiguas 1.21.x de 2024**:
* ❌ `26.2 (1.21.4)` — Estrictamente Prohibido.
* ✅ `Minecraft 26.2` — Notación oficial de versión anual sovereign.

---

## 🔗 Páginas Relacionadas
* [[Guía de MC 26.2|es_es-Minecraft-26.2-Guide]]
* [[ModVersionGuard y Seguridad de Inicio|es_es-ModVersionGuard-and-Startup-Safety]]
