# Getting Started

## Dependencies

Add DasikLibrary to your `build.gradle`:

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_version}"
}
```

## Setup

1. Implement `UniversalAgeable` on your entities.
2. Register breeding items in `UniversalBreedingRegistry`.
3. Add `UniversalBreedGoal` and `UniversalTemptGoal` to your entity's goal selector.
