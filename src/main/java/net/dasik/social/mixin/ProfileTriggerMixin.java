package net.dasik.social.mixin;

import net.dasik.social.api.profile.ProfileAware;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.portal.TeleportTransition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Triggers profile re-evaluation on dimension change.
 * 
 * <pre>
 * ============================================
 * [GUIDE - DO NOT DELETE]
 * This mixin auto-triggers profile evaluation when:
 * - Entity changes dimension (cross-dimension teleport)
 * See: Doc/Develop/profile_guide.md
 * ============================================
 * </pre>
 */
@Mixin(Entity.class)
public abstract class ProfileTriggerMixin {

    /**
     * Hook into teleportCrossDimension to detect dimension changes.
     * Method signature: teleportCrossDimension(ServerLevel oldLevel, ServerLevel
     * newLevel, TeleportTransition transition)
     */
    @Inject(method = "teleportCrossDimension", at = @At("RETURN"))
    private void dasik$onDimensionChange(ServerLevel oldLevel, ServerLevel newLevel, TeleportTransition transition,
            CallbackInfoReturnable<Entity> cir) {
        Entity result = cir.getReturnValue();
        if (result instanceof ProfileAware aware && aware.hasProfileSupport()) {
            aware.getProfileManager().evaluateProfiles();
        }
    }
}
