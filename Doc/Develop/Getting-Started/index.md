# Getting Started

## Dependencies

Add DasikLibrary to your `build.gradle`:

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_version}"
}
```

## Setup

1. Implement `SocialEntity` on your entity (or Mixin).
2. Register social events in `SocialEventRegistry`.
3. Use `BehaviorProfile` to define context-aware AI configurations.
