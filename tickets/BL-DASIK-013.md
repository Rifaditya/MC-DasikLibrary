# 🎫 [BL-DASIK-013] Multi-Era Anchor Parity & Legacy ModVersionGuard Purge Across Older Anchors (`1.21.11`, `1.21.1`, `1.20.1`)

- **Ticket ID**: `[BL-DASIK-013]`
- **Category**: `[TECH_DEBT]`
- **Priority**: `[HIGH]`
- **Target Version**: `1.21.11, 1.21.1, 1.20.1`
- **Scope**: `Multi-Era Anchor Parity & Legacy Cleanup` (`Dasik Library v1.21.11/`, `Dasik Library v1.21.1/`, `Dasik Library v1.20.1/`)
- **Status**: `✅ RESOLVED`

---

## 🎯 1. Problem Statement & Player Impact
The comprehensive audit of legacy `DasikLibrary` anchor directories revealed significant parity drift and constitutional violations:
1. **Unpurged `ModVersionGuard.java` Violation**:
   All 3 legacy anchors (`1.21.11`, `1.21.1`, `1.20.1`) still contain `src/main/java/net/dasik/social/util/ModVersionGuard.java`, directly violating the **Comprehensive Legacy Guard Purge Invariant** in the Studio Constitution.
2. **Package Structure Divergence in `1.21.11`**:
   In `Dasik Library v1.21.11`, 10 classes (`FollowLeaderGoal`, `GroupManager`, `GroupMember`, `FlockState`, `FlockingStrategy`, `AerialFlockingStrategy`, `TerrestrialFlockingStrategy`, `GroupParameters`, `Strategies`, `PlayerVisionTracker`) reside under `net.dasik.social.api.ai.*` instead of the standardized packages (`net.dasik.social.ai.goal.*`, `net.dasik.social.api.group.*`, `net.dasik.social.core.group.*`, and `net.dasik.social.api.vision.*`) used by `26.2+`, `1.21.1`, and `1.20.1`. Furthermore, `1.21.11` is missing `APIDasikStatus.java` and `FlockType.java`.
3. **Stray Subfolder in `v1.21.11`**:
   `e:\Minecraft Project\Dasik Library v1.21.11\Dasik Library 1.21.1` is a leftover duplicate folder containing only 1 file (`ModVersionGuard.java`).

## 🔬 2. Architectural Blueprint & Target Classes
- **Purge Obsolete Guards**:
  - Delete `src/main/java/net/dasik/social/util/ModVersionGuard.java` across `Dasik Library v1.21.11`, `Dasik Library v1.21.1`, and `Dasik Library v1.20.1`.
  - Remove all references and unused imports.
  - Delete stray nested folder `Dasik Library v1.21.11\Dasik Library 1.21.1`.
- **Align Package Architecture in `1.21.11`**:
  - Re-align package paths in `Dasik Library v1.21.11` to match `26.2+`, `1.21.1`, and `1.20.1`.
  - Backport `APIDasikStatus.java` and `FlockType.java` to `1.21.11`.
- **Verify Compilation**:
  - Run `./gradlew test --no-daemon --console=plain` in each anchor directory.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- Guarantees 100% identical public API packages, status annotations, and clean classloader safety across all 4 supported studio anchors.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Package Relocation Breaking Changes**: `DasikLibrary` v1.3.x on older anchors is a library used by downstream mods; package standardization must ensure downstream mods can locate `net.dasik.social.api.*` uniformly across all versions.

## 🚀 5. Implementation Steps
1. Delete `ModVersionGuard.java` from `1.21.11`, `1.21.1`, and `1.20.1`.
2. Delete stray subfolder in `1.21.11`.
3. Standardize package structure in `1.21.11` and add missing `APIDasikStatus.java` and `FlockType.java`.
4. Compile and test `./gradlew test` across all 3 anchors.

## 🏁 6. 5-Pillar Verification Checklist
- [x] **Pillar 1 (Purge Verification)**: `git status` confirms zero `ModVersionGuard.java` files remain across any anchor.
- [x] **Pillar 2 (Clean Directory Tree)**: Stray subfolder in `1.21.11` deleted.
- [x] **Pillar 3 (Package Uniformity)**: Package structures identical across `26.2+`, `1.21.11`, `1.21.1`, and `1.20.1`.
- [x] **Pillar 4 (Anchor Compilation)**: Clean `./gradlew test` pass on all legacy anchors (1.20.1, 1.21.1, 1.21.11).
- [x] **Pillar 5 (Multi-Era Parity)**: Zero orphaned classes or missing annotations across anchors.
