package net.dasik.social.impl.size;

import net.dasik.social.api.size.IScaleProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class SizeEvents {

    public static void onEntityJoin(Entity entity) {
        if (!(entity instanceof LivingEntity living)) {
            return;
        }

        IScaleProvider provider = SizeRegistry.getProvider(entity.getType());
        if (provider == null) {
            return;
        }

        AttributeInstance scaleAttribute = living.getAttribute(Attributes.SCALE);
        if (scaleAttribute == null) {
            return;
        }

        // Check if value is already modified to avoid re-application (though Attributes
        // usually handle this,
        // we want to be safe and only apply our deterministic base scale).
        // However, standard generic.scale is 1.0. If we modify it, the base value
        // changes.
        // We will assume the base is 1.0 (or whatever the entity default is) and apply
        // a multiplier.
        // Actually, Attribute modifiers are better. But generic.scale often works
        // directly on the base.
        // Let's set the base value if we want a permanent "size" for this entity.
        // Or adding a permanent AttributeModifier.

        // For simplicity and robustness, we check if we have already applied a size
        // modifier?
        // No, let's just calculate what it SHOULD be.

        // Wait, modification of base value persists? Yes.
        // But if we calculate purely from UUID, we can re-calculate safely every time
        // without storage.
        // But we must NOT multiply an already multiplied value.

        // Strategy: We force the value to the calculated value.
        // We assume the intended "base" scale is 1.0 (or what the model dictates).
        // To be safe, we should arguably use an AttributeModifier with
        // OPERATION_MULTIPLY_BASE.
        // That is the cleanest way and allows stacking and removal.

        float scaleFactor = provider.getScale(entity, 1.0f); // 1.0f assumed as base relative scale

        // If scaleFactor is 1.0, do nothing.
        if (scaleFactor == 1.0f) {
            return;
        }

        // We want to apply a modifier.
        // But how do we know if WE applied it?
        // AttributeModifiers have IDs. We should use a fixed UUID for our modifier
        // based on the...
        // well, if we use a single fixed UUID for the "Random Size" modifier, then we
        // can check if it exists.

        // Actually, the simpler approach defined in the plan was just "set scale".
        // But `generic.scale` is an attribute.
        // Let's use `setAttribute` on the base value IF it is currently default.
        // BUT, entity might have saved NBT with the modified base value.
        // If we load it, it will be the modified value.
        // So we need to know if it's "fresh" or check if the current value matches our
        // expectation.

        // If the scale is derived from UUID, then `Function(UUID) -> Scale`.
        // Current Scale should == `Function(UUID)`.
        // If not, set it.

        double targetValue = scaleFactor;
        if (Math.abs(scaleAttribute.getBaseValue() - targetValue) > 0.0001) {
            // It is different. Update it.
            scaleAttribute.setBaseValue(targetValue);
        }
    }
}
