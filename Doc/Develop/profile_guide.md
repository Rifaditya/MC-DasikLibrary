# Behavior Profile System - Developer Guide

> [!CAUTION]
> **DO NOT DELETE THIS FILE** - Core reference for profile system

## Overview

The Behavior Profile System allows mobs to have multiple AI configurations that switch based on context (dimension, biome, state).

## Quick Start

```java
// 1. Create profiles
BehaviorProfile overworldProfile = BehaviorProfile.builder("overworld")
    .priority(10)
    .condition(BehaviorCondition.inDimension(Level.OVERWORLD))
    .goals(g -> {
        g.add(1, new BreedGoal(...));
        g.add(2, new TemptGoal(...));
        g.add(5, new WanderGoal(...));
    })
    .build();

// 2. Register with manager
manager.register(overworldProfile);
```

## Design Rules

| Rule | Limit |
|:-----|:------|
| Conditions per profile | Max 5 |
| Evaluation | Event-driven (NOT polling) |

## Trigger Events

| Trigger | When to Call |
|:--------|:-------------|
| Dimension change | Auto (via mixin) |
| Biome change | Auto (via mixin) |
| State change | `manager.markDirty()` |
| Manual switch | `manager.setActiveProfile(id)` |

## Profile Selection

1. Evaluate all profiles' conditions
2. Pick profile with **highest match count**
3. If tie → use `priority` value
4. If still tie → keep current profile
