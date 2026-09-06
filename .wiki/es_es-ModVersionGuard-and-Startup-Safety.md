# ModVersionGuard y Seguridad de Inicio

| Clase | `net.dasik.social.util.ModVersionGuard` |
| :--- | :--- |
| **Política de ClassLoader** | Invocación explícita de `Thread.currentThread().getContextClassLoader()` |
| **Patrón Prohibido** | Uso de `Class.forName(name)` por defecto sin ClassLoader |
| **Punto de Entrada** | `DasikLibraryMod.onInitialize()` |

---

## 🛡️ Regla de Resolución de Knot ClassLoader

En Fabric, invocar `Class.forName(String className)` por defecto recurre al ClassLoader del sistema, el cual falla bajo Knot ClassLoader durante las etapas tempranas de carga del mod arrojando un error falso `ClassNotFoundException`.

`ModVersionGuard` pasa explícitamente `Thread.currentThread().getContextClassLoader()`:

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

## 🔗 Páginas Relacionadas
* [[Compatibilidad de Versiones|es_es-Version-Compatibility]]
* [[Guía de MC 26.2|es_es-Minecraft-26.2-Guide]]
