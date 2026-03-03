/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.api.profile;

import net.dasik.social.api.profile.BehaviorProfileManager;

public interface ProfileAware {
    public BehaviorProfileManager getProfileManager();

    public boolean hasProfileSupport();
}

