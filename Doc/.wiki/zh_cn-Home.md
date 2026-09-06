# Dasik Library 维基首页

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

欢迎来到 **Dasik Library** 的官方技术文档。Dasik Library 是专为 Fabric Minecraft 模组构建的共享蜂巢思维社交 AI 引擎、遗传学框架、Boids 集群计算器以及动态 GameRule 基础设施。

> 📌 **代码仓库源码声明**：本 Wiki 中的文档反映的是**当前代码仓库的源码状态**，可能包含领先于 CurseForge 与 Modrinth 公开发布版本的最新未发布提交或开发中特性。

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 架构设计哲学

Dasik Library 遵循两大核心架构原则：

1. **"Thin Mod, Fat Library" (轻模组，重库)**：模组本体专注于实体内容与注册。复杂数学运算、Tick 调度、遗传继承、Boids 转向向量计算以及 NBT 状态序列化均集中在 Dasik Library 中。
2. **"One Brain, Many Minds" (一脑多思)**：通过 `GlobalSocialSystem` 集中执行脉冲，严格执行 **Highlander 法则**（每个游戏 Tick 严格仅运行 1 个全局 Tick 周期），以低性能开销高效管理数千个活动实体。

---

## 📦 Minecraft 版本目录

* [[MC 26.2 指南|zh_cn-Minecraft-26.2-Guide]] — Minecraft 26.2+ 的安装与配置指南。
* [[版本兼容性|zh_cn-Version-Compatibility]] — 多版本兼容性生命周期矩阵（`>=26.1.2-` 至 `26.2+`）、Knot ClassLoader 安全防护与版本守卫规则。

---

## 🎮 核心系统与机制矩阵

探索完整的技术机制与配置指南：

* [[蜂巢思维社交系统|zh_cn-Hive-Mind-Social-System]] — 单脉冲引擎、Highlander 法则、$O(1)$ 分片式 `SocialRegistry` 与 Tick 预算控制。
* [[社交调度器与事件|zh_cn-Social-Scheduler-and-Events]] — `EntitySocialScheduler`、双轨情绪/环境任务执行、`PriorityTier` 与 `SocialEventRegistry`。
* [[动物遗传学引擎|zh_cn-Animal-Genetics-Engine]] — `EntityGenetics` 附加组件、DNA 长整型编码、体型与缩放边界（`0.1x` - `3.0x`）、劣株判定与遗传数学。
* [[遗传学 API 与谱系|zh_cn-Genetics-API-and-Pedigree]] — `DasikAnimalGeneticsAPI` 门面、亲缘与系谱家族树计算、近亲繁殖风险预测公式与动态特征修饰符。
* [[遗传学战利品修饰符|zh_cn-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`、`GeneticsLootRegistry` 与 `LivingEntityLootMixin` 掉落物包装逻辑。
* [[头领跟随与集群算法|zh_cn-Leader-Follower-and-Flocking]] — `GroupMember`、`FollowLeaderGoal`、空中与地面 Boids 集群策略与 `GroupParameters` 权重参数。
* [[动态游戏规则管理器|zh_cn-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`、动态注册、英语翻译自动注入、粗体分类标题（`§l`）与数学辅助转换。
* [[游戏规则 Codec 与序列化|zh_cn-GameRule-Codec-and-Serialization]] — 整数 GameRule 边界验证（`Integer.MIN_VALUE` 后备值），彻底防止 `SavedDataStorage.encodeUnchecked` 崩溃。
* [[客户端游戏规则与 GUI 助手|zh_cn-Client-GameRule-and-GUI-Helpers]] — `ClientGameRuleHelper` 整合服务器查询、`GuiHelper`、`ConfigHelper` 原子 JSON 替换与服务器防崩溃防护。
* [[动态附魔与视野追踪|zh_cn-Dynamic-Enchantments-and-Vision]] — `DynamicEnchantmentManager` 运行时注入与 `PlayerVisionTracker` 视锥光线投射检测。
* [[随机与数学实用工具|zh_cn-Stochastic-and-Math-Utilities]] — `FastRandom` XORSHIFT 算法、`StochasticUtil` 十分位/千分位判定与 `TimeUtil` Tick 到秒（$20\text{ ticks} = 1\text{s}$）转换。
* [[过期属性清理与体型缩放|zh_cn-Stale-Attribute-Purging-and-Scale]] — 属性修饰符清除规则、`ADD_VALUE` `-1.0f` 缩放基准偏移数学与 `genetics_` 修饰符安全。
* [[ModVersionGuard 与启动安全|zh_cn-ModVersionGuard-and-Startup-Safety]] — `ModVersionGuard` Knot ClassLoader 安全防护（`Thread.currentThread().getContextClassLoader()`）与启动崩溃防御。

---

## 💻 开发者与技术参考

* [[开发者环境配置与构建|zh_cn-Developer-Setup-and-Building]] — JDK 25 环境依赖、Gradle 9.3+、`./gradlew build --no-daemon` 与 `./gradlew test`。
* [[架构与包结构设计|zh_cn-Architecture-and-Package-Layout]] — 完整的 ASCII 包目录树（`ai`、`api`、`config`、`core`、`mixin`、`util`）与线程安全模型。
* [[Mixin 参考与注入钩子|zh_cn-Mixin-Reference-and-Hooks]] — 详细的 Mixin 分析表（`LanguageMixin`、`LivingEntityLootMixin`、`MobGoalAccessor`、`PathfinderMobMixin`、`ProfileTriggerMixin`）。
* [[行为配置文件与触发条件|zh_cn-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`、`BehaviorProfile`、`BehaviorCondition`、`ProfileAware` 与 `DefaultProfileBuilder`。
* [[下游 Mod 接入集成指南|zh_cn-Consumer-Mods-Integration-Guide]] — 下游模组（*Better Dogs*、*Natural Reproduction*、*Collapsible Game Rule Screen*、*Bat Ecology*、*Ore Amplifier*）的接入指南与代码示例。

---

## 🔗 外部链接

* [[GitHub Repository|Home]]
* [Modrinth Project Page](https://modrinth.com/mod/dasik-library)
* [CurseForge Project Page](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
