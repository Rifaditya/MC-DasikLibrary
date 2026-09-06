# Dasik Library 維基首頁

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

歡迎來到 **Dasik Library** 的官方技術文檔。Dasik Library 是專為 Fabric Minecraft 模組構建的共享蜂巢思維社交 AI 引擎、遺傳學框架、Boids 群聚計算器以及動態 GameRule 基礎架構。

> 📌 **代碼倉庫原始碼聲明**：本 Wiki 中的文檔反映的是**當前代碼倉庫的原始碼狀態**，可能包含領先於 CurseForge 與 Modrinth 公開發布版本的最新未發布提交或開發中特性。

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 架構設計哲學

Dasik Library 遵循兩大核心架構原則：

1. **"Thin Mod, Fat Library" (輕模組，重庫)**：模組本體專注於實體內容與註冊。複雜數學運算、Tick 調度、遺傳繼承、Boids 轉向向量計算以及 NBT 狀態序列化均集中在 Dasik Library 中。
2. **"One Brain, Many Minds" (一腦多思)**：通過 `GlobalSocialSystem` 集中執行脈衝，嚴格執行 **Highlander 法則**（每個遊戲 Tick 嚴格僅運行 1 個全局 Tick 週期），以低性能開銷高效管理數千個活動實體。

---

## 📦 Minecraft 版本目錄

* [[MC 26.2 指南|zh_tw-Minecraft-26.2-Guide]] — Minecraft 26.2+ 的安裝與配置指南。
* [[版本相容性|zh_tw-Version-Compatibility]] — 多版本相容性生命週期矩陣（`>=26.1.2-` 至 `26.2+`）、Knot ClassLoader 安全防護與版本守衛規則。

---

## 🎮 核心系統與機制矩陣

探索完整的技術機制與配置指南：

* [[蜂巢思維社交系統|zh_tw-Hive-Mind-Social-System]] — 單脈衝引擎、Highlander 法則、$O(1)$ 分片式 `SocialRegistry` 與 Tick 預算控制。
* [[社交調度器與事件|zh_tw-Social-Scheduler-and-Events]] — `EntitySocialScheduler`、雙軌情緒/環境任務執行、`PriorityTier` 與 `SocialEventRegistry`。
* [[動物遺傳學引擎|zh_tw-Animal-Genetics-Engine]] — `EntityGenetics` 附加組件、DNA 長整型編碼、體型與縮放邊界（`0.1x` - `3.0x`）、劣株判定與遺傳數學。
* [[遺傳學 API 與譜系|zh_tw-Genetics-API-and-Pedigree]] — `DasikAnimalGeneticsAPI` 門面、親緣與系譜家族樹計算、近親繁殖風險預測公式與動態特徵修飾符。
* [[遺傳學戰利品修飾符|zh_tw-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`、`GeneticsLootRegistry` 與 `LivingEntityLootMixin` 掉落物包裝邏輯。
* [[頭領跟隨與集群演算法|zh_tw-Leader-Follower-and-Flocking]] — `GroupMember`、`FollowLeaderGoal`、空中與地面 Boids 群聚策略與 `GroupParameters` 權重參數。
* [[動態遊戲規則管理器|zh_tw-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`、動態註冊、英語翻譯自動注入、粗體分類標題（`§l`）與數學輔助轉換。
* [[遊戲規則 Codec 與序列化|zh_tw-GameRule-Codec-and-Serialization]] — 整數 GameRule 邊界驗證（`Integer.MIN_VALUE` 後備值），徹底防止 `SavedDataStorage.encodeUnchecked` 崩潰。
* [[客戶端遊戲規則與 GUI 助手|zh_tw-Client-GameRule-and-GUI-Helpers]] — `ClientGameRuleHelper` 整合伺服器查詢、`GuiHelper`、`ConfigHelper` 原子 JSON 替換與伺服器防崩潰防護。
* [[動態附魔與視野追蹤|zh_tw-Dynamic-Enchantments-and-Vision]] — `DynamicEnchantmentManager` 運行時注入與 `PlayerVisionTracker` 視錐光線投射檢測。
* [[隨機與數學實用工具|zh_tw-Stochastic-and-Math-Utilities]] — `FastRandom` XORSHIFT 演算法、`StochasticUtil` 十分位/千分位判定與 `TimeUtil` Tick 到秒（$20\text{ ticks} = 1\text{s}$）轉換。
* [[過期屬性清理與體型縮放|zh_tw-Stale-Attribute-Purging-and-Scale]] — 屬性修飾符清除規則、`ADD_VALUE` `-1.0f` 縮放基準偏移數學與 `genetics_` 修飾符安全。
* [[ModVersionGuard 與啟動安全|zh_tw-ModVersionGuard-and-Startup-Safety]] — `ModVersionGuard` Knot ClassLoader 安全防護（`Thread.currentThread().getContextClassLoader()`）與啟動崩潰防禦。

---

## 💻 開發者與技術參考

* [[開發者環境配置與構建|zh_tw-Developer-Setup-and-Building]] — JDK 25 環境依賴、Gradle 9.3+、`./gradlew build --no-daemon` 與 `./gradlew test`。
* [[架構與套件結構設計|zh_tw-Architecture-and-Package-Layout]] — 完整的 ASCII 套件目錄樹（`ai`、`api`、`config`、`core`、`mixin`、`util`）與執行緒安全模型。
* [[Mixin 參考與注入鉤子|zh_tw-Mixin-Reference-and-Hooks]] — 詳細的 Mixin 分析表（`LanguageMixin`、`LivingEntityLootMixin`、`MobGoalAccessor`、`PathfinderMobMixin`、`ProfileTriggerMixin`）。
* [[行為設定檔與觸發條件|zh_tw-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`、`BehaviorProfile`、`BehaviorCondition`、`ProfileAware` 與 `DefaultProfileBuilder`。
* [[下游 Mod 接入集成指南|zh_tw-Consumer-Mods-Integration-Guide]] — 下游模組（*Better Dogs*、*Natural Reproduction*、*Collapsible Game Rule Screen*、*Bat Ecology*、*Ore Amplifier*）的接入指南與代碼範例。

---

## 🔗 外部鏈接

* [[GitHub Repository|Home]]
* [Modrinth Project Page](https://modrinth.com/mod/dasik-library)
* [CurseForge Project Page](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
