# GameRules do Cliente e Auxiliares de GUI

| Classe Utilitária | Ambiente | Finalidade |
| :--- | :--- | :--- |
| `ClientGameRuleHelper` | Cliente / Servidor Integrado | Consulta GameRules no servidor integrado pela thread cliente |
| `ConfigHelper` | Comum (Cliente/Servidor) | Carregamento, gravação e backup atômico de arquivos JSON |
| `GuiHelper` | Cliente | Auxiliares de telas de configuração ModMenu / Cloth Config |

---

## 🖥️ Proteção Contra Falhas de ClassLoader em Servidores Dedicados

Para assegurar 100% de integridade em servidores dedicados e evitar que classes de cliente quebrem a inicialização, todas as rotinas gráficas e helpers integrados contam com checagens tardias (`FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT`).

```ascii
                      DynamicGameRuleManager.getInt(level, ruleKey)
                                        │
                    ┌───────────────────┴───────────────────┐
                    ▼                                       ▼
          [ level instanceof ServerLevel ]       [ Client Environment ]
                    │                                       │
                    ▼                                       ▼
            Direct Level Lookup                   ClientGameRuleHelper
                                            (Queries Integrated Server)
```

---

## 📄 Trocas Atômicas de Arquivos JSON no `ConfigHelper`

O `ConfigHelper` implementa proteção contra perda de dados durante quedas de energia:

1. Grava os novos dados em arquivo temporário `config.json.tmp`.
2. Valida o formato JSON e confere o tamanho do arquivo.
3. Cria backup automático de segurança `config.json.bak`.
4. Conclui a substituição atômica `config.json.tmp` -> `config.json` com `Files.move(..., StandardCopyOption.ATOMIC_MOVE)`.

---

## 🔗 Páginas Relacionadas
* [[Gerenciador Dinâmico de GameRules|pt_br-Dynamic-GameRules-Manager]]
* [[Perfis de Comportamento e Condições|pt_br-Behavior-Profiles-and-Conditions]]
