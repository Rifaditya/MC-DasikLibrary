# Système Social d'Intelligence Collective

| Paramètre Système | Valeur |
| :--- | :--- |
| **Classe Principale** | `net.dasik.social.core.GlobalSocialSystem` |
| **Classe de Registre** | `net.dasik.social.core.SocialRegistry` |
| **Fréquence d'Impulsion** | `1 tick` (Règle Highlander) |
| **Complexité de Recherche** | $O(1)$ HashMap Partitionnée |
| **Stratégie de Purge Mémoire** | Nettoyage automatique via `isAlive()` et `isRemoved()` |

---

## ⚡ Présentation & Règle Highlander

Le **Système Social d'Intelligence Collective (Hive Mind)** est le moteur central d'orchestration de Dasik Library. Au lieu de laisser des centaines d'entités sociales exécuter indépendamment des requêtes spatiales lourdes à chaque tick, Dasik Library utilise un coordinateur unique à impulsion globale (`GlobalSocialSystem`).

### 👑 La Règle Highlander (« Il ne peut en rester qu'un »)
`GlobalSocialSystem` garantit qu'exactement **un cycle d'impulsion global** est exécuté par tick de serveur ($20\text{ ticks} = 1\text{s}$). Si plusieurs threads ou sous-systèmes tentent de déclencher une pulsation dans le même tick, `GlobalSocialSystem` ignore les appels redondants :

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

## 🗂️ Architecture de `SocialRegistry`

La `SocialRegistry` organise les entités sociales actives par espèce et identifiant d'univers (UUID) :

* **Enregistrement en $O(1)$** : Les entités s'enregistrent via `SocialRegistry.register(SocialEntity entity)`.
* **Purge Automatique** : Les références périmées (chunks déchargés, entités mortes) sont éliminées pendant la boucle d'impulsion via `entity.dasik$asEntity().isAlive()` et `isRemoved()`.
* **Partitionnement par Espèce** : Les entités sont indexées par `dasik$getSpeciesId()`, ce qui permet des requêtes de voisinage ultra-rapides pour les meutes et nuées sans parcourir le monde entier.

---

## 💻 Exemple de Code Développeur

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

## 🔗 Pages Liées
* [[Planificateur Social et Événements|fr_fr-Social-Scheduler-and-Events]]
* [[Architecture et Structure des Paquets|fr_fr-Architecture-and-Package-Layout]]
