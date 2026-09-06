# 🎛️ Master Release Queue: Dasik Library

> **Mod Project Master Ground-Truth Document**  
> *Last Synchronized: 2026-09-04*  
> **Modrinth ID**: `JVMIalSJ` | **CurseForge ID**: `1453456` | **Lead SemVer**: `1.8.36`

---

## 📊 Multi-Version Release Matrix & Queue Status

| Target MC | Generational Era | Live on Platforms | Next Queued Version | Status & Cadence Action | Feature Highlights / Notes |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **MC 26.2** | Modern Standard | `1.8.35` | `1.8.36` | 🔒 **Updated (2026-09-04)** | Latest update live (`1.8.35`). Daily limit active until next reset. |

---

## 🏛️ Project Operating Rules & Architectural Invariants

1. **🔢 Universal Direct SemVer Inheritance**:
   - Modern subprojects share unified SemVer milestone lineage targeting `1.8.35`.
   - Each Minecraft version anchor manages its own organic progression to ensure 100% clean, verified parity.

2. **📅 Daily Update Guard**:
   - Strict maximum of 1 release per day per targeted Minecraft version anchor across Modrinth and CurseForge.

---

## 🛠️ CLI Publisher Commands for Dasik Library

```powershell
# 1. Check current status across all targeted Minecraft versions
python ".agents/skills/platform-publisher/scripts/platform_publisher.py" --mod "Dasik Library" --status

# 2. Publish next sequential batch across all active versions
python ".agents/skills/platform-publisher/scripts/platform_publisher.py" --mod "Dasik Library" --publish-next --yes

```
