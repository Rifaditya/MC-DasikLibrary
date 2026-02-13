package net.dasik.social.mixin;

import net.dasik.social.api.breeding.UniversalAgeable;
import net.dasik.social.api.breeding.UniversalBreedingRegistry;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class UniversalMobMixin {

    @org.spongepowered.asm.mixin.Shadow
    protected abstract void usePlayerItem(Player player, InteractionHand hand, ItemStack itemStack);

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void dasik$handleUniversalInteraction(Player player, InteractionHand hand, Vec3 location,
            CallbackInfoReturnable<InteractionResult> cir) {
        Mob mob = (Mob) (Object) this;
        ItemStack itemStack = player.getItemInHand(hand);

        if (UniversalBreedingRegistry.isFood(mob.getType(), itemStack)) {
            if (mob instanceof UniversalAgeable ageable) {
                if (!mob.level().isClientSide() && !ageable.isUniversalBaby() && !ageable.isInLove()) {
                    int cooldown = UniversalBreedingRegistry.getCooldown(mob.getType());
                    if (!player.getAbilities().instabuild) {
                        this.usePlayerItem(player, hand, itemStack);
                    }
                    ageable.setInLove(cooldown);
                    mob.level().broadcastEntityEvent(mob, (byte) 18); // HEART particles
                    cir.setReturnValue(InteractionResult.SUCCESS);
                    return;
                } else if (ageable.isUniversalBaby()) {
                    // Growth acceleration
                    if (!player.getAbilities().instabuild) {
                        this.usePlayerItem(player, hand, itemStack);
                    }
                    int currentAge = ageable.getUniversalAge();
                    // Speed up by 10% of remaining time (Vanilla Standard)
                    int growth = (int) ((float) (-currentAge) / 20.0F);
                    ageable.setUniversalAge(currentAge + growth);
                    mob.level().addParticle(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                            mob.getRandomX(1.0), mob.getRandomY() + 0.5, mob.getRandomZ(1.0), 0.0, 0.0, 0.0);
                    cir.setReturnValue(InteractionResult.SUCCESS);
                    return;
                }
            }
        }

        // Spawn Egg Logic: Fix for "cannot spawn baby version"
        if (itemStack.getItem() instanceof SpawnEggItem egg) {
            if (SpawnEggItem.getType(itemStack) == mob.getType()) {
                if (mob instanceof UniversalAgeable ageable && !ageable.isUniversalBaby()) {
                    if (!mob.level().isClientSide()) {
                        Mob child = (Mob) mob.getType().create(mob.level(), EntitySpawnReason.SPAWN_ITEM_USE);
                        if (child instanceof UniversalAgeable ua) {
                            ua.setUniversalBaby(true);
                            child.setPos(mob.getX(), mob.getY(), mob.getZ());
                            mob.level().addFreshEntity(child);
                            if (!player.getAbilities().instabuild) {
                                itemStack.shrink(1);
                            }
                        }
                    }
                    cir.setReturnValue(InteractionResult.SUCCESS);
                }
            }
        }
    }

    @Inject(method = "handleEntityEvent", at = @At("HEAD"), cancellable = true)
    private void dasik$handleParticles(byte id, CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (mob instanceof UniversalAgeable) {
            if (id == 18) { // HEARTS
                for (int i = 0; i < 7; ++i) {
                    double d = mob.getRandom().nextGaussian() * 0.02;
                    double e = mob.getRandom().nextGaussian() * 0.02;
                    double f = mob.getRandom().nextGaussian() * 0.02;
                    mob.level().addParticle(net.minecraft.core.particles.ParticleTypes.HEART, mob.getRandomX(1.0),
                            mob.getRandomY() + 0.5, mob.getRandomZ(1.0), d, e, f);
                }
                ci.cancel();
            } else if (id == 19) { // SMOKE
                for (int i = 0; i < 7; ++i) {
                    double d = mob.getRandom().nextGaussian() * 0.02;
                    double e = mob.getRandom().nextGaussian() * 0.02;
                    double f = mob.getRandom().nextGaussian() * 0.02;
                    mob.level().addParticle(net.minecraft.core.particles.ParticleTypes.SMOKE, mob.getRandomX(1.0),
                            mob.getRandomY() + 0.5, mob.getRandomZ(1.0), d, e, f);
                }
                ci.cancel();
            }
        }
    }
}
