package net.dasik.social.api;

import org.jetbrains.annotations.Nullable;

/**
 * Defines a social behavior or action.
 * Contracts are future-proofed using {@link TickContext}.
 */
public interface SocialEvent {

    /**
     * @return Unique ID of the event (e.g. "better_dogs:howl").
     */
    String getId();

    /**
     * @return Priority value (0-1000). Higher preempts lower.
     */
    int getPriorityValue();

    /**
     * @return The ID of the track this event runs on (e.g. "mood", "ambient").
     *         Supports dynamic tracks registered by other mods.
     */
    String getTrackId();

    /**
     * @return True if this event can interrupt the provided running event.
     */
    boolean canPreempt(SocialEvent other);

    // --- Lifecycle ---

    /**
     * Called when the event starts.
     * 
     * @param context Context object containing entity, time, random, etc.
     */
    void onStart(TickContext context);

    /**
     * Called every tick.
     * 
     * @param context Context object.
     * @return True if the event is complete and should end.
     */
    boolean tick(TickContext context);

    /**
     * Called when the event ends.
     * 
     * @param entity The entity.
     * @param reason Why the event ended.
     */
    void onEnd(SocialEntity entity, EndReason reason);

    enum EndReason {
        EXPIRED,
        PREEMPTED,
        CANCELLED,
        ENTITY_DIED
    }
}
