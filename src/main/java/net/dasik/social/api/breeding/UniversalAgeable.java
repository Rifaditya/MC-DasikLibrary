package net.dasik.social.api.breeding;

/**
 * Interface injected into Mobs to support "Ageable-like" behavior for
 * non-Ageable entities (e.g., Bats).
 * This allows defining "Babies" for entities that don't natively support it.
 */
public interface UniversalAgeable {
    boolean isUniversalBaby();

    void setUniversalBaby(boolean isBaby);

    default boolean isInLove() {
        return false;
    }

    default void setInLove(int ticks) {
    }

    /**
     * Optional: Get the age in ticks.
     * Default implementation can return 0 if not tracked.
     */
    default int getUniversalAge() {
        return 0;
    }

    /**
     * Optional: Set the age in ticks.
     */
    default void setUniversalAge(int age) {
    }

    /**
     * Allows AI goals to signal when legacy movement AI (customServerAiStep) should
     * be suppressed.
     */
    default boolean isMovementSuppressed() {
        return false;
    }

    default void setMovementSuppressed(boolean suppressed) {
    }

    /**
     * Called when a social goal (breed, tempt) starts.
     * Consumers can override this to handle entity-specific logic
     * (e.g., waking a resting Bat).
     */
    default void onSocialGoalStart() {
    }

    /**
     * Called when a social goal (breed, tempt) stops.
     */
    default void onSocialGoalStop() {
    }
}
