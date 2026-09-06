# 下游 Mod 接入集成指南

| 下游 Mod | 集成亮点 |
| :--- | :--- |
| **Better Dogs** | 狼群动态、战术号角指令、劣株/巨型遗传缩放 |
| **Natural Reproduction** | 亲缘系谱检查、近亲繁殖风险预测、动物体型属性 |
| **Collapsible Game Rule Screen** | 分类标题格式化（`§l`）、动态 GameRule 注册 |
| **Bat Ecology** | 空中集群旋转策略（`FlockType.AERIAL`） |
| **Ore Amplifier** | 随机生成缩放（`StochasticUtil`） |

---

## 🛠️ 逐步接入清单

1. **添加依赖**：在 `fabric.mod.json` 中声明 `"dasik-library": "*"`。
2. **实现接口**：在目标生物实体上实现 `SocialEntity` / `GroupMember`。
3. **注册动态游戏规则**：在模组初始化时调用 `DynamicGameRuleManager.registerBoolean` / `registerInt`。
4. **调用遗传学门面**：通过 `DasikAnimalGeneticsAPI` 查询体型属性与系谱。

---

## 🔗 相关页面
* [[开发者环境配置与构建|zh_cn-Developer-Setup-and-Building]]
* [[架构与包结构设计|zh_cn-Architecture-and-Package-Layout]]
