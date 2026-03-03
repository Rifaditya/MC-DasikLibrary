/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.Nullable
 */
package net.dasik.social.api.profile;

import net.dasik.social.api.profile.BehaviorProfile;
import org.jetbrains.annotations.Nullable;

public interface BehaviorProfileManager {
    public void register(BehaviorProfile var1);

    public void unregister(String var1);

    public void setActiveProfile(String var1);

    @Nullable
    public String getActiveProfileId();

    @Nullable
    public BehaviorProfile getActiveProfile();

    public void markDirty();

    public void evaluateProfiles();

    public void tick();
}

