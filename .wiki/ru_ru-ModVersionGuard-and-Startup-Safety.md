# ModVersionGuard и безопасность при запуске

| Класс | `net.dasik.social.util.ModVersionGuard` |
| :--- | :--- |
| **Политика ClassLoader** | Явный вызов `Thread.currentThread().getContextClassLoader()` |
| **Запрещенный шаблон** | Стандартный `Class.forName(name)` без ClassLoader |
| **Точка вызова** | `DasikLibraryMod.onInitialize()` |

---

## 🛡️ Закон разрешения классов Knot ClassLoader

В среде Fabric стандартный вызов `Class.forName(String className)` использует системный ClassLoader, который на ранних этапах загрузки под Knot ClassLoader выбрасывает ложный `ClassNotFoundException`, даже когда нужный класс существует.

`ModVersionGuard` явно передает `Thread.currentThread().getContextClassLoader()`:

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

## 🔗 Связанные страницы
* [[Совместимость версий|ru_ru-Version-Compatibility]]
* [[Руководство по MC 26.2|ru_ru-Minecraft-26.2-Guide]]
