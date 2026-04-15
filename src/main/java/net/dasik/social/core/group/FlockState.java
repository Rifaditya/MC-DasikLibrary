/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.core.group;

import net.minecraft.world.phys.Vec3;

/**
 * Data class representing the aggregated state of a flock/group.
 * Stored on the leader to prevent O(N²) calculations.
 */
public class FlockState {
    private Vec3 centerOfMass = Vec3.ZERO;
    private Vec3 averageVelocity = Vec3.ZERO;
    private long lastUpdateTime = 0;

    public Vec3 getCenterOfMass() {
        return centerOfMass;
    }

    public void setCenterOfMass(Vec3 centerOfMass) {
        this.centerOfMass = centerOfMass;
    }

    public Vec3 getAverageVelocity() {
        return averageVelocity;
    }

    public void setAverageVelocity(Vec3 averageVelocity) {
        this.averageVelocity = averageVelocity;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
