# Mixin 參考與注入鉤子

| Mixin 類別 | 目標類別 | 注入點 | 用途 |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `@Inject` on `loadFromJson` (`RETURN`) | 動態 GameRule 翻譯注入 |
| `LivingEntityLootMixin` | `LivingEntity` | `@ModifyVariable` on `dropFromLootTable` | 攔截掉落物進行遺傳掉落縮放 |
| `MobGoalAccessor` | `Mob` | `@Accessor("goalSelector")` | 直接訪問 goalSelector 用於 AI 注入 |
| `PathfinderMobMixin` | `PathfinderMob` | `@Inject` on `<init>` (`RETURN`) | 蜂巢思維脈衝註冊鉤子 |
| `ProfileTriggerMixin` | `Entity` | `@Inject` on `teleportCrossDimension` (`RETURN`) | 跨維度旅行時的設定檔觸發 |

---

## 🔍 詳細攔截器解析

### 1. `LanguageMixin`
將 `DynamicGameRuleManager` 動態生成的英語標籤直接注入到 Minecraft 的本地化提供器中，無需手動寫入 `.json` 檔案。

### 2. `LivingEntityLootMixin`
在 `dropFromLootTable` 執行期間包裝掉落物消費者，委託給 `GeneticsLootRegistry` 根據生物遺傳特徵動態縮放或替換掉落物。

---

## 🔗 相關頁面
* [[架構與套件結構設計|zh_tw-Architecture-and-Package-Layout]]
* [[動態遊戲規則管理器|zh_tw-Dynamic-GameRules-Manager]]
