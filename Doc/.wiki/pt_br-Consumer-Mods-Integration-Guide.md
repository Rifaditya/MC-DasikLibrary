# Guia de Integração para Mods Consumidores

| Mod Consumidor | Destaques da Integração |
| :--- | :--- |
| **Better Dogs** | Dinâmica de matilha, comandos com berrante, escala física e nanismo |
| **Natural Reproduction** | Pedigree e parentesco, previsão de consanguinidade, escalas de porte |
| **Collapsible Game Rule Screen** | Cabeçalhos em negrito (`§l`), registro dinâmico de GameRules |
| **Bat Ecology** | Estratégias de revoada em espiral aérea (`FlockType.AERIAL`) |
| **Ore Amplifier** | Geração estocástica proporcional de minérios (`StochasticUtil`) |

---

## 🛠️ Checklist Passo a Passo de Integração

1. **Declarar Dependência**: Adicione `"dasik-library": "*"` no `fabric.mod.json`.
2. **Implementar Interfaces**: Implemente `SocialEntity` ou `GroupMember` em suas criaturas.
3. **Registrar GameRules Dinâmicas**: Invoque `DynamicGameRuleManager.registerBoolean` / `registerInt` na inicialização do mod.
4. **Utilizar Fachada de Genética**: Consulte atributos de tamanho e parentesco com `DasikAnimalGeneticsAPI`.

---

## 🔗 Páginas Relacionadas
* [[Configuração de Desenvolvedor e Compilação|pt_br-Developer-Setup-and-Building]]
* [[Arquitetura e Estrutura de Pacotes|pt_br-Architecture-and-Package-Layout]]
