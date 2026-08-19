/*
 * Zenith Sovereign Engineering - Dasik Library
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
// Verified against: GameRules.java (Snapshot 10)
package net.dasik.social.api.gamerule;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
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

    public static BooleanBuilder booleanRule(String ruleName, GameRuleCategory category, boolean defaultValue) {
        return new BooleanBuilder(ruleName, category, defaultValue);
    }

    public static IntegerBuilder integerRule(String ruleName, GameRuleCategory category, int defaultValue) {
        return new IntegerBuilder(ruleName, category, defaultValue);
    }

    /**
     * @deprecated Use {@link #integerRule(String, GameRuleCategory, int)} instead.
     */
    @Deprecated
    public static GameRule<Integer> registerInteger(String ruleName, GameRuleCategory category, int defaultValue) {
        return integerRule(ruleName, category, defaultValue).register();
    }

    /**
     * @deprecated Use {@link #booleanRule(String, GameRuleCategory, boolean)} instead.
     */
    @Deprecated
    public static GameRule<Boolean> registerBoolean(String ruleName, GameRuleCategory category, boolean defaultValue) {
        return booleanRule(ruleName, category, defaultValue).register();
    }

    public static class BooleanBuilder {
        private final String ruleName;
        private final GameRuleCategory category;
        private final boolean defaultValue;
        private String description;
        private String readableName;

        BooleanBuilder(String ruleName, GameRuleCategory category, boolean defaultValue) {
            this.ruleName = ruleName;
            this.category = category;
            this.defaultValue = defaultValue;
            this.readableName = generateReadableName(ruleName);
        }

        public BooleanBuilder description(String description) {
            this.description = description;
            return this;
        }

        public BooleanBuilder name(String readableName) {
            this.readableName = readableName;
            return this;
        }

        @SuppressWarnings("unchecked")
        public GameRule<Boolean> register() {
            if (DYNAMIC_RULES.containsKey(ruleName)) {
                return (GameRule<Boolean>) DYNAMIC_RULES.get(ruleName);
            }
            Identifier id = Identifier.parse(ruleName);
            GameRule<?> existing = BuiltInRegistries.GAME_RULE.getValue(id);
            if (existing != null) {
                DYNAMIC_RULES.put(ruleName, existing);
                injectTranslations(id);
                return (GameRule<Boolean>) existing;
            }
            try {
                GameRule<Boolean> rule = Registry.register(BuiltInRegistries.GAME_RULE, ruleName, 
                    new GameRule<>(category, GameRuleType.BOOL, BoolArgumentType.bool(), 
                    GameRuleTypeVisitor::visitBoolean, Codec.BOOL, b -> b ? 1 : 0, defaultValue, FeatureFlagSet.of()));
                DYNAMIC_RULES.put(ruleName, rule);
                injectTranslations(id);
                return rule;
            } catch (IllegalStateException e) {
                return null;
            }
        }

        private void injectTranslations(Identifier id) {
            String translationKey = Util.makeDescriptionId("gamerule", id);
            GENERATED_TRANSLATIONS.put(translationKey, readableName);
            if (description != null) {
                GENERATED_TRANSLATIONS.put(translationKey + ".description", description);
            }
        }
    }

    public static class IntegerBuilder {
        private final String ruleName;
        private final GameRuleCategory category;
        private final int defaultValue;
        private int min = Integer.MIN_VALUE;
        private int max = Integer.MAX_VALUE;
        private String description;
        private String readableName;

        IntegerBuilder(String ruleName, GameRuleCategory category, int defaultValue) {
            this.ruleName = ruleName;
            this.category = category;
            this.defaultValue = defaultValue;
            this.readableName = generateReadableName(ruleName);
        }

        public IntegerBuilder description(String description) {
            this.description = description;
            return this;
        }

        public IntegerBuilder name(String readableName) {
            this.readableName = readableName;
            return this;
        }

        public IntegerBuilder min(int min) {
            this.min = min;
            return this;
        }

        public IntegerBuilder max(int max) {
            this.max = max;
            return this;
        }

        public IntegerBuilder range(int min, int max) {
            this.min = min;
            this.max = max;
            return this;
        }

        @SuppressWarnings("unchecked")
        public GameRule<Integer> register() {
            if (DYNAMIC_RULES.containsKey(ruleName)) {
                return (GameRule<Integer>) DYNAMIC_RULES.get(ruleName);
            }
            Identifier id = Identifier.parse(ruleName);
            GameRule<?> existing = BuiltInRegistries.GAME_RULE.getValue(id);
            if (existing != null) {
                DYNAMIC_RULES.put(ruleName, existing);
                injectTranslations(id);
                return (GameRule<Integer>) existing;
            }
            try {
                int effectiveMin = Math.min(min, defaultValue);
                int effectiveMax = Math.max(max, defaultValue);
                Codec<Integer> codec = (effectiveMin == Integer.MIN_VALUE && effectiveMax == Integer.MAX_VALUE)
                        ? Codec.INT
                        : Codec.intRange(effectiveMin, effectiveMax);
                IntegerArgumentType argType = (effectiveMin == Integer.MIN_VALUE && effectiveMax == Integer.MAX_VALUE)
                        ? IntegerArgumentType.integer()
                        : IntegerArgumentType.integer(effectiveMin, effectiveMax);

                GameRule<Integer> rule = Registry.register(BuiltInRegistries.GAME_RULE, ruleName, 
                    new GameRule<>(category, GameRuleType.INT, argType, 
                    GameRuleTypeVisitor::visitInteger, codec, i -> i, defaultValue, FeatureFlagSet.of()));
                DYNAMIC_RULES.put(ruleName, rule);
                injectTranslations(id);
                return rule;
            } catch (IllegalStateException e) {
                return null;
            }
        }

        private void injectTranslations(Identifier id) {
            String translationKey = Util.makeDescriptionId("gamerule", id);
            GENERATED_TRANSLATIONS.put(translationKey, readableName);
            if (description != null) {
                GENERATED_TRANSLATIONS.put(translationKey + ".description", description);
            }
        }
    }

    public static int getInt(Level level, GameRule<Integer> rule) {
        if (level == null || rule == null) {
            return rule != null ? rule.defaultValue() : 0;
        }
        if (level instanceof ServerLevel serverLevel) {
            try {
                return serverLevel.getGameRules().get(rule);
            } catch (Exception e) {
                return rule.defaultValue();
            }
        }
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            try {
                return ClientGameRuleHelper.getInt(level, rule);
            } catch (Throwable t) {
                // Classloading safety / fallback
            }
        }
        return rule.defaultValue();
    }

    public static boolean getBoolean(Level level, GameRule<Boolean> rule) {
        if (level == null || rule == null) {
            return rule != null ? rule.defaultValue() : false;
        }
        if (level instanceof ServerLevel serverLevel) {
            try {
                return serverLevel.getGameRules().get(rule);
            } catch (Exception e) {
                return rule.defaultValue();
            }
        }
        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            try {
                return ClientGameRuleHelper.getBoolean(level, rule);
            } catch (Throwable t) {
                // Classloading safety / fallback
            }
        }
        return rule.defaultValue();
    }

    public static double getPct(Level level, GameRule<Integer> rule) {
        return getInt(level, rule) / 100.0;
    }

    public static float getProb(Level level, GameRule<Integer> rule) {
        return getInt(level, rule) / 1000.0f;
    }

    public static float getChance(Level level, GameRule<Integer> rule) {
        return getInt(level, rule) / 100.0f;
    }

    public static float getDecileFloat(Level level, GameRule<Integer> rule) {
        return getInt(level, rule) / 10.0f;
    }

    /**
     * Looks up a registered integer GameRule by its string key.
     *
     * @param key The GameRule key (e.g., "betterdogs:bd_wolf_min_scale_percent").
     * @return The registered GameRule, or null if not found.
     */
    @SuppressWarnings("unchecked")
    public static GameRule<Integer> getIntRule(String key) {
        return (GameRule<Integer>) DYNAMIC_RULES.get(key);
    }

    /**
     * Queries an integer GameRule value by its string key.
     *
     * @param level        The world level.
     * @param key          The GameRule key.
     * @param defaultValue Fallback value if the GameRule is not registered.
     * @return The GameRule value, or the default value if not found.
     */
    public static int getIntVal(Level level, String key, int defaultValue) {
        GameRule<Integer> rule = getIntRule(key);
        return rule != null ? getInt(level, rule) : defaultValue;
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
            // Remove 'truesleep:' or similar prefixes
            if (part.contains(":")) {
                part = part.substring(part.indexOf(":") + 1);
                if (part.isEmpty()) continue;
            }
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
