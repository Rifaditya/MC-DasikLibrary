# Utilitaires Stochastiques & Mathématiques

| Utilitaire | Classe | Rôle |
| :--- | :--- | :--- |
| **Générateur Pseudo-Aléatoire** | `net.dasik.social.util.FastRandom` | Générateur XORSHIFT 64-bit non bloquant |
| **Échantillonnage de Probabilités**| `net.dasik.social.util.StochasticUtil` | Déciles (`1/10`), Pourcentages (`1/100`), Pour Mille (`1/1000`) |
| **Constante Temporelle** | $20\text{ ticks} = 1\text{ seconde}$ | Facteur de conversion temporel Minecraft |

---

## 🎲 Algorithme XORSHIFT dans `FastRandom`

`FastRandom` surpasse `java.util.Random` en évitant les synchronisations atomiques tout en offrant d'excellentes propriétés statistiques de distribution :

$$x \leftarrow x \oplus (x \ll 13); \quad x \leftarrow x \oplus (x \gg 7); \quad x \leftarrow x \oplus (x \ll 17)$$

---

## 📊 Échantillonnage Pratique avec `StochasticUtil`

`StochasticUtil` élimine les erreurs de troncature entière lors des calculs d'apparition et de tirage :

```java
// Roll probability out of 1000 (e.g. 5 permille = 0.5% chance)
if (StochasticUtil.rollPermille(random, 5)) {
    // Rare mutation triggered
}

// Roll percentage chance (0-100)
if (StochasticUtil.rollPercent(random, 25)) {
    // 25% chance branch
}
```

---

## 🔗 Pages Liées
* [[Moteur de Génétique Animale|fr_fr-Animal-Genetics-Engine]]
* [[Système Social d'Intelligence Collective|fr_fr-Hive-Mind-Social-System]]
