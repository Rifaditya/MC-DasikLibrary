# Mixin Reference & Hooks

| Mixin Class | Target Class | Injection Point | Purpose |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `Language` / `ClientLanguage` | `@Inject` on `get` | Dynamic GameRule translation injection |
| `LivingEntityLootMixin` | `LivingEntity` | `@ModifyVariable` on `dropFromLootTable` | Intercept drops for genetics loot scaling |
| `MobGoalAccessor` | `Mob` | `@Accessor` on `goalSelector` | Direct goal selector access for AI injections |
| `PathfinderMobMixin` | `PathfinderMob` | `@Inject` on `tick` | Hive Mind pulse registration hook |
| `ProfileTriggerMixin` | `LivingEntity` | `@Inject` on `customServerAiStep` | Behavior profile state machine trigger |

---

## 🔍 Detailed Interceptor Breakdown

### 1. `LanguageMixin`
Injects dynamically generated English labels from `DynamicGameRuleManager` directly into Minecraft's localization provider without requiring raw `.json` entries.

### 2. `LivingEntityLootMixin`
Wraps loot drop consumers during `dropFromLootTable`, delegating to `GeneticsLootRegistry` to dynamically scale or swap drops based on mob genetics.

---

## 🔗 Related Pages
* [[Architecture & Package Layout|Architecture-and-Package-Layout]]
* [[Dynamic GameRules Manager|Dynamic-GameRules-Manager]]
