/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Signal.java (Snapshot 10)
 */
package net.dasik.social.signal;

import net.dasik.social.api.Scope;
import net.dasik.social.api.SignalType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * Immutable signal record for entity communication.
 * Java 25 records provide optimal performance and thread-safety for high-frequency broadcasting.
 */
public record Signal(
    SignalType type,
    Vec3 origin,
    @Nullable LivingEntity source,
    Scope scope
) {
    @Override
    public String toString() {
        return "Signal{type=" + type + ", origin=" + origin + ", source=" + (source != null ? source.getName().getString() : "null") + "}";
    }
}

