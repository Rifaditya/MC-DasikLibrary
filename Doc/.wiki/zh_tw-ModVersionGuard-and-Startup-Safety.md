# ModVersionGuard 與啟動安全

| 類別 | `net.dasik.social.util.ModVersionGuard` |
| :--- | :--- |
| **ClassLoader 策略** | 明確使用 `Thread.currentThread().getContextClassLoader()` |
| **禁用模式** | 不帶 ClassLoader 的預設 `Class.forName(name)` |
| **入口點目標** | `DasikLibraryMod.onInitialize()` |

---

## 🛡️ Knot ClassLoader 解析法則

在 Fabric 模組開發中，預設的 `Class.forName(String className)` 使用系統 ClassLoader，這在模組早期初始化階段於 Fabric Knot ClassLoader 環境下會失敗，即使目標類別確實存在也會拋出錯誤的 `ClassNotFoundException`。

`ModVersionGuard` 明確傳入 `Thread.currentThread().getContextClassLoader()`：

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

## 🔗 相關頁面
* [[版本相容性|zh_tw-Version-Compatibility]]
* [[MC 26.2 指南|zh_tw-Minecraft-26.2-Guide]]
