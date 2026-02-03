package net.dasik.social.signal;

import net.dasik.social.api.Scope;
import net.dasik.social.api.SignalType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/**
 * data carrier for a social signal.
 * designed to be pooled and reused to avoid allocation pressure.
 */
public class Signal {
    private SignalType type;
    private Vec3 origin;
    private LivingEntity source;
    private Scope scope;

    // Additional context if needed

    public Signal() {
        // Pooled object
    }

    public void init(SignalType type, Vec3 origin, @Nullable LivingEntity source, Scope scope) {
        this.type = type;
        this.origin = origin;
        this.source = source;
        this.scope = scope;
    }

    public SignalType getType() {
        return type;
    }

    public Vec3 getOrigin() {
        return origin;
    }

    @Nullable
    public LivingEntity getSource() {
        return source;
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public String toString() {
        return "Signal{type=" + type + ", origin=" + origin + "}";
    }
}
