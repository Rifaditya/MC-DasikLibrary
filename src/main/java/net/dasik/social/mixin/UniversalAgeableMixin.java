package net.dasik.social.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntitySpawnReason;
import net.dasik.social.api.breeding.UniversalAgeable;
import net.dasik.social.api.breeding.UniversalBreedingRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class UniversalAgeableMixin extends net.minecraft.world.entity.Entity implements UniversalAgeable {

    @Unique
    private static final EntityDataAccessor<Boolean> IS_UNIVERSAL_BABY = SynchedEntityData.defineId(Mob.class,
            EntityDataSerializers.BOOLEAN);
    @Unique
    private int dasik$age;
    @Unique
    private int dasik$inLoveTicks;

    public UniversalAgeableMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isUniversalBaby() {
        return this.dasik$age < 0;
    }

    @Override
    public void setUniversalBaby(boolean isBaby) {
        this.setUniversalAge(isBaby ? -24000 : 0);
    }

    @Override
    public boolean isInLove() {
        return this.dasik$inLoveTicks > 0;
    }

    @Override
    public void setInLove(int ticks) {
        this.dasik$inLoveTicks = ticks;
    }

    @Override
    public int getUniversalAge() {
        return this.dasik$age;
    }

    @Override
    public void setUniversalAge(int age) {
        int oldAge = this.dasik$age;
        this.dasik$age = age;
        if ((oldAge < 0) != (age < 0)) {
            boolean isBaby = age < 0;
            this.entityData.set(IS_UNIVERSAL_BABY, isBaby);
            this.refreshDimensions();
        }
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void dasik$tickLove(CallbackInfo ci) {
        if (this.dasik$inLoveTicks > 0) {
            this.dasik$inLoveTicks--;
        }
        if (this.isAlive()) {
            int age = this.getUniversalAge();
            if (age < 0) {
                this.setUniversalAge(++age);
            } else if (age > 0) {
                this.setUniversalAge(--age);
            }
        }
    }

    @Inject(method = "defineSynchedData", at = @At("TAIL"))
    private void dasik$defineUniversalData(SynchedEntityData.Builder builder, CallbackInfo ci) {
        builder.define(IS_UNIVERSAL_BABY, false);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void dasik$saveUniversalData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("Age", this.getUniversalAge());
        if (this.isUniversalBaby()) {
            compound.putBoolean("IsUniversalBaby", true);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void dasik$readUniversalData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("Age")) {
            this.setUniversalAge(compound.getInt("Age").orElse(0));
        } else if (compound.getBoolean("IsUniversalBaby").orElse(false)) {
            this.setUniversalBaby(true);
        }
    }

    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    private void dasik$scaleDimensions(Pose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (this.isUniversalBaby()) {
            // Apply 50% scale to the base dimensions
            // We call super.getDimensions(pose) conceptually, but we can't easily here
            // without recursion loop via Shadow?
            // Actually, we can just get the type's dimensions and scale them.
            cir.setReturnValue(this.getType().getDimensions().scale(0.5f));
        }
    }

    @Inject(method = "interact", at = @At("HEAD"), cancellable = true)
    private void dasik$handleUniversalInteraction(Player player, InteractionHand hand,
            CallbackInfoReturnable<InteractionResult> cir) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (UniversalBreedingRegistry.isFood(this.getType(), itemStack)) {
            if (!this.level().isClientSide() && !this.isUniversalBaby() && !this.isInLove()) {
                int cooldown = UniversalBreedingRegistry.getCooldown(this.getType());
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                this.setInLove(cooldown);
                this.level().broadcastEntityEvent((net.minecraft.world.entity.Entity) (Object) this, (byte) 18); // HEART
                                                                                                                 // particles
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            } else if (this.isUniversalBaby()) {
                // Growth acceleration
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }
                int currentAge = this.getUniversalAge();
                // Speed up by 10% of remaining time (Vanilla Standard)
                int growth = (int) ((float) (-currentAge) / 20.0F);
                this.setUniversalAge(currentAge + growth);
                this.level().addParticle(net.minecraft.core.particles.ParticleTypes.HAPPY_VILLAGER,
                        this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
                cir.setReturnValue(InteractionResult.SUCCESS);
                return;
            }
        }

        // Spawn Egg Logic: Fix for "cannot spawn baby version"
        if (itemStack.getItem() instanceof SpawnEggItem egg) {
            if (egg.getType(itemStack) == this.getType()) {
                if (!this.isUniversalBaby()) {
                    if (!this.level().isClientSide()) {
                        Mob child = (Mob) this.getType().create(this.level(), EntitySpawnReason.SPAWN_ITEM_USE);
                        if (child instanceof UniversalAgeable ua) {
                            ua.setUniversalBaby(true);
                            child.setPos(this.getX(), this.getY(), this.getZ());
                            this.level().addFreshEntity(child);
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
}
