# Portal Principal da Wiki - Dasik Library

[![Minecraft](https://img.shields.io/badge/Minecraft-26.2%2B-brightgreen.svg)](https://minecraft.net)
[![Fabric Loader](https://img.shields.io/badge/Fabric%20Loader-%3E%3D0.18.4-blue.svg)](https://fabricmc.net)
[![License](https://img.shields.io/badge/License-LGPL--3.0-orange.svg)](https://www.gnu.org/licenses/lgpl-3.0)
[![Version](https://img.shields.io/badge/DasikLibrary-v1.8.15-purple.svg)](https://modrinth.com/mod/dasik-library)

Bem-vindo à documentação técnica oficial da **Dasik Library**, o motor compartilhado de IA social com Mente Coletiva (Hive Mind), framework de genética, calculadora de revoadas Boids e infraestrutura dinâmica de GameRules para mods do Fabric Minecraft.

> 📌 **Aviso sobre o Código Fonte do Repositório**: A documentação nesta Wiki reflete o **estado atual do código fonte no repositório**, que pode incluir commits recentes não lançados ou recursos em desenvolvimento antes das versões públicas no CurseForge e Modrinth.

---

🌐 **Languages**: [[🇺🇸 English|Home]] | [[🇨🇳 简体中文|zh_cn-Home]] | [[🇭🇰 繁體中文|zh_tw-Home]] | [[🇷🇺 Русский|ru_ru-Home]] | [[🇪🇸 Español|es_es-Home]] | [[🇩🇪 Deutsch|de_de-Home]] | [[🇫🇷 Français|fr_fr-Home]] | [[🇧🇷 Português|pt_br-Home]] | [[🇯🇵 日本語|ja_jp-Home]] | [[🇮🇩 Bahasa Indonesia|id_id-Home]] | [[🇰🇷 한국어|ko_kr-Home]]

---

## 💡 Filosofia de Arquitetura

A Dasik Library opera sob dois princípios arquiteturais essenciais:

1. **"Thin Mod, Fat Library" (Mod Enxuto, Biblioteca Robusta)**: Mods consumidores modulares focam estritamente no conteúdo e no registro de entidades. Cálculos matemáticos complexos, agendamento de ticks, herança genética, vetores de direcionamento Boids e serialização de estado NBT são centralizados na Dasik Library.
2. **"One Brain, Many Minds" (Um Cérebro, Várias Mentes)**: A execução centralizada de pulsos via `GlobalSocialSystem` impõe a **Regra do Imortal (Highlander Rule)** (estritamente 1 ciclo de pulso global por tick do jogo) para gerenciar milhares de entidades ativas com baixíssimo impacto de desempenho.

---

## 📦 Diretório de Versões do Minecraft

* [[Guia MC 26.2|pt_br-Minecraft-26.2-Guide]] — Instalação e configuração para Minecraft 26.2+.
* [[Compatibilidade de Versões|pt_br-Version-Compatibility]] — Matriz do ciclo de vida de compatibilidade multiversão (`>=26.1.2-` a `26.2+`), segurança do Knot ClassLoader e regras de guarda de versão.

---

## 🎮 Matriz de Sistemas Principais e Mecânicas

Explore os guias técnicos e mecânicas completas:

* [[Sistema Social de Mente Coletiva|pt_br-Hive-Mind-Social-System]] — Motor de pulso único, Regra do Imortal, registro shard $O(1)$ `SocialRegistry` e orçamento de ticks.
* [[Agendador Social e Eventos|pt_br-Social-Scheduler-and-Events]] — `EntitySocialScheduler`, execução de via dupla Mood/Ambient, escalonamento `PriorityTier` e `SocialEventRegistry`.
* [[Motor de Genética Animal|pt_br-Animal-Genetics-Engine]] — Anexo `EntityGenetics`, codificação DNA long, limites de escala (`0.1x` - `3.0x`), indicadores de nanismo e matemática de herança.
* [[API de Genética e Pedigree|pt_br-Genetics-API-and-Pedigree]] — Fachada `DasikAnimalGeneticsAPI`, cálculo de árvore genealógica e parentesco, fórmulas de predição de consanguinidade e modificadores dinâmicos.
* [[Modificadores de Saque Genético|pt_br-Genetics-Loot-Modifiers]] — `GeneticsLootModifier`, `GeneticsLootRegistry` e lógica de interceptação de drops com `LivingEntityLootMixin`.
* [[Seguidor de Líder e Revoada Boids|pt_br-Leader-Follower-and-Flocking]] — `GroupMember`, `FollowLeaderGoal`, estratégias de revoada Boids aéreas e terrestres e pesos de `GroupParameters`.
* [[Gerenciador Dinâmico de GameRules|pt_br-Dynamic-GameRules-Manager]] — `DynamicGameRuleManager`, registro dinâmico, injeção automática de traduções em inglês, cabeçalhos em negrito (`§l`) e helpers matemáticos.
* [[Codec e Serialização de GameRules|pt_br-GameRule-Codec-and-Serialization]] — Validação de limites inteiros de GameRule (`Integer.MIN_VALUE` fallback), prevenindo falhas em `SavedDataStorage.encodeUnchecked`.
* [[GameRules do Cliente e Auxiliares de GUI|pt_br-Client-GameRule-and-GUI-Helpers]] — Consultas ao servidor integrado via `ClientGameRuleHelper`, `GuiHelper`, substituição atômica de JSON em `ConfigHelper` e segurança para servidores dedicados.
* [[Encantamentos Dinâmicos e Rastreador de Visão|pt_br-Dynamic-Enchantments-and-Vision]] — Injeção em tempo de execução com `DynamicEnchantmentManager` e checagem de cone visual com `PlayerVisionTracker`.
* [[Utilitários Estocásticos e Matemáticos|pt_br-Stochastic-and-Math-Utilities]] — Algoritmos XORSHIFT no `FastRandom`, sorteios por milhar/decis no `StochasticUtil` e conversão de ticks em segundos ($20\text{ ticks} = 1\text{s}$).
* [[Purga de Atributos Obsoletos e Escala|pt_br-Stale-Attribute-Purging-and-Scale]] — Regras de purga de modificadores de atributos, matemática de deslocamento base `ADD_VALUE` `-1.0f` e segurança de identificadores `genetics_`.
* [[ModVersionGuard e Segurança de Inicialização|pt_br-ModVersionGuard-and-Startup-Safety]] — Segurança do Knot ClassLoader (`Thread.currentThread().getContextClassLoader()`) e prevenção de travamentos na inicialização.

---

## 💻 Referência Técnica e de Desenvolvedor

* [[Configuração de Desenvolvedor e Compilação|pt_br-Developer-Setup-and-Building]] — Pré-requisitos de JDK 25, Gradle 9.3+, `./gradlew build --no-daemon` e `./gradlew test`.
* [[Arquitetura e Estrutura de Pacotes|pt_br-Architecture-and-Package-Layout]] — Árvore completa de pacotes em ASCII (`ai`, `api`, `config`, `core`, `mixin`, `util`) e modelos de concorrência segura.
* [[Referência de Mixin e Pontos de Injeção|pt_br-Mixin-Reference-and-Hooks]] — Tabela detalhada de Mixins (`LanguageMixin`, `LivingEntityLootMixin`, `MobGoalAccessor`, `PathfinderMobMixin`, `ProfileTriggerMixin`).
* [[Perfis de Comportamento e Condições|pt_br-Behavior-Profiles-and-Conditions]] — `BehaviorProfileManager`, `BehaviorProfile`, `BehaviorCondition`, `ProfileAware` e `DefaultProfileBuilder`.
* [[Guia de Integração para Mods Consumidores|pt_br-Consumer-Mods-Integration-Guide]] — Guia de integração e trechos de código para mods (*Better Dogs*, *Natural Reproduction*, *Collapsible Game Rule Screen*, *Bat Ecology*, *Ore Amplifier*).

---

## 🔗 Links Externos

* [[GitHub Repository|Home]]
* [Modrinth Project Page](https://modrinth.com/mod/dasik-library)
* [CurseForge Project Page](https://www.curseforge.com/minecraft/mc-mods/dasik-library)
