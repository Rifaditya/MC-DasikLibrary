package net.dasik.social.api.profile;

/**
 * Interface for entities that support behavior profiles.
 * Implement this on entity mixins to enable profile switching.
 * 
 * <pre>
 * ============================================
 * [GUIDE - DO NOT DELETE]
 * Implementation:
 * 1. Add @Unique BehaviorProfileManager field
 * 2. Initialize in constructor or first tick
 * 3. Call manager.tick() in entity tick
 * See: Doc/Develop/profile_guide.md
 * ============================================
 * </pre>
 */
public interface ProfileAware {

    /**
     * Gets the profile manager for this entity.
     */
    BehaviorProfileManager getProfileManager();

    /**
     * Checks if this entity has profile support initialized.
     */
    boolean hasProfileSupport();
}
