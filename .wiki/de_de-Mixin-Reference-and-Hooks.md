# Mixin-Referenz & Hooks

| Mixin-Klasse | Zielklasse | Injektionspunkt | Zweck |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `@Inject` bei `loadFromJson` (`RETURN`) | Dynamische GameRule-Übersetzungsinjektion |
| `LivingEntityLootMixin` | `LivingEntity` | `@ModifyVariable` bei `dropFromLootTable` | Beuteabfang zur genetischen Skalierung |
| `MobGoalAccessor` | `Mob` | `@Accessor("goalSelector")` | Direkter Zugriff auf Goal-Selector für KI-Einspeisung |
| `PathfinderMobMixin` | `PathfinderMob` | `@Inject` bei `<init>` (`RETURN`) | Registrierungshook im Hive-Mind-Impuls |
| `ProfileTriggerMixin` | `Entity` | `@Inject` bei `teleportCrossDimension` (`RETURN`) | Profilauslösung bei Dimensionsreisen |

---

## 🔍 Detaillierte Interceptor-Aufschlüsselung

### 1. `LanguageMixin`
Injiziert dynamisch generierte englische Texte aus dem `DynamicGameRuleManager` direkt in den Sprachprovider des Spiels, ohne dass Rohdateien vom Typ `.json` benötigt werden.

### 2. `LivingEntityLootMixin`
Fängt die Beuteabgabe in `dropFromLootTable` ab und delegiert an die `GeneticsLootRegistry`, um Beutemengen oder Items anhand der Tiergröße dynamisch zu modifizieren.

---

## 🔗 Verwandte Seiten
* [[Architektur & Paketstruktur|de_de-Architecture-and-Package-Layout]]
* [[Dynamischer GameRules-Manager|de_de-Dynamic-GameRules-Manager]]
