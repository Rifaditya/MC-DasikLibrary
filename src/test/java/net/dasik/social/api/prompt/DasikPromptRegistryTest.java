// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.prompt;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Headless unit verification suite for {@link DasikPromptRegistry}.
 * Validates registration contracts, dynamic enabling suppliers, mod query filtering, and defensive guards.
 */
class DasikPromptRegistryTest {

    @BeforeEach
    void setUp() {
        DasikPromptRegistry.clear();
    }

    @AfterEach
    void tearDown() {
        DasikPromptRegistry.clear();
    }

    @Test
    @DisplayName("Pillar 3: Verify basic registration and retrieval")
    void testBasicRegistrationAndRetrieval() {
        DasikPromptRegistry.register("betterdogs", "Better Dogs", "betterdogs:milestone/tame_all_breeds");

        assertTrue(DasikPromptRegistry.isRegistered("betterdogs:milestone/tame_all_breeds"));

        Optional<DasikPromptRegistry.PromptRegistration> opt = DasikPromptRegistry.get("betterdogs:milestone/tame_all_breeds");
        assertTrue(opt.isPresent(), "Registered prompt must be present");

        DasikPromptRegistry.PromptRegistration reg = opt.get();
        assertEquals("betterdogs", reg.modId());
        assertEquals("Better Dogs", reg.displayName());
        assertEquals("betterdogs:milestone/tame_all_breeds", reg.advancementId());
        assertTrue(reg.isEnabled(), "Unconditional registration must be enabled");
    }

    @Test
    @DisplayName("Pillar 3: Verify dynamic enabled supplier")
    void testDynamicEnabledSupplier() {
        AtomicBoolean flag = new AtomicBoolean(false);
        DasikPromptRegistry.register("agrarianreform", "Agrarian Reform", "agrarianreform:harvest_100", flag::get);

        Optional<DasikPromptRegistry.PromptRegistration> opt = DasikPromptRegistry.get("agrarianreform:harvest_100");
        assertTrue(opt.isPresent());

        assertFalse(opt.get().isEnabled(), "Must reflect dynamic supplier value (false)");

        flag.set(true);
        assertTrue(opt.get().isEnabled(), "Must reflect dynamic supplier value (true)");
    }

    @Test
    @DisplayName("Pillar 3: Verify getForMod filtering and getAll collection")
    void testModFilteringAndGetAll() {
        DasikPromptRegistry.register("betterdogs", "Better Dogs", "betterdogs:adv1");
        DasikPromptRegistry.register("betterdogs", "Better Dogs", "betterdogs:adv2");
        DasikPromptRegistry.register("othermod", "Other Mod", "othermod:adv1");

        Collection<DasikPromptRegistry.PromptRegistration> all = DasikPromptRegistry.getAll();
        assertEquals(3, all.size(), "Total registrations count must match");

        List<DasikPromptRegistry.PromptRegistration> betterDogsPrompts = DasikPromptRegistry.getForMod("betterdogs");
        assertEquals(2, betterDogsPrompts.size(), "Should find 2 prompts for betterdogs");

        List<DasikPromptRegistry.PromptRegistration> otherPrompts = DasikPromptRegistry.getForMod("othermod");
        assertEquals(1, otherPrompts.size(), "Should find 1 prompt for othermod");

        List<DasikPromptRegistry.PromptRegistration> missingPrompts = DasikPromptRegistry.getForMod("nonexistent");
        assertTrue(missingPrompts.isEmpty(), "Should return empty list for unknown mod");
    }

    @Test
    @DisplayName("Pillar 3: Verify input validation constraints")
    void testInputValidationConstraints() {
        assertThrows(IllegalArgumentException.class, () ->
                DasikPromptRegistry.register(null, "Name", "mod:adv"));

        assertThrows(IllegalArgumentException.class, () ->
                DasikPromptRegistry.register("", "Name", "mod:adv"));

        assertThrows(IllegalArgumentException.class, () ->
                DasikPromptRegistry.register("mod", "Name", null));

        assertThrows(IllegalArgumentException.class, () ->
                DasikPromptRegistry.register("mod", "Name", "  "));
    }

    @Test
    @DisplayName("Pillar 3: Verify clear method resets all registrations")
    void testClearRegistry() {
        DasikPromptRegistry.register("mod", "Mod", "mod:adv");
        assertEquals(1, DasikPromptRegistry.getAll().size());

        DasikPromptRegistry.clear();
        assertEquals(0, DasikPromptRegistry.getAll().size());
        assertFalse(DasikPromptRegistry.isRegistered("mod:adv"));
    }
}
