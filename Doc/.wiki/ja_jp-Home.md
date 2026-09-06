# Wiki メインポータル - Dasik Library

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

**Dasik Library** の公式技術ドキュメントへようこそ。本ライブラリは、Fabric Minecraft Mod 向けの群知能ソーシャル AI（Hive Mind）、動物遺伝学エンジン、Boids 群れ制御ベクトル計算、および動的 GameRule インフラストラクチャを提供する共通コアフレームワークです。

> 📌 **ソースリポジトリに関する注意事項**: 本 Wiki のドキュメントは**現在のリポジトリ内ソースコード**の状態を反映しており、CurseForge や Modrinth での一般リリース版に先駆けて実装された最新のコミットや開発中の機能が含まれている場合があります。

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 設計思想とアーキテクチャ

Dasik Library は以下の 2 つのコア設計原則に基づいて構築されています：

1. **「Thin Mod, Fat Library」（軽量 Mod、重厚な共通ライブラリ）**: 各コンシューマー Mod はエンティティ固有の挙動や登録処理に専念し、複雑な数学的計算、Tick スケジューリング、遺伝的継承、Boids 群れベクトル計算、NBT 状態シリアライズなどはすべて Dasik Library に集約します。
2. **「One Brain, Many Minds」（一つの頭脳、無数の意識）**: `GlobalSocialSystem` による集中パルス実行により、**ハイランダーの原則**（1 ゲーム Tick あたり厳格に 1 グローバル Tick サイクル）を強制し、何千ものアクティブなエンティティをサーバー負荷を最小限に抑えて統合制御します。

---

## 📦 Minecraft バージョンディレクトリ

* [[MC 26.2 ガイド|ja_jp-Minecraft-26.2-Guide]] — Minecraft 26.2+ 向けの導入方法と設定。
* [[バージョン互換性|ja_jp-Version-Compatibility]] — マルチバージョン対応マトリクス（`>=26.1.2-` 〜 `26.2+`）、Knot ClassLoader の安全性、および互換性保護ルール。

---

## 🎮 コアシステム＆メカニクスマトリクス

全技術メカニクスおよび設定の詳細ガイド：

* [[群知能ソーシャルシステム|ja_jp-Hive-Mind-Social-System]] — 単一パルス駆動エンジン、ハイランダーの原則、$O(1)$ シャーディング `SocialRegistry`、および Tick バジェット。
* [[ソーシャルスケジューラ＆イベント|ja_jp-Social-Scheduler-and-Events]] — `EntitySocialScheduler`、Mood/Ambient デュアルトラック実行、`PriorityTier`、および `SocialEventRegistry`。
* [[動物遺伝学エンジン|ja_jp-Animal-Genetics-Engine]] — `EntityGenetics` アタッチメント、DNA Long 値エンコーディング、サイズスケーリング（`0.1x` 〜 `3.0x`）、未熟個体（Runt）判定、遺伝の数学的計算。
* [[遺伝学 API と血統ツリー|ja_jp-Genetics-API-and-Pedigree]] — `DasikAnimalGeneticsAPI` ファサード、親族判定・血統分析、近親交配リスク予測、動的特性モディファイア。
* [[遺伝学ドロップ修飾子|ja_jp-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`、`GeneticsLootRegistry`、および `LivingEntityLootMixin` によるドロップ割り込みフック。
* [[リーダー追従＆群れ形成アルゴリズム|ja_jp-Leader-Follower-and-Flocking]] — `GroupMember`、`FollowLeaderGoal`、飛行および地上向け Boids アルゴリズム、`GroupParameters` 重み付け。
* [[動的 GameRules マネージャー|ja_jp-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`、動的登録、自動英語翻訳注入、太字カテゴリヘッダー（`§l`）、計算ヘルパー。
* [[GameRule Codec とシリアライズ|ja_jp-GameRule-Codec-and-Serialization]] — 整数 GameRule の境界値バリデーション（`Integer.MIN_VALUE`）、`SavedDataStorage.encodeUnchecked` クラッシュ防止。
* [[クライアント GameRule と GUI ヘルパー|ja_jp-Client-GameRule-and-GUI-Helpers]] — `ClientGameRuleHelper` サーバー照会、`GuiHelper`、`ConfigHelper` による JSON アトミック置換。
* [[動的エンチャント＆視界トラッカー|ja_jp-Dynamic-Enchantments-and-Vision]] — `DynamicEnchantmentManager` による実行時注入、および `PlayerVisionTracker` によるフラスタム・レイキャスト判定。
* [[確率・数学ユーティリティ|ja_jp-Stochastic-and-Math-Utilities]] — `FastRandom`（XORSHIFT）、`StochasticUtil` によるパーミル／デシル判定、Tick-秒変換（$20\text{ ticks} = 1\text{秒}$）。
* [[古い属性のパージとスケール補正|ja_jp-Stale-Attribute-Purging-and-Scale]] — 属性モディファイアのクリーンアップルール、`ADD_VALUE` `-1.0f` ベースオフセット計算、`genetics_` 接頭辞の安全性。
* [[ModVersionGuard と起動時クラッシュ防止|ja_jp-ModVersionGuard-and-Startup-Safety]] — Knot ClassLoader 検証（`Thread.currentThread().getContextClassLoader()`）によるバージョン不整合の早期検知。

---

## 💻 開発者向け技術リファレンス

* [[開発環境セットアップとビルド|ja_jp-Developer-Setup-and-Building]] — JDK 25 要件、Gradle 9.3+、`./gradlew build --no-daemon`、および `./gradlew test`。
* [[アーキテクチャとパッケージ構成|ja_jp-Architecture-and-Package-Layout]] — 完全な ASCII パッケージツリー（`ai`, `api`, `config`, `core`, `mixin`, `util`）およびスレッド安全性。
* [[Mixin リファレンスとフック|ja_jp-Mixin-Reference-and-Hooks]] — 詳細な Mixin インジェクション一覧表（`LanguageMixin`, `LivingEntityLootMixin`, `MobGoalAccessor`, `PathfinderMobMixin`, `ProfileTriggerMixin`）。
* [[行動プロファイルと発動条件|ja_jp-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`, `BehaviorProfile`, `BehaviorCondition`, `ProfileAware`, `DefaultProfileBuilder`。
* [[下流 Mod 連携・統合ガイド|ja_jp-Consumer-Mods-Integration-Guide]] — 下流 Mod（*Better Dogs*, *Natural Reproduction*, *Collapsible Game Rule Screen*, *Bat Ecology*, *Ore Amplifier*）向けの導入手順とコード例。

---

## 🔗 外部リンク

* [[GitHub リポジトリ|Home]]
* [Modrinth プロジェクトページ](https://modrinth.com/mod/dasik-library)
* [CurseForge プロジェクトページ](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
