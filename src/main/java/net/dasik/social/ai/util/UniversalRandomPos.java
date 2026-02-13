package net.dasik.social.ai.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

/**
 * Universal Random Position Utility.
 * <p>
 * Mirrors vanilla {@link net.minecraft.world.entity.ai.util.RandomPos} and
 * {@link net.minecraft.world.entity.ai.util.HoverRandomPos}, but designated for
 * ANY {@link Mob},
 * not just {@code PathfinderMob}.
 * <p>
 * This allows {@code AmbientCreature} (like Bats) to use advanced pathfinding
 * target selection.
 */
public class UniversalRandomPos {

    /**
     * Finds a random position for a mob to hover/fly towards.
     * Mirrors {@link net.minecraft.world.entity.ai.util.HoverRandomPos#getPos}.
     */
    @Nullable
    public static Vec3 getHoverPos(Mob mob, int horizontalDist, int verticalDist, double xDir, double zDir,
            float maxXzRadiansDifference, int hoverMaxHeight, int hoverMinHeight) {
        return generateRandomPos(mob, () -> {
            BlockPos direction = generateRandomDirectionWithinRadians(
                    mob.getRandom(), 0.0, horizontalDist, verticalDist, 0, xDir, zDir, maxXzRadiansDifference);
            if (direction == null) {
                return null;
            } else {
                BlockPos pos = generateRandomPosTowardDirection(mob, horizontalDist, direction);
                if (pos == null) {
                    return null;
                } else {
                    pos = moveUpToAboveSolid(
                            pos,
                            mob.getRandom().nextInt(hoverMaxHeight - hoverMinHeight + 1) + hoverMinHeight,
                            mob.level().getMaxY(),
                            blockPos -> isSolid(mob, blockPos));
                    return !isWater(mob, pos) && !hasMalus(mob, pos) ? pos : null;
                }
            }
        });
    }

    @Nullable
    public static Vec3 generateRandomPos(Mob mob, Supplier<BlockPos> posSupplier) {
        return generateRandomPos(posSupplier, pos -> {
            if (mob instanceof PathfinderMob pathfinderMob) {
                return pathfinderMob.getWalkTargetValue(pos);
            }
            return 0.0;
        });
    }

    @Nullable
    public static Vec3 generateRandomPos(Supplier<BlockPos> posSupplier,
            ToDoubleFunction<BlockPos> positionWeightFunction) {
        double bestWeight = Double.NEGATIVE_INFINITY;
        BlockPos bestPos = null;

        for (int i = 0; i < 10; ++i) {
            BlockPos pos = posSupplier.get();
            if (pos != null) {
                double value = positionWeightFunction.applyAsDouble(pos);
                if (value > bestWeight) {
                    bestWeight = value;
                    bestPos = pos;
                }
            }
        }

        return bestPos != null ? Vec3.atBottomCenterOf(bestPos) : null;
    }

    @Nullable
    public static BlockPos generateRandomDirectionWithinRadians(RandomSource random, double minHorizontalDist,
            double maxHorizontalDist, int verticalDist, int flyingHeight, double xDir, double zDir,
            double maxXzRadiansFromDir) {
        double yRadiansCenter = Mth.atan2(zDir, xDir) - (float) (Math.PI / 2);
        double yRadians = yRadiansCenter + (double) (2.0F * random.nextFloat() - 1.0F) * maxXzRadiansFromDir;
        double dist = Mth.lerp(Math.sqrt(random.nextDouble()), minHorizontalDist, maxHorizontalDist)
                * (double) Mth.SQRT_OF_TWO;
        double xt = -dist * Math.sin(yRadians);
        double zt = dist * Math.cos(yRadians);
        if (!(Math.abs(xt) > maxHorizontalDist) && !(Math.abs(zt) > maxHorizontalDist)) {
            int yt = random.nextInt(2 * verticalDist + 1) - verticalDist + flyingHeight;
            return BlockPos.containing(xt, (double) yt, zt);
        } else {
            return null;
        }
    }

    @Nullable
    public static BlockPos generateRandomPosTowardDirection(Mob mob, int horizontalDist, BlockPos direction) {
        BlockPos pos = generateRandomPosTowardDirectionBase(mob, horizontalDist, mob.getRandom(), direction);
        return !isOutsideLimits(pos, mob)
                && !isNotStable(mob.getNavigation(), pos)
                && !hasMalus(mob, pos)
                        ? pos
                        : null;
    }

    public static BlockPos generateRandomPosTowardDirectionBase(Mob mob, double xzDist, RandomSource random,
            BlockPos direction) {
        double xt = (double) direction.getX();
        double zt = (double) direction.getZ();

        // Use Mob's home/restriction methods which are available on the base class
        // CORRECTED NAMES: hasHome(), getHomePosition()
        if (mob.hasHome() && xzDist > 1.0) {
            BlockPos center = mob.getHomePosition();
            if (mob.getX() > (double) center.getX()) {
                xt -= random.nextDouble() * xzDist / 2.0;
            } else {
                xt += random.nextDouble() * xzDist / 2.0;
            }

            if (mob.getZ() > (double) center.getZ()) {
                zt -= random.nextDouble() * xzDist / 2.0;
            } else {
                zt += random.nextDouble() * xzDist / 2.0;
            }
        }

        return BlockPos.containing(xt + mob.getX(), (double) direction.getY() + mob.getY(), zt + mob.getZ());
    }

    public static BlockPos moveUpToAboveSolid(BlockPos pos, int aboveSolidAmount, int maxY,
            Predicate<BlockPos> solidityTester) {
        if (aboveSolidAmount < 0) {
            throw new IllegalArgumentException("aboveSolidAmount was " + aboveSolidAmount + ", expected >= 0");
        } else if (!solidityTester.test(pos)) {
            return pos;
        } else {
            BlockPos.MutableBlockPos mutablePos = pos.mutable().move(Direction.UP);

            while (mutablePos.getY() <= maxY && solidityTester.test(mutablePos)) {
                mutablePos.move(Direction.UP);
            }

            int firstNonSolidY = mutablePos.getY();

            while (mutablePos.getY() <= maxY && mutablePos.getY() - firstNonSolidY < aboveSolidAmount) {
                mutablePos.move(Direction.UP);
                if (solidityTester.test(mutablePos)) {
                    mutablePos.move(Direction.DOWN);
                    break;
                }
            }

            return mutablePos.immutable();
        }
    }

    // --- GoalUtils Helpers for Mob ---

    public static boolean isOutsideLimits(BlockPos pos, Mob mob) {
        return mob.level().isOutsideBuildHeight(pos.getY());
    }

    public static boolean isNotStable(net.minecraft.world.entity.ai.navigation.PathNavigation navigation,
            BlockPos pos) {
        return !navigation.isStableDestination(pos);
    }

    public static boolean isWater(Mob mob, BlockPos pos) {
        return mob.level().getFluidState(pos).is(FluidTags.WATER);
    }

    public static boolean hasMalus(Mob mob, BlockPos pos) {
        return mob.getPathfindingMalus(WalkNodeEvaluator.getPathTypeStatic(mob, pos)) != 0.0F;
    }

    public static boolean isSolid(Mob mob, BlockPos pos) {
        return mob.level().getBlockState(pos).isSolid();
    }
}
