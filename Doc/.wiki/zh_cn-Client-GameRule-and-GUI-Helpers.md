# 客户端游戏规则与 GUI 助手

| 助手类 | 适用环境 | 用途 |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | 客户端单人游戏 / 整合服务器 | 在客户端线程查询整合服务器的 GameRules |
| `ConfigHelper` | Common (客户端/服务器) | 原子性 JSON 配置加载、保存与备份替换 |
| `GuiHelper` | 客户端 | 可选 ModMenu / Cloth Config 界面助手 |

---

## 🖥️ 专用服务器类加载安全

为确保 100% 服务器端兼容性且绝不导致专用服务器因调用客户端类崩溃，客户端 GUI 与整合服务器助手全面采用延迟类加载检查（`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`）。

```ascii
                      DynamicGameRuleManager.getInt(level, ruleKey)
                                        │
                    ┌───────────────────┴───────────────────┐
                    ▼                                       ▼
          [ level instanceof ServerLevel ]       [ Client Environment ]
                    │                                       │
                    ▼                                       ▼
            Direct Level Lookup                   ClientGameRuleHelper
                                            (Queries Integrated Server)
```

---

## 📄 `ConfigHelper` 中的原子性 JSON 替换

`ConfigHelper` 提供了带有原子替换逻辑的高可靠性 JSON 文件持久化机制：

1. 将新配置数据写入临时文件 `config.json.tmp`。
2. 验证 JSON 结构完整性与文件大小。
3. 自动创建备份文件 `config.json.bak`。
4. 通过 `Files.move(..., StandardCopyOption.ATOMIC_MOVE)` 执行原子文件替换 `config.json.tmp` -> `config.json`。

---

## 🔗 相关页面
* [[动态游戏规则管理器|zh_cn-Dynamic-GameRules-Manager]]
* [[行为配置文件与触发条件|zh_cn-Behavior-Profiles-and-Conditions]]
