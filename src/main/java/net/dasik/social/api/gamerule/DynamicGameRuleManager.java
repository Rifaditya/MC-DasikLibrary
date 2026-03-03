/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: GameRules.java (Snapshot 10)
 */
package net.dasik.social.api.gamerule;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Util;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRuleCategory;
import net.minecraft.world.level.gamerules.GameRuleType;
import net.minecraft.world.level.gamerules.GameRuleTypeVisitor;

/**
 * Manager for dynamic Gamerule registration and state access.
 */
public class DynamicGameRuleManager {
    private static final Map<String, GameRule<?>> DYNAMIC_RULES = new ConcurrentHashMap<>();
    private static final Map<String, String> GENERATED_TRANSLATIONS = new ConcurrentHashMap<>();

    public static GameRuleCategory registerCategory(Identifier id) {
        return GameRuleCategory.register(id);
    }

    @SuppressWarnings("unchecked")
    public static GameRule<Integer> registerInteger(String ruleName, GameRuleCategory category, int defaultValue) {
        if (DYNAMIC_RULES.containsKey(ruleName)) {
            return (GameRule<Integer>) DYNAMIC_RULES.get(ruleName);
        }
        Identifier id = Identifier.parse(ruleName);
        GameRule<?> existing = BuiltInRegistries.GAME_RULE.getValue(id);
        if (existing != null) {
            DYNAMIC_RULES.put(ruleName, existing);
            String key = Util.makeDescriptionId("gamerule", id);
            GENERATED_TRANSLATIONS.putIfAbsent(key, generateReadableName(ruleName));
            return (GameRule<Integer>) existing;
        }
        try {
            GameRule<Integer> rule = Registry.register(BuiltInRegistries.GAME_RULE, ruleName, 
                new GameRule<>(category, GameRuleType.INT, IntegerArgumentType.integer(0), 
                GameRuleTypeVisitor::visitInteger, Codec.INT, i -> i, defaultValue, FeatureFlagSet.of()));
            DYNAMIC_RULES.put(ruleName, rule);
            String translationKey = Util.makeDescriptionId("gamerule", id);
            GENERATED_TRANSLATIONS.put(translationKey, generateReadableName(ruleName));
            return rule;
        } catch (IllegalStateException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public static GameRule<Boolean> registerBoolean(String ruleName, GameRuleCategory category, boolean defaultValue) {
        if (DYNAMIC_RULES.containsKey(ruleName)) {
            return (GameRule<Boolean>) DYNAMIC_RULES.get(ruleName);
        }
        Identifier id = Identifier.parse(ruleName);
        GameRule<?> existing = BuiltInRegistries.GAME_RULE.getValue(id);
        if (existing != null) {
            DYNAMIC_RULES.put(ruleName, existing);
            String key = Util.makeDescriptionId("gamerule", id);
            GENERATED_TRANSLATIONS.putIfAbsent(key, generateReadableName(ruleName));
            return (GameRule<Boolean>) existing;
        }
        try {
            GameRule<Boolean> rule = Registry.register(BuiltInRegistries.GAME_RULE, ruleName, 
                new GameRule<>(category, GameRuleType.BOOL, BoolArgumentType.bool(), 
                GameRuleTypeVisitor::visitBoolean, Codec.BOOL, b -> b ? 1 : 0, defaultValue, FeatureFlagSet.of()));
            DYNAMIC_RULES.put(ruleName, rule);
            String translationKey = Util.makeDescriptionId("gamerule", id);
            GENERATED_TRANSLATIONS.put(translationKey, generateReadableName(ruleName));
            return rule;
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public static int getInt(Level level, GameRule<Integer> rule) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(rule);
        }
        return 0;
    }

    public static boolean getBoolean(Level level, GameRule<Boolean> rule) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.getGameRules().get(rule);
        }
        return false;
    }

    public static Map<String, GameRule<?>> getDynamicRules() {
        return DYNAMIC_RULES;
    }

    public static Map<String, String> getGeneratedTranslations() {
        return GENERATED_TRANSLATIONS;
    }

    public static String generateReadableName(String ruleName) {
        if (ruleName == null || ruleName.isEmpty()) {
            return ruleName;
        }
        String cleanName = ruleName;
        if (cleanName.startsWith("ig_ore_")) {
            cleanName = cleanName.substring(7);
        } else if (cleanName.startsWith("ig_")) {
            cleanName = cleanName.substring(3);
        }
        String[] parts = cleanName.split("_");
        StringBuilder readable = new StringBuilder();
        for (int i = 0; i < parts.length; ++i) {
            String part = parts[i];
            if (part.isEmpty()) continue;
            readable.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            if (i < parts.length - 1) {
                readable.append(" ");
            }
        }
        if (ruleName.startsWith("ig_ore_")) {
            readable.append(" Multiplier");
        }
        return readable.toString();
    }
}
