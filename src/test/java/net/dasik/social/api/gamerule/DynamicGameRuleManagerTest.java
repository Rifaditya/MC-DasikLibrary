// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.gamerule;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DynamicGameRuleManagerTest {

    private static final GameRuleCategory DUMMY_CATEGORY = GameRuleCategory.register(Identifier.parse("testmod:dummy_cat"));
    private static final GameRule<Boolean> DUMMY_RULE = new GameRule<>(
            DUMMY_CATEGORY, GameRuleType.BOOL, BoolArgumentType.bool(),
            GameRuleTypeVisitor::visitBoolean, Codec.BOOL, b -> b ? 1 : 0, true, FeatureFlagSet.of()
    );

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

    @Test
    @DisplayName("Verify unregister by Identifier and String removes rule and generated translations")
    public void testUnregisterRemovesRulesAndTranslations() {
        Identifier ruleId = Identifier.parse("testmod:sample_rule");
        String ruleName = ruleId.toString();

        // Simulate registered state in dynamic maps
        DynamicGameRuleManager.getDynamicRules().put(ruleName, DUMMY_RULE);
        DynamicGameRuleManager.getGeneratedTranslations().put("gamerule.testmod.sample_rule", "Sample Rule");
        DynamicGameRuleManager.getGeneratedTranslations().put("gamerule.testmod.sample_rule.description", "Description of sample rule");

        assertTrue(DynamicGameRuleManager.getDynamicRules().containsKey(ruleName));
        assertTrue(DynamicGameRuleManager.getGeneratedTranslations().containsKey("gamerule.testmod.sample_rule"));
        assertTrue(DynamicGameRuleManager.getGeneratedTranslations().containsKey("gamerule.testmod.sample_rule.description"));

        // Unregister by Identifier
        boolean unregistered = DynamicGameRuleManager.unregister(ruleId);
        assertTrue(unregistered);
        assertFalse(DynamicGameRuleManager.getDynamicRules().containsKey(ruleName));
        assertFalse(DynamicGameRuleManager.getGeneratedTranslations().containsKey("gamerule.testmod.sample_rule"));
        assertFalse(DynamicGameRuleManager.getGeneratedTranslations().containsKey("gamerule.testmod.sample_rule.description"));
    }

    @Test
    @DisplayName("Verify bulk unregisterModRules removes all rules for target mod ID")
    public void testBulkUnregisterModRules() {
        // Register entries for target mod and other mods
        DynamicGameRuleManager.getDynamicRules().put("custommod:rule_a", DUMMY_RULE);
        DynamicGameRuleManager.getDynamicRules().put("custommod:rule_b", DUMMY_RULE);
        DynamicGameRuleManager.getDynamicRules().put("ig:ore_custommod_ruby_ore", DUMMY_RULE);
        DynamicGameRuleManager.getDynamicRules().put("othermod:rule_c", DUMMY_RULE);

        int unregisteredCount = DynamicGameRuleManager.unregisterModRules("custommod");
        assertEquals(3, unregisteredCount);

        assertFalse(DynamicGameRuleManager.getDynamicRules().containsKey("custommod:rule_a"));
        assertFalse(DynamicGameRuleManager.getDynamicRules().containsKey("custommod:rule_b"));
        assertFalse(DynamicGameRuleManager.getDynamicRules().containsKey("ig:ore_custommod_ruby_ore"));
        assertTrue(DynamicGameRuleManager.getDynamicRules().containsKey("othermod:rule_c"));

        // Cleanup
        DynamicGameRuleManager.unregister("othermod:rule_c");
    }
}
