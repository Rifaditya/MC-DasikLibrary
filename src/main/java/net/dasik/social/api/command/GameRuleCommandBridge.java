// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.command;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.gamerules.GameRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Two-way reactive synchronizer bridging Brigadier commands, Minecraft GameRules,
 * and persistent JSON configurations with Freedom Over Anti-Crash saturated bounds.
 */
@DasikApiStatus.Public
public class GameRuleCommandBridge {
    private static final Logger LOGGER = LoggerFactory.getLogger(GameRuleCommandBridge.class);

    public enum Type {
        BOOLEAN,
        INTEGER,
        DOUBLE,
        STRING
    }

    public enum Status {
        SUCCESS,
        SATURATED_WARNING,
        ERROR_NOT_FOUND,
        ERROR_PARSE
    }

    public static class SetResult {
        public final Status status;
        public final Object value;
        public final String message;

        public SetResult(Status status, Object value, String message) {
            this.status = status;
            this.value = value;
            this.message = message;
        }

        public Status getStatus() {
            return status;
        }

        public Status status() {
            return status;
        }

        public Object getValue() {
            return value;
        }

        public Object value() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public String message() {
            return message;
        }

        public boolean isSuccess() {
            return status == Status.SUCCESS || status == Status.SATURATED_WARNING;
        }

        public boolean isWarning() {
            return status == Status.SATURATED_WARNING;
        }

        public boolean isError() {
            return status == Status.ERROR_NOT_FOUND || status == Status.ERROR_PARSE;
        }

        public static SetResult success(Object value) {
            return new SetResult(Status.SUCCESS, value, "Successfully set value to " + value);
        }

        public static SetResult saturatedWarning(Object value, String message) {
            return new SetResult(Status.SATURATED_WARNING, value, message);
        }

        public static SetResult errorNotFound(String key) {
            return new SetResult(Status.ERROR_NOT_FOUND, null, "Configuration key '" + key + "' not found.");
        }

        public static SetResult errorParse(String rawValue, String reason) {
            return new SetResult(Status.ERROR_PARSE, null, "Failed to parse '" + rawValue + "': " + reason);
        }
    }

    public static class RuleEntry {
        private final String key;
        private final Type type;
        private final GameRule<?> rule;
        private final Object defaultValue;
        private volatile Object currentValue;
        private final double min;
        private final double max;

        public RuleEntry(String key, Type type, GameRule<?> rule, Object defaultValue, double min, double max) {
            this.key = key;
            this.type = type;
            this.rule = rule;
            this.defaultValue = defaultValue;
            this.currentValue = defaultValue;
            this.min = min;
            this.max = max;
        }

        public String getKey() {
            return key;
        }

        public Type getType() {
            return type;
        }

        public GameRule<?> getRule() {
            return rule;
        }

        public Object getDefaultValue() {
            return defaultValue;
        }

        public Object getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(Object currentValue) {
            this.currentValue = currentValue;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }
    }

    private final Map<String, RuleEntry> rules = new ConcurrentHashMap<>();
    private Consumer<MinecraftServer> saveCallback;

    public GameRuleCommandBridge() {
    }

    public GameRuleCommandBridge registerBoolean(String key, GameRule<Boolean> rule, boolean defaultValue) {
        rules.put(key, new RuleEntry(key, Type.BOOLEAN, rule, defaultValue, 0.0, 1.0));
        return this;
    }

    public GameRuleCommandBridge registerInteger(String key, GameRule<Integer> rule, int defaultValue, int min, int max) {
        rules.put(key, new RuleEntry(key, Type.INTEGER, rule, defaultValue, min, max));
        return this;
    }

    public GameRuleCommandBridge registerDouble(String key, GameRule<?> rule, double defaultValue, double min, double max) {
        rules.put(key, new RuleEntry(key, Type.DOUBLE, rule, defaultValue, min, max));
        return this;
    }

