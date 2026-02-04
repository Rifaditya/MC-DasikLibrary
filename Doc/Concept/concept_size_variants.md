# Concept: Size Variants System (Modular API)

## 1. Overview

The **Size Variants System** provides a modular framework for introducing natural physical diversity among entities. By leveraging entity UUIDs and a registry-based configuration, it allows external mods to define custom scaling logic without modifying the core library.

## 2. Core Mechanics

- **UUID-Based Determinism**: The `scale_factor` is derived using the entity's **UUID** as a seed. This ensures unique, reproducible size that remains consistent.
- **Lifecycle Persistence**: Calculated on first spawn or load. Persists naturally via UUID stability.
- **Attribute-Based Scaling**: Utilizes `generic.scale` attribute for hitbox, eye height, and rendering.

## 3. Technical Architecture

### 3.1 Registry System (`SizeRegistry`)

- **Central Registry**: `SizeModifierRegistry` maps `EntityType<?>` to `ScaleRange`.
- **API**: Methods to register and query scale ranges for entities.

### 3.2 Interfaces

- **`IScaleProvider`**: Interface for dynamic scaling logic (biome/difficulty based).
- **`ScaleRange`**: Data object holding min/max scale values (e.g., 0.9f - 1.1f).

### 3.3 Event Handling

- **`EntityJoinLevelEvent` / `FinalizeSpawn`**:
  - Check if entity is in `SizeRegistry`.
  - Calculate scale from UUID logic.
  - Apply `generic.scale` attribute modification.

## 4. Configuration

- **Library Defaults**: Optional global default (disabled by default).
- **External Overrides**: API allows mods to override defaults.

## 5. Feature Requirements (Concept Parity)

1. **UUID Seeding**: logical derivation of scale from UUID.
2. **Attribute Application**: Use `Attributes.SCALE`.
3. **Registry API**: `register(EntityType, ScaleRange)`.
4. **Persistence**: Ensure scale remains consistent for the same entity instance.
