/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Entity.java (Snapshot 10)
 */
package net.dasik.social.api;

/**
 * Defines the priority of a behavior track.
 */
public enum PriorityTier {
    CRITICAL(2),
    HIGH(8),
    NORMAL(16),
    LOW(32);

    private final int maxTracks;

    private PriorityTier(int maxTracks) {
        this.maxTracks = maxTracks;
    }

    public int getMaxTracks() {
        return this.maxTracks;
    }
}
