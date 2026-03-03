/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.phys.Vec3
 *  org.jetbrains.annotations.Nullable
 */
package net.dasik.social.signal;

import net.dasik.social.api.Scope;
import net.dasik.social.api.SignalType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class Signal {
    private SignalType type;
    private Vec3 origin;
    private LivingEntity source;
    private Scope scope;

    public void init(SignalType type, Vec3 origin, @Nullable LivingEntity source, Scope scope) {
        this.type = type;
        this.origin = origin;
        this.source = source;
        this.scope = scope;
    }

    public SignalType getType() {
        return this.type;
    }

    public Vec3 getOrigin() {
        return this.origin;
    }

    @Nullable
    public LivingEntity getSource() {
        return this.source;
    }

    public Scope getScope() {
        return this.scope;
    }

    public String toString() {
        return "Signal{type=" + String.valueOf(this.type) + ", origin=" + String.valueOf(this.origin) + "}";
    }
}

