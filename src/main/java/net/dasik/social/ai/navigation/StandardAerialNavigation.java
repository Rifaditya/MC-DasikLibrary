package net.dasik.social.ai.navigation;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathType;

/**
 * A standardized navigation for aerial mobs that ensures they fly properly
 * and do not hug the ground.
 * <p>
 * Key behaviors:
 * <ul>
 * <li><b>Ground Penalty</b>: {@code WALKABLE} has a cost of 4.0F to discourage
 * walking.</li>
 * <li><b>Air Preference</b>: {@code OPEN} (Air) has a cost of 0.0F
 * (Default).</li>
 * <li><b>Hazard Avoidance</b>: Fire and Lava are blocked (-1.0F).</li>
 * <li><b>Water Avoidance</b>: Water is penalized (8.0F) but traversable if
 * necessary.</li>
 * <li><b>Door Handling</b>: Cannot open doors (typical for bats/birds).</li>
 * </ul>
 */
public class StandardAerialNavigation extends FlyingPathNavigation {

    public StandardAerialNavigation(Mob mob, Level level) {
        super(mob, level);
        this.setCanOpenDoors(false);
        this.setCanFloat(true);
        this.applyAerialPenalties();
    }

    /**
     * Apply standard penalties to force aerial pathing.
     */
    public void applyAerialPenalties() {
        this.mob.setPathfindingMalus(PathType.WALKABLE, 4.0F);
        this.mob.setPathfindingMalus(PathType.WATER_BORDER, 0.0F);
        this.mob.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.mob.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.mob.setPathfindingMalus(PathType.LAVA, -1.0F);
    }
}
