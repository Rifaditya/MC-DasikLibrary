/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.dasik.social.api.PriorityTier;

public class TrackRegistry {
    private static final Map<String, PriorityTier> TRACKS = new ConcurrentHashMap<String, PriorityTier>();

    public static void register(String trackId, PriorityTier tier) {
        if (TRACKS.containsKey(trackId)) {
            return;
        }
        TRACKS.put(trackId, tier);
    }

    public static PriorityTier getTier(String trackId) {
        return TRACKS.getOrDefault(trackId, PriorityTier.NORMAL);
    }
}

