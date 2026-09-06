# Portal Principal de la Wiki - Dasik Library

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

Bienvenido a la documentación técnica oficial de **Dasik Library**, el motor de IA social compartida con Mente Colmena (Hive Mind), marco de genética, calculadora de bandadas Boids e infraestructura dinámica de GameRules para mods de Fabric Minecraft.

> 📌 **Aviso sobre el Código Fuente del Repositorio**: La documentación de esta Wiki refleja el **estado actual del código fuente en el repositorio**, que puede incluir confirmaciones recientes no publicadas o características en desarrollo antes de las compilaciones públicas en CurseForge y Modrinth.

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 Filosofía Arquitectónica

Dasik Library opera bajo dos principios arquitectónicos fundamentales:

1. **"Thin Mod, Fat Library" (Mod Ligero, Biblioteca Robusta)**: Los mods consumidores modulares se enfocan estrictamente en el contenido de entidades y registros. Las matemáticas complejas, la planificación de ticks, la herencia genética, el cálculo de vectores de dirección Boids y la serialización de estado NBT se centralizan dentro de Dasik Library.
2. **"One Brain, Many Minds" (Un Cerebro, Múltiples Mentes)**: La ejecución de pulsos centralizada a través de `GlobalSocialSystem` aplica la **Regla del Inmortal (Highlander Rule)** (estrictamente 1 ciclo de pulso global por tick del juego) para administrar miles de entidades activas con bajo impacto de rendimiento.

---

## 📦 Directorio de Versiones de Minecraft

* [[Guía de MC 26.2|es_es-Minecraft-26.2-Guide]] — Instalación y configuración para Minecraft 26.2+.
* [[Compatibilidad de Versiones|es_es-Version-Compatibility]] — Matriz del ciclo de vida de compatibilidad multiversión (`>=26.1.2-` a `26.2+`), seguridad de Knot ClassLoader y reglas de protección de versión.

---

## 🎮 Matriz de Sistemas y Mecánicas Principales

Explore las mecánicas técnicas completas y las guías de configuración:

* [[Sistema Social de Mente Colmena|es_es-Hive-Mind-Social-System]] — Motor de pulso único, Regla del Inmortal, registro fragmentado $O(1)$ `SocialRegistry` y presupuestos de tick.
* [[Programador Social y Eventos|es_es-Social-Scheduler-and-Events]] — `EntitySocialScheduler`, ejecución dual Mood/Ambient, niveles `PriorityTier` y `SocialEventRegistry`.
* [[Motor de Genética Animal|es_es-Animal-Genetics-Engine]] — Adjunto `EntityGenetics`, codificación DNA long, límites de escala (`0.1x` - `3.0x`), indicadores de cría raquítica y matemáticas de herencia.
* [[API de Genética y Pedigrí|es_es-Genetics-API-and-Pedigree]] — Fachada `DasikAnimalGeneticsAPI`, cálculo de árbol genealógico y parentesco, fórmulas de predicción de endogamia y modificadores dinámicos.
* [[Modificadores de Botín Genético|es_es-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`, `GeneticsLootRegistry` y lógica de envoltura de caídas con `LivingEntityLootMixin`.
* [[Seguidor de Líder y Bandadas|es_es-Leader-Follower-and-Flocking]] — `GroupMember`, `FollowLeaderGoal`, estrategias de bandadas Boids aéreas y terrestres y pesos de `GroupParameters`.
* [[Gestor de GameRules Dinámicas|es_es-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`, registro dinámico, inyección automática de traducciones en inglés, encabezados en negrita (`§l`) y ayudantes matemáticos.
* [[Codec y Serialización de GameRules|es_es-GameRule-Codec-and-Serialization]] — Validación de límites enteros de GameRule (`Integer.MIN_VALUE` fallback), evitando cierres inesperados en `SavedDataStorage.encodeUnchecked`.
* [[GameRules del Cliente y Ayudantes de GUI|es_es-Client-GameRule-and-GUI-Helpers]] — Consultas al servidor integrado con `ClientGameRuleHelper`, `GuiHelper`, reemplazo atómico de JSON en `ConfigHelper` y seguridad para servidores dedicados.
* [[Encantamientos Dinámicos y Rastreador de Visión|es_es-Dynamic-Enchantments-and-Vision]] — Inyección en tiempo de ejecución con `DynamicEnchantmentManager` y comprobaciones de cono visual con `PlayerVisionTracker`.
* [[Utilidades Estocásticas y Matemáticas|es_es-Stochastic-and-Math-Utilities]] — Algoritmos XORSHIFT en `FastRandom`, tiradas de probabilidad por mil/deciles en `StochasticUtil` y conversión de ticks a segundos ($20\text{ ticks} = 1\text{s}$).
* [[Purga de Atributos Obsoletos y Escala|es_es-Stale-Attribute-Purging-and-Scale]] — Reglas de purga de modificadores de atributos, matemáticas de desplazamiento base `ADD_VALUE` `-1.0f` y seguridad de identificadores `genetics_`.
* [[ModVersionGuard y Seguridad de Inicio|es_es-ModVersionGuard-and-Startup-Safety]] — Seguridad de Knot ClassLoader (`Thread.currentThread().getContextClassLoader()`) y prevención de fallos al iniciar el juego.

---

## 💻 Referencia Técnica y para Desarrolladores

* [[Configuración de Desarrollador y Compilación|es_es-Developer-Setup-and-Building]] — Requisitos de JDK 25, Gradle 9.3+, `./gradlew build --no-daemon` y `./gradlew test`.
* [[Arquitectura y Distribución de Paquetes|es_es-Architecture-and-Package-Layout]] — Árbol de paquetes ASCII completo (`ai`, `api`, `config`, `core`, `mixin`, `util`) y modelos de concurrencia segura.
* [[Referencia de Mixins y Puntos de Inyección|es_es-Mixin-Reference-and-Hooks]] — Tabla detallada de Mixins (`LanguageMixin`, `LivingEntityLootMixin`, `MobGoalAccessor`, `PathfinderMobMixin`, `ProfileTriggerMixin`).
* [[Perfiles de Comportamiento y Condiciones|es_es-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`, `BehaviorProfile`, `BehaviorCondition`, `ProfileAware` y `DefaultProfileBuilder`.
* [[Guía de Integración para Mods Consumidores|es_es-Consumer-Mods-Integration-Guide]] — Guía de integración y fragmentos de código para mods (*Better Dogs*, *Natural Reproduction*, *Collapsible Game Rule Screen*, *Bat Ecology*, *Ore Amplifier*).

---

## 🔗 Enlaces Externos

* [[GitHub Repository|Home]]
* [Modrinth Project Page](https://modrinth.com/mod/dasik-library)
* [CurseForge Project Page](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
