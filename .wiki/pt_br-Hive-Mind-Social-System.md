# Sistema Social de Mente Coletiva

| Parâmetro do Sistema | Valor |
| :--- | :--- |
| **Classe Principal** | `net.dasik.social.core.GlobalSocialSystem` |
| **Classe de Registro** | `net.dasik.social.core.SocialRegistry` |
| **Frequência de Pulso** | `1 tick` (Regra do Imortal) |
| **Complexidade de Busca** | $O(1)$ HashMap Fragmentado |
| **Estratégia de Limpeza** | Purga automática via `isAlive()` e `isRemoved()` |

---

## ⚡ Visão Geral e a Regra do Imortal (Highlander Rule)

O **Sistema Social de Mente Coletiva** é o coração pulsante da Dasik Library. Em vez de centenas de entidades sociais executarem consultas espaciais pesadas individualmente a cada tick, a Dasik Library emprega um coordenador de pulsos global único (`GlobalSocialSystem`).

### 👑 A Regra do Imortal ("Só Pode Haver Um")
O `GlobalSocialSystem` assegura que apenas **um ciclo de pulso global** seja executado por tick de jogo do servidor ($20\text{ ticks} = 1\text{s}$). Se múltiplas threads ou subsistemas solicitarem pulsos no mesmo tick, o `GlobalSocialSystem` descarta as chamadas redundantes:

$$\text{Global Pulse Execution} = \begin{cases} \text{Execute Ticks}, & \text{if } \text{currentTick} > \text{lastTick} \\ \text{Skip (No-Op)}, & \text{if } \text{currentTick} \le \text{lastTick} \end{cases}$$

```ascii
[ Server Level Tick ]
         │
         ▼
 ┌──────────────────────┐
 │ GlobalSocialSystem   │  ◄── Highlander Rule Guard (1 tick per server tick)
 └──────────┬───────────┘
            │
            ▼
 ┌──────────────────────┐
 │   SocialRegistry     │  ◄── O(1) Shard Lookup & Dead Entity Purge
 └──────────┬───────────┘
            │
      ┌─────┴────────────────┐
      ▼                      ▼
 ┌──────────────┐      ┌──────────────┐
 │ Entity 1     │      │ Entity N     │
 │ Mood Task    │      │ Mood Task    │
 └──────────────┘      └──────────────┘
```

---

## 🗂️ Arquitetura do `SocialRegistry`

O `SocialRegistry` organiza as entidades sociais ativas por espécies e UUIDs dos mundos:

* **Registro $O(1)$**: Entidades se registram via `SocialRegistry.register(SocialEntity entity)`.
* **Purga Automática**: Referências inválidas (chunks descarregados, entidades mortas) são descartadas automaticamente no pulso checando `entity.dasik$asEntity().isAlive()` e `isRemoved()`.
* **Fragmentação por Espécie**: Entidades são agrupadas por `dasik$getSpeciesId()`, permitindo buscas espaciais ultra-rápidas para manilhas e bandos sem examinar todas as entidades do mundo.

---

## 💻 Exemplo de Código para Desenvolvedores

```java
// Registering an entity to the Hive Mind
public class CustomSocialMob extends PathfinderMob implements SocialEntity {
    private final EntitySocialScheduler scheduler = new EntitySocialScheduler();

    @Override
    public void tick() {
        super.tick();
        if (this.level() instanceof ServerLevel serverLevel) {
            SocialRegistry.register(this);
            GlobalSocialSystem.pulse(serverLevel);
        }
    }

    @Override
    public SocialScheduler dasik$getScheduler() {
        return this.scheduler;
    }
}
```

---

## 🔗 Páginas Relacionadas
* [[Agendador Social e Eventos|pt_br-Social-Scheduler-and-Events]]
* [[Arquitetura e Estrutura de Pacotes|pt_br-Architecture-and-Package-Layout]]
