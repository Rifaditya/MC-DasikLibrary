package net.dasik.social.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.dasik.social.api.breeding.UniversalAgeable;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.model.EntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class UniversalRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>>
        extends LivingEntityRenderer<T, S, M> {

    protected UniversalRendererMixin(EntityRendererProvider.Context context, M model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(method = "scale", at = @At("RETURN"))
    private void dasik$applyUniversalScale(LivingEntity entity, PoseStack poseStack, float partialTickTime,
            CallbackInfo ci) {
        if (entity instanceof UniversalAgeable ua && ua.isUniversalBaby()) {
            poseStack.scale(0.5f, 0.5f, 0.5f);
        }
    }
}
