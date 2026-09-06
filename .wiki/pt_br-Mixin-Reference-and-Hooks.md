# Referência de Mixin e Pontos de Injeção

| Classe Mixin | Classe Alvo | Ponto de Injeção | Finalidade |
| :--- | :--- | :--- | :--- |
| `LanguageMixin` | `net.minecraft.locale.Language` | `@Inject` em `loadFromJson` (`RETURN`) | Injeção de traduções dinâmicas de GameRules |
| `LivingEntityLootMixin` | `LivingEntity` | `@ModifyVariable` em `dropFromLootTable` | Interceptação de saque para escala genética |
| `MobGoalAccessor` | `Mob` | `@Accessor("goalSelector")` | Acesso direto ao seletor de objetivos de IA |
| `PathfinderMobMixin` | `PathfinderMob` | `@Inject` em `<init>` (`RETURN`) | Gancho de registro de pulso na Mente Coletiva |
| `ProfileTriggerMixin` | `Entity` | `@Inject` em `teleportCrossDimension` (`RETURN`) | Disparo de perfis em viagens dimensionais |

---

## 🔍 Detalhamento dos Interceptores

### 1. `LanguageMixin`
Injeta os rótulos em inglês gerados por `DynamicGameRuleManager` diretamente nas tabelas de linguagem do Minecraft sem requerer arquivos `.json`.

### 2. `LivingEntityLootMixin`
Intercepta a rotina de drops de `dropFromLootTable`, delegando ao `GeneticsLootRegistry` o redimensionamento ou substituição dos saques com base no tamanho da criatura.

---

## 🔗 Páginas Relacionadas
* [[Arquitetura e Estrutura de Pacotes|pt_br-Architecture-and-Package-Layout]]
* [[Gerenciador Dinâmico de GameRules|pt_br-Dynamic-GameRules-Manager]]
