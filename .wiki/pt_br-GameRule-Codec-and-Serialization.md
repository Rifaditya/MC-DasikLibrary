# Codec e Serialização de GameRules

| Tópico | Especificação |
| :--- | :--- |
| **Falha de Serialização** | Crash em `SavedDataStorage.encodeUnchecked` durante salvamento |
| **Versão Corrigida** | `1.8.15` |
| **Limites de Inteiro** | `min = Integer.MIN_VALUE` (ou `Math.min(Integer.MIN_VALUE, defaultValue)`) |

---

## 🛠️ Correção de Codec para GameRules Inteiras

No Minecraft 26.2, GameRules dinâmicas são gravadas em `level.dat` / `game_rules.dat` através do `SavedDataStorage.encodeUnchecked`. Versões anteriores sem limites mínimos explícitos forçavam `min = 0`, lançando `IllegalStateException` ao serializar valores negativos ou padrões.

### Correção no `DynamicGameRuleManager`

```java
// IntegerBuilder initialization fix in DynamicGameRuleManager
public class IntegerBuilder {
    private int min = Integer.MIN_VALUE;
    private int max = Integer.MAX_VALUE;

    public GameRule<Integer> register() {
        int effectiveMin = Math.min(min, defaultValue);
        int effectiveMax = Math.max(max, defaultValue);
        // Uses Codec.INT.intRange(effectiveMin, effectiveMax) to prevent encodeUnchecked bounds check failure
        GameRule<Integer> rule = new GameRule<>(
            GameRuleType.INT, defaultValue, Codec.INT.intRange(effectiveMin, effectiveMax), 
            FeatureFlagSet.of(), category, visitor
        );
        return Registry.register(BuiltInRegistries.GAME_RULE, Identifier.parse(ruleName), rule);
    }
}
```

---

## 🔒 Segurança de Namespaces no NBT

Sempre inclua namespace em suas GameRules com `modid:rule_name` (`[ERR-20260510-002]`). Regras sem namespace não são gravadas corretamente nos arquivos do mundo, resultando em perda de configurações ao reiniciar o servidor.

---

## 🔗 Páginas Relacionadas
* [[Gerenciador Dinâmico de GameRules|pt_br-Dynamic-GameRules-Manager]]
* [[GameRules do Cliente e Auxiliares de GUI|pt_br-Client-GameRule-and-GUI-Helpers]]
