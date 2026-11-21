# 📌 Dasik Library Backlog

<!-- Highest Historical ID: BL-DASIK-025 (Next Ticket Must Start at BL-DASIK-026) -->
> **Next Available Ticket ID**: `[BL-DASIK-026]`

This file tracks planned features, technical refinements, performance optimizations, and deferred bug fixes for **Dasik Library**.

---

## 📊 Backlog Summary

| ID | Category | Title | Priority | Target Version | Status | Specification |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| [`BL-DASIK-025`](tickets/BL-DASIK-025.md) | `[BUGFIX]` | Fix DynamicGameRuleManager Duplicate Method ClassFormatError via Idempotent Fabric Bridge Injection | `[CRITICAL]` | `1.4.5+1.20.1` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-025.md) |
| [`BL-DASIK-024`](tickets/BL-DASIK-024.md) | `[BUGFIX]` | Fix PlayerVisionTracker ClipContext Null Entity NPE in Anchor 1.20.1 | `[HIGH]` | `1.4.4+1.20.1` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-024.md) |
| [`BL-DASIK-023`](tickets/BL-DASIK-023.md) | `[BUGFIX]` | Fix Ko-fi & Discord YACL Button No-Op on MC 26.3+ (Migrate ConfirmLinkScreen to URI Overload Across All & Future Anchors) | `[CRITICAL]` | `All & Future Anchors` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-023.md) |
| [`BL-DASIK-022`](tickets/BL-DASIK-022.md) | `[FEATURE]` | Per-Mod Advancement-Triggered Early Access Prompt Engine (Replacing World-Join Greeting) | `[HIGH]` | `All Anchors` | `📌 DEFERRED` | [View Ticket](tickets/BL-DASIK-022.md) |
| [`BL-DASIK-021`](tickets/BL-DASIK-021.md) | `[BUGFIX]` | Transitive Vanilla-Client Server Compatibility: Forge 1.20.1 DisplayTest & DasikSupportHelper Fallback Text | `[CRITICAL]` | `1.20.1` Forge & All Anchors | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-021.md) |
| [`BL-DASIK-020`](tickets/BL-DASIK-020.md) | `[FEATURE]` | Native Multi-Loader Port (Fabric + NeoForge) for Minecraft 26.1.2 | `[HIGH]` | `1.13.0+26.1.2` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-020.md) |
| [`BL-DASIK-019`](tickets/BL-DASIK-019.md) | `[FEATURE]` | Native Multi-Loader Port (Fabric + NeoForge) for Minecraft 1.21.11 | `[HIGH]` | `1.4.1+1.21.11` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-019.md) |
| [`BL-DASIK-017`](tickets/BL-DASIK-017.md) | `[GOVERNANCE]` | Storefront Reconciliation: Align Mod Pages with Current Public Release | `[HIGH]` | `Platform Pages` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-017.md) |
| [`BL-DASIK-018`](tickets/BL-DASIK-018.md) | `[TECH_DEBT]` | Wiki Audit & Update to Latest Version: MC-DasikLibrary | `[MEDIUM]` | `All Anchors` | `📌 DEFERRED` | [View Ticket](tickets/BL-DASIK-018.md) |

---

## 🏷 Legend & Status Tags
- **Categories**: `[FEATURE]`, `[REFINEMENT]`, `[BUGFIX]`, `[PERF]`, `[TECH_DEBT]`
- **Priorities**: `[CRITICAL]` (Crash/sidedness hazard), `[HIGH]` (Critical logic fix/enhancement), `[MEDIUM]` (Quality of life / optimization), `[LOW]` (Minor polish)
- **Statuses**: `📌 DEFERRED` (Queued for future work), `🚧 IN_PROGRESS` (Active development), `✅ RESOLVED` (Implemented and verified)

---

## 📝 Detailed Backlog Entries (Modular Storage)

> [!NOTE]
> **Modular Backlog Architecture**: In accordance with Item 11 of the Studio Constitution (*The Modular Backlog Standard*), detailed specifications, implementation blueprints, and verification checklists are maintained in dedicated atomic ticket files located in [`tickets/`](tickets/README.md). Click any **View Ticket** link in the summary table above or prompt worker agents directly with `tickets/BL-DASIK-XXX.md`.
