# Mixin 参考与注入钩子

| Mixin 类 | 目标类 | 注入点 | 用途 |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `@Inject` on `loadFromJson` (`RETURN`) | 动态 GameRule 翻译注入 |
| `LivingEntityLootMixin` | `LivingEntity` | `@ModifyVariable` on `dropFromLootTable` | 拦截掉落物进行遗传掉落缩放 |
| `MobGoalAccessor` | `Mob` | `@Accessor("goalSelector")` | 直接访问 goalSelector 用于 AI 注入 |
| `PathfinderMobMixin` | `PathfinderMob` | `@Inject` on `<init>` (`RETURN`) | 蜂巢思维脉冲注册钩子 |
| `ProfileTriggerMixin` | `Entity` | `@Inject` on `teleportCrossDimension` (`RETURN`) | 跨维度旅行时的配置文件触发 |

---

## 🔍 详细拦截器解析

### 1. `LanguageMixin`
将 `DynamicGameRuleManager` 动态生成的英语标签直接注入到 Minecraft 的本地化提供器中，无需手动写入 `.json` 文件。

### 2. `LivingEntityLootMixin`
在 `dropFromLootTable` 执行期间包装掉落物消费者，委托给 `GeneticsLootRegistry` 根据生物遗传特征动态缩放或替换掉落物。

---

## 🔗 相关页面
* [[架构与包结构设计|zh_cn-Architecture-and-Package-Layout]]
* [[动态游戏规则管理器|zh_cn-Dynamic-GameRules-Manager]]
