package net.dasik.social.core;

import net.dasik.social.api.*;
import net.dasik.social.signal.Signal;
import net.dasik.social.util.FastRandom;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The Brain of the Hive Mind entity.
 * Handles the actual execution of tasks on tracks.
 * Thread-safe: Signals can be received from any thread, but tick() must run on
 * Main Thread.
 */
public class EntitySocialScheduler implements SocialScheduler {

    private final SocialEntity socialEntity;

    // Active events by Track ID
    private final Map<String, SocialEvent> activeTracks = new ConcurrentHashMap<>();

    // Thread-safe inbox for incoming signals/events from other threads
    private final Queue<SocialEvent> inbox = new ConcurrentLinkedQueue<>();

    public EntitySocialScheduler(SocialEntity entity) {
        this.socialEntity = entity;
    }

    @Override
    public void onSignalReceived(Signal signal) {
        if (signal == null)
            return;
        SocialEvent event = socialEntity.dasik$processSignal(signal);
        if (event != null) {
            inbox.offer(event);
        }
    }

    /**
     * Manual schedule (e.g. from AI goals)
     */
    public void schedule(SocialEvent event) {
        if (event != null)
            inbox.offer(event);
    }

    @Override
    public boolean isEventActive(String eventId) {
        for (SocialEvent event : activeTracks.values()) {
            if (event.getId().equals(eventId)) {
                return true;
            }
        }
        return false;
    }

    public boolean isIdle() {
        return activeTracks.isEmpty();
    }

    @Override
    public void tick() {
        // Enforce Main Thread (implicitly, as tick is called by entity)
        // 1. Process Inbox (New events)
        processInbox();

        // 2. Tick Active Events
        tickActiveEvents();
    }

    private void processInbox() {
        SocialEvent next;
        while ((next = inbox.poll()) != null) {
            tryStartEvent(next);
        }
    }

    private void tryStartEvent(SocialEvent newEvent) {
        String trackId = newEvent.getTrackId();
        SocialEvent current = activeTracks.get(trackId);

        if (current == null) {
            // Track free, start immediately
            start(newEvent);
        } else {
            // Track busy, check priority
            if (newEvent.getPriorityValue() >= current.getPriorityValue() && newEvent.canPreempt(current)) {
                // Preempt
                current.onEnd(socialEntity, SocialEvent.EndReason.PREEMPTED);
                activeTracks.remove(trackId);
                start(newEvent);
            } else {
                // Dropped (Lower priority)
                // Lower priority: silently dropped (by design)
            }
        }
    }

    private void start(SocialEvent event) {
        activeTracks.put(event.getTrackId(), event);
        TickContext ctx = createTickContext();
        event.onStart(ctx);
    }

    private void tickActiveEvents() {
        if (activeTracks.isEmpty())
            return;

        TickContext ctx = createTickContext();

        // RemoveIf allows safe modification during iteration
        activeTracks.values().removeIf(event -> {
            boolean finished = event.tick(ctx);
            if (finished) {
                event.onEnd(socialEntity, SocialEvent.EndReason.EXPIRED);
                return true;
            }
            return false;
        });
    }

    private TickContext createTickContext() {
        ServerLevel level = (ServerLevel) socialEntity.dasik$asEntity().level();
        return new TickContext() {
            @Override
            public SocialEntity entity() {
                return socialEntity;
            }

            @Override
            public long gameTime() {
                return level.getGameTime();
            }

            @Override
            public float partialTick() {
                return 1.0f;
            } // Always 1.0 for server ticks

            @Override
            public net.minecraft.util.RandomSource random() {
                return FastRandom.INSTANCE;
            }
        };
    }
}
