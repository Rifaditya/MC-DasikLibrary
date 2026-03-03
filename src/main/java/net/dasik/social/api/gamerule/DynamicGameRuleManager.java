/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.BoolArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.serialization.Codec
 *  net.minecraft.core.Registry
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.resources.Identifier
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Util
 *  net.minecraft.world.flag.FeatureFlagSet
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.gamerules.GameRule
 *  net.minecraft.world.level.gamerules.GameRuleCategory
 *  net.minecraft.world.level.gamerules.GameRuleType
 *  net.minecraft.world.level.gamerules.GameRuleTypeVisitor
 */
package net.dasik.social.api.gamerule;
// Verified against: GameRules.java (Snapshot 10)


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

public class DynamicGameRuleManager {
    private static final Map<String, GameRule<?>> DYNAMIC_RULES = new ConcurrentHashMap();
    private static final Map<String, String> GENERATED_TRANSLATIONS = new ConcurrentHashMap<String, String>();

    public static GameRuleCategory registerCategory(Identifier id) {
        return GameRuleCategory.register((Identifier)id);
    }

    public static GameRule<Integer> registerInteger(String ruleName, GameRuleCategory category, int defaultValue) {
        if (DYNAMIC_RULES.containsKey(ruleName)) {
            return (GameRule<Integer>)DYNAMIC_RULES.get(ruleName);
        }
        Identifier id = Identifier.parse((String)ruleName);
        GameRule existing = (GameRule)BuiltInRegistries.GAME_RULE.getValue(id);
        if (existing != null) {
            DYNAMIC_RULES.put(ruleName, existing);
            String key = Util.makeDescriptionId((String)"gamerule", (Identifier)id);
            GENERATED_TRANSLATIONS.putIfAbsent(key, DynamicGameRuleManager.generateReadableName(ruleName));
            return existing;
        }
        try {
            GameRule rule = (GameRule)Registry.register((Registry)BuiltInRegistries.GAME_RULE, (String)ruleName, (Object)new GameRule(category, GameRuleType.INT, (ArgumentType)IntegerArgumentType.integer((int)0), GameRuleTypeVisitor::visitInteger, (Codec)Codec.INT, i -> (Integer)i, (Object)defaultValue, FeatureFlagSet.of()));
            DYNAMIC_RULES.put(ruleName, rule);
            String translationKey = Util.makeDescriptionId((String)"gamerule", (Identifier)id);
            GENERATED_TRANSLATIONS.put(translationKey, DynamicGameRuleManager.generateReadableName(ruleName));
            return rule;
        }
        catch (IllegalStateException e) {
            return null;
        }
    }

    public static GameRule<Boolean> registerBoolean(String ruleName, GameRuleCategory category, boolean defaultValue) {
        if (DYNAMIC_RULES.containsKey(ruleName)) {
            return (GameRule<Boolean>)DYNAMIC_RULES.get(ruleName);
        }
        Identifier id = Identifier.parse((String)ruleName);
        GameRule existing = (GameRule)BuiltInRegistries.GAME_RULE.getValue(id);
        if (existing != null) {
            DYNAMIC_RULES.put(ruleName, existing);
            String key = Util.makeDescriptionId((String)"gamerule", (Identifier)id);
            GENERATED_TRANSLATIONS.putIfAbsent(key, DynamicGameRuleManager.generateReadableName(ruleName));
            return existing;
        }
        try {
            GameRule rule = (GameRule)Registry.register((Registry)BuiltInRegistries.GAME_RULE, (String)ruleName, (Object)new GameRule(category, GameRuleType.BOOL, (ArgumentType)BoolArgumentType.bool(), GameRuleTypeVisitor::visitBoolean, (Codec)Codec.BOOL, b -> (Boolean)b != false ? 1 : 0, (Object)defaultValue, FeatureFlagSet.of()));
            DYNAMIC_RULES.put(ruleName, rule);
            String translationKey = Util.makeDescriptionId((String)"gamerule", (Identifier)id);
            GENERATED_TRANSLATIONS.put(translationKey, DynamicGameRuleManager.generateReadableName(ruleName));
            return rule;
        }
        catch (IllegalStateException e) {
            return null;
        }
    }

    public static int getInt(Level level, GameRule<Integer> rule) {
        if (level == null || level.isClientSide()) {
            return 0;
        }
        return (Integer)((ServerLevel)level).getGameRules().get(rule);
    }

    public static boolean getBoolean(Level level, GameRule<Boolean> rule) {
        if (level == null || level.isClientSide()) {
            return false;
        }
        return (Boolean)((ServerLevel)level).getGameRules().get(rule);
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
            if (i >= parts.length - 1) continue;
            readable.append(" ");
        }
        if (ruleName.startsWith("ig_ore_")) {
            readable.append(" Multiplier");
        }
        return readable.toString();
    }
}

