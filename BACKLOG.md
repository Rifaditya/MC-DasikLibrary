# 📌 Dasik Library Backlog

<!-- Highest Historical ID: BL-DASIK-015 (Next Ticket Must Start at BL-DASIK-016) -->
> **Next Available Ticket ID**: `[BL-DASIK-016]`

This file tracks planned features, technical refinements, performance optimizations, and deferred bug fixes for **Dasik Library**.

---

## 📊 Backlog Summary

| ID | Category | Title | Priority | Target Version | Status | Specification |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| [`BL-DASIK-008`](tickets/BL-DASIK-008.md) | `[BUGFIX]` | Dedicated Server Sidedness & Synthetic Lambda Isolation (`DasikSupportHelper` & `SocialLinks`) | `[CRITICAL]` | All Anchors | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-008.md) |
| [`BL-DASIK-009`](tickets/BL-DASIK-009.md) | `[BUGFIX]` | SignalBus Watchdog Freeze, Dimension Isolation & Safe Source Casting | `[CRITICAL]` | All Anchors | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-009.md) |
| [`BL-DASIK-010`](tickets/BL-DASIK-010.md) | `[BUGFIX]` | PathfinderMobMixin Resolution & Defensive GroupMember Cast Guards | `[CRITICAL]` | All Anchors | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-010.md) |
| [`BL-DASIK-011`](tickets/BL-DASIK-011.md) | `[BUGFIX]` | Genetics API Scale/Size Key Parity & Uncapped Freedom Over Anti-Crash Alignment | `[CRITICAL]` | All Anchors | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-011.md) |
| [`BL-DASIK-012`](tickets/BL-DASIK-012.md) | `[PERF]` | SocialRegistry Heap Leak Purge, Level Retention Guard & Hot-Path Zero-Allocation Polish | `[HIGH]` | All Anchors | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-012.md) |
| [`BL-DASIK-013`](tickets/BL-DASIK-013.md) | `[TECH_DEBT]` | Multi-Era Anchor Parity & Legacy ModVersionGuard Purge Across Older Anchors (`1.21.11`, `1.21.1`, `1.20.1`) | `[HIGH]` | `1.21.11, 1.21.1, 1.20.1` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-013.md) |
| [`BL-DASIK-015`](tickets/BL-DASIK-015.md) | `[FEATURE]` | Public Headless Testing & VirtualPlayerBot API (`net.dasik.social.api.test`) | `[HIGH]` | All Anchors | `📌 DEFERRED` | [View Ticket](tickets/BL-DASIK-015.md) |
| [`BL-DASIK-004`](tickets/BL-DASIK-004.md) | `[TECH_DEBT]` | Adopt 5-Pillar Testing & Sidedness Verification Harness | `[MEDIUM]` | All Anchors | `📌 DEFERRED` | [View Ticket](tickets/BL-DASIK-004.md) |
| [`BL-DASIK-014`](tickets/BL-DASIK-014.md) | `[TECH_DEBT]` | Manifest, Architecture Map & Release Queue Calibration (`ARCHITECTURE.md`, 4-Point Vault, `@DasikApiStatus`) | `[MEDIUM]` | All Anchors | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-014.md) |
| [`BL-DASIK-005`](tickets/BL-DASIK-005.md) | `[FEATURE]` | Universal Brigadier Command Framework Implementation | `[HIGH]` | `1.10.0` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-005.md) |
| [`BL-DASIK-006`](tickets/BL-DASIK-006.md) | `[FEATURE]` | Universal Brigadier Command Framework Multi-Era Anchor Parity Port | `[HIGH]` | `1.2.0+1.21.11, 1.2.0+1.21.1, 1.2.0+1.20.1` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-006.md) |
| [`BL-DASIK-007`](tickets/BL-DASIK-007.md) | `[FEATURE]` | Zero-Boilerplate Universal YACL Config Screen Generator (`UniversalConfigScreen`) | `[HIGH]` | `1.11.0, 1.3.0+1.21.11, 1.3.0+1.21.1, 1.3.0+1.20.1` | `✅ RESOLVED` | [View Ticket](tickets/BL-DASIK-007.md) |

---

## 🏷 Legend & Status Tags
- **Categories**: `[FEATURE]`, `[REFINEMENT]`, `[BUGFIX]`, `[PERF]`, `[TECH_DEBT]`
- **Priorities**: `[CRITICAL]` (Crash/sidedness hazard), `[HIGH]` (Critical logic fix/enhancement), `[MEDIUM]` (Quality of life / optimization), `[LOW]` (Minor polish)
- **Statuses**: `📌 DEFERRED` (Queued for future work), `🚧 IN_PROGRESS` (Active development), `✅ RESOLVED` (Implemented and verified)

---

## 📝 Detailed Backlog Entries (Modular Storage)

> [!NOTE]
> **Modular Backlog Architecture**: In accordance with Item 11 of the Studio Constitution (*The Modular Backlog Standard*), detailed specifications, implementation blueprints, and verification checklists are maintained in dedicated atomic ticket files located in [`tickets/`](tickets/README.md). Click any **View Ticket** link in the summary table above or prompt worker agents directly with `tickets/BL-DASIK-XXX.md`.
