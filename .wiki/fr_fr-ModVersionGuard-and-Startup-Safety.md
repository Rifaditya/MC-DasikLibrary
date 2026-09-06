# ModVersionGuard & Sécurité au Démarrage

| Composant | Classe |
| :--- | :--- |
| **Garde de Version** | `net.dasik.social.core.ModVersionGuard` |
| **ClassLoader Requis** | `Thread.currentThread().getContextClassLoader()` |
| **Prévention de Crash** | Détection anticipée des incompatibilités d'API à l'initialisation |

---

## 🛡️ Rôle & Fonctionnement de `ModVersionGuard`

Sous le chargeur Knot de Fabric Loader, l'appel standard `Class.forName(name)` sans ClassLoader explicite peut échouer ou masquer des dépendances manquantes. `ModVersionGuard` force l'inspection via le chargeur de contexte du thread courant pour intercepter les classes absentes avant que la boucle de jeu ne démarre.

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

## 🔗 Pages Liées
* [[Compatibilité des Versions|fr_fr-Version-Compatibility]]
* [[Guide MC 26.2|fr_fr-Minecraft-26.2-Guide]]
