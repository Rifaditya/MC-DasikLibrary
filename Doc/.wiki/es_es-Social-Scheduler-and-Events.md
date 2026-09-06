# Programador Social y Sistema de Eventos

| Componente | Clase |
| :--- | :--- |
| **Motor de Programación** | `net.dasik.social.core.EntitySocialScheduler` |
| **Registro de Eventos** | `net.dasik.social.api.SocialEventRegistry` |
| **Niveles de Prioridad** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **Modos de Señal** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

---

## 🔄 Modelo de Programación de Doble Vía

Cada entidad social contiene un `EntitySocialScheduler` que procesa tareas en dos vías independientes:

1. **Mood Track (Vía de Estado Emocional/Largo Plazo)**: Se evalúa a menor frecuencia (por ejemplo, cada 20-100 ticks) para actualizar el estado anímico, la agresividad o la energía.
2. **Ambient Track (Vía Táctica Ambiental/Corto Plazo)**: Se evalúa a alta frecuencia (cada 1-5 ticks) para reaccionar inmediatamente a señales, peligros o ajustes de formación.

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

## 📢 `SocialEventRegistry` e Implementación de Eventos

Los mods implementan `SocialEvent` y registran instancias en `SocialEventRegistry` durante la inicialización:

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

## 📊 Matriz de Prioridad PriorityTier

| Nivel | Vías Máximas | Política de Asignación | Uso Habitual |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | Reemplazo inmediato, prioridad máxima | Huida de peligros, combate |
| `HIGH` | `8` | Desplaza tareas normales o bajas | Comandos tácticos, llamadas de manada |
| `NORMAL` | `16` | Presupuesto de ejecución estándar | Interacciones sociales, deambular |
| `LOW` | `32` | Capacidad máxima, baja prioridad | Inspección ambiental de fondo |

---

## 🔗 Páginas Relacionadas
* [[Sistema Social de Mente Colmena|es_es-Hive-Mind-Social-System]]
* [[Perfiles de Comportamiento y Condiciones|es_es-Behavior-Profiles-and-Conditions]]
