/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  org.jetbrains.annotations.Nullable
 */
package net.dasik.social.api;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.dasik.social.api.SocialEvent;
import org.jetbrains.annotations.Nullable;

public class SocialEventRegistry {
    private static final Map<String, SocialEvent> PENDING = new ConcurrentHashMap<String, SocialEvent>();
    private static volatile ImmutableMap<String, SocialEvent> EVENTS = ImmutableMap.of();
    private static volatile boolean FROZEN = false;

    public static void register(SocialEvent event) {
        if (FROZEN) {
            throw new IllegalStateException("SocialEventRegistry is frozen! Register events during mod initialization.");
        }
        PENDING.put(event.getId(), event);
    }

    public static void freeze() {
        EVENTS = ImmutableMap.copyOf(PENDING);
        FROZEN = true;
    }

    @Nullable
    public static SocialEvent get(String id) {
        return (SocialEvent)EVENTS.get((Object)id);
    }

    public static boolean isFrozen() {
        return FROZEN;
    }
}

