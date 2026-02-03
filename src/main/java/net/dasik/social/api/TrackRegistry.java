package net.dasik.social.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for dynamic scheduler tracks.
 * Mods must register their tracks here during initialization.
 * <p>
 * Format: "modid:track_name"
 */
public class TrackRegistry {

    private static final Map<String, PriorityTier> TRACKS = new ConcurrentHashMap<>();

    /**
     * Registers a new track.
     * 
     * @param trackId Unique ID (e.g. "better_dogs:mood").
     * @param tier    The priority tier for capacity planning.
     */
    public static void register(String trackId, PriorityTier tier) {
        if (TRACKS.containsKey(trackId)) {
            // Already registered? Just log warning or ignore if same tier.
            // For now, allow overwrite or idempotent re-registration.
            return;
        }
        TRACKS.put(trackId, tier);
    }

    /**
     * @return The tier of the requested track, or NORMAL if not found.
     */
    public static PriorityTier getTier(String trackId) {
        return TRACKS.getOrDefault(trackId, PriorityTier.NORMAL);
    }
}
