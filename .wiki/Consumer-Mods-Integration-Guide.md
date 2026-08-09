# Consumer Mods Integration Guide

| Consumer Mod | Integration Highlights |
| :--- | :--- |
| **Better Dogs** | Wolf pack dynamics, tactical horn commands, runt/giant genetics scaling |
| **Natural Reproduction** | Kinship pedigree checks, inbreeding risk prediction, animal size stats |
| **Collapsible Game Rule Screen** | Category header formatting (`§l`), dynamic GameRule registration |
| **Bat Ecology** | Aerial murmuration flocking strategies (`FlockType.AERIAL`) |
| **Ore Amplifier** | Stochastic generation scaling (`StochasticUtil`) |

---

## 🛠️ Step-by-Step Integration Checklist

1. **Add Dependency**: Declare `"dasik-library": "*"` in `fabric.mod.json`.
2. **Implement `SocialEntity` / `GroupMember`**: Implement interfaces on target mob entities.
3. **Register Dynamic GameRules**: Call `DynamicGameRuleManager.registerBoolean` / `registerInt` during mod initialization.
4. **Utilize Genetics Facade**: Query size stats via `DasikAnimalGeneticsAPI`.

---

## 🔗 Related Pages
* [[Developer Setup & Building|Developer-Setup-and-Building]]
* [[Architecture & Package Layout|Architecture-and-Package-Layout]]
