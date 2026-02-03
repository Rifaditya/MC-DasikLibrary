package net.dasik.social.api;

import net.dasik.social.signal.Signal;

/**
 * Public interface for the entity's social brain.
 * Accessible via {@link SocialEntity#dasik$getScheduler()}.
 */
public interface SocialScheduler {

    /**
     * Main social tick.
     */
    void tick();

    /**
     * Processes an incoming signal.
     * 
     * @param signal The received signal.
     */
    void onSignalReceived(Signal signal);

    /**
     * Checks if a specific event ID is currently active on any track.
     */
    boolean isEventActive(String eventId);
}
