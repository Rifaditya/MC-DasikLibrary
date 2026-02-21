package net.dasik.social.api.gamerule;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A central API for "Just-In-Time" (JIT) dynamic GameRule registration.
 * This handles the boilerplate of registering custom rules for modded items
 * dynamically.
 * Safe to be called during game loops for lazy evaluation.
 */
@SuppressWarnings({ "null", "unchecked" })
public class DynamicGameRuleManager {
    private static final Map<String, GameRule<?>> DYNAMIC_RULES = new ConcurrentHashMap<>();

    /**
     * Registers a custom GameRule Category.
     * Often used in onInitialize().
     */
    public static GameRuleCategory registerCategory(Identifier id) {
        return GameRuleCategory.register(id);
    }

    /**
     * Dynamically registers and caches an Integer GameRule.
     * If it already exists, returns the cached instance.
     * Note: JIT registration requires the rule to be registered correctly in the
     * BuiltInRegistries.
     */
    @SuppressWarnings("unchecked")
    public static GameRule<Integer> registerInteger(String ruleName, GameRuleCategory category, int defaultValue) {
        if (DYNAMIC_RULES.containsKey(ruleName)) {
            return (GameRule<Integer>) DYNAMIC_RULES.get(ruleName);
        }

        GameRule<Integer> rule = Registry.register(BuiltInRegistries.GAME_RULE, ruleName, new GameRule<>(
                category,
                GameRuleType.INT,
                IntegerArgumentType.integer(0), // Assumes >= 0
                GameRuleTypeVisitor::visitInteger,
                Codec.INT,
                i -> i,
                defaultValue,
                FeatureFlagSet.of()));

        DYNAMIC_RULES.put(ruleName, rule);

        // Generate and cache a human-readable translation for this rule
        String translationKey = "gamerule." + ruleName;
        GENERATED_TRANSLATIONS.put(translationKey, generateReadableName(ruleName));

        return rule;
    }

    /**
     * Dynamically registers and caches a Boolean GameRule.
     */
    @SuppressWarnings("unchecked")
    public static GameRule<Boolean> registerBoolean(String ruleName, GameRuleCategory category, boolean defaultValue) {
        if (DYNAMIC_RULES.containsKey(ruleName)) {
            return (GameRule<Boolean>) DYNAMIC_RULES.get(ruleName);
        }

        GameRule<Boolean> rule = Registry.register(BuiltInRegistries.GAME_RULE, ruleName, new GameRule<>(
                category,
                GameRuleType.BOOL,
                BoolArgumentType.bool(),
                GameRuleTypeVisitor::visitBoolean,
                Codec.BOOL,
                b -> b ? 1 : 0,
                defaultValue,
                FeatureFlagSet.of()));

        DYNAMIC_RULES.put(ruleName, rule);

        // Generate and cache a human-readable translation for this rule
        String translationKey = "gamerule." + ruleName;
        GENERATED_TRANSLATIONS.put(translationKey, generateReadableName(ruleName));

        return rule;
    }

    /**
     * Safe retrieval of an Integer rule from the server level.
     */
    public static int getInt(Level level, GameRule<Integer> rule) {
        if (level == null || level.isClientSide())
            return 0;
        return ((ServerLevel) level).getGameRules().get(rule);
    }

    /**
     * Safe retrieval of a Boolean rule from the server level.
     */
    public static boolean getBoolean(Level level, GameRule<Boolean> rule) {
        if (level == null || level.isClientSide())
            return false;
        return ((ServerLevel) level).getGameRules().get(rule);
    }

    /**
     * Get all currently registered dynamic rules. Useful for syncing.
     */
    public static Map<String, GameRule<?>> getDynamicRules() {
        return DYNAMIC_RULES;
    }

    /**
     * Holds dynamically generated language translations for injected GameRules.
     */
    private static final Map<String, String> GENERATED_TRANSLATIONS = new ConcurrentHashMap<>();

    /**
     * Get the dynamically generated translations.
     */
    public static Map<String, String> getGeneratedTranslations() {
        return GENERATED_TRANSLATIONS;
    }

    /**
     * Helper to generate a human-readable name from a dynamic GameRule key.
     * e.g., "ig_ore_techrewrite_tin_ore" -> "Techrewrite Tin Ore Multiplier"
     */
    public static String generateReadableName(String ruleName) {
        if (ruleName == null || ruleName.isEmpty())
            return ruleName;

        // Remove prefix if it exists, e.g. "ig_ore_" or "om_"
        String cleanName = ruleName;
        if (cleanName.startsWith("ig_ore_")) {
            cleanName = cleanName.substring(7);
        } else if (cleanName.startsWith("ig_")) {
            cleanName = cleanName.substring(3);
        }

        // Split by underscores
        String[] parts = cleanName.split("_");
        StringBuilder readable = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (part.isEmpty())
                continue;

            // Capitalize first letter
            readable.append(Character.toUpperCase(part.charAt(0)))
                    .append(part.substring(1));

            if (i < parts.length - 1) {
                readable.append(" ");
            }
        }

        // Add context for ore rules
        if (ruleName.startsWith("ig_ore_")) {
            readable.append(" Multiplier");
        }

        return readable.toString();
    }
}
