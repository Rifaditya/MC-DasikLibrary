/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Entity.java (Snapshot 10)
 */
package net.dasik.social.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global registry for behavior tracks and their associated priority tiers.
 */
public class TrackRegistry {
    private static final Map<String, PriorityTier> TRACKS = new ConcurrentHashMap<>();

    public static void register(String trackId, PriorityTier tier) {
        TRACKS.putIfAbsent(trackId, tier);
    }

    public static PriorityTier getTier(String trackId) {
        return TRACKS.getOrDefault(trackId, PriorityTier.NORMAL);
    }
}
