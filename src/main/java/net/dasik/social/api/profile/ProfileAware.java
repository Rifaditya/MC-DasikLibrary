// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Mob.java (Snapshot 10)
package net.dasik.social.api.profile;

/**
 * Interface for entities that support dynamic behavior profiles.
 */
public interface ProfileAware {
    public BehaviorProfileManager getProfileManager();

    public boolean hasProfileSupport();
}
