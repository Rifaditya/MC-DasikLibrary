# Verhaltensprofile & Bedingungen

| Komponente | Klasse |
| :--- | :--- |
| **Profilmanager** | `net.dasik.social.api.profile.BehaviorProfileManager` |
| **Profil-Interface** | `net.dasik.social.api.profile.BehaviorProfile` |
| **Bedingungs-Interface** | `net.dasik.social.api.profile.BehaviorCondition` |
| **Interface-Kennzeichnung**| `net.dasik.social.api.profile.ProfileAware` |

---

## 🎭 Übersicht & Dynamische Zustandsautomaten

`BehaviorProfileManager` gestattet Kreaturen das dynamische Umschalten von KI-Zielen abhängig von Umweltbedingungen (Nachtzeit, niedrige Lebenspunkte, Rudelführerschaft, Wetter).

```ascii
[ LivingEntity Tick ]
         │
         ▼
┌─────────────────────────────┐
│    BehaviorProfileManager   │  ◄── Evaluate Conditions
└────────┬────────────┬───────┘
         │            │
         ▼            ▼
   [ Night Profile ] [ Combat Profile ]
```

---

## 💻 Entwickler-Codebeispiel

```java
// Create a profile using DefaultProfileBuilder
BehaviorProfile netherProfile = new DefaultProfileBuilder("nether_hunter")
    .priority(10)
    .condition(BehaviorCondition.inDimension(Level.NETHER))
    .goals(configurator -> configurator.add(2, new FollowLeaderGoal<>(mob, GroupParameters.DEFAULT_TERRESTRIAL, 16.0D)))
    .build();

// Register profile on manager instance
BehaviorProfileManager manager = new BehaviorProfileManager();
manager.registerProfile(netherProfile);
```

---

## 🔗 Verwandte Seiten
* [[Sozialer Planer & Ereignisse|de_de-Social-Scheduler-and-Events]]
* [[Architektur & Paketstruktur|de_de-Architecture-and-Package-Layout]]
