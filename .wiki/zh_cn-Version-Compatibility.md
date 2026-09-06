# 版本兼容性矩阵

| 目标 Minecraft 版本 | 库版本 | Mod Version Guard | 依赖范围 | 支持状态 |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **主线活跃支持** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | 向后移植 / 稳定 |
| **Minecraft 1.21.x** | *已废弃* | *N/A* | *Legacy 1.x* | **生命周期结束 (EOL)** |

> 📌 **代码仓库源码声明**：本 Wiki 中的文档反映的是**当前代码仓库的源码状态**，可能包含领先于 CurseForge 与 Modrinth 公开发布版本的最新未发布提交或开发中特性。

---

## 🛡️ 1 Jar 1 Version 与向前兼容策略

Dasik Library 推行 **1 Jar 1 Version 政策**，并结合**开放式向前兼容性**：

1. **开放式版本下界 (`"minecraft": ">=26.1.2-"`)**：在 `fabric.mod.json` 中指定开放式下界，允许 Fabric Loader 在后续补丁更新中加载该 JAR，避免因硬编码版本号将玩家拒之门外。
2. **Knot ClassLoader 安全检查 (`ModVersionGuard`)**：在 `onInitialize()` 阶段，`ModVersionGuard.checkClass` 通过 `Thread.currentThread().getContextClassLoader()` 主动验证类是否存在，以便在 API 发生重大变化时第一时间抛出友好的错误横幅，避免 JVM 静默崩溃。

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

## 🚫 版本标识守卫（禁止混淆历史版本）

根据严格的设计规范（`[DIR-20260614-001]`），**Minecraft 26.x 年度版本绝不能与 2024 年的旧版 1.21.x 混为一谈**：
* ❌ `26.2 (1.21.4)` — 严格禁止。
* ✅ `Minecraft 26.2` — 标准主权年度版本记法。

---

## 🔗 相关页面
* [[MC 26.2 指南|zh_cn-Minecraft-26.2-Guide]]
* [[ModVersionGuard 与启动安全|zh_cn-ModVersionGuard-and-Startup-Safety]]
