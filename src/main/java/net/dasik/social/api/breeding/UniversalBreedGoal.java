package net.dasik.social.api.breeding;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class UniversalBreedGoal extends Goal {
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0)
            .ignoreLineOfSight();
    protected final Mob mob;
    protected final Class<? extends Mob> partnerClass;
    protected final Level level;
    @Nullable
    protected Mob partner;
    private int loveTime;
    private final double speedModifier;

    public UniversalBreedGoal(Mob mob, double speedModifier) {
        this(mob, speedModifier, mob.getClass());
    }

    public UniversalBreedGoal(Mob mob, double speedModifier, Class<? extends Mob> partnerClass) {
        this.mob = mob;
        this.level = mob.level();
        this.partnerClass = partnerClass;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!(mob instanceof UniversalAgeable ageable) || !ageable.isInLove()) {
            return false;
        }
        this.partner = this.getFreePartner();
        return this.partner != null;
    }

    @Override
    public boolean canContinueToUse() {
        return this.partner != null && this.partner.isAlive() && this.partner instanceof UniversalAgeable ua
                && ua.isInLove() && this.loveTime < 60;
    }

    @Override
    public void start() {
        if (this.mob instanceof UniversalAgeable ua) {
            ua.setMovementSuppressed(true);
            ua.onSocialGoalStart();
        }
    }

    @Override
    public void stop() {
        if (this.mob instanceof UniversalAgeable ua) {
            ua.setMovementSuppressed(false);
            ua.onSocialGoalStop();
        }
        this.partner = null;
        this.loveTime = 0;
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.partner, 10.0F, (float) this.mob.getMaxHeadXRot());
        this.mob.getNavigation().moveTo(this.partner, this.speedModifier);
        this.loveTime++;
        if (this.loveTime >= 60 && this.mob.distanceToSqr(this.partner) < 9.0) {
            this.breed();
        }
    }

    @Nullable
    private Mob getFreePartner() {
        List<? extends Mob> potentialPartners = this.level.getEntitiesOfClass(this.partnerClass,
                this.mob.getBoundingBox().inflate(8.0));
        double dist = Double.MAX_VALUE;
        Mob partner = null;

        for (Mob other : potentialPartners) {
            if (this.canBreedWith(other) && this.mob.distanceToSqr(other) < dist) {
                partner = other;
                dist = this.mob.distanceToSqr(other);
            }
        }
        return partner;
    }

    private boolean canBreedWith(Mob other) {
        if (other == this.mob)
            return false;
        if (other.getClass() != this.partnerClass)
            return false;
        return other instanceof UniversalAgeable ua && ua.isInLove() && !ua.isUniversalBaby();
    }

    protected void breed() {
        if (level instanceof ServerLevel serverLevel) {
            Mob child = (Mob) this.mob.getType().create(serverLevel, EntitySpawnReason.BREEDING);
            if (child instanceof UniversalAgeable ua) {
                ua.setUniversalBaby(true);
                child.setPos(this.mob.getX(), this.mob.getY(), this.mob.getZ());
                serverLevel.addFreshEntity(child);

                // Reset Love
                if (this.mob instanceof UniversalAgeable parent1)
                    parent1.setInLove(0);
                if (this.partner instanceof UniversalAgeable parent2)
                    parent2.setInLove(0);
            }
        }
    }
}
