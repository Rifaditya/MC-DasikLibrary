/*
 * Dasik Library
 * Verified against: LivingEntity.java (26.2+)
 */
package net.dasik.social.mixin;

import java.util.function.Consumer;
import net.dasik.social.api.genetics.EntityGenetics;
import net.dasik.social.api.genetics.GeneticsEngine;
import net.dasik.social.api.genetics.GeneticsLootModifier;
import net.dasik.social.api.genetics.GeneticsLootRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityLootMixin {

    @ModifyVariable(
        method = "dropFromLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;ZLnet/minecraft/resources/ResourceKey;Ljava/util/function/Consumer;)V",
        at = @At("HEAD"),
        argsOnly = true
    )
    private Consumer<ItemStack> dasik$modifyLootConsumer(
        Consumer<ItemStack> original,
        ServerLevel level,
        DamageSource source,
        boolean playerKilled,
        ResourceKey<LootTable> lootTable
    ) {
        LivingEntity self = (LivingEntity) (Object) this;
        GeneticsLootModifier modifier = GeneticsLootRegistry.get(self.getType());
        if (modifier == null) {
            return original;
        }

        EntityGenetics genetics = GeneticsEngine.getGenetics(self);
        if (genetics == null) {
            return original;
        }

        return stack -> {
            ItemStack modified = modifier.modifyDrop(self, genetics, stack, self.getRandom());
            if (modified != null && !modified.isEmpty()) {
                original.accept(modified);
            }
        };
    }
}
