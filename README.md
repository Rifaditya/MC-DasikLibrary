# Dasik Library

**Social AI Engine for Minecraft Mods**

A shared Fabric library providing the "Hive Mind" social behavior system for Vanilla Outsider mods.

---

## Philosophy

**"One Brain, Many Minds"**

Instead of each mod duplicating social AI code, Dasik Library provides:

- **GlobalSocialSystem** - Single pulse engine using Highlander Rule (only one tick per game tick)
- **SocialRegistry** - Shared entity tracking across all consumer mods
- **EntitySocialScheduler** - Per-entity mood/ambient event scheduling
- **SocialEvent** - Event contract for behaviors (foraging, roosting, howling, etc.)

---

## For Mod Developers

### Dependency

```json
// fabric.mod.json
"depends": {
    "dasik-library": ">=1.0.0"
}
```

### Interface Implementation

Your entity mixin implements `SocialEntity`:

```java
public interface SocialEntity {
    long dasik$getDNA();
    String dasik$getSpeciesId();
    LivingEntity dasik$asEntity();
    float dasik$getSocialScale();
    @Nullable EntitySocialScheduler dasik$getScheduler();
}
```

### Registering Events

```java
SocialEventRegistry.register("mymod:howl", new HowlEvent());
```

---

## Consumer Mods

- **Bat Ecology** - Bat social behaviors
- **Better Dogs** - Wolf/dog pack dynamics

---

## License

GNU General Public License v3.0

---

## Links

- [GitHub Repository](https://github.com/DasikIgaijinn/DasikLibrary)
- [Modrinth](https://modrinth.com/mod/dasik-library) *(planned)*
