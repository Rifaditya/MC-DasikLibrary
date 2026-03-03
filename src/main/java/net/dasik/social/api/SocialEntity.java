/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  org.jetbrains.annotations.Nullable
 */
package net.dasik.social.api;

import net.dasik.social.api.SignalType;
import net.dasik.social.api.SocialEvent;
import net.dasik.social.api.SocialScheduler;
import net.dasik.social.signal.Signal;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public interface SocialEntity {
    public long dasik$getDNA();

    public String dasik$getSpeciesId();

    public LivingEntity dasik$asEntity();

    public float dasik$getSocialScale();

    @Nullable
    public SocialScheduler dasik$getScheduler();

    @Nullable
    default public SocialEvent dasik$processSignal(Signal signal) {
        return null;
    }

    default public double dasik$getSignalRange(SignalType type) {
        return type.getDefaultRange();
    }
}

