/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: GoalSelector.java (Snapshot 10)
 */
package net.dasik.social.api;

import net.dasik.social.signal.Signal;

/**
 * Manages the lifecycle and execution of SocialEvents for an entity.
 */
public interface SocialScheduler {
    /** Run a single logic tick. */
    public void tick();

    /** Handle an incoming social signal. */
    public void onSignalReceived(Signal signal);

    /** @return True if an event with the given ID is currently active. */
    public boolean isEventActive(String eventId);
}

