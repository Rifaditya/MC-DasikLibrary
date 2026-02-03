package net.dasik.social.api;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Main interface implemented by entities that want to participate in the Hive
 * Mind.
 * Typically injected via Mixin.
 */
public interface SocialEntity {

    long dasik$getDNA();

    String dasik$getSpeciesId();

    LivingEntity dasik$asEntity();

    float dasik$getSocialScale();

    @Nullable
    SocialScheduler dasik$getScheduler();

    /**
     * Reacts to a signal and optionally returns an event to schedule.
     * Thread-safe access required as this may be called from other threads.
     * 
     * @param signal The received signal.
     * @return An event instance to start, or null to ignore.
     */
    @Nullable
    default net.dasik.social.api.SocialEvent dasik$processSignal(net.dasik.social.signal.Signal signal) {
        return null;
    }

    /**
     * Gets the detection range for a specific signal type.
     * Override this to customize sensitivity (e.g., Bats hear DANGER further than
     * Wolves).
     * 
     * @param type The signal type.
     * @return The detection radius in blocks.
     */
    default double dasik$getSignalRange(SignalType type) {
        return type.getDefaultRange();
    }
}
