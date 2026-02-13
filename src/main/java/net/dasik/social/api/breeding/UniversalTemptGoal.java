package net.dasik.social.api.breeding;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

/**
 * Universal Temptation Goal.
 * Makes any UniversalAgeable mob follow players holding their corresponding
 * breeding food.
 */
public class UniversalTemptGoal extends Goal {
    private static final TargetingConditions TEMPT_TARGETING = TargetingConditions.forNonCombat().ignoreLineOfSight();
    private final Mob mob;
    private final double speedModifier;
    @Nullable
    private Player player;
    private int calmDown;

    public UniversalTemptGoal(Mob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (this.calmDown > 0) {
            --this.calmDown;
            return false;
        } else {
            double range = this.mob.getAttributes().hasAttribute(Attributes.TEMPT_RANGE)
                    ? this.mob.getAttributeValue(Attributes.TEMPT_RANGE)
                    : 10.0;
            this.player = getServerLevel(this.mob).getNearestPlayer(
                    TEMPT_TARGETING.range(range), this.mob);
            if (this.player == null) {
                return false;
            } else {
                return this.isTempting(this.player.getItemInHand(InteractionHand.MAIN_HAND))
                        || this.isTempting(this.player.getItemInHand(InteractionHand.OFF_HAND));
            }
        }
    }

    private boolean isTempting(ItemStack itemStack) {
        return UniversalBreedingRegistry.isFood(this.mob.getType(), itemStack);
    }

    @Override
    public boolean canContinueToUse() {
        if (this.player == null || !this.player.isAlive()) {
            return false;
        }
        if (this.mob.distanceToSqr(this.player) > 100.0) {
            return false;
        }
        return this.isTempting(this.player.getItemInHand(InteractionHand.MAIN_HAND))
                || this.isTempting(this.player.getItemInHand(InteractionHand.OFF_HAND));
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
        this.player = null;
        this.stopNavigation();
        this.calmDown = reducedTickDelay(100);
    }

    @Override
    public void tick() {
        this.mob.getLookControl().setLookAt(this.player, (float) (this.mob.getMaxHeadYRot() + 20),
                (float) this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.player) < 6.25) {
            this.stopNavigation();
        } else {
            this.navigateTowards(this.player);
        }
    }

    private void stopNavigation() {
        if (this.mob instanceof PathfinderMob pathfinder) {
            pathfinder.getNavigation().stop();
        } else {
            this.mob.getMoveControl().setWait();
        }
    }

    private void navigateTowards(Player player) {
        // Use the mob's navigation system.
        // This supports both Ground (WalkNavigation) and Air (StandardAerialNavigation)
        // polymorphically.
        // Previously, this checked for PathfinderMob, which excluded Bats
        // (AmbientCreature), preventing them
        // from using our custom aerial navigation.
        this.mob.getNavigation().moveTo(player, this.speedModifier);
    }
}
