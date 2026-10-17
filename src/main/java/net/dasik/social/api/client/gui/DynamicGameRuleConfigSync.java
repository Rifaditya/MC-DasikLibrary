// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: Minecraft.java (26.2+), GameRules.java (26.2+)
package net.dasik.social.api.client.gui;

import net.dasik.social.api.annotation.DasikApiStatus;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-side bridge that synchronizes configuration GUI value updates directly to
 * in-game {@link GameRule} instances when operating in a singleplayer world.
 */
@DasikApiStatus.Public
@Environment(EnvType.CLIENT)
public class DynamicGameRuleConfigSync {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|ConfigSync");

    /**
     * Synchronizes a configuration value to an in-game GameRule if a local singleplayer server is running.
     *
     * @param gameRuleName the identifier or key of the GameRule (e.g. "ig:ore_coal_ore" or "doFireTick").
     * @param value the new value (e.g. Boolean, Integer, Number).
     */
    public static void syncToGameRule(String gameRuleName, Object value) {
        if (gameRuleName == null || gameRuleName.isEmpty() || value == null) {
            return;
        }

        Minecraft minecraft = null;
        try {
            minecraft = Minecraft.getInstance();
        } catch (Throwable t) {
            return;
        }
        if (minecraft == null) {
            return;
        }

        MinecraftServer server = minecraft.getSingleplayerServer();
        if (server == null) {
            return;
        }

        try {
            GameRule<?> rule = findGameRule(server, gameRuleName);
            if (rule == null) {
                LOGGER.debug("GameRule '{}' not found for config synchronization", gameRuleName);
                return;
            }

            setGameRuleValue(server, rule, value);
        } catch (Throwable t) {
            LOGGER.warn("Failed to synchronize GameRule '{}' with value '{}': {}", gameRuleName, value, t.getMessage());
        }
    }

    /**
     * Resolves a {@link GameRule} by key across dynamic rule caches, registries, and active server rules.
     */
    public static GameRule<?> findGameRule(MinecraftServer server, String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        // 1. DynamicGameRuleManager active cache
        GameRule<?> rule = DynamicGameRuleManager.getDynamicRules().get(name);
        if (rule != null) {
            return rule;
        }

        // 2. BuiltInRegistries.GAME_RULE via Identifier
        try {
            Identifier id = Identifier.tryParse(name);
            if (id != null) {
                rule = BuiltInRegistries.GAME_RULE.getValue(id);
                if (rule != null) {
                    return rule;
                }
            }
        } catch (Throwable ignored) {
            // BuiltInRegistries unbootstrapped or uninitialized in test/early environment
        }

        // 3. Search server available rules by id or short name
        if (server != null) {
            GameRules serverRules = server.getGameRules();
            if (serverRules != null) {
                for (GameRule<?> r : (Iterable<GameRule<?>>) serverRules.availableRules()::iterator) {
                    if (r.id().equals(name)) {
                        return r;
                    }
                    Identifier ruleId = r.getIdentifierWithFallback();
                    if (ruleId.getPath().equals(name) || ruleId.toString().equals(name)) {
                        return r;
                    }
                }
            }
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private static <T> void setGameRuleValue(MinecraftServer server, GameRule<T> rule, Object val) {
        Class<T> valClass = rule.valueClass();
        GameRules gameRules = server.getGameRules();
        if (gameRules == null) {
            return;
        }

        if (valClass == Boolean.class) {
            Boolean b = null;
            if (val instanceof Boolean boolVal) {
                b = boolVal;
            } else if (val instanceof String s) {
                b = Boolean.parseBoolean(s);
            }
            if (b != null) {
                gameRules.set((GameRule<Boolean>) rule, b, server);
                LOGGER.debug("Synchronized Boolean GameRule '{}' = {}", rule.id(), b);
            }
        } else if (valClass == Integer.class) {
            Integer i = null;
            if (val instanceof Number num) {
                i = num.intValue();
            } else if (val instanceof String s) {
                try {
                    i = Integer.parseInt(s);
                } catch (NumberFormatException ignored) {}
            }
            if (i != null) {
                gameRules.set((GameRule<Integer>) rule, i, server);
                LOGGER.debug("Synchronized Integer GameRule '{}' = {}", rule.id(), i);
            }
        } else if (valClass.isInstance(val)) {
            gameRules.set(rule, valClass.cast(val), server);
            LOGGER.debug("Synchronized GameRule '{}' = {}", rule.id(), val);
        }
    }
}
