# Referencia de Mixins y Puntos de Inyección

| Clase Mixin | Clase Objetivo | Punto de Inyección | Propósito |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `@Inject` en `loadFromJson` (`RETURN`) | Inyección de traducciones dinámicas de GameRules |
| `LivingEntityLootMixin` | `LivingEntity` | `@ModifyVariable` en `dropFromLootTable` | Intercepción de botín para escala genética |
| `MobGoalAccessor` | `Mob` | `@Accessor("goalSelector")` | Acceso directo al selector de metas de IA |
| `PathfinderMobMixin` | `PathfinderMob` | `@Inject` en `<init>` (`RETURN`) | Gancho de registro en el pulso de Mente Colmena |
| `ProfileTriggerMixin` | `Entity` | `@Inject` en `teleportCrossDimension` (`RETURN`) | Activación de perfiles al cambiar de dimensión |

---

## 🔍 Análisis Detallado de Interceptores

### 1. `LanguageMixin`
Inyecta etiquetas en inglés generadas dinámicamente desde `DynamicGameRuleManager` directamente en las tablas de idioma del cliente sin tocar archivos `.json`.

### 2. `LivingEntityLootMixin`
Envuelve el consumidor de caídas durante `dropFromLootTable`, delegando en `GeneticsLootRegistry` para modificar o sustituir objetos según la genética del espécimen.

---

## 🔗 Páginas Relacionadas
* [[Arquitectura y Distribución de Paquetes|es_es-Architecture-and-Package-Layout]]
* [[Gestor de GameRules Dinámicas|es_es-Dynamic-GameRules-Manager]]
