# Nettoyage d'Attributs Obsolètes & Échelle

| Problématique | Règle de Résolution |
| :--- | :--- |
| **Modificateurs d'Attributs Orphelins** | Purge des modificateurs préfixés `genetics_` lors du recalcul |
| **Offset d'Addition de Taille** | Utilisation de l'offset `-1.0f` pour l'opération `ADD_VALUE` |
| **Attribut de Taille Cible** | `EntityAttributes.SCALE` |

---

## 🧹 Purge des Modificateurs Périmés

Lors de la réévaluation de la taille ou des caractéristiques d'une créature (après croissance, mutation ou soins), les modificateurs appliqués précédemment doivent être purgés pour éviter l'accumulation infinie de bonus :

```java
public static void purgeStaleModifiers(LivingEntity entity, Holder<Attribute> attribute) {
    AttributeInstance instance = entity.getAttribute(attribute);
    if (instance != null) {
        instance.getModifiers().stream()
            .filter(m -> m.id().getPath().startsWith("genetics_"))
            .toList()
            .forEach(instance::removeModifier);
    }
}
```

---

## 📏 Calcul Mathématique pour `ADD_VALUE`

Dans Minecraft, la valeur par défaut de l'attribut `SCALE` est $1.0$. Lorsqu'un modificateur utilise l'opération `ADD_VALUE`, l'augmentation doit compenser la base de $1.0$ :

$$\text{Modifier Amount} = \text{TargetScale} - 1.0f$$

Une créature avec une échelle cible de $1.5x$ nécessite donc un montant de modificateur de $+0.5f$.

---

## 🔗 Pages Liées
* [[Moteur de Génétique Animale|fr_fr-Animal-Genetics-Engine]]
* [[API Génétique et Arbre Généalogique|fr_fr-Genetics-API-and-Pedigree]]
