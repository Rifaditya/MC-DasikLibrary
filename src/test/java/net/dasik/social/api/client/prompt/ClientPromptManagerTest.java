// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.client.prompt;

import net.dasik.social.api.config.DasikClientConfig;
import net.dasik.social.api.prompt.DasikPromptRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Headless unit verification suite for {@link ClientPromptManager}.
 * Validates session lifecycle persistence, portal suppression, delay pacing, and opt-out guards.
 */
class ClientPromptManagerTest {

    @BeforeEach
    void setUp() {
        ClientPromptManager.onWorldDisconnect();
        DasikPromptRegistry.clear();
        DasikClientConfig.resetToDefaults();
    }

    @AfterEach
    void tearDown() {
        ClientPromptManager.onWorldDisconnect();
        DasikPromptRegistry.clear();
        DasikClientConfig.resetToDefaults();
    }

    @Test
    @DisplayName("Pillar 3: Verify portal transition preserves current session ID")
    void testPortalTransitionPreservesSessionId() {
        assertNull(ClientPromptManager.getCurrentSessionId());

        ClientPromptManager.onWorldJoin(null);
        String firstSession = ClientPromptManager.getCurrentSessionId();
        assertNotNull(firstSession, "Session ID must be generated on first join");

        // Simulate dimension change (Nether/End portal transition)
        ClientPromptManager.onWorldJoin(null);
        assertEquals(firstSession, ClientPromptManager.getCurrentSessionId(),
                "Session ID must be preserved across dimension changes to prevent portal spam");

        // Simulate disconnecting from world
        ClientPromptManager.onWorldDisconnect();
        assertNull(ClientPromptManager.getCurrentSessionId(),
                "Session ID must be null after disconnecting");
    }

    @Test
    @DisplayName("Pillar 3: Verify milestone queuing and 3-second (60 ticks) delay")
    void testMilestoneQueuingAndDelay() {
        DasikPromptRegistry.register("betterdogs", "Better Dogs", "betterdogs:tame_wolf");

        ClientPromptManager.onAdvancementCompleted("betterdogs:tame_wolf");

        assertFalse(ClientPromptManager.getPendingQueue().isEmpty(), "Prompt must be queued");
        ClientPromptManager.QueuedMilestone queued = ClientPromptManager.getPendingQueue().peek();
        assertNotNull(queued);
        assertEquals(ClientPromptManager.MILESTONE_INITIAL_DELAY, queued.getDelayTicks(),
                "Initial delay must be 60 ticks (3 seconds)");
        assertEquals("betterdogs:tame_wolf", queued.getRegistration().advancementId());
    }

    @Test
    @DisplayName("Pillar 3: Verify duplicate advancement triggers are suppressed in same session")
    void testDuplicateAdvancementSuppression() {
        DasikPromptRegistry.register("betterdogs", "Better Dogs", "betterdogs:tame_wolf");

        ClientPromptManager.onAdvancementCompleted("betterdogs:tame_wolf");
        assertEquals(1, ClientPromptManager.getPendingQueue().size());

        // Second trigger in the same session
        ClientPromptManager.onAdvancementCompleted("betterdogs:tame_wolf");
        assertEquals(1, ClientPromptManager.getPendingQueue().size(),
                "Duplicate advancement in same session must be suppressed");
    }

    @Test
    @DisplayName("Pillar 3: Verify opt-out toggle suppresses prompt queuing")
    void testOptOutSuppressesPrompts() {
        DasikClientConfig.get().showSupportPrompts = false;
        DasikPromptRegistry.register("betterdogs", "Better Dogs", "betterdogs:tame_wolf");

        ClientPromptManager.onAdvancementCompleted("betterdogs:tame_wolf");
        assertTrue(ClientPromptManager.getPendingQueue().isEmpty(),
                "Pending queue must remain empty when showSupportPrompts is disabled");
    }

    @Test
    @DisplayName("Pillar 5: Verify timing constants adherence to specification")
    void testTimingConstants() {
        assertEquals(160, ClientPromptManager.GREETING_DELAY_TICKS, "Greeting delay must be 160 ticks (~8s)");
        assertEquals(60, ClientPromptManager.MILESTONE_INITIAL_DELAY, "Milestone delay must be 60 ticks (3s)");
        assertEquals(6000, ClientPromptManager.MILESTONE_PACING_COOLDOWN, "Pacing cooldown must be 6000 ticks (5m)");
    }
}