    public GameRuleCommandBridge registerString(String key, GameRule<?> rule, String defaultValue) {
        rules.put(key, new RuleEntry(key, Type.STRING, rule, defaultValue, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
        return this;
    }

    public RuleEntry getRuleEntry(String key) {
        return rules.get(key);
    }

    public Set<String> getRegisteredKeys() {
        return Collections.unmodifiableSet(new TreeSet<>(rules.keySet()));
    }

    public Object getValue(ServerLevel level, String key) {
        RuleEntry entry = rules.get(key);
        if (entry == null) {
            return null;
        }
        if (level != null && entry.getRule() != null) {
            try {
                Object val = readGameRuleValue(level, entry.getRule());
                if (val != null) {
                    if (entry.getType() == Type.DOUBLE && val instanceof Number num) {
                        double d = num.doubleValue();
                        entry.setCurrentValue(d);
                        return d;
                    } else if (entry.getType() == Type.INTEGER && val instanceof Number num) {
                        int i = num.intValue();
                        entry.setCurrentValue(i);
                        return i;
                    } else if (entry.getType() == Type.BOOLEAN && val instanceof Boolean b) {
                        entry.setCurrentValue(b);
                        return b;
                    } else if (entry.getType() == Type.STRING) {
                        String s = String.valueOf(val);
                        entry.setCurrentValue(s);
                        return s;
                    }
                    entry.setCurrentValue(val);
                    return val;
                }
            } catch (Exception ignored) {
            }
        }
        return entry.getCurrentValue();
    }

    @SuppressWarnings("unchecked")
    private <T> T readGameRuleValue(ServerLevel level, GameRule<T> rule) {
        return level.getGameRules().get(rule);
    }

    public SetResult setValue(ServerLevel level, String key, String rawValue) {
        RuleEntry entry = rules.get(key);
        if (entry == null) {
            return SetResult.errorNotFound(key);
        }
        if (rawValue == null) {
            return SetResult.errorParse("null", "Value cannot be null.");
        }

        switch (entry.getType()) {
            case BOOLEAN -> {
                String trimmed = rawValue.trim().toLowerCase(Locale.ROOT);
                boolean boolVal;
                if (trimmed.equals("true") || trimmed.equals("1") || trimmed.equals("yes") || trimmed.equals("on")
                        || trimmed.equals("enable") || trimmed.equals("enabled")) {
                    boolVal = true;
                } else if (trimmed.equals("false") || trimmed.equals("0") || trimmed.equals("no") || trimmed.equals("off")
                        || trimmed.equals("disable") || trimmed.equals("disabled")) {
                    boolVal = false;
                } else {
                    return SetResult.errorParse(rawValue, "Expected boolean ('true' or 'false').");
                }
                entry.setCurrentValue(boolVal);
                if (level != null && entry.getRule() != null) {
                    syncGameRule(level, entry.getRule(), boolVal);
                }
                executeSave(level != null ? level.getServer() : null);
                return SetResult.success(boolVal);
            }
            case INTEGER -> {
                String trimmed = rawValue.trim();
                BigInteger parsedBig;
                try {
                    parsedBig = new BigInteger(trimmed);
                } catch (NumberFormatException e) {
                    return SetResult.errorParse(rawValue, "Not a valid integer.");
                }

                int min = (int) entry.getMin();
                int max = (int) entry.getMax();
                BigInteger bigMin = BigInteger.valueOf(min);
                BigInteger bigMax = BigInteger.valueOf(max);

                if (parsedBig.compareTo(bigMax) > 0) {
                    entry.setCurrentValue(max);
                    if (level != null && entry.getRule() != null) {
                        syncGameRule(level, entry.getRule(), max);
                    }
                    executeSave(level != null ? level.getServer() : null);
                    return SetResult.saturatedWarning(max,
                            "Value " + rawValue + " exceeds maximum bound (" + max + "). Saturated to " + max + ".");
                } else if (parsedBig.compareTo(bigMin) < 0) {
                    entry.setCurrentValue(min);
                    if (level != null && entry.getRule() != null) {
                        syncGameRule(level, entry.getRule(), min);
                    }
                    executeSave(level != null ? level.getServer() : null);
                    return SetResult.saturatedWarning(min,
                            "Value " + rawValue + " is below minimum bound (" + min + "). Saturated to " + min + ".");
                } else {
                    int val = parsedBig.intValue();
                    entry.setCurrentValue(val);
                    if (level != null && entry.getRule() != null) {
                        syncGameRule(level, entry.getRule(), val);
                    }
                    executeSave(level != null ? level.getServer() : null);
                    return SetResult.success(val);
                }
            }
            case DOUBLE -> {
                String trimmed = rawValue.trim();
                double parsed;
                try {
                    parsed = Double.parseDouble(trimmed);
                } catch (NumberFormatException e) {
                    return SetResult.errorParse(rawValue, "Not a valid decimal number.");
                }

                if (Double.isNaN(parsed)) {
                    return SetResult.errorParse(rawValue, "Value cannot be NaN.");
                }

                double min = entry.getMin();
                double max = entry.getMax();

                if (parsed > max) {
                    entry.setCurrentValue(max);
                    if (level != null && entry.getRule() != null) {
                        syncGameRule(level, entry.getRule(), max);
                    }
                    executeSave(level != null ? level.getServer() : null);
                    return SetResult.saturatedWarning(max,
                            "Value " + rawValue + " exceeds maximum bound (" + max + "). Saturated to " + max + ".");
                } else if (parsed < min) {
                    entry.setCurrentValue(min);
                    if (level != null && entry.getRule() != null) {
                        syncGameRule(level, entry.getRule(), min);
                    }
                    executeSave(level != null ? level.getServer() : null);
                    return SetResult.saturatedWarning(min,
                            "Value " + rawValue + " is below minimum bound (" + min + "). Saturated to " + min + ".");
                } else {
                    entry.setCurrentValue(parsed);
                    if (level != null && entry.getRule() != null) {
                        syncGameRule(level, entry.getRule(), parsed);
                    }
                    executeSave(level != null ? level.getServer() : null);
                    return SetResult.success(parsed);
                }
            }
            case STRING -> {
                entry.setCurrentValue(rawValue);
                if (level != null && entry.getRule() != null) {
                    syncGameRule(level, entry.getRule(), rawValue);
                }
                executeSave(level != null ? level.getServer() : null);
                return SetResult.success(rawValue);
            }
        }
        return SetResult.errorParse(rawValue, "Unknown configuration type.");
    }

    public void resetToDefaults(ServerLevel level) {
        for (RuleEntry entry : rules.values()) {
            entry.setCurrentValue(entry.getDefaultValue());
            if (level != null && entry.getRule() != null) {
                syncGameRule(level, entry.getRule(), entry.getDefaultValue());
            }
        }
        executeSave(level != null ? level.getServer() : null);
    }

    public void setSaveCallback(Consumer<MinecraftServer> callback) {
        this.saveCallback = callback;
    }

    public void executeSave(MinecraftServer server) {
        if (this.saveCallback != null) {
            try {
                this.saveCallback.accept(server);
            } catch (Exception e) {
                LOGGER.error("GameRuleCommandBridge: Failed to execute save callback", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> void syncGameRule(ServerLevel level, GameRule<T> rule, Object val) {
        try {
            if (rule != null && level != null) {
                Class<T> valClass = rule.valueClass();
                if (valClass.isInstance(val)) {
                    level.getGameRules().set(rule, valClass.cast(val), level.getServer());
                } else if (valClass == Integer.class && val instanceof Number num) {
                    level.getGameRules().set(rule, valClass.cast(num.intValue()), level.getServer());
                } else if (valClass == Boolean.class && val instanceof Boolean b) {
                    level.getGameRules().set(rule, valClass.cast(b), level.getServer());
                }
            }
        } catch (Exception e) {
            LOGGER.debug("GameRuleCommandBridge: Failed to sync GameRule '{}': {}", rule.id(), e.getMessage());
        }
    }
}
