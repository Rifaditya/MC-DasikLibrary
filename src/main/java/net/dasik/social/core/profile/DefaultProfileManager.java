package net.dasik.social.core.profile;

import net.dasik.social.api.profile.BehaviorProfile;
import net.dasik.social.api.profile.BehaviorProfileManager;
import net.dasik.social.mixin.MobGoalAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of BehaviorProfileManager.
 * 
 * <pre>
 * ============================================
 * [GUIDE - DO NOT DELETE]
 * Usage:
 * 1. Create manager: new DefaultProfileManager(mob)
 * 2. Register profiles: manager.register(profile)
 * 3. Trigger evaluation on events
 * 4. Call tick() in entity tick (only processes if dirty)
 * See: Doc/Develop/profile_guide.md
 * ============================================
 * </pre>
 */
public class DefaultProfileManager implements BehaviorProfileManager {

    private final Mob mob;
    private final Map<String, BehaviorProfile> profiles = new HashMap<>();
    private @Nullable String activeProfileId;
    private boolean dirty = true; // Dirty on init to evaluate first profile

    public DefaultProfileManager(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void register(BehaviorProfile profile) {
        profiles.put(profile.getId(), profile);
        markDirty();
    }

    @Override
    public void unregister(String profileId) {
        BehaviorProfile removed = profiles.remove(profileId);
        if (removed != null && profileId.equals(activeProfileId)) {
            removed.removeGoals(mob, getGoalSelector());
            activeProfileId = null;
            markDirty();
        }
    }

    @Override
    public void setActiveProfile(String profileId) {
        if (profileId.equals(activeProfileId))
            return;
        switchToProfile(profileId);
    }

    @Override
    public @Nullable String getActiveProfileId() {
        return activeProfileId;
    }

    @Override
    public @Nullable BehaviorProfile getActiveProfile() {
        return activeProfileId != null ? profiles.get(activeProfileId) : null;
    }

    @Override
    public void markDirty() {
        this.dirty = true;
    }

    @Override
    public void evaluateProfiles() {
        if (profiles.isEmpty())
            return;

        // Find best matching profile
        BehaviorProfile best = profiles.values().stream()
                .max(Comparator
                        .comparingInt((BehaviorProfile p) -> p.getMatchScore(mob))
                        .thenComparingInt(BehaviorProfile::getPriority))
                .orElse(null);

        if (best != null && !best.getId().equals(activeProfileId)) {
            switchToProfile(best.getId());
        }
    }

    @Override
    public void tick() {
        if (dirty) {
            dirty = false;
            evaluateProfiles();
        }
    }

    private void switchToProfile(String newProfileId) {
        // Remove old profile goals
        if (activeProfileId != null) {
            BehaviorProfile old = profiles.get(activeProfileId);
            if (old != null) {
                old.removeGoals(mob, getGoalSelector());
            }
        }

        // Apply new profile goals
        BehaviorProfile newProfile = profiles.get(newProfileId);
        if (newProfile != null) {
            newProfile.applyGoals(mob, getGoalSelector());
            activeProfileId = newProfileId;
        }
    }

    private GoalSelector getGoalSelector() {
        return ((MobGoalAccessor) mob).dasik$getGoalSelector();
    }
}
