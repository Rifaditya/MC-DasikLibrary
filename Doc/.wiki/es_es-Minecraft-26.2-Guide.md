# Guía de Minecraft 26.2+

| Parámetro | Especificación |
| :--- | :--- |
| **Versión Objetivo de Minecraft** | `26.2` (y compatible con `26.x`) |
| **Restricción de Fabric Loader** | `>=0.18.4` |
| **Entorno de Java** | JDK 25 |
| **Versión de la Biblioteca** | `1.8.15` |
| **ID del Mod** | `dasik-library` |
| **Nombre del Mod** | Dasik Library |
| **Licencia** | LGPL-3.0 |

> 📌 **Aviso sobre el Código Fuente del Repositorio**: La documentación de esta Wiki refleja el **estado actual del código fuente en el repositorio**, que puede incluir confirmaciones recientes no publicadas o características en desarrollo antes de las compilaciones públicas en CurseForge y Modrinth.

---

## 🛠️ Descripción General e Instalación

**Dasik Library** es una dependencia obligatoria en tiempo de ejecución para los mods de las colecciones *Vanilla Outsider*, *Instant Gratification* y *Delayed Gratification*. Ofrece programación centralizada de ticks para IA social, genética animal, matemáticas vectoriales Boids y registro dinámico de GameRules.

### 📥 Instalación para Jugadores
1. Instale **Fabric Loader** (`0.18.4` o superior) para Minecraft `26.2`.
2. Asegúrese de que **Fabric API** (`0.152.1+26.2` o superior) esté instalado en su carpeta `.minecraft/mods`.
3. Descargue `dasik-library-1.8.15.jar` y colóquelo en su directorio `.minecraft/mods` junto con los mods dependientes (por ejemplo, *Better Dogs*, *Natural Reproduction*).

### 💻 Dependencia para Desarrolladores de Mods

Agregue **Dasik Library** a su archivo `fabric.mod.json`:

```json
{
  "schemaVersion": 1,
  "id": "my_consumer_mod",
  "version": "1.0.0+26.2",
  "name": "My Consumer Mod",
  "depends": {
    "fabricloader": ">=0.18.4",
    "minecraft": ">=26.1.2-",
    "dasik-library": "*"
  }
}
```

En `gradle.properties`:

```properties
dasik_library_version=1.8.15
```

En `build.gradle`:

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_library_version}"
}
```

---

## ⚙️ Cambios Arquitectónicos Principales en 26.2+

1. **Mojang Sovereign Mappings**: Utiliza mapeos de Mojang (`level`, `ServerLevel`, `EntityTypes`). Los métodos heredados de Yarn (`world`, `getWorld`) están totalmente obsoletos.
2. **Identifier API**: Utiliza `Identifier.fromNamespaceAndPath(namespace, path)` o `Identifier.parse(string)`. La sintaxis heredada `Identifier.of()` no es compatible.
3. **Límites Abiertos de Versión (`>=26.1.2-`)**: Garantiza que un solo JAR compilado mantenga compatibilidad hacia adelante con las actualizaciones de Minecraft `26.2+`, empleando `ModVersionGuard` para la seguridad en tiempo de ejecución.

---

## 🔗 Páginas Relacionadas
* [[Compatibilidad de Versiones|es_es-Version-Compatibility]]
* [[ModVersionGuard y Seguridad de Inicio|es_es-ModVersionGuard-and-Startup-Safety]]
* [[Configuración de Desarrollador y Compilación|es_es-Developer-Setup-and-Building]]
