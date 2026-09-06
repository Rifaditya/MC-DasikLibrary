# Guide Minecraft 26.2+

| Paramètre | Spécification |
| :--- | :--- |
| **Version Cible de Minecraft** | `26.2` (compatible avec `26.x`) |
| **Prérequis Fabric Loader** | `>=0.18.4` |
| **Environnement Java** | JDK 25 |
| **Version de la Bibliothèque** | `1.8.15` |
| **ID du Mod** | `dasik-library` |
| **Nom du Mod** | Dasik Library |
| **Licence** | LGPL-3.0 |

> 📌 **Avis sur le Dépôt Source** : La documentation de ce wiki reflète l'**état actuel du code source du dépôt**, qui peut inclure des commits récents et non publiés ou des fonctionnalités en cours de développement précédant les versions publiques sur CurseForge et Modrinth.

---

## 🛠️ Présentation & Installation

**Dasik Library** est une dépendance d'exécution indispensable pour les mods des séries *Vanilla Outsider*, *Instant Gratification* et *Delayed Gratification*. Elle fournit la planification centralisée de l'IA sociale, la génétique animale, le calcul des vecteurs Boids et l'enregistrement dynamique des GameRules.

### 📥 Installation Joueur
1. Installez **Fabric Loader** (`0.18.4` ou plus récent) pour Minecraft `26.2`.
2. Vérifiez que la **Fabric API** (`0.152.1+26.2` ou plus récent) est présente dans le dossier `.minecraft/mods`.
3. Téléchargez `dasik-library-1.8.15.jar` et placez-le avec vos mods dépendants (par ex. *Better Dogs*, *Natural Reproduction*) dans votre dossier `.minecraft/mods`.

### 💻 Dépendance pour Développeurs de Mods

Ajoutez **Dasik Library** dans votre `fabric.mod.json` :

```json
{
  "schemaVersion": 1,
  "id": "my_consumer_mod",
  "version": "1.0.0+26.2",
  "name": "My Consumer Mod",
  "depends": {
    "fabricloader": ">=0.18.4",
    "minecraft": ">=26.1.2-",
    "dasik-library": "*"
  }
}
```

Dans `gradle.properties` :

```properties
dasik_library_version=1.8.15
```

Dans `build.gradle` :

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_library_version}"
}
```

---

## ⚙️ Principales Évolutions Architecturales en 26.2+

1. **Mojang Sovereign Mappings** : Utilise les mappings Mojang (`level`, `ServerLevel`, `EntityTypes`). Les anciennes méthodes Yarn (`world`, `getWorld`) sont obsolètes.
2. **API Identifier** : Utilise `Identifier.fromNamespaceAndPath(namespace, path)` ou `Identifier.parse(string)`. L'ancienne méthode `Identifier.of()` n'est pas prise en charge.
3. **Limites Ouvertes (`>=26.1.2-`)** : Assure qu'un seul binaire JAR reste compatible avec les versions mineures ultérieures de Minecraft `26.2+`, pendant que `ModVersionGuard` garantit la sécurité à l'exécution.

---

## 🔗 Pages Liées
* [[Compatibilité des Versions|fr_fr-Version-Compatibility]]
* [[ModVersionGuard et Sécurité au Démarrage|fr_fr-ModVersionGuard-and-Startup-Safety]]
* [[Configuration Développeur et Compilation|fr_fr-Developer-Setup-and-Building]]
