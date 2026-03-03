/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Entity.java (Snapshot 10)
 */
package net.dasik.social.mixin;

import net.dasik.social.api.profile.ProfileAware;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class ProfileTriggerMixin {
    @Inject(method = "teleportCrossDimension", at = @At("RETURN"))
    private void dasik$onDimensionChange(ServerLevel oldLevel, ServerLevel newLevel, TeleportTransition transition, CallbackInfoReturnable<Entity> cir) {
        Entity result = cir.getReturnValue();
        if (result instanceof ProfileAware aware && aware.hasProfileSupport()) {
            aware.getProfileManager().evaluateProfiles();
        }
    }
}
