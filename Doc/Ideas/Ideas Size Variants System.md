### Size Variants System (Modular API)

The **Size Variants System** provides a modular framework for introducing natural physical diversity among entities. By leveraging entity UUIDs and a registry-based configuration, it allows external mods to define custom scaling logic without modifying the core library.

#### Core Mechanics

- **UUID-Based Determinism:** The `scale_factor` is derived using the entity's **UUID** as a seed. This ensures that every individual entity has a unique, reproducible size that remains consistent even if NBT data is temporarily unavailable.
- **Lifecycle Persistence:** The scale factor is calculated upon the first spawn and stored in NBT. If a baby entity (like a wolf pup) is assigned a scale of **110%**, it will retain that exact **110%** scale relative to its growth stage as it matures.
- **Modular Registration:** The system acts as a service. External mods register their entities to the library's `SizeRegistry` to enable or customize scaling behavior.

#### Technical Implementation

- **Attribute-Based Scaling:** Utilizes the `generic.scale` attribute to automatically handle hitbox, eye height, and rendering adjustments.
- **API & Registry:**
  - `SizeModifierRegistry`: A central registry where external mods map `EntityType` to specific `ScaleRange` objects.
  - `IScaleProvider`: An interface that mods can implement to provide dynamic scaling logic (e.g., scaling based on biome or difficulty).
- **Configuration:**
  - **Library Defaults:** Global min/max settings for unregistered entities (if enabled).
  - **External Overrides:** Mods can provide their own config files or use the library's API to set `entity_overrides` programmatically.
- **Event Hooks:** The library handles `EntityJoinLevelEvent` and `FinalizeSpawn`, checking the registry and UUID to apply the attribute before the entity is rendered.

#### Example Scenario (Mod Integration)

1. **Mod A** (a prehistoric mod) registers its `T-Rex` entity to the library with a range of **0.9x to 1.5x**.
2. A `T-Rex` spawns with UUID `550e8400-e29b...`. The library seeds a randomizer with this **UUID**, resulting in a scale of **1.42x**.
3. The library applies the attribute. No code was changed in the library mod to support the `T-Rex`.
4. If **Mod B** wants to change the `T-Rex` size later, it simply sends a new registration to the library's registry, which overwrites the previous logic.

