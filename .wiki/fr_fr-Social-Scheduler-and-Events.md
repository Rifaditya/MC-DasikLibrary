# Planificateur Social et Système d'Événements

| Composant | Classe |
| :--- | :--- |
| **Moteur de Planification** | `net.dasik.social.core.EntitySocialScheduler` |
| **Registre d'Événements** | `net.dasik.social.api.SocialEventRegistry` |
| **Niveaux de Priorité** | `PriorityTier` (`CRITICAL`, `HIGH`, `NORMAL`, `LOW`) |
| **Types de Signaux** | `SignalType` (`DANGER`, `OWNER_ACTION`, `THUNDER`, `DEATH_CRY`, `FOOD_DETECTED`, `SOCIAL_INVITE`) |

---

## 🔄 Modèle de Planification à Double Piste

Chaque entité sociale dispose d'un `EntitySocialScheduler` qui traite les tâches sur deux pistes distinctes :

1. **Piste Humeur (Mood Track - États à long terme)** : Évaluée à basse fréquence (ex. tous les 20 à 100 ticks) pour actualiser le tempérament, la faim ou l'état de fatigue général.
2. **Piste Ambiance (Ambient Track - Réactions tactiques immédiates)** : Évaluée à haute fréquence (tous les 1 à 5 ticks) pour réagir instantanément aux stimuli sensoriels, dangers ou ajustements de nuée.

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

## 📢 `SocialEventRegistry` & Enregistrement d'Événements

Les mods implémentent l'interface `SocialEvent` et enregistrent leurs instances à l'initialisation :

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

## 📊 Matrice des Niveaux de Priorité

| Niveau | Pistes Max | Politique d'Éviction | Utilisation Typique |
| :--- | :--- | :--- | :--- |
| `CRITICAL` | `2` | Éviction immédiate, priorité absolue | Fuite face au danger mortel, combat |
| `HIGH` | `8` | Interrompt les tâches normales/basses | Ordres de meute, signaux tactiques |
| `NORMAL` | `16` | Budget d'exécution standard | Interaction sociale, jeu, vagabondage |
| `LOW` | `32` | Capacité maximale, arrière-plan | Observation passive de l'environnement |

---

## 🔗 Pages Liées
* [[Système Social d'Intelligence Collective|fr_fr-Hive-Mind-Social-System]]
* [[Profils de Comportement et Conditions|fr_fr-Behavior-Profiles-and-Conditions]]
