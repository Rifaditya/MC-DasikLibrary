// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.test;

import net.dasik.social.api.profile.BehaviorCondition;
import net.dasik.social.api.profile.BehaviorProfile;
import net.dasik.social.api.profile.BehaviorProfileManager;
import net.dasik.social.api.profile.DefaultProfileBuilder;
import net.dasik.social.api.profile.ProfileAware;
import net.dasik.social.core.profile.DefaultProfileManager;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BehaviorProfileTest {

    @BeforeAll
    public static void setUpAll() {
        try {
            net.minecraft.SharedConstants.tryDetectVersion();
            net.minecraft.server.Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
    }

    private static class DummyGoal extends Goal {
        private final String name;

        public DummyGoal(String name) {
            this.name = name;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public String toString() {
            return "DummyGoal[" + name + "]";
        }
    }

    private static GoalSelector createGoalSelector() {
        try {
            return GoalSelector.class.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            try {
                Class<?> profilerClass = Class.forName("net.minecraft.util.profiling.InactiveProfiler");
                Object inactiveProfiler = profilerClass.getField("INSTANCE").get(null);
                java.util.function.Supplier<?> supplier = () -> inactiveProfiler;
                return (GoalSelector) GoalSelector.class.getConstructor(java.util.function.Supplier.class).newInstance(supplier);
            } catch (Exception ex) {
                throw new RuntimeException("Failed to instantiate GoalSelector", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate GoalSelector", e);
        }
    }

    @Test
    @DisplayName("Verify Profile Creation via DefaultProfileBuilder and condition bounds")
    public void testProfileCreationViaBuilder() {
        BehaviorProfile profile = BehaviorProfile.builder("foraging")
                .priority(15)
                .condition(mob -> true)
                .condition(mob -> false)
                .build();

        assertNotNull(profile);
        assertEquals("foraging", profile.getId());
        assertEquals(15, profile.getPriority());
        assertEquals(2, profile.getConditions().size());

        // Max 5 conditions enforcement
        BehaviorProfile.Builder fullBuilder = BehaviorProfile.builder("bounded");
        for (int i = 0; i < 5; i++) {
            fullBuilder.condition(mob -> true);
        }
        IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
            fullBuilder.condition(mob -> true);
        });
        assertTrue(ex.getMessage().contains("Max 5 conditions"));
    }

    @Test
    @DisplayName("Verify Goal Configuration, Application, and Removal on GoalSelector")
    public void testGoalConfigurationApplicationAndRemoval() {
        DummyGoal goal1 = new DummyGoal("wander");
        DummyGoal goal2 = new DummyGoal("look");

        BehaviorProfile profile = BehaviorProfile.builder("ambient")
                .priority(5)
                .goals(cfg -> {
                    cfg.add(1, goal1);
                    cfg.add(2, goal2);
                })
                .build();

        GoalSelector selector = createGoalSelector();
        assertEquals(0, selector.getAvailableGoals().size());

        // Apply goals
        profile.applyGoals(null, selector);
        assertEquals(2, selector.getAvailableGoals().size());

        // Remove goals
        profile.removeGoals(null, selector);
        assertEquals(0, selector.getAvailableGoals().size());
    }

    @Test
    @DisplayName("Verify BehaviorCondition Evaluation and Match Scores")
    public void testBehaviorConditionEvaluation() {
        BehaviorCondition alwaysTrue = mob -> true;
        BehaviorCondition alwaysFalse = mob -> false;

        assertTrue(alwaysTrue.test(null));
        assertFalse(alwaysFalse.test(null));

        ResourceKey<Level> overworldKey = Level.OVERWORLD;
        BehaviorCondition dimensionCondition = BehaviorCondition.inDimension(overworldKey);
        assertNotNull(dimensionCondition);
        // Null entity should safely evaluate to false without NPE
        assertFalse(dimensionCondition.test(null));

        BehaviorProfile profile = BehaviorProfile.builder("scored_profile")
                .condition(alwaysTrue)
                .condition(alwaysTrue)
                .condition(alwaysFalse)
                .build();

        assertEquals(2, profile.getMatchScore(null));
    }

    @Test
    @DisplayName("Verify Profile Registration, Unregistration, and Manual Activation")
    public void testProfileRegistrationAndLookup() {
        DefaultProfileManager manager = new DefaultProfileManager(null);

        assertNull(manager.getActiveProfileId());
        assertNull(manager.getActiveProfile());

        BehaviorProfile profileA = BehaviorProfile.builder("idle").priority(1).build();
        BehaviorProfile profileB = BehaviorProfile.builder("alert").priority(10).build();

        manager.register(profileA);
        manager.register(profileB);

        manager.setActiveProfile("idle");
        assertEquals("idle", manager.getActiveProfileId());
        assertEquals(profileA, manager.getActiveProfile());

        manager.setActiveProfile("alert");
        assertEquals("alert", manager.getActiveProfileId());
        assertEquals(profileB, manager.getActiveProfile());

        // Unregister active profile
        manager.unregister("alert");
        assertNull(manager.getActiveProfileId());
        assertNull(manager.getActiveProfile());
    }

    @Test
    @DisplayName("Verify Profile Evaluation and Priority Resolution")
    public void testProfileEvaluationAndPriorities() {
        DefaultProfileManager manager = new DefaultProfileManager(null);

        // Profile A: score 1, priority 5
        BehaviorProfile profileA = BehaviorProfile.builder("profileA")
                .priority(5)
                .condition(mob -> true)
                .build();

        // Profile B: score 1, priority 20 (wins tie-breaker against A)
        BehaviorProfile profileB = BehaviorProfile.builder("profileB")
                .priority(20)
                .condition(mob -> true)
                .build();

        // Profile C: score 0, priority 100 (loses due to 0 matches)
        BehaviorProfile profileC = BehaviorProfile.builder("profileC")
                .priority(100)
                .condition(mob -> false)
                .build();

        manager.register(profileA);
        manager.register(profileB);
        manager.register(profileC);

        manager.evaluateProfiles();
        assertEquals("profileB", manager.getActiveProfileId(), "Profile B should win due to higher priority with matching score");

        // Profile D: score 2, priority 1 (wins over B because match score takes precedence)
        BehaviorProfile profileD = BehaviorProfile.builder("profileD")
                .priority(1)
                .condition(mob -> true)
                .condition(mob -> true)
                .build();

        manager.register(profileD);
        manager.evaluateProfiles();
        assertEquals("profileD", manager.getActiveProfileId(), "Profile D should win due to higher match score (2 vs 1)");
    }

    @Test
    @DisplayName("Verify ProfileManager Tick Lifecycle and Dirty Flag Handling")
    public void testProfileManagerTick() {
        DefaultProfileManager manager = new DefaultProfileManager(null);

        BehaviorProfile profile = BehaviorProfile.builder("active")
                .priority(10)
                .condition(mob -> true)
                .build();

        manager.register(profile);
        assertNull(manager.getActiveProfileId(), "Profile should not be active before evaluate or tick");

        // Tick evaluates dirty manager
        manager.tick();
        assertEquals("active", manager.getActiveProfileId());

        // Subsequent tick with dirty = false does not throw or change state
        manager.tick();
        assertEquals("active", manager.getActiveProfileId());
    }

    @Test
    @DisplayName("Verify Fallback Behavior When Active Profile Is Removed")
    public void testFallbackOnUnregister() {
        DefaultProfileManager manager = new DefaultProfileManager(null);

        BehaviorProfile profile1 = BehaviorProfile.builder("primary")
                .priority(50)
                .condition(mob -> true)
                .build();

        BehaviorProfile profile2 = BehaviorProfile.builder("secondary_fallback")
                .priority(10)
                .condition(mob -> true)
                .build();

        manager.register(profile1);
        manager.register(profile2);

        manager.tick();
        assertEquals("primary", manager.getActiveProfileId());

        // Remove active primary profile
        manager.unregister("primary");
        assertNull(manager.getActiveProfileId());

        // Next tick/evaluation should seamlessly fallback to secondary profile
        manager.tick();
        assertEquals("secondary_fallback", manager.getActiveProfileId());
    }

    @Test
    @DisplayName("Verify ProfileAware Interface Contract")
    public void testProfileAwareInterface() {
        DefaultProfileManager manager = new DefaultProfileManager(null);
        ProfileAware awareEntity = new ProfileAware() {
            @Override
            public BehaviorProfileManager getProfileManager() {
                return manager;
            }

            @Override
            public boolean hasProfileSupport() {
                return true;
            }
        };

        assertTrue(awareEntity.hasProfileSupport());
        assertSame(manager, awareEntity.getProfileManager());
    }
}
