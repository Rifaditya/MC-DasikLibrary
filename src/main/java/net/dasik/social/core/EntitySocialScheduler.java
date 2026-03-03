/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.RandomSource
 */
package net.dasik.social.core;
// Verified against: ServerLevel.java (Snapshot 10)


import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.dasik.social.api.SocialEntity;
import net.dasik.social.api.SocialEvent;
import net.dasik.social.api.SocialScheduler;
import net.dasik.social.api.TickContext;
import net.dasik.social.signal.Signal;
import net.dasik.social.util.FastRandom;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

public class EntitySocialScheduler
implements SocialScheduler {
    private final SocialEntity socialEntity;
    private final Map<String, SocialEvent> activeTracks = new ConcurrentHashMap<String, SocialEvent>();
    private final Queue<SocialEvent> inbox = new ConcurrentLinkedQueue<SocialEvent>();

    public EntitySocialScheduler(SocialEntity entity) {
        this.socialEntity = entity;
    }

    @Override
    public void onSignalReceived(Signal signal) {
        if (signal == null) {
            return;
        }
        SocialEvent event = this.socialEntity.dasik$processSignal(signal);
        if (event != null) {
            this.inbox.offer(event);
        }
    }

    public void schedule(SocialEvent event) {
        if (event != null) {
            this.inbox.offer(event);
        }
    }

    @Override
    public boolean isEventActive(String eventId) {
        for (SocialEvent event : this.activeTracks.values()) {
            if (!event.getId().equals(eventId)) continue;
            return true;
        }
        return false;
    }

    public boolean isIdle() {
        return this.activeTracks.isEmpty();
    }

    @Override
    public void tick() {
        this.processInbox();
        this.tickActiveEvents();
    }

    private void processInbox() {
        SocialEvent next;
        while ((next = this.inbox.poll()) != null) {
            this.tryStartEvent(next);
        }
    }

    private void tryStartEvent(SocialEvent newEvent) {
        String trackId = newEvent.getTrackId();
        SocialEvent current = this.activeTracks.get(trackId);
        if (current == null) {
            this.start(newEvent);
        } else if (newEvent.getPriorityValue() >= current.getPriorityValue() && newEvent.canPreempt(current)) {
            current.onEnd(this.socialEntity, SocialEvent.EndReason.PREEMPTED);
            this.activeTracks.remove(trackId);
            this.start(newEvent);
        }
    }

    private void start(SocialEvent event) {
        this.activeTracks.put(event.getTrackId(), event);
        TickContext ctx = this.createTickContext();
        event.onStart(ctx);
    }

    private void tickActiveEvents() {
        if (this.activeTracks.isEmpty()) {
            return;
        }
        TickContext ctx = this.createTickContext();
        this.activeTracks.values().removeIf(event -> {
            boolean finished = event.tick(ctx);
            if (finished) {
                event.onEnd(this.socialEntity, SocialEvent.EndReason.EXPIRED);
                return true;
            }
            return false;
        });
    }

    private TickContext createTickContext() {
        final ServerLevel level = (ServerLevel)this.socialEntity.dasik$asEntity().level();
        return new TickContext(){

            @Override
            public SocialEntity entity() {
                return EntitySocialScheduler.this.socialEntity;
            }

            @Override
            public long gameTime() {
                return level.getGameTime();
            }

            @Override
            public float partialTick() {
                return 1.0f;
            }

            @Override
            public RandomSource random() {
                return FastRandom.INSTANCE;
            }
        };
    }
}

