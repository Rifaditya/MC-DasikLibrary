package net.dasik.social.api.profile;

import org.jetbrains.annotations.Nullable;

/**
 * Manages behavior profiles for an entity.
 * 
 * <pre>
 * ============================================
 * [GUIDE - DO NOT DELETE]
 * Trigger Events (event-driven, NOT polling):
 * - Dimension change → evaluateProfiles()
 * - Biome change → evaluateProfiles()
 * - State change → markDirty()
 * - Manual → setActiveProfile(id)
 * See: Doc/Develop/profile_guide.md
 * ============================================
 * </pre>
 */
public interface BehaviorProfileManager {

    /**
     * Registers a profile. Can be called multiple times.
     */
    void register(BehaviorProfile profile);

    /**
     * Removes a profile by ID.
     */
    void unregister(String profileId);

    /**
     * Forces manual profile switch.
     */
    void setActiveProfile(String profileId);

    /**
     * Gets currently active profile ID.
     */
    @Nullable
    String getActiveProfileId();

    /**
     * Gets the active profile.
     */
    @Nullable
    BehaviorProfile getActiveProfile();

    /**
     * Marks manager as dirty; will re-evaluate on next tick.
     * Use for state-based condition changes.
     */
    void markDirty();

    /**
     * Evaluates all profiles and switches to best match.
     * Called by trigger events.
     */
    void evaluateProfiles();

    /**
     * Called each tick. Only processes if dirty.
     */
    void tick();
}
