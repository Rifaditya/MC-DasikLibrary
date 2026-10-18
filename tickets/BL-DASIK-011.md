# 🎫 [BL-DASIK-011] Genetics API Scale/Size Key Parity & Uncapped Freedom Over Anti-Crash Alignment

- **Ticket ID**: `[BL-DASIK-011]`
- **Category**: `[BUGFIX]`
- **Priority**: `[CRITICAL]`
- **Target Version**: `All Anchors` (`1.11.3` / `26.2+`, `1.3.1+1.21.11`, `1.3.1+1.21.1`, `1.3.1+1.20.1`)
- **Scope**: `Genetics API & Scale Multipliers` (`net.dasik.social.api.genetics.DasikAnimalGeneticsAPI`, `net.dasik.social.api.genetics.SizeStatsBuilder`)
- **Status**: `✅ RESOLVED`

---

## 🎯 1. Problem Statement & Player Impact
In `DasikAnimalGeneticsAPI.java`:
1. **Trait Key Mismatch (`"scale"` vs `"size"`)**:
   `SizeStatsBuilder` defines `public static final String SIZE_TRAIT_ID = "size"`, and `LivingEntityScaleMixin` reads `"size"`. However, `DasikAnimalGeneticsAPI.getScale(LivingEntity)` and `setScale(LivingEntity, float)` in `MC-DasikLibrary` (`26.2`), `1.21.1`, and `1.20.1` read and write `"scale"`. As a result, `getScale()`, `isRunt()`, and `isGiant()` completely ignore the `"size"` trait generated during entity breeding, causing downstream mods to perceive all animals as 1.0x scale!
2. **Freedom Over Anti-Crash Violation**:
   In `MC-DasikLibrary` (`26.2`), `DasikAnimalGeneticsAPI.getScale()` (line 88) and `setScale()` (line 95) hard-clamp scale to `Math.clamp(scale, 0.5f, 2.0f)`, directly contradicting `GeneticsScaleTest` and `LivingEntityScaleMixin` (`0.0625f` to `16.0f`), dynamic `GeneticsLimitRegistry` GameRule limits, and the **Freedom Over Anti-Crash Policy** (which forbids arbitrary nanny limits).

## 🔬 2. Architectural Blueprint & Target Classes
- **Trait ID Harmonization**:
  - In `DasikAnimalGeneticsAPI.java`:
    - In `setScale()`, store under `SizeStatsBuilder.SIZE_TRAIT_ID` (`"size"`).
    - In `getScale()`, read `SizeStatsBuilder.SIZE_TRAIT_ID` (`"size"`), with fallback check for `"scale"` for backward save compatibility.
- **Uncapped / Saturated Bounds**:
  - Remove the hardcoded `[0.5f, 2.0f]` clamp.
  - Defer clamping to `GeneticsLimitRegistry.getScaleLimit()` / `[0.0625f, 16.0f]`, allowing full custom scale ranges up to `Float.MAX_VALUE`.

## ⚓ 3. Multi-Era Anchor Parity Specifications
- `1.21.11` already uses `"size"` in `setScale`.
- Unify `MC-DasikLibrary` (`26.2+`), `1.21.1`, and `1.20.1` so that all 4 anchors share identical `"size"` trait keys and unconstrained bounds.

## 🛡️ 4. Edge Cases, Collisions & Failure Modes
- **Legacy World Saves**: Animals bred with older versions may have `"scale"` instead of `"size"` in their NBT/DataAttachments. Fallback ensures zero stat loss.
- **NaN / Infinite Scale**: Use saturated arithmetic (`Float.isNaN(val) ? 1.0f : val`) to prevent render engine scale poisoning.

## 🚀 5. Implementation Steps
1. Refactor `DasikAnimalGeneticsAPI.java` across `MC-DasikLibrary` to use `SizeStatsBuilder.SIZE_TRAIT_ID` and fallback.
2. Remove hardcoded `[0.5f, 2.0f]` clamps.
3. Update `GeneticsScaleTest` asserting `DasikAnimalGeneticsAPI.getScale()` returns values across the full `[0.0625f, 16.0f]` range.
4. Backport fixes to `1.21.11`, `1.21.1`, and `1.20.1`.

## 🏁 6. 5-Pillar Verification Checklist
- [x] **Pillar 1 (Chaos Fuzzing)**: Saturated bounds verified for scale inputs from `-100.0f` to `1,000,000.0f`.
- [x] **Pillar 2 (Backward Compatibility)**: Existing entities with `"scale"` trait load correct scale values.
- [x] **Pillar 3 (Unit Tests)**: `GeneticsScaleTest` passes with 100% assertion coverage.
- [x] **Pillar 4 (Freedom Over Anti-Crash)**: Player configurations for giant mobs (> 2.0f) render correctly without clamping.
- [x] **Pillar 5 (Multi-Era Parity)**: All 4 version anchors share the exact same scale contract.
