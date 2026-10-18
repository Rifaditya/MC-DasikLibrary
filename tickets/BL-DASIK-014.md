# 🎫 [BL-DASIK-014] Manifest, Architecture Map & Release Queue Calibration (`ARCHITECTURE.md`, 4-Point Vault, `@DasikApiStatus`)

- **Ticket ID**: `[BL-DASIK-014]`
- **Category**: `[TECH_DEBT]`
- **Priority**: `[MEDIUM]`
- **Target Version**: `All Anchors` (`1.11.3` / `26.2+`, `1.3.1+1.21.11`, `1.3.1+1.21.1`, `1.3.1+1.20.1`)
- **Scope**: `Manifest Integrity, Documentation & Release Toolchain` (`MC-DasikLibrary/ARCHITECTURE.md`, `build.gradle`, `RELEASE_QUEUE.md`)
- **Status**: `✅ RESOLVED`

---

## 🎯 1. Problem Statement & Player Impact
Several documentation, manifest, and release toolchain inconsistencies exist in `MC-DasikLibrary`:
1. **Missing `ARCHITECTURE.md`**: `MC-DasikLibrary` root lacks a standardized `ARCHITECTURE.md` map (required by Item 1 of the Studio Constitution), while obsolete copy `DasikLibrary-Rebuilt` has one.
2. **Version Queue & Changelog Desync**: `gradle.properties` is set to `mod_version=1.11.2`, but `CHANGELOG.md` and `RELEASE_QUEUE.md` stop at `1.11.0`, missing entries for `1.11.1` and `1.11.2`.
3. **Missing External Vault in `build.gradle`**: `archiveReleaseJar` task copies to Local Archive, Modrinth profiles, and Release Hub, but omits the mandatory External Vault destination (`D:\Minecraft Project Vault`) required by the Universal 4-Point Distribution Law.
4. **Git Remote & Manifest URL Mismatch**: `.git/config` points to `Rifaditya/MC-DasikLibrary.git` while `fabric.mod.json` points to `Rifaditya/DasikLibrary-Rebuilt`.
5. **Missing `@DasikApiStatus` Coverage**: Legacy classes in `api.genetics.*`, `api.group.*`, `api.profile.*`, and `core.*` omit branded API status annotations, and [`GroupMember.java`](src/main/java/net/dasik/social/api/group/GroupMember.java) exposes internal class `net.dasik.social.core.group.FlockState` in its public contract.

## 🔬 2. Architectural Blueprint & Target Classes
- **Author `ARCHITECTURE.md`**:
  - Generate a standardized ~50-line architecture map for `MC-DasikLibrary` documenting key packages: `api.command`, `api.config`, `api.gamerule`, `api.client.gui`, `api.genetics`, `core.scheduler`, `core.signal`.
- **Release Queue & Changelog Alignment**:
  - Record technical entries in `History.md` and update `CHANGELOG.md` and `RELEASE_QUEUE.md` to match current versioning.
- **Update `build.gradle` 4-Point Deployment**:
  - Add unconditional External Vault copy:
    ```groovy
    File externalVaultDir = new File("D:/Minecraft Project Vault/MC " + project.minecraft_version)
    ```
- **Branded API Annotations & Encapsulation**:
  - Add `@DasikApiStatus.Public` across all public API classes.
  - Add `@DasikApiStatus.Internal` across internal engine and utility classes.
  - Relocate or decouple `FlockState` to ensure clean public API encapsulation.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- Applies across all 4 anchors.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Drive D: Offline**: Ensure `build.gradle` checks `if (externalVaultDir.getParentFile().exists())` or warns without failing the build if Drive D: is disconnected.

## 🚀 5. Implementation Steps
1. Author `ARCHITECTURE.md` in `MC-DasikLibrary`.
2. Update `build.gradle` `archiveReleaseJar` with the 4th deployment point.
3. Align `fabric.mod.json` contact URLs.
4. Add missing `@DasikApiStatus` annotations.
5. Update `CHANGELOG.md` and `RELEASE_QUEUE.md`.

## 🏁 6. 5-Pillar Verification Checklist
- [x] **Pillar 1 (Architecture Map)**: `ARCHITECTURE.md` exists and accurately describes codebase layout.
- [x] **Pillar 2 (Build Script 4-Point Deploy)**: `./gradlew archiveReleaseJar` verifies all 4 deployment targets (including External Vault D:\).
- [x] **Pillar 3 (Manifest URLs)**: `fabric.mod.json` repository URLs match active git remotes (`Rifaditya/MC-DasikLibrary`).
- [x] **Pillar 4 (API Status Coverage)**: Public API and internal classes annotated with `@DasikApiStatus` (`GroupMember`, `FlockState`).
- [x] **Pillar 5 (Queue Synchronization)**: `RELEASE_QUEUE.md`, `CHANGELOG.md`, and `History.md` synchronized.
