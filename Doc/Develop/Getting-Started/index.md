# Getting Started

## Dependencies

Add DasikLibrary to your `build.gradle`:

```gradle
dependencies {
    modImplementation "net.dasik.social:dasik-library:${project.dasik_version}"
}
```

## Setup

1. Implement `SocialEntity` on your entity (or Mixin) for global event tracking.
2. Implement `GroupMember` on your entity if you want flocking/swarming capabilities.
3. Register social events in `SocialEventRegistry`.
4. Use `BehaviorProfile` to define context-aware AI configurations.
