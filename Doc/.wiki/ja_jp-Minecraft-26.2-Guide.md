# Minecraft 26.2+ ガイド

| パラメータ | 仕様 |
| :--- | :--- |
| **対象 Minecraft バージョン** | `26.2`（および今後の `26.x` と互換） |
| **Fabric Loader 要件** | `>=0.18.4` |
| **Java 実行環境** | JDK 25 |
| **ライブラリバージョン** | `1.8.15` |
| **Mod ID** | `dasik-library` |
| **Mod 名称** | Dasik Library |
| **ライセンス** | LGPL-3.0 |

> 📌 **ソースリポジトリに関する注意事項**: 本 Wiki のドキュメントは**現在のリポジトリ内ソースコード**の状態を反映しており、CurseForge や Modrinth での一般リリース版に先駆けて実装された最新のコミットや開発中の機能が含まれている場合があります。

---

## 🛠️ 概要＆導入方法

**Dasik Library** は、*Vanilla Outsider*、*Instant Gratification*、および *Delayed Gratification* シリーズの各 Mod にとって必須の実行時前提ライブラリです。ソーシャル AI の統合 Tick スケジューリング、動物遺伝学、Boids ベクトル計算、動的 GameRule 登録を提供します。

### 📥 プレイヤー向けインストール
1. Minecraft `26.2` に対応した **Fabric Loader**（`0.18.4` 以降）を導入します。
2. `.minecraft/mods` フォルダに **Fabric API**（`0.152.1+26.2` 以降）が配置されていることを確認します。
3. `dasik-library-1.8.15.jar` をダウンロードし、依存 Mod（*Better Dogs*, *Natural Reproduction* など）とともに `.minecraft/mods` に配置します。

### 💻 Mod 開発者向け依存設定

自身の `fabric.mod.json` に **Dasik Library** を追加します：

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

`gradle.properties` に追加：

```properties
dasik_library_version=1.8.15
```

`build.gradle` に追加：

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_library_version}"
}
```

---

## ⚙️ 26.2+ における主要アーキテクチャ変更点

1. **Mojang Sovereign マッピング**: Mojang 公式マッピング（`level`, `ServerLevel`, `EntityTypes`）を使用します。旧来の Yarn 命名（`world`, `getWorld`）は完全廃止されました。
2. **Identifier API**: `Identifier.fromNamespaceAndPath(namespace, path)` または `Identifier.parse(string)` を使用します。非推奨の `Identifier.of()` は使用されません。
3. **開放型バージョン境界（`>=26.1.2-`）**: 単一の JAR ファイルで今後の Minecraft `26.2+` パッチアップデートに対応しつつ、`ModVersionGuard` により実行時の安全性を保証します。

---

## 🔗 関連ページ
* [[バージョン互換性|ja_jp-Version-Compatibility]]
* [[ModVersionGuard と起動時クラッシュ防止|ja_jp-ModVersionGuard-and-Startup-Safety]]
* [[開発環境セットアップとビルド|ja_jp-Developer-Setup-and-Building]]
