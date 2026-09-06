# バージョン互換性マトリクス

| 対象 Minecraft バージョン | ライブラリバージョン | Mod Version Guard | 依存関係指定 | サポート状況 |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **アクティブ主系統** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | バックポート／安定版 |
| **Minecraft 1.21.x** | *廃止* | *N/A* | *Legacy 1.x* | **サポート終了 (EOL)** |

> 📌 **ソースリポジトリに関する注意事項**: 本 Wiki のドキュメントは**現在のリポジトリ内ソースコード**の状態を反映しており、CurseForge や Modrinth での一般リリース版に先駆けて実装された最新のコミットや開発中の機能が含まれている場合があります。

---

## 🛡️ 「1 Jar 1 Version」ポリシーと上位互換性設計

Dasik Library は **1 Jar 1 Version ポリシー** と **開放型前方互換性** を採用しています：

1. **開放型バージョン範囲（`"minecraft": ">=26.1.2-"`）**: `fabric.mod.json` で下限のみを指定することにより、マイナーアップデート時に Fabric Loader が JAR を安全に読み込めるようにします。
2. **Knot ClassLoader の保護（`ModVersionGuard`）**: `onInitialize()` 実行時に `ModVersionGuard.checkClass` が `Thread.currentThread().getContextClassLoader()` を介して必須クラスの存在を事前検証し、クラッシュ時に分かりやすい警告を出力します。

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

## 🚫 バージョン表記混同防止ルール

設計方針 `[DIR-20260614-001]` に従い、**Minecraft 26.x の年間リリース表記と 2024 年の旧 1.21.x 表記を決して混同してはなりません**：
* ❌ `26.2 (1.21.4)` — 厳格に禁止。
* ✅ `Minecraft 26.2` — 標準的な年間バージョン表記。

---

## 🔗 関連ページ
* [[MC 26.2 ガイド|ja_jp-Minecraft-26.2-Guide]]
* [[ModVersionGuard と起動時クラッシュ防止|ja_jp-ModVersionGuard-and-Startup-Safety]]
