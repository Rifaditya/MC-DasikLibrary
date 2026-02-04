# Size Variants (Natural Diversity)

The **Size Variants** feature adds natural physical variety to mobs in the world. Instead of every Zombie being identical in height, you will now encounter slightly smaller or larger individuals.

## How it Works

* **Unique Sizes**: Every entity has a unique size determined by its "soul" (UUID).
* **Consistency**: A specific Zombie will always be that size, even if you leave the area and come back.
* **Hitboxes**: Logic adapts! Smaller mobs have smaller hitboxes, and larger mobs have larger ones.

## Configuration

This system is currently **opt-in** by other mods using the Dasik Library. It does not touch vanilla mobs by default unless a "Content Mod" (like *Bat Ecology*) registers them.
