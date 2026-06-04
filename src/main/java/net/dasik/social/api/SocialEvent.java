/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Goal.java (Snapshot 10)
 */
package net.dasik.social.api;

/**
 * Represents a discrete behavioral task or state triggered by the social system.
 */
public interface SocialEvent {
    /** @return Registry ID of the event. */
    public String getId();

    /** @return Execution priority. Higher values preempt lower values on the same track. */
    public int getPriorityValue();

    /** @return Track ID for mutual exclusion. */
    public String getTrackId();

    /** @return True if this event can preempt the specified running event. */
    public boolean canPreempt(SocialEvent otherEvent);

    /** Called when the event is first scheduled. */
    public void onStart(TickContext context);

    /** 
     * Called every tick while active.
     * @return True if the event should continue, false if it should terminate.
     */
    public boolean tick(TickContext context);

    /** Called when the event ends. */
    public void onEnd(SocialEntity entity, EndReason reason);

    public enum EndReason {
        EXPIRED,
        PREEMPTED,
        CANCELLED,
        ENTITY_DIED
    }
}

