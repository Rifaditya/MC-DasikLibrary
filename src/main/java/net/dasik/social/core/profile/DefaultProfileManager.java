/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: Mob.java (Snapshot 10)
 */
package net.dasik.social.core.profile;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import net.dasik.social.api.profile.BehaviorProfile;
import net.dasik.social.api.profile.BehaviorProfileManager;
import net.dasik.social.mixin.MobGoalAccessor;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import org.jetbrains.annotations.Nullable;

public class DefaultProfileManager implements BehaviorProfileManager {
    private final Mob mob;
    private final Map<String, BehaviorProfile> profiles = new HashMap<>();
    
    @Nullable
    private String activeProfileId;
    private boolean dirty = true;

    public DefaultProfileManager(Mob mob) {
        this.mob = mob;
    }

    @Override
    public void register(BehaviorProfile profile) {
        this.profiles.put(profile.getId(), profile);
        this.markDirty();
    }

    @Override
    public void unregister(String profileId) {
        BehaviorProfile removed = this.profiles.remove(profileId);
        if (removed != null && profileId.equals(this.activeProfileId)) {
            removed.removeGoals(this.mob, this.getGoalSelector());
            this.activeProfileId = null;
            this.markDirty();
        }
    }

    @Override
    public void setActiveProfile(String profileId) {
        if (profileId.equals(this.activeProfileId)) {
            return;
        }
        this.switchToProfile(profileId);
    }

    @Override
    @Nullable
    public String getActiveProfileId() {
        return this.activeProfileId;
    }

    @Override
    @Nullable
    public BehaviorProfile getActiveProfile() {
        return this.activeProfileId != null ? this.profiles.get(this.activeProfileId) : null;
    }

    @Override
    public void markDirty() {
        this.dirty = true;
    }

    @Override
    public void evaluateProfiles() {
        if (this.profiles.isEmpty()) {
            return;
        }
        BehaviorProfile best = this.profiles.values().stream()
            .max(Comparator.comparingInt((BehaviorProfile p) -> p.getMatchScore(this.mob))
            .thenComparingInt(BehaviorProfile::getPriority))
            .orElse(null);
        if (best != null && !best.getId().equals(this.activeProfileId)) {
            this.switchToProfile(best.getId());
        }
    }

    @Override
    public void tick() {
        if (this.dirty) {
            this.dirty = false;
            this.evaluateProfiles();
        }
    }

    private void switchToProfile(String newProfileId) {
        BehaviorProfile newProfile;
        BehaviorProfile old;
        if (this.activeProfileId != null && (old = this.profiles.get(this.activeProfileId)) != null) {
            old.removeGoals(this.mob, this.getGoalSelector());
        }
        if ((newProfile = this.profiles.get(newProfileId)) != null) {
            newProfile.applyGoals(this.mob, this.getGoalSelector());
            this.activeProfileId = newProfileId;
        }
    }

    private GoalSelector getGoalSelector() {
        return ((MobGoalAccessor)this.mob).dasik$getGoalSelector();
    }
}
