package net.dasik.social.api;

/**
 * Defines priority tiers for dynamic event tracks.
 * Each tier limits the number of concurrent events an entity can handle.
 */
public enum PriorityTier {
    /** Emergency behaviors. Max 2 tracks per entity. */
    CRITICAL(2),

    /** High importance (Mood/Emotional). Max 8 tracks per entity. */
    HIGH(8),

    /** Standard behaviors. Max 16 tracks per entity. */
    NORMAL(16),

    /** Background/Ambient behaviors. Max 32 tracks per entity. */
    LOW(32);

    private final int maxTracks;

    PriorityTier(int maxTracks) {
        this.maxTracks = maxTracks;
    }

    public int getMaxTracks() {
        return maxTracks;
    }
}
