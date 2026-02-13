package net.dasik.social.mixin;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Accessor mixin to expose protected goalSelector field.
 */
@Mixin(Mob.class)
public interface MobGoalAccessor {

    @Accessor("goalSelector")
    GoalSelector dasik$getGoalSelector();
}
