package net.dasik.social.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.dasik.social.api.breeding.UniversalAgeable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Universal aging system for non-AgeableMob entities.
 *
 * <pre>
 * ============================================
 * [GUIDE - DO NOT DELETE]
 * Baby scaling uses Attributes.SCALE (vanilla attribute).
 * - Baby = SCALE * 0.5 (via ADD_MULTIPLIED_BASE modifier)
 * - Hitbox, collision, and visuals all scale automatically.
 * - Matches vanilla AgeableMob behavior exactly.
 * See: Doc/Develop/profile_guide.md
 * ============================================
 * </pre>
 */
@Mixin(LivingEntity.class)
public abstract class UniversalAgeableMixin extends net.minecraft.world.entity.Entity implements UniversalAgeable {

    @Unique
    private static final Identifier DASIK_BABY_SCALE_ID = Identifier.withDefaultNamespace("dasik_baby_scale");

    @Unique
    private static final EntityDataAccessor<Boolean> IS_UNIVERSAL_BABY = SynchedEntityData.defineId(LivingEntity.class,
            EntityDataSerializers.BOOLEAN);
    @Unique
    private int dasik$age;
    @Unique
    private int dasik$inLoveTicks;
    @Unique
    private boolean dasik$movementSuppressed;

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
    public boolean isMovementSuppressed() {
        return this.dasik$movementSuppressed;
    }

    @Override
    public void setMovementSuppressed(boolean suppressed) {
        this.dasik$movementSuppressed = suppressed;
    }

    @Override
    public void setUniversalAge(int age) {
        int oldAge = this.dasik$age;
        this.dasik$age = age;
        if ((oldAge < 0) != (age < 0)) {
            boolean isBaby = age < 0;
            this.entityData.set(IS_UNIVERSAL_BABY, isBaby);
            dasik$applyScaleModifier(isBaby);
            this.refreshDimensions();
        }
    }

    /**
     * Apply or remove the SCALE attribute modifier for baby state.
     * Uses ADD_MULTIPLIED_BASE with -0.5 to achieve 50% scale (same as vanilla
     * DEFAULT_BABY_SCALE).
     */
    @Unique
    private void dasik$applyScaleModifier(boolean isBaby) {
        LivingEntity self = (LivingEntity) (Object) this;
        AttributeInstance scaleAttr = self.getAttribute(Attributes.SCALE);
        if (scaleAttr == null)
            return;

        if (isBaby) {
            if (scaleAttr.getModifier(DASIK_BABY_SCALE_ID) == null) {
                scaleAttr.addPermanentModifier(new AttributeModifier(
                        DASIK_BABY_SCALE_ID,
                        -0.5,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
            }
        } else {
            scaleAttr.removeModifier(DASIK_BABY_SCALE_ID);
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
    private void dasik$saveUniversalData(ValueOutput output, CallbackInfo ci) {
        output.putInt("DasikAge", this.getUniversalAge());
        if (this.isUniversalBaby()) {
            output.putBoolean("IsUniversalBaby", true);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void dasik$readUniversalData(ValueInput input, CallbackInfo ci) {
        this.setUniversalAge(input.getIntOr("DasikAge", 0));
        if (input.getBooleanOr("IsUniversalBaby", false)) {
            this.setUniversalBaby(true);
        }
    }
}
