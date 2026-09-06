# Hauptportal der Wiki - Dasik Library

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

Willkommen zur offiziellen technischen Dokumentation von **Dasik Library**, der gemeinsamen Schwarmintelligenz-Sozial-KI (Hive Mind), Genetik-Engine, Boids-Schwarmberechnung und dynamischen GameRule-Infrastruktur für Fabric Minecraft-Mods.

> 📌 **Hinweis zum Quellcode-Repository**: Die Dokumentation in diesem Wiki spiegelt den **aktuellen Stand des Quellcodes im Repository** wider, der möglicherweise kürzlich durchgeführte, noch nicht veröffentlichte Commits oder Entwicklungsfunktionen vor den öffentlichen Release-Builds auf CurseForge und Modrinth enthält.

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 Architekturphilosophie

Dasik Library folgt zwei zentralen Architekturprinzipien:

1. **"Thin Mod, Fat Library" (Schlanke Mods, Mächtige Bibliothek)**: Modulare Consumer-Mods konzentrieren sich ausschließlich auf Entitätsinhalte und Registrierungen. Komplexe Mathematik, Tick-Scheduling, genetische Vererbung, Boids-Lenkvektorberechnungen und NBT-Zustandsserialisierung sind in Dasik Library gebündelt.
2. **"One Brain, Many Minds" (Ein Gehirn, Viele Geister)**: Eine zentralisierte Impulsausführung über `GlobalSocialSystem` erzwingt die **Highlander-Regel** (strikt 1 globaler Tick-Zyklus pro Spiel-Tick), um Tausende aktiver Entitäten mit minimalem Leistungsaufwand zu verwalten.

---

## 📦 Minecraft-Versionsverzeichnis

* [[MC 26.2 Leitfaden|de_de-Minecraft-26.2-Guide]] — Installation und Konfiguration für Minecraft 26.2+.
* [[Versionskompatibilität|de_de-Version-Compatibility]] — Matrix des Multi-Versions-Lebenszyklus (`>=26.1.2-` bis `26.2+`), Knot ClassLoader-Sicherheit und Versionsschutzregeln.

---

## 🎮 Kernsysteme & Mechanik-Matrix

Erkunden Sie die vollständigen technischen Mechaniken und Konfigurationsleitfäden:

* [[Schwarmintelligenz-Sozialsystem|de_de-Hive-Mind-Social-System]] — Einzelimpuls-Engine, Highlander-Regel, $O(1)$ geshardete `SocialRegistry` und Tick-Budgets.
* [[Sozialer Planer & Ereignisse|de_de-Social-Scheduler-and-Events]] — `EntitySocialScheduler`, zweispurige Mood/Ambient-Ausführung, `PriorityTier` und `SocialEventRegistry`.
* [[Tiergenetik-Engine|de_de-Animal-Genetics-Engine]] — `EntityGenetics`-Attachment, DNA-Long-Codierung, Größenskalierung (`0.1x` - `3.0x`), Kümmerling-Indikatoren und Vererbungsmathematik.
* [[Genetik-API & Stammbaum|de_de-Genetics-API-and-Pedigree]] — `DasikAnimalGeneticsAPI`-Fassade, Stammbaum- und Verwandtschaftsberechnung, Inzuchtrisikovorhersage und dynamische Merkmalsmodifikatoren.
* [[Genetik-Beutemodifikatoren|de_de-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`, `GeneticsLootRegistry` und Drop-Abfanglogik via `LivingEntityLootMixin`.
* [[Anführer-Gefolge & Schwarmbildung|de_de-Leader-Follower-and-Flocking]] — `GroupMember`, `FollowLeaderGoal`, Flug- und Boden-Boids-Schwarmstrategien und `GroupParameters`-Gewichtungen.
* [[Dynamischer GameRules-Manager|de_de-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`, dynamische Registrierung, automatische englische Übersetzungsinjektion, fette Kategorie-Header (`§l`) und Mathe-Helfer.
* [[GameRule-Codec & Serialisierung|de_de-GameRule-Codec-and-Serialization]] — Validierung von Integer-GameRule-Grenzen (`Integer.MIN_VALUE` Fallback), verhindert Abstürze bei `SavedDataStorage.encodeUnchecked`.
* [[Client-GameRules & GUI-Helfer|de_de-Client-GameRule-and-GUI-Helpers]] — `ClientGameRuleHelper`-Serverabfragen, `GuiHelper`, atomarer JSON-Austausch in `ConfigHelper` und Sicherheit für dedizierte Server.
* [[Dynamische Verzauberungen & Sichtverfolgung|de_de-Dynamic-Enchantments-and-Vision]] — Laufzeiteinspeisung via `DynamicEnchantmentManager` und Frustum-Raycast-Prüfungen mit `PlayerVisionTracker`.
* [[Stochastik- & Mathe-Werkzeuge|de_de-Stochastic-and-Math-Utilities]] — XORSHIFT-Algorithmen in `FastRandom`, Promille-/Dezilstichproben in `StochasticUtil` und Tick-zu-Sekunden-Umrechnung ($20\text{ ticks} = 1\text{s}$).
* [[Veraltete Attributsbereinigung & Skalierung|de_de-Stale-Attribute-Purging-and-Scale]] — Bereinigungsregeln für Attributsmodifikatoren, `ADD_VALUE` `-1.0f` Basis-Offset-Mathematik und `genetics_`-Modifikatorsicherheit.
* [[ModVersionGuard & Start-Sicherheit|de_de-ModVersionGuard-and-Startup-Safety]] — Knot ClassLoader-Sicherheit (`Thread.currentThread().getContextClassLoader()`) und Schutz vor Vorabversionsabstürzen.

---

## 💻 Entwickler- & Technische Referenz

* [[Entwickler-Setup & Build|de_de-Developer-Setup-and-Building]] — JDK 25 Voraussetzungen, Gradle 9.3+, `./gradlew build --no-daemon` und `./gradlew test`.
* [[Architektur & Paketstruktur|de_de-Architecture-and-Package-Layout]] — Vollständiger ASCII-Paketbaum (`ai`, `api`, `config`, `core`, `mixin`, `util`) und Thread-Sicherheitsmodelle.
* [[Mixin-Referenz & Hooks|de_de-Mixin-Reference-and-Hooks]] — Detaillierte Mixin-Aufschlüsselungstabelle (`LanguageMixin`, `LivingEntityLootMixin`, `MobGoalAccessor`, `PathfinderMobMixin`, `ProfileTriggerMixin`).
* [[Verhaltensprofile & Bedingungen|de_de-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`, `BehaviorProfile`, `BehaviorCondition`, `ProfileAware` und `DefaultProfileBuilder`.
* [[Integrationsleitfaden für nachgelagerte Mods|de_de-Consumer-Mods-Integration-Guide]] — Leitfaden und Codebeispiele für Mods (*Better Dogs*, *Natural Reproduction*, *Collapsible Game Rule Screen*, *Bat Ecology*, *Ore Amplifier*).

---

## 🔗 Externe Links

* [[GitHub Repository|Home]]
* [Modrinth Project Page](https://modrinth.com/mod/dasik-library)
* [CurseForge Project Page](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
