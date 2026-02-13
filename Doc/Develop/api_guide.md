# Universal Entity API

The **Universal Entity API** allows any Mob to support complex social behaviors, aging, and interactions that are normally restricted to `AgeableMob` or specific subclasses.

## Core Interface: `UniversalAgeable`

Implement `UniversalAgeable` on your entity (or Mixin) to unlock:

- **Aging**: Baby states, growth ticks.
- **Breeding**: Love mode, partner finding.
- **Social Hooks**: Reactions to social goals (e.g., stopping sleep).

### 1. Basic Implementation

```java
public class MyEntity extends Mob implements UniversalAgeable {
    // Implement required methods (isBaby, setBaby, etc.)
    // Use UniversalAgeableMixin/Impl helper if available
}
```

### 2. Social Hooks

Override these methods to handle state changes when your entity starts or stops a social interaction (Breeding, Tempting, Following Leader).

**Example: Waking a Bat**

```java
@Override
public void onSocialGoalStart() {
    // Called when a tempt/breed goal activates
    this.setResting(false); // Wake up!
}

@Override
public void onSocialGoalStop() {
    // Called when goal finishes or aborts
}
```

## Goals

The library provides universal goals that work with any `UniversalAgeable`:

- **UniversalBreedGoal**: Handles finding a partner, mating, and spawning babies.
- **UniversalTemptGoal**: Follows players holding breeding items (defined in `UniversalBreedingRegistry`).

## Registration

Register breeding items in your `ModInitializer`:

```java
UniversalBreedingRegistry.register(
    EntityType.BAT, 
    Ingredient.of(Items.SPIDER_EYE), 
    6000 // Cooldown ticks
);
```
