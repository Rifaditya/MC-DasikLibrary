# ModVersionGuard と起動時クラッシュ防止

| コンポーネント | クラス |
| :--- | :--- |
| **バージョンガード** | `net.dasik.social.core.ModVersionGuard` |
| **使用 ClassLoader** | `Thread.currentThread().getContextClassLoader()` |
| **保護対象** | 起動時のクラスローディング不整合による強制終了の防止 |

---

## 🛡️ `ModVersionGuard` の仕組み

Fabric Loader の Knot 環境下において、ClassLoader を指定せずに `Class.forName(name)` を呼び出すと、クラスの検出に失敗したり不整合を見逃したりすることがあります。`ModVersionGuard` はカレントスレッドのコンテキスト ClassLoader を明示的に参照し、必要な API が存在するかを初期化の最上流で検証します。

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

## 🔗 関連ページ
* [[バージョン互換性|ja_jp-Version-Compatibility]]
* [[MC 26.2 ガイド|ja_jp-Minecraft-26.2-Guide]]
