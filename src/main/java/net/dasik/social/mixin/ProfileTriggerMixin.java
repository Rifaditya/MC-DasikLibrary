/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.level.portal.TeleportTransition
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
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

@Mixin(value={Entity.class})
public abstract class ProfileTriggerMixin {
    @Inject(method={"teleportCrossDimension"}, at={@At(value="RETURN")})
    private void dasik$onDimensionChange(ServerLevel oldLevel, ServerLevel newLevel, TeleportTransition transition, CallbackInfoReturnable<Entity> cir) {
        ProfileAware aware;
        Entity result = (Entity)cir.getReturnValue();
        if (result instanceof ProfileAware && (aware = (ProfileAware)result).hasProfileSupport()) {
            aware.getProfileManager().evaluateProfiles();
        }
    }
}

