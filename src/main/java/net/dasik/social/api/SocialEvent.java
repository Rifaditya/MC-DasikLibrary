/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.api;

import net.dasik.social.api.SocialEntity;
import net.dasik.social.api.TickContext;

public interface SocialEvent {
    public String getId();

    public int getPriorityValue();

    public String getTrackId();

    public boolean canPreempt(SocialEvent var1);

    public void onStart(TickContext var1);

    public boolean tick(TickContext var1);

    public void onEnd(SocialEntity var1, EndReason var2);

    public static enum EndReason {
        EXPIRED,
        PREEMPTED,
        CANCELLED,
        ENTITY_DIED;

    }
}

