# ModVersionGuard 与启动安全

| 类 | `net.dasik.social.util.ModVersionGuard` |
| :--- | :--- |
| **ClassLoader 策略** | 明确使用 `Thread.currentThread().getContextClassLoader()` |
| **禁用模式** | 不带 ClassLoader 的默认 `Class.forName(name)` |
| **入口点目标** | `DasikLibraryMod.onInitialize()` |

---

## 🛡️ Knot ClassLoader 解析法则

在 Fabric 模组开发中，默认的 `Class.forName(String className)` 使用系统 ClassLoader，这在模组早期初始化阶段于 Fabric Knot ClassLoader 环境下会失败，即使目标类确实存在也会抛出错误的 `ClassNotFoundException`。

`ModVersionGuard` 明确传入 `Thread.currentThread().getContextClassLoader()`：

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

## 🔗 相关页面
* [[版本兼容性|zh_cn-Version-Compatibility]]
* [[MC 26.2 指南|zh_cn-Minecraft-26.2-Guide]]
