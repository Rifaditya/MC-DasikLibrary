// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.mixin;

import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleMap;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRuleMap.class)
public abstract class GameRuleMapMixin {

    @Shadow @Final
    private Reference2ObjectMap<GameRule<?>, Object> map;

    @Inject(method = "has", at = @At("HEAD"), cancellable = true)
    private void dasik$dynamicHas(GameRule<?> rule, CallbackInfoReturnable<Boolean> cir) {
        if (rule == null) return;
        if (this.map.containsKey(rule)) {
            cir.setReturnValue(true);
            return;
        }
        if (DynamicGameRuleManager.getDynamicRules().containsValue(rule) ||
                (BuiltInRegistries.GAME_RULE != null && BuiltInRegistries.GAME_RULE.getKey(rule) != null)) {
            this.map.put(rule, rule.defaultValue());
            cir.setReturnValue(true);
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "get", at = @At("HEAD"), cancellable = true)
    private <T> void dasik$dynamicGet(GameRule<T> rule, CallbackInfoReturnable<T> cir) {
        if (rule == null) return;
        if (!this.map.containsKey(rule)) {
            if (DynamicGameRuleManager.getDynamicRules().containsValue(rule) ||
                    (BuiltInRegistries.GAME_RULE != null && BuiltInRegistries.GAME_RULE.getKey(rule) != null)) {
                T def = rule.defaultValue();
                this.map.put(rule, def);
                cir.setReturnValue(def);
            }
        }
    }

    @Inject(method = "set", at = @At("HEAD"))
    private <T> void dasik$dynamicSet(GameRule<T> rule, T value, CallbackInfo ci) {
        if (rule == null) return;
        if (!this.map.containsKey(rule)) {
            this.map.put(rule, value);
        }
    }
}
