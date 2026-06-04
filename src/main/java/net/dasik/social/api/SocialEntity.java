/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: LivingEntity.java (Snapshot 10)
 */
package net.dasik.social.api;

import net.dasik.social.api.SignalType;
import net.dasik.social.signal.Signal;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

/**
 * Interface injected into entities to enable social behaviors and signal processing.
 */
public interface SocialEntity {
    /** @return Unique genetic/personality seed for this entity. */
    public long dasik$getDNA();

    /** @return String ID representing the species (e.g., "minecraft:zombie"). */
    public String dasik$getSpeciesId();

    /** @return The underlying Minecraft entity. */
    public LivingEntity dasik$asEntity();

    /** @return Visual or behavioral scale factor. */
    public float dasik$getSocialScale();

    /** @return The entity's individual social scheduler. */
    @Nullable
    public SocialScheduler dasik$getScheduler();

    /**
     * Entry point for signal processing.
     * @param signal The received signal.
     * @return A SocialEvent to start in response, or null.
     */
    @Nullable
    default public SocialEvent dasik$processSignal(Signal signal) {
        return null;
    }

    /**
     * @param type The signal type.
     * @return The maximum distance this entity can perceive this signal type.
     */
    default public double dasik$getSignalRange(SignalType type) {
        return type.getDefaultRange();
    }
}

