package net.dasik.social.mixin;

import net.dasik.social.api.breeding.UniversalAgeable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class AmbientAiMixin {

    @Inject(method = "customServerAiStep", at = @At("HEAD"), cancellable = true)
    private void dasik$suppressLegacyAi(ServerLevel level, CallbackInfo ci) {
        if ((Object) this instanceof UniversalAgeable ageable && ageable.isMovementSuppressed()) {
            ci.cancel();
        }
    }
}
