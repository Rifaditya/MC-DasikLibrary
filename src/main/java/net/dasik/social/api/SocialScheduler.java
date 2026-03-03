/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.api;

import net.dasik.social.signal.Signal;

public interface SocialScheduler {
    public void tick();

    public void onSignalReceived(Signal var1);

    public boolean isEventActive(String var1);
}

