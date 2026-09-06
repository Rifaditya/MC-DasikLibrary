# Integrationsleitfaden für nachgelagerte Mods

| Mod-Projekt | Integrations-Highlights |
| :--- | :--- |
| **Better Dogs** | Rudeldynamiken, taktische Hornbefehle, Kümmerling-/Riesenskalierung |
| **Natural Reproduction** | Stammbaumprüfungen, Inzuchtrisikovorhersage, Größenattribute |
| **Collapsible Game Rule Screen** | Kategorie-Formatierung (`§l`), dynamische GameRule-Registrierung |
| **Bat Ecology** | Schwarmflugstrategien (`FlockType.AERIAL`) |
| **Ore Amplifier** | Stochastische Erzgenerierungsskalierung (`StochasticUtil`) |

---

## 🛠️ Schritt-für-Schritt Integrations-Checkliste

1. **Abhängigkeit deklarieren**: Tragen Sie `"dasik-library": "*"` in Ihre `fabric.mod.json` ein.
2. **Interfaces implementieren**: Implementieren Sie `SocialEntity` oder `GroupMember` auf Ihren Mobs.
3. **GameRules registrieren**: Rufen Sie `DynamicGameRuleManager.registerBoolean` / `registerInt` bei der Mod-Initialisierung auf.
4. **Genetik-Fassade nutzen**: Fragen Sie Größenattribute über `DasikAnimalGeneticsAPI` ab.

---

## 🔗 Verwandte Seiten
* [[Entwickler-Setup & Build|de_de-Developer-Setup-and-Building]]
* [[Architektur & Paketstruktur|de_de-Architecture-and-Package-Layout]]
