# Matrice de Compatibilité des Versions

| Version Minecraft Cible | Version Bibliothèque | Garde de Version Mod | Spécification Dépendance | Statut de Support |
| :--- | :--- | :--- | :--- | :--- |
| **Minecraft 26.2+** | `1.8.15` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | **Branche Active Principale** |
| **Minecraft 26.1.2** | `1.8.9` | Knot ClassLoader Verified | `"minecraft": ">=26.1.2-"` | Rétroportage / Stable |
| **Minecraft 1.21.x** | *Obsolète* | *N/A* | *Legacy 1.x* | **Fin de Cycle de Vie (EOL)** |

> 📌 **Avis sur le Dépôt Source** : La documentation de ce wiki reflète l'**état actuel du code source du dépôt**, qui peut inclure des commits récents et non publiés ou des fonctionnalités en cours de développement précédant les versions publiques sur CurseForge et Modrinth.

---

## 🛡️ Politique « 1 Jar 1 Version » et Compatibilité Ascendante

Dasik Library applique la politique **1 Jar 1 Version** combinée avec une **compatibilité ascendante ouverte** :

1. **Bornes Ouvertes (`"minecraft": ">=26.1.2-"`)** : La bibliothèque spécifie une borne inférieure ouverte dans `fabric.mod.json`, permettant à Fabric Loader d'accepter le JAR sur les mises à jour mineures sans bloquer les joueurs.
2. **Sécurité Knot ClassLoader (`ModVersionGuard`)** : Durant `onInitialize()`, `ModVersionGuard.checkClass` valide la présence des classes requises via `Thread.currentThread().getContextClassLoader()`, évitant les crashs silencieux de la JVM en levant une erreur claire et explicite.

```java
public final class ModVersionGuard {
    public static void checkClass(String modName, String requiredClassName) {
        try {
            Class.forName(requiredClassName, true, Thread.currentThread().getContextClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("\n" +
                "=====================================================================\n" +
                " [" + modName + "] Minecraft API Mismatch!\n" +
                " A required Minecraft class or API was not found in your game version.\n" +
                " Try updating your Minecraft version or download a matching build.\n" +
                "=====================================================================");
        }
    }
}
```

---

## 🚫 Règle de Non-Confusion des Versions

Conformément à la directive stricte `[DIR-20260614-001]`, **les versions millésimées Minecraft 26.x ne doivent JAMAIS être confondues avec les versions 1.21.x de 2024** :
* ❌ `26.2 (1.21.4)` — Strictement Interdit.
* ✅ `Minecraft 26.2` — Notation souveraine standard.

---

## 🔗 Pages Liées
* [[Guide MC 26.2|fr_fr-Minecraft-26.2-Guide]]
* [[ModVersionGuard et Sécurité au Démarrage|fr_fr-ModVersionGuard-and-Startup-Safety]]
