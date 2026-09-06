# Portail Principal du Wiki - Dasik Library

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

Bienvenue dans la documentation technique officielle de **Dasik Library**, le framework partagé d'intelligence collective sociale (Hive Mind), de moteur génétique, de calcul de nuées Boids et d'infrastructure de GameRules dynamiques pour les mods Fabric Minecraft.

> 📌 **Avis sur le Dépôt Source** : La documentation de ce wiki reflète l'**état actuel du code source du dépôt**, qui peut inclure des commits récents et non publiés ou des fonctionnalités en cours de développement précédant les versions publiques sur CurseForge et Modrinth.

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 Philosophie Architecturale

Dasik Library repose sur deux principes directeurs :

1. **« Thin Mod, Fat Library » (Mod Léger, Bibliothèque Robuste)** : Les mods consommateurs se concentrent uniquement sur le contenu spécifique et les enregistrements d'entités. Les calculs mathématiques lourds, la planification de ticks, la transmission génétique, les vecteurs de nuée et la sérialisation NBT sont centralisés dans Dasik Library.
2. **« One Brain, Many Minds » (Un Cerveau, Plusieurs Esprits)** : Une exécution par pulsation centralisée via `GlobalSocialSystem` applique la **Règle Highlander** (strictement 1 cycle de tick global par tick de jeu) pour orchestrer des milliers d'entités sans dégradation des performances.

---

## 📦 Annuaire des Versions Minecraft

* [[Guide MC 26.2|fr_fr-Minecraft-26.2-Guide]] — Installation et configuration pour Minecraft 26.2+.
* [[Compatibilité des Versions|fr_fr-Version-Compatibility]] — Matrice du cycle de vie multi-versions (`>=26.1.2-` à `26.2+`), sécurité Knot ClassLoader et règles de protection.

---

## 🎮 Systèmes Centraux & Matrice des Mécaniques

Explorez les mécaniques techniques complètes et les guides de configuration :

* [[Système Social d'Intelligence Collective|fr_fr-Hive-Mind-Social-System]] — Moteur à impulsion unique, Règle Highlander, `SocialRegistry` partitionné en $O(1)$ et budgets de tick.
* [[Planificateur Social et Événements|fr_fr-Social-Scheduler-and-Events]] — `EntitySocialScheduler`, exécution double piste Humeur/Ambiance, `PriorityTier` et `SocialEventRegistry`.
* [[Moteur de Génétique Animale|fr_fr-Animal-Genetics-Engine]] — Attachement `EntityGenetics`, encodage ADN en Long, mise à l'échelle (`0.1x` - `3.0x`), indicateurs de chétif et mathématiques d'hérédité.
* [[API Génétique et Arbre Généalogique|fr_fr-Genetics-API-and-Pedigree]] — Façade `DasikAnimalGeneticsAPI`, calcul de parenté, prédiction des risques de consanguinité et modificateurs dynamiques.
* [[Modificateurs de Butin Génétique|fr_fr-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`, `GeneticsLootRegistry` et interception des drops via `LivingEntityLootMixin`.
* [[Suivi de Leader et Comportement de Nuée|fr_fr-Leader-Follower-and-Flocking]] — `GroupMember`, `FollowLeaderGoal`, stratégies Boids aériennes et terrestres, pondérations de `GroupParameters`.
* [[Gestionnaire Dynamique de GameRules|fr_fr-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`, enregistrement dynamique, injection automatique des traductions, en-têtes gras (`§l`) et convertisseurs.
* [[Codec et Sérialisation des GameRules|fr_fr-GameRule-Codec-and-Serialization]] — Validation des bornes entières (`Integer.MIN_VALUE`), prévention des crashs `SavedDataStorage.encodeUnchecked`.
* [[GameRules Client et Assistants GUI|fr_fr-Client-GameRule-and-GUI-Helpers]] — Requêtes client `ClientGameRuleHelper`, `GuiHelper`, remplacement atomique JSON dans `ConfigHelper`.
* [[Enchantements Dynamiques et Suivi de Vision|fr_fr-Dynamic-Enchantments-and-Vision]] — Injection à chaud via `DynamicEnchantmentManager` et tests de champ de vision avec `PlayerVisionTracker`.
* [[Utilitaires Stochastiques et Mathématiques|fr_fr-Stochastic-and-Math-Utilities]] — Algorithmes XORSHIFT dans `FastRandom`, échantillonnage par mille/décile dans `StochasticUtil` et conversion tick-seconde ($20\text{ ticks} = 1\text{s}$).
* [[Nettoyage d'Attributs Obsolètes et Échelle|fr_fr-Stale-Attribute-Purging-and-Scale]] — Règles de purge pour modificateurs d'attributs, calcul d'offset de base `-1.0f` pour `ADD_VALUE`.
* [[ModVersionGuard et Sécurité au Démarrage|fr_fr-ModVersionGuard-and-Startup-Safety]] — Vérification Knot ClassLoader (`Thread.currentThread().getContextClassLoader()`) et protection contre les incompatibilités.

---

## 💻 Référence Technique & Développeur

* [[Configuration Développeur et Compilation|fr_fr-Developer-Setup-and-Building]] — Prérequis JDK 25, Gradle 9.3+, `./gradlew build --no-daemon` et `./gradlew test`.
* [[Architecture et Structure des Paquets|fr_fr-Architecture-and-Package-Layout]] — Arborescence complète des paquets ASCII (`ai`, `api`, `config`, `core`, `mixin`, `util`) et concurrence.
* [[Référence Mixin et Points d'Injection|fr_fr-Mixin-Reference-and-Hooks]] — Tableau d'injection détaillé (`LanguageMixin`, `LivingEntityLootMixin`, `MobGoalAccessor`, `PathfinderMobMixin`, `ProfileTriggerMixin`).
* [[Profils de Comportement et Conditions|fr_fr-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`, `BehaviorProfile`, `BehaviorCondition`, `ProfileAware` et `DefaultProfileBuilder`.
* [[Guide d'Intégration pour Mods Consommateurs|fr_fr-Consumer-Mods-Integration-Guide]] — Guide d'intégration et exemples de code pour mods tiers (*Better Dogs*, *Natural Reproduction*, *Collapsible Game Rule Screen*, *Bat Ecology*, *Ore Amplifier*).

---

## 🔗 Liens Externes

* [[Dépôt GitHub|Home]]
* [Page du Projet Modrinth](https://modrinth.com/mod/dasik-library)
* [Page du Projet CurseForge](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
