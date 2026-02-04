# Size Variants System

The **Size Variants System** (`net.dasik.social.impl.size`) provides a deterministic, UUID-based way to vary entity sizes without modifying their base code. It utilizes the standard `generic.scale` attribute.

## Key Concepts

1. **Deterministic**: The same UUID always results in the same size. No NBT storage required for the scale factor itself (though the Attribute stores the base value).
2. **Additive**: Does not override existing logic; it applies a base value modification to the `generic.scale` attribute.

## API Usage

### Registration

To register a standard random range for an entity:

```java
import net.dasik.social.impl.size.SizeRegistry;
import net.dasik.social.api.size.ScaleRange;

// In your ModInitializer
SizeRegistry.register(EntityType.ZOMBIE, new ScaleRange(0.8f, 1.2f));
```

### Custom Logic

To implement custom sizing logic (e.g., biome-dependent):

```java
SizeRegistry.register(EntityType.SKELETON, (entity, original) -> {
    if (entity.level().isRaining()) {
        return 1.2f; // Bigger when raining!
    }
    return 1.0f;
});
```
