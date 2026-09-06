# Gerenciador Dinâmico de GameRules

| Componente | Classe |
| :--- | :--- |
| **Classe Gerenciadora** | `net.dasik.social.api.gamerule.DynamicGameRuleManager` |
| **Formato de Namespace da Chave** | `modid:rule_name` |
| **Injeção de Tradução** | `LanguageMixin` -> Mapa `GENERATED_TRANSLATIONS` |
| **Formato de Cabeçalho de Categoria** | `§lCategory Title (N rules)` |

---

## 🎲 Visão Geral e GameRules com Namespace

O `DynamicGameRuleManager` permite que mods criem infinitas GameRules dinamicamente sem necessidade de inserções manuais em arquivos `en_us.json`.

### Recursos Notáveis
1. **Injeção Automática de Tradução**: Converte automaticamente `modid:rule_name` em títulos amigáveis (ex: `bd_enable_guard_mode` -> `BD Enable Guard Mode`) inserindo no dicionário de idioma via `LanguageMixin`.
2. **Cabeçalhos em Negrito (`§l`)**: Categorias cadastradas via `registerCategory` recebem destaque em negrito para organização limpa na tela de opções.
3. **Helpers Matemáticos de Conversão**:
   - `getPct(Level level, GameRule<Integer> rule)` — Converte valor inteiro (`0-100`) para double (`/ 100.0`).
   - `getChance(Level level, GameRule<Integer> rule)` — Converte percentual em float (`/ 100.0f`).
   - `getProb(Level level, GameRule<Integer> rule)` — Converte por milhar (`0-1000`) em float (`/ 1000.0f`).
   - `getDecileFloat(Level level, GameRule<Integer> rule)` — Converte decis em float (`/ 10.0f`).
   - `getIntVal(Level level, String key, int defaultValue)` — Busca valor inteiro via string da chave.

---

## 💻 Exemplo de Código para Desenvolvedores

```java
// Register a dynamic boolean GameRule with description
GameRule<Boolean> ENABLE_GUARD = DynamicGameRuleManager.booleanRule(
    "betterdogs:bd_enable_guard_mode",
    GameRuleCategory.MOBS,
    true
).description("Enable wolf sentinel guard mode").register();

// Querying GameRule safely across client/server
boolean isGuardEnabled = DynamicGameRuleManager.getBoolean(level, ENABLE_GUARD);
```

---

## 🔗 Páginas Relacionadas
* [[Codec e Serialização de GameRules|pt_br-GameRule-Codec-and-Serialization]]
* [[GameRules do Cliente e Auxiliares de GUI|pt_br-Client-GameRule-and-GUI-Helpers]]
