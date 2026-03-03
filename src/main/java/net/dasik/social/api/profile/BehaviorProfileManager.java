/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Mob.java (Snapshot 10)
 */
package net.dasik.social.api.profile;

import org.jetbrains.annotations.Nullable;

/**
 * Manages behavior profiles for a single entity.
 */
public interface BehaviorProfileManager {
    public void register(BehaviorProfile profile);

    public void unregister(String profileId);

    public void setActiveProfile(String profileId);

    @Nullable
    public String getActiveProfileId();

    @Nullable
    public BehaviorProfile getActiveProfile();

    public void markDirty();

    public void evaluateProfiles();

    public void tick();
}
