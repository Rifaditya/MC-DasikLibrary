/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Entity.java (Snapshot 10)
 */
package net.dasik.social.api;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.Nullable;

/**
 * Registry for SocialEvents. Frozen after initialization to ensure constant-time lookups.
 */
public class SocialEventRegistry {
    private static final Map<String, SocialEvent> PENDING = new ConcurrentHashMap<>();
    private static volatile ImmutableMap<String, SocialEvent> EVENTS = ImmutableMap.of();
    private static volatile boolean FROZEN = false;

    public static void register(SocialEvent event) {
        if (FROZEN) {
            throw new IllegalStateException("SocialEventRegistry is frozen! Register events during mod initialization.");
        }
        PENDING.put(event.getId(), event);
    }

    public static void freeze() {
        if (!FROZEN) {
            EVENTS = ImmutableMap.copyOf(PENDING);
            FROZEN = true;
        }
    }

    @Nullable
    public static SocialEvent get(String id) {
        return EVENTS.get(id);
    }

    public static boolean isFrozen() {
        return FROZEN;
    }
}
