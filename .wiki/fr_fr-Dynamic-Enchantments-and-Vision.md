# Enchantements Dynamiques et Suivi de Vision

| Composant | Classe |
| :--- | :--- |
| **Gestionnaire d'Enchantements**| `net.dasik.social.api.enchantment.DynamicEnchantmentManager` |
| **Suivi du Regard Joueur** | `net.dasik.social.util.PlayerVisionTracker` |
| **Format d'Enchantement Clé** | `Identifier` dynamique |

---

## ✨ Présentation & Enregistrement Dynamique

`DynamicEnchantmentManager` permet d'injecter des enchantements à l'exécution sans nécessiter de structures complexes de datapacks JSON au démarrage du jeu.

```java
// Registering dynamic enchantment
DynamicEnchantmentManager.registerEnchantment(
    Identifier.parse("betterdogs:loyal_bond"),
    new DynamicEnchantmentConfig(...)
);
```

---

## 👁️ Suivi de Vision Joueur (`PlayerVisionTracker`)

`PlayerVisionTracker` fournit des méthodes optimisées de lancer de rayon (raycast) et de test de frustum visuel pour déterminer si une créature est observée directement par un joueur :

1. **Test Angulaire de Cône de Vision** : Vérifie si le produit scalaire entre le vecteur d'orientation du joueur et le vecteur cible dépasse le seuil visuel.
2. **Lancer de Rayon d'Obstacle** : Valide qu'aucun bloc opaque n'obstrue la vue entre les deux entités.

---

## 🔗 Pages Liées
* [[Utilitaires Stochastiques et Mathématiques|fr_fr-Stochastic-and-Math-Utilities]]
* [[Architecture et Structure des Paquets|fr_fr-Architecture-and-Package-Layout]]
