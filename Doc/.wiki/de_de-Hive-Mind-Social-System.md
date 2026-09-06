# Schwarmintelligenz-Sozialsystem

| Systemparameter | Wert |
| :--- | :--- |
| **Hauptklasse** | `net.dasik.social.core.GlobalSocialSystem` |
| **Registerklasse** | `net.dasik.social.core.SocialRegistry` |
| **Impulsfrequenz** | `1 tick` (Highlander-Regel) |
| **Suchzeit-Komplexität** | $O(1)$ Geshardete HashMap |
| **Speicherbereinigungsstrategie**| Automatische Bereinigung via `isAlive()` und `isRemoved()` |

---

## ⚡ Übersicht & Highlander-Regel

Das **Schwarmintelligenz-Sozialsystem** ist der zentrale Taktgeber der Dasik Library. Anstatt dass Hunderte von sozialen Entitäten jeden Tick unabhängig schwere Raumabfragen ausführen, setzt Dasik Library auf einen einzigen globalen Impulskoordinator (`GlobalSocialSystem`).

### 👑 Die Highlander-Regel ("Es kann nur einen geben")
`GlobalSocialSystem` erzwingt, dass pro Server-Tick ($20\text{ ticks} = 1\text{s}$) exakt **ein globaler Impulszyklus** läuft. Versuchen mehrere Threads oder Teilsysteme innerhalb desselben Ticks einen Impuls auszulösen, ignoriert `GlobalSocialSystem` mehrfache Aufrufe:

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

## 🗂️ Architektur der `SocialRegistry`

Die `SocialRegistry` verwaltet aktive soziale Kreaturen unterteilt nach Spezies und Welt-UUIDs:

* **$O(1)$ Registrierung**: Entitäten registrieren sich über `SocialRegistry.register(SocialEntity entity)`.
* **Automatische Bereinigung**: Veraltete Referenzen (entladene Chunks, getötete Mobs) werden während des Impulszyklus über `entity.dasik$asEntity().isAlive()` und `isRemoved()` selbsttätig entfernt.
* **Spezies-Sharding**: Entitäten werden nach `dasik$getSpeciesId()` gruppiert, was blitzschnelle lokale Nachbarschaftsabfragen für Rudelmechaniken und Schwarmbildung ermöglicht, ohne die gesamte Welt abzusuchen.

---

## 💻 Entwickler-Codebeispiel

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

## 🔗 Verwandte Seiten
* [[Sozialer Planer & Ereignisse|de_de-Social-Scheduler-and-Events]]
* [[Architektur & Paketstruktur|de_de-Architecture-and-Package-Layout]]
