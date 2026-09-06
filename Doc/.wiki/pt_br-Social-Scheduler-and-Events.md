# Agendador Social e Sistema de Eventos

| Componente | Classe |
| :--- | :--- |
| **Motor de Agendamento** | `net.dasik.social.core.EntitySocialScheduler` |
| **Registro de Eventos** | `net.dasik.social.api.SocialEventRegistry` |
| **Níveis de Prioridade** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **Tipos de Sinal** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

---

## 🔄 Modelo de Agendamento em Via Dupla

Cada criatura social possui um `EntitySocialScheduler` encarregado de processar tarefas paralelas em duas vias:

1. **Mood Track (Via Emocional/Longo Prazo)**: Avaliada em menor frequência (a cada 20-100 ticks) para atualizar o estado emocional, agressividade ou estamina.
2. **Ambient Track (Via Tática Ambiental/Curto Prazo)**: Avaliada em alta frequência (a cada 1-5 ticks) para resposta instantânea a sinais, predadores ou comandos do bando.

```ascii
                      ┌───────────────────────────────┐
                      │    EntitySocialScheduler      │
                      └───────────────┬───────────────┘
                                      │
              ┌───────────────────────┴───────────────────────┐
              ▼                                               ▼
   ┌─────────────────────┐                         ┌─────────────────────┐
   │     Mood Track      │                         │    Ambient Track    │
   │ (Low Frequency Ticks)│                         │(High Frequency Ticks)│
   │ - Pack Hierarchy   │                         │ - Obstacle Avoidance│
   │ - Hunger / Fatigue  │                         │ - Signal Response   │
   └─────────────────────┘                         └─────────────────────┘
```

---

## 📢 `SocialEventRegistry` e Implementação de Eventos

Mods criam implementações de `SocialEvent` e registram instâncias no `SocialEventRegistry` durante a inicialização:

```java
public class HowlEvent implements SocialEvent {
    @Override public String getId() { return "betterdogs:howl"; }
    @Override public int getPriorityValue() { return 80; }
    @Override public String getTrackId() { return "pack_command"; }
    @Override public boolean canPreempt(SocialEvent other) { return other.getPriorityValue() < 80; }
    @Override public void onStart(TickContext context) {}
    @Override public boolean tick(TickContext context) { return false; }
    @Override public void onEnd(SocialEntity entity, EndReason reason) {}
}

// Register during mod initialization (frozen on first pulse)
SocialEventRegistry.register(new HowlEvent());
```

---

## 📊 Matriz de Prioridade PriorityTier

| Nível | Vias Máximas | Política de Alocação | Utilização Típica |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | Substituição imediata, prioridade máxima | Fuga de dano fatal, combate crítico |
| `HIGH` | `8` | Interrompe tarefas normais ou baixas | Chamados de matilha, ordens táticas |
| `NORMAL` | `16` | Orçamento padrão de ticks | Convívio social, caminhada |
| `LOW` | `32` | Capacidade máxima, prioridade de fundo | Checagem ambiental em segundo plano |

---

## 🔗 Páginas Relacionadas
* [[Sistema Social de Mente Coletiva|pt_br-Hive-Mind-Social-System]]
* [[Perfis de Comportamento e Condições|pt_br-Behavior-Profiles-and-Conditions]]
