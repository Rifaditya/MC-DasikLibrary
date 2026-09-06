# 開発環境セットアップとビルド

| ツール | 推奨・必須バージョン |
| :--- | :--- |
| **Java Development Kit (JDK)** | JDK 25 |
| **Gradle** | `9.3+` |
| **Fabric Loom** | `1.11+` |

---

## 🚀 ビルド手順

リポジトリをクローンしてローカルでビルドするコマンド：

```bash
# Clone the repository
git clone https://github.com/Rifaditya/MC-DasikLibrary.git
cd MC-DasikLibrary

# Build without daemon
./gradlew build --no-daemon

# Run automated tests
./gradlew test
```

---

## 🔗 関連ページ
* [[アーキテクチャとパッケージ構成|ja_jp-Architecture-and-Package-Layout]]
* [[MC 26.2 ガイド|ja_jp-Minecraft-26.2-Guide]]
