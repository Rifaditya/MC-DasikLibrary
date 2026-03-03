/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: PathfinderMob.java (Snapshot 10)
 */
package net.dasik.social.mixin;

import net.dasik.social.api.SocialEntity;
import net.dasik.social.api.group.FlockType;
import net.dasik.social.api.group.GroupMember;
import net.dasik.social.api.profile.BehaviorProfileManager;
import net.dasik.social.api.profile.ProfileAware;
import net.dasik.social.core.SocialRegistry;
import net.dasik.social.api.SocialScheduler;
import net.dasik.social.core.EntitySocialScheduler;
import net.dasik.social.core.profile.DefaultProfileManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathfinderMob.class)
public abstract class PathfinderMobMixin extends Mob implements SocialEntity, GroupMember, ProfileAware {
    
    @Unique
    private SocialScheduler dasik$scheduler;
    
    @Unique
    private BehaviorProfileManager dasik$profileManager;
    
    @Unique
    private LivingEntity dasik$leader;

    protected PathfinderMobMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void dasik$onInit(EntityType<? extends PathfinderMob> type, Level level, CallbackInfo ci) {
        this.dasik$scheduler = new EntitySocialScheduler(this);
        this.dasik$profileManager = new DefaultProfileManager(this);
        SocialRegistry.register(this);
    }

    @Override
    public Mob dasik$asEntity() {
        return this;
    }

    @Override
    public SocialScheduler dasik$getScheduler() {
        return this.dasik$scheduler;
    }

    @Override
    public BehaviorProfileManager getProfileManager() {
        return this.dasik$profileManager;
    }

    @Override
    public boolean hasProfileSupport() {
        return true;
    }

    @Override
    public String dasik$getSpeciesId() {
        return EntityType.getKey(this.getType()).toString();
    }

    // GroupMember Implementation
    @Override
    public LivingEntity getLeader() {
        return this.dasik$leader;
    }

    @Override
    public void setLeader(LivingEntity leader) {
        this.dasik$leader = leader;
    }

    @Override
    public boolean hasLeader() {
        return this.dasik$leader != null && this.dasik$leader.isAlive();
    }

    @Override
    public FlockType getFlockType() {
        return this.isNoGravity() ? FlockType.AERIAL : FlockType.TERRESTRIAL;
    }

    @Override
    public int getGroupSize() {
        return 1; // Default
    }

    @Override
    public void remove(Entity.RemovalReason reason) {
        SocialRegistry.unregister(this);
        super.remove(reason);
    }
}
