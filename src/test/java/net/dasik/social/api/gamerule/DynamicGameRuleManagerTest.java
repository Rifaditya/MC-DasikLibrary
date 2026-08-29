// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.gamerule;

import net.minecraft.resources.Identifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicGameRuleManagerTest {

    @Test
    @DisplayName("Verify mod ID extraction from standard and prefixed rule identifiers")
    public void testModIdExtraction() {
        // Standard namespaced rules
        assertEquals("durability_multiplier", DynamicGameRuleManager.extractModId("durability_multiplier:item_diamond_sword"));
        assertEquals("agrarian_reform", DynamicGameRuleManager.extractModId("agrarian_reform:enable_crop_scaling"));
        assertEquals("betterdogs", DynamicGameRuleManager.extractModId("betterdogs:bd_wolf_min_scale"));

        // Instant Gratification prefixed ore rules
        assertEquals("create", DynamicGameRuleManager.extractModId("ig:ore_create_zinc_ore"));
        assertEquals("techreborn", DynamicGameRuleManager.extractModId("ig:ore_techreborn_tin_ore"));
        assertEquals("thermal", DynamicGameRuleManager.extractModId("ig:ore_thermal_lead_ore"));

        // Protected / core namespaces must return null
        assertNull(DynamicGameRuleManager.extractModId("minecraft:randomTickSpeed"));
        assertNull(DynamicGameRuleManager.extractModId("c:ore_amplifier_blacklist"));
        assertNull(DynamicGameRuleManager.extractModId("fabric:load_priority"));
        assertNull(DynamicGameRuleManager.extractModId("fabric-api:registry_sync"));
        assertNull(DynamicGameRuleManager.extractModId("dasik-library:debug_mode"));
        assertNull(DynamicGameRuleManager.extractModId("ig:ore_minecraft_iron_ore"));
        assertNull(DynamicGameRuleManager.extractModId("ig:ore_minecraft_ancient_debris"));

        // Null and empty safety
        assertNull(DynamicGameRuleManager.extractModId(null));
        assertNull(DynamicGameRuleManager.extractModId(""));
        assertNull(DynamicGameRuleManager.extractModId("invalid-key-no-colon"));
    }

    @Test
    @DisplayName("Verify human-readable name generation")
    public void testReadableNameGeneration() {
        assertEquals("Minecraft Iron Ore Multiplier", DynamicGameRuleManager.generateReadableName("ig_ore_minecraft_iron_ore"));
        assertEquals("Diamond Sword", DynamicGameRuleManager.generateReadableName("ig_diamond_sword"));
        assertNull(DynamicGameRuleManager.generateReadableName(null));
        assertEquals("", DynamicGameRuleManager.generateReadableName(""));
    }

    @Test
    @DisplayName("Verify unregistration null and boundary safety")
    public void testUnregistrationSafety() {
        assertFalse(DynamicGameRuleManager.unregister((Identifier) null));
        assertFalse(DynamicGameRuleManager.unregister((String) null));
        assertFalse(DynamicGameRuleManager.unregister(""));
        assertFalse(DynamicGameRuleManager.unregister("nonexistent_mod:nonexistent_rule"));
        assertEquals(0, DynamicGameRuleManager.unregisterModRules(null));
        assertEquals(0, DynamicGameRuleManager.unregisterModRules(""));
        assertEquals(0, DynamicGameRuleManager.unregisterModRules("nonexistent_mod"));
    }
}
