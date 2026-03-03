/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.api;

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

