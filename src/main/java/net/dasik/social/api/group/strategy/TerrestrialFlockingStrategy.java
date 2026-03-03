/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.ai.navigation.PathNavigation
 */
package net.dasik.social.api.group.strategy;

import net.dasik.social.api.group.strategy.FlockingStrategy;
import net.dasik.social.api.group.strategy.GroupParameters;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class TerrestrialFlockingStrategy
implements FlockingStrategy {
    @Override
    public void execute(LivingEntity mob, LivingEntity leader, GroupParameters params) {
        Mob groundedMob;
        block9: {
            block8: {
                if (!(mob instanceof Mob)) break block8;
                groundedMob = (Mob)mob;
                if (leader != null) break block9;
            }
            return;
        }
        double distSq = groundedMob.distanceToSqr((Entity)leader);
        PathNavigation navigation = groundedMob.getNavigation();
        if (distSq < (double)(params.separationRadius() * params.separationRadius())) {
            navigation.stop();
            return;
        }
        if (distSq > (double)(params.cohesionRadius() * params.cohesionRadius())) {
            double speedMod = params.maxSpeed();
            if (distSq > (double)(params.cohesionRadius() * 2.0f * (params.cohesionRadius() * 2.0f))) {
                speedMod *= 1.5;
            }
            navigation.moveTo((Entity)leader, speedMod);
        } else if (groundedMob.getRandom().nextInt(10) == 0) {
            navigation.stop();
            groundedMob.getLookControl().setLookAt((Entity)leader, 10.0f, (float)groundedMob.getMaxHeadXRot());
        }
    }
}

