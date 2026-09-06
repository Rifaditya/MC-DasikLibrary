# Главная страница Wiki - Dasik Library

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

Добро пожаловать в официальную техническую документацию **Dasik Library** — общего движка социального ИИ коллективного разума (Hive Mind), фреймворка генетики, калькулятора стайного поведения Boids и инфраструктуры динамических GameRules для модов Fabric Minecraft.

> 📌 **Предупреждение об исходном коде репозитория**: Документация в этой Wiki отражает **текущее состояние исходного кода в репозитории**, которое может содержать недавние невыпущенные коммиты или разрабатываемые функции, опережающие общедоступные релизные сборки на CurseForge и Modrinth.

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 Архитектурная философия

Dasik Library работает в соответствии с двумя фундаментальными архитектурными принципами:

1. **"Thin Mod, Fat Library" (Тонкий мод, толстая библиотека)**: Модульные моды-потребители фокусируются исключительно на контенте сущностей и их регистрации. Сложная математика, планирование тиков, генетическое наследование, расчет векторов руления Boids и сериализация NBT-состояния централизованы в Dasik Library.
2. **"One Brain, Many Minds" (Один мозг, множество разумов)**: Централизованное импульсное выполнение через `GlobalSocialSystem` строго соблюдает **Правило Горца (Highlander Rule)** (ровно 1 глобальный цикл тика за игровой тик), управляя тысячами активных сущностей с минимальной нагрузкой на производительность.

---

## 📦 Каталог версий Minecraft

* [[Руководство по MC 26.2|ru_ru-Minecraft-26.2-Guide]] — Установка и настройка для Minecraft 26.2+.
* [[Совместимость версий|ru_ru-Version-Compatibility]] — Матрица жизненного цикла многоверсионной совместимости (`>=26.1.2-` до `26.2+`), безопасность Knot ClassLoader и правила защиты версий.

---

## 🎮 Матрица основных систем и механик

Ознакомьтесь с полным списком технических руководств по механикам:

* [[Социальная система коллективного разума|ru_ru-Hive-Mind-Social-System]] — Одноимпульсный движок, Правило Горца, шардированный $O(1)$ реестр `SocialRegistry` и бюджет тиков.
* [[Социальный планировщик и события|ru_ru-Social-Scheduler-and-Events]] — `EntitySocialScheduler`, двухколейное выполнение Mood/Ambient, уровни `PriorityTier` и `SocialEventRegistry`.
* [[Движок генетики животных|ru_ru-Animal-Genetics-Engine]] — Вложение `EntityGenetics`, кодирование DNA long, границы масштабирования (`0.1x` - `3.0x`), индикаторы недомерков и математика наследования.
* [[API генетики и родословная|ru_ru-Genetics-API-and-Pedigree]] — Фасад `DasikAnimalGeneticsAPI`, вычисление родословной и родства, формулы предсказания риска инбридинга и динамические модификаторы.
* [[Модификаторы добычи генетики|ru_ru-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`, `GeneticsLootRegistry` и логика перехвата дропа через `LivingEntityLootMixin`.
* [[Следование за лидером и стайное поведение|ru_ru-Leader-Follower-and-Flocking]] — `GroupMember`, `FollowLeaderGoal`, воздушные и наземные стратегии Boids и параметры весов `GroupParameters`.
* [[Менеджер динамических GameRules|ru_ru-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`, динамическая регистрация, автоинъекция английских переводов, жирные заголовки категорий (`§l`) и математические помощники.
* [[Codec и сериализация GameRules|ru_ru-GameRule-Codec-and-Serialization]] — Валидация границ целочисленных GameRules (`Integer.MIN_VALUE` fallback), предотвращающая краши `SavedDataStorage.encodeUnchecked`.
* [[Клиентские GameRules и помощники GUI|ru_ru-Client-GameRule-and-GUI-Helpers]] — Запросы к встроенному серверу через `ClientGameRuleHelper`, `GuiHelper`, атомарная замена JSON в `ConfigHelper` и безопасность выделенного сервера.
* [[Динамические зачарования и трекер поля зрения|ru_ru-Dynamic-Enchantments-and-Vision]] — Инъекция во время выполнения через `DynamicEnchantmentManager` и лучевые проверки фрустума `PlayerVisionTracker`.
* [[Стохастические и математические утилиты|ru_ru-Stochastic-and-Math-Utilities]] — Алгоритмы XORSHIFT в `FastRandom`, броски промилле/децилей в `StochasticUtil` и конверсия тиков в секунды ($20\text{ ticks} = 1\text{s}$).
* [[Очистка устаревших атрибутов и масштабирование|ru_ru-Stale-Attribute-Purging-and-Scale]] — Правила очистки модификаторов атрибутов, смещение базы `ADD_VALUE` `-1.0f` и безопасность модификаторов `genetics_`.
* [[ModVersionGuard и безопасность при запуске|ru_ru-ModVersionGuard-and-Startup-Safety]] — Безопасность Knot ClassLoader (`Thread.currentThread().getContextClassLoader()`) и предотвращение предрелизных крашей запуска.

---

## 💻 Справочник для разработчиков

* [[Настройка среды разработчика и сборка|ru_ru-Developer-Setup-and-Building]] — Требования JDK 25, Gradle 9.3+, `./gradlew build --no-daemon` и `./gradlew test`.
* [[Архитектура и структура пакетов|ru_ru-Architecture-and-Package-Layout]] — Полное дерево пакетов ASCII (`ai`, `api`, `config`, `core`, `mixin`, `util`) и модели потокобезопасности.
* [[Справочник Mixin и точки внедрения|ru_ru-Mixin-Reference-and-Hooks]] — Детальная таблица Mixin (`LanguageMixin`, `LivingEntityLootMixin`, `MobGoalAccessor`, `PathfinderMobMixin`, `ProfileTriggerMixin`).
* [[Профили поведения и условия|ru_ru-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`, `BehaviorProfile`, `BehaviorCondition`, `ProfileAware` и `DefaultProfileBuilder`.
* [[Руководство по интеграции модов-потребителей|ru_ru-Consumer-Mods-Integration-Guide]] — Руководство по интеграции и примеры кода для модов (*Better Dogs*, *Natural Reproduction*, *Collapsible Game Rule Screen*, *Bat Ecology*, *Ore Amplifier*).

---

## 🔗 Внешние ссылки

* [[GitHub Repository|Home]]
* [Modrinth Project Page](https://modrinth.com/mod/dasik-library)
* [CurseForge Project Page](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
