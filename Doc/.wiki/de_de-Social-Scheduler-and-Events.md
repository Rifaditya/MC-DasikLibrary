# Sozialer Planer & Ereignissystem

| Komponente | Klasse |
| :--- | :--- |
| **Scheduler-Engine** | `net.dasik.social.core.EntitySocialScheduler` |
| **Ereignis-Registry** | `net.dasik.social.api.SocialEventRegistry` |
| **Prioritätsstufen** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **Signalmodi** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

---

## 🔄 Zweispuriges Scheduling-Modell

Jede soziale Entität verfügt über einen `EntitySocialScheduler`, der Aufgaben auf zwei parallelen Spuren abarbeitet:

1. **Mood Track (Langfristige Verhaltenszustände)**: Wird mit geringerer Frequenz (z. B. alle 20-100 Ticks) evaluiert, um das allgemeine Gemüt, Aggression oder Ausdauer zu aktualisieren.
2. **Ambient Track (Kurzfristige taktische Signale)**: Wird mit hoher Frequenz (alle 1-5 Ticks) evaluiert, um sofort auf Reize, Gefahren oder Schwarmjustierungen zu reagieren.

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

## 📢 `SocialEventRegistry` & Ereignisimplementierung

Mods implementieren `SocialEvent` und registrieren Instanzen bei der Initialisierung:

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

## 📊 Prioritätsstufen-Matrix

| Stufe | Max. Spuren | Zuweisungsrichtlinie | Typische Verwendung |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | Sofortige Übernahme, höchste Priorität | Flucht vor Gefahren, Kampf |
| `HIGH` | `8` | Unterbricht normale/niedrige Aufgaben | Taktische Befehle, Rudelrufe |
| `NORMAL` | `16` | Standard-Ausführungsbudget | Soziale Interaktion, Umherstreifen |
| `LOW` | `32` | Maximale Kapazität, Hintergrund | Hintergrundbeobachtung der Umgebung |

---

## 🔗 Verwandte Seiten
* [[Schwarmintelligenz-Sozialsystem|de_de-Hive-Mind-Social-System]]
* [[Verhaltensprofile & Bedingungen|de_de-Behavior-Profiles-and-Conditions]]
