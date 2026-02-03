package net.dasik.social.api;

import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for all SocialEvents.
 * Follows "Freeze-on-Init" pattern: Mutable during mod init, Immutable during
 * gameplay.
 */
public class SocialEventRegistry {

    private static final Map<String, SocialEvent> PENDING = new ConcurrentHashMap<>();
    private static volatile ImmutableMap<String, SocialEvent> EVENTS = ImmutableMap.of();
    private static volatile boolean FROZEN = false;

    /**
     * Registers a new event. Must be called during initialization.
     */
    public static void register(SocialEvent event) {
        if (FROZEN) {
            throw new IllegalStateException(
                    "SocialEventRegistry is frozen! Register events during mod initialization.");
        }
        PENDING.put(event.getId(), event);
    }

    /**
     * Freezes the registry, making it immutable.
     * Called by the Dasik Library mod initializer.
     */
    public static void freeze() {
        EVENTS = ImmutableMap.copyOf(PENDING);
        FROZEN = true;
    }

    /**
     * Retrieves an event by ID.
     */
    @Nullable
    public static SocialEvent get(String id) {
        return EVENTS.get(id);
    }

    public static boolean isFrozen() {
        return FROZEN;
    }
}
