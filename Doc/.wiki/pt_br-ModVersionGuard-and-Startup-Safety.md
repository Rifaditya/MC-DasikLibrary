# ModVersionGuard e Segurança de Inicialização

| Classe | `net.dasik.social.util.ModVersionGuard` |
| :--- | :--- |
| **Regra de ClassLoader** | Invocação explícita de `Thread.currentThread().getContextClassLoader()` |
| **Prática Proibida** | Uso de `Class.forName(name)` padrão sem especificar o ClassLoader |
| **Ponto de Chamada** | `DasikLibraryMod.onInitialize()` |

---

## 🛡️ Resolução de Classes no Knot ClassLoader

No Fabric, a chamada tradicional `Class.forName(String className)` consulta o carregador de classes do sistema, o qual falha sob o Knot ClassLoader nas fases iniciais de inicialização do mod, levantando falso `ClassNotFoundException`.

O `ModVersionGuard` repassa explicitamente `Thread.currentThread().getContextClassLoader()`:

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
* [[Compatibilidade de Versões|pt_br-Version-Compatibility]]
* [[Guia MC 26.2|pt_br-Minecraft-26.2-Guide]]
