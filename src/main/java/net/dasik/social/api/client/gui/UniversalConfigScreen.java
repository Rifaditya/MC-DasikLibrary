// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: YetAnotherConfigLib v3 (26.2+), ModMenu (26.2+)
package net.dasik.social.api.client.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.dasik.social.api.config.ConfigOption;
import net.dasik.social.api.config.DasikSupportHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Universal zero-boilerplate configuration screen generator for YetAnotherConfigLib (YACL) v3.
 * <p>
 * Introspects configuration classes or instances for fields annotated with {@link ConfigOption},
 * automatically generates categorized and grouped YACL screens, binds reflection getters/setters,
 * wires singleplayer DynamicGameRule synchronization, injects Ko-fi and Discord creator support buttons,
 * and handles custom or parameterless {@code save()} methods.
 * <p>
 * Uses {@link YaclSafetyBridge} reflection under the hood so no hard compile-time or runtime
 * dependency on YACL is required.
 */
@DasikApiStatus.Public
@Environment(EnvType.CLIENT)
public class UniversalConfigScreen {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|ConfigScreen");

    // ==========================================
    // PUBLIC STATIC FACTORY METHODS
    // ==========================================

    /**
     * Creates a ModMenu {@link ConfigScreenFactory} for a static configuration class.
     *
     * @param configClass the class containing static fields annotated with {@link ConfigOption}.
     * @return a factory producing the configuration screen, or a dummy factory if YACL is absent.
     */
    public static ConfigScreenFactory<?> createFactory(Class<?> configClass) {
        return createFactory(null, (Object) configClass, null);
    }

    /**
     * Creates a ModMenu {@link ConfigScreenFactory} for a configuration object instance.
     *
     * @param configInstance the instance (or class) containing fields annotated with {@link ConfigOption}.
     * @return a factory producing the configuration screen, or a dummy factory if YACL is absent.
     */
    public static ConfigScreenFactory<?> createFactory(Object configInstance) {
        return createFactory(null, configInstance, null);
    }

    /**
     * Creates a ModMenu {@link ConfigScreenFactory} with an explicit mod identifier.
     *
     * @param modId the mod ID used for auto-inferred translation keys (e.g. {@code config.<modId>.title}).
     * @param configInstance the configuration class or instance.
     * @return a factory producing the configuration screen, or a dummy factory if YACL is absent.
     */
    public static ConfigScreenFactory<?> createFactory(String modId, Object configInstance) {
        return createFactory(modId, configInstance, null);
    }

    /**
     * Creates a ModMenu {@link ConfigScreenFactory} with explicit mod ID and custom save action.
     *
     * @param modId the mod ID used for auto-inferred translation keys.
     * @param configInstance the configuration class or instance.
     * @param saveAction custom action to run when saving (or {@code null} to auto-invoke {@code save()}).
     * @return a factory producing the configuration screen, or a dummy factory if YACL is absent.
     */
    public static ConfigScreenFactory<?> createFactory(String modId, Object configInstance, Runnable saveAction) {
        if (!YaclSafetyBridge.isYaclAvailable()) {
            LOGGER.warn("YetAnotherConfigLib (YACL) is not loaded; returning fallback null config screen factory.");
            return parent -> null;
        }
        return parent -> createScreen(parent, modId, configInstance, saveAction);
    }

    // ==========================================
    // SCREEN CREATION API
    // ==========================================

    /**
     * Creates a configuration screen for a static configuration class with auto-inferred mod ID.
     */
    public static Screen createScreen(Screen parent, Class<?> configClass) {
        return createScreen(parent, null, (Object) configClass, null);
    }

    /**
     * Creates a configuration screen for a configuration instance with auto-inferred mod ID.
     */
    public static Screen createScreen(Screen parent, Object configInstance) {
        return createScreen(parent, null, configInstance, null);
    }

    /**
     * Creates a configuration screen for a configuration instance with explicit mod ID.
     */
    public static Screen createScreen(Screen parent, String modId, Object configInstance) {
        return createScreen(parent, modId, configInstance, null);
    }

    /**
     * Core generator: builds a complete YACL configuration {@link Screen} reflectively.
     *
     * @param parent the parent screen to return to when closed.
     * @param modId the mod identifier for translation keys (or {@code null} to auto-infer).
     * @param configInstance the target config instance or static config class.
     * @param saveAction explicit action executed on save (or {@code null} to invoke {@code save()}).
     * @return the built {@link Screen}, or {@code null} if YACL is absent or an error occurs.
     */
    public static Screen createScreen(Screen parent, String modId, Object configInstance, Runnable saveAction) {
        if (configInstance == null) {
            LOGGER.warn("Config instance or class cannot be null when generating configuration screen.");
            return null;
        }
        if (!YaclSafetyBridge.isYaclAvailable()) {
            LOGGER.warn("YetAnotherConfigLib (YACL) is not available; cannot generate configuration screen.");
            return null;
        }

        Class<?> configClass;
        Object targetInstance;
        if (configInstance instanceof Class<?> clazz) {
            configClass = clazz;
            targetInstance = null;
        } else {
            configClass = configInstance.getClass();
            targetInstance = configInstance;
        }

        String resolvedModId = (modId != null && !modId.isBlank()) ? modId : inferModId(configClass);

        Object yaclBuilder = YaclSafetyBridge.createYaclBuilder();
        if (yaclBuilder == null) {
            LOGGER.warn("Failed to instantiate YACL builder via reflection.");
            return null;
        }

        // 1. Discover all fields annotated with @ConfigOption across class hierarchy
        List<Field> fields = discoverConfigFields(configClass);

        // 2. Group options by category and optional sub-group
        Map<String, CategoryData> categories = new LinkedHashMap<>();

        for (Field field : fields) {
            ConfigOption annotation = field.getAnnotation(ConfigOption.class);
            if (annotation == null) {
                continue;
            }

            try {
                field.setAccessible(true);
            } catch (Throwable t) {
                LOGGER.warn("Failed to make field '{}' accessible: {}", field.getName(), t.getMessage());
            }

            boolean isStatic = Modifier.isStatic(field.getModifiers());
            Object target = isStatic ? null : targetInstance;

            if (target == null && !isStatic) {
                LOGGER.warn("Field '{}' in {} is not static but no instance was provided; skipping.", field.getName(), configClass.getName());
                continue;
            }

            Object builtOption = buildFieldOption(field, annotation, target, resolvedModId, configClass);
            if (builtOption == null) {
                continue;
            }

            String catKey = annotation.category();
            if (catKey == null || catKey.isBlank()) {
                catKey = "general";
            }

            CategoryData catData = categories.computeIfAbsent(catKey, CategoryData::new);
            String groupKey = annotation.group();
            if (groupKey == null || groupKey.isBlank()) {
                catData.rootOptions.add(new OptionItem(field, annotation, builtOption));
            } else {
                catData.groups.computeIfAbsent(groupKey, k -> new ArrayList<>())
                        .add(new OptionItem(field, annotation, builtOption));
            }
        }

        // If no options were discovered, create at least one default category
        if (categories.isEmpty()) {
            categories.put("general", new CategoryData("general"));
        }

        // 3. Build categories and attach to YACL builder
        for (CategoryData catData : categories.values()) {
            Component categoryTitle = resolveCategoryName(catData.categoryKey, resolvedModId);
            Object categoryBuilder = YaclSafetyBridge.createCategoryBuilder(categoryTitle);
            if (categoryBuilder == null) {
                continue;
            }

            // Intersperse Ko-fi & Discord creator support buttons into category header
            Object kofiButton = DasikSupportHelper.createYaclButton();
            if (kofiButton != null) {
                YaclSafetyBridge.addOptionToCategory(categoryBuilder, kofiButton);
            }

            Object discordButton = DasikSupportHelper.createDiscordYaclButton();
            if (discordButton != null) {
                YaclSafetyBridge.addOptionToCategory(categoryBuilder, discordButton);
            }

            // Sort root options by order ascending
            catData.rootOptions.sort(Comparator.comparingInt(item -> item.annotation.order()));
            for (OptionItem item : catData.rootOptions) {
                YaclSafetyBridge.addOptionToCategory(categoryBuilder, item.builtOption);
            }

            // Sort and build sub-groups
            List<Map.Entry<String, List<OptionItem>>> sortedGroups = new ArrayList<>(catData.groups.entrySet());
            sortedGroups.sort(Comparator.comparingInt(entry ->
                    entry.getValue().stream().mapToInt(item -> item.annotation.order()).min().orElse(0)
            ));

            for (Map.Entry<String, List<OptionItem>> groupEntry : sortedGroups) {
                Component groupTitle = resolveGroupName(groupEntry.getKey(), resolvedModId);
                Object groupBuilder = YaclSafetyBridge.createOptionGroupBuilder(groupTitle);
                if (groupBuilder == null) {
                    continue;
                }

                List<OptionItem> groupItems = groupEntry.getValue();
                groupItems.sort(Comparator.comparingInt(item -> item.annotation.order()));

                for (OptionItem item : groupItems) {
                    YaclSafetyBridge.addOptionToGroup(groupBuilder, item.builtOption);
                }

                Object builtGroup = YaclSafetyBridge.buildGroup(groupBuilder);
                if (builtGroup != null) {
                    YaclSafetyBridge.addGroupToCategory(categoryBuilder, builtGroup);
                }
            }

            Object builtCategory = YaclSafetyBridge.buildCategory(categoryBuilder);
            if (builtCategory != null) {
                YaclSafetyBridge.addCategoryToYacl(yaclBuilder, builtCategory);
            }
        }

        // 4. Set Title
        Component titleComponent = Component.translatable("config." + resolvedModId + ".title");
        YaclSafetyBridge.setYaclTitle(yaclBuilder, titleComponent);

        // 5. Wire Save Action
        Runnable effectiveSaveAction = () -> {
            if (saveAction != null) {
                try {
                    saveAction.run();
                } catch (Throwable t) {
                    LOGGER.error("Failed executing custom save action for {}: {}", resolvedModId, t.getMessage(), t);
                }
            } else {
                invokeSaveMethod(configClass, targetInstance);
            }
        };
        YaclSafetyBridge.setYaclSave(yaclBuilder, effectiveSaveAction);

        // 6. Generate and return final Screen
        return YaclSafetyBridge.buildScreen(yaclBuilder, parent);
    }

    // ==========================================
    // OPTION INTROSPECTION & BUILDER
    // ==========================================

    private static Object buildFieldOption(Field field, ConfigOption annotation, Object target, String modId, Class<?> configClass) {
        Class<?> fieldType = field.getType();
        Object optionBuilder;
        Function<Object, Object> controllerFactory;

        if (fieldType == boolean.class || fieldType == Boolean.class) {
            optionBuilder = YaclSafetyBridge.createOptionBuilder(Boolean.class);
            controllerFactory = YaclSafetyBridge.booleanControllerFactory(!annotation.slider());
        } else if (fieldType == int.class || fieldType == Integer.class) {
            optionBuilder = YaclSafetyBridge.createOptionBuilder(Integer.class);
            int min = !Double.isInfinite(annotation.min()) ? (int) Math.round(annotation.min()) : Integer.MIN_VALUE;
            int max = !Double.isInfinite(annotation.max()) ? (int) Math.round(annotation.max()) : Integer.MAX_VALUE;
            int step = (!Double.isInfinite(annotation.step()) && annotation.step() > 0) ? (int) Math.round(annotation.step()) : 1;
            boolean isFinite = min != Integer.MIN_VALUE && max != Integer.MAX_VALUE;
            boolean useSlider = annotation.slider() && isFinite;
            controllerFactory = YaclSafetyBridge.integerControllerFactory(useSlider, min, max, step);
        } else if (fieldType == float.class || fieldType == Float.class) {
            optionBuilder = YaclSafetyBridge.createOptionBuilder(Float.class);
            float min = !Double.isInfinite(annotation.min()) ? (float) annotation.min() : Float.NEGATIVE_INFINITY;
            float max = !Double.isInfinite(annotation.max()) ? (float) annotation.max() : Float.POSITIVE_INFINITY;
            float step = (!Double.isInfinite(annotation.step()) && annotation.step() > 0) ? (float) annotation.step() : 1.0f;
            boolean isFinite = !Float.isInfinite(min) && !Float.isInfinite(max);
            boolean useSlider = annotation.slider() && isFinite;
            controllerFactory = YaclSafetyBridge.floatControllerFactory(useSlider, min, max, step);
        } else if (fieldType == double.class || fieldType == Double.class) {
            optionBuilder = YaclSafetyBridge.createOptionBuilder(Double.class);
            double min = annotation.min();
            double max = annotation.max();
            double step = annotation.step() > 0 ? annotation.step() : 1.0;
            boolean isFinite = !Double.isInfinite(min) && !Double.isInfinite(max);
            boolean useSlider = annotation.slider() && isFinite;
            controllerFactory = YaclSafetyBridge.doubleControllerFactory(useSlider, min, max, step);
        } else if (fieldType == String.class) {
            optionBuilder = YaclSafetyBridge.createOptionBuilder(String.class);
            controllerFactory = YaclSafetyBridge.stringControllerFactory();
        } else if (fieldType.isEnum()) {
            optionBuilder = YaclSafetyBridge.createOptionBuilder(fieldType);
            controllerFactory = YaclSafetyBridge.enumControllerFactory(fieldType, !annotation.slider());
        } else {
            LOGGER.warn("Unsupported @ConfigOption field type '{}' on field '{}' in {}; skipping.",
                    fieldType.getName(), field.getName(), configClass.getName());
            return null;
        }

        if (optionBuilder == null) {
            return null;
        }

        // Set Option Name
        Component nameComponent = resolveOptionName(annotation, field, modId);
        YaclSafetyBridge.setOptionName(optionBuilder, nameComponent);

        // Set Option Description
        Component descComponent = resolveOptionDescription(annotation, field, modId);
        Object descriptionObj = YaclSafetyBridge.createOptionDescription(descComponent);
        if (descriptionObj != null) {
            YaclSafetyBridge.setOptionDescription(optionBuilder, descriptionObj);
        }

        // Compute default value for binding
        Object defaultValue = resolveDefaultValue(field, target, fieldType);

        // Wire Getter and Setter
        Supplier<Object> getter = () -> {
            try {
                return field.get(target);
            } catch (Throwable t) {
                LOGGER.warn("Failed to get config field value '{}': {}", field.getName(), t.getMessage());
                return null;
            }
        };

        Consumer<Object> setter = newVal -> {
            if (newVal == null && fieldType.isPrimitive()) {
                LOGGER.warn("Cannot set null value to primitive field '{}'; skipping.", field.getName());
                return;
            }
            try {
                field.set(target, newVal);
            } catch (Throwable t) {
                LOGGER.warn("Failed to set config field '{}' = '{}': {}", field.getName(), newVal, t.getMessage());
            }

            if (annotation.syncGameRule()) {
                String ruleName;
                if (!annotation.gameRuleName().isEmpty()) {
                    ruleName = annotation.gameRuleName();
                } else if (modId != null && !modId.isEmpty()) {
                    ruleName = modId + ":" + field.getName();
                } else {
                    ruleName = field.getName();
                }
                DynamicGameRuleConfigSync.syncToGameRule(ruleName, newVal);
            }
        };

        YaclSafetyBridge.setOptionBinding(optionBuilder, defaultValue, getter, setter);
        YaclSafetyBridge.setOptionController(optionBuilder, controllerFactory);

        return YaclSafetyBridge.buildOption(optionBuilder);
    }

    private static Object resolveDefaultValue(Field field, Object target, Class<?> fieldType) {
        Object val = null;
        try {
            val = field.get(target);
        } catch (Throwable ignored) {}

        if (val != null) {
            return val;
        }

        if (fieldType == boolean.class || fieldType == Boolean.class) return Boolean.FALSE;
        if (fieldType == int.class || fieldType == Integer.class) return 0;
        if (fieldType == float.class || fieldType == Float.class) return 0.0f;
        if (fieldType == double.class || fieldType == Double.class) return 0.0;
        if (fieldType == String.class) return "";
        if (fieldType.isEnum()) {
            Object[] constants = fieldType.getEnumConstants();
            return (constants != null && constants.length > 0) ? constants[0] : null;
        }
        return null;
    }

    static List<Field> discoverConfigFields(Class<?> configClass) {
        List<Field> result = new ArrayList<>();
        List<Class<?>> hierarchy = new ArrayList<>();
        Class<?> curr = configClass;
        while (curr != null && curr != Object.class) {
            hierarchy.add(0, curr);
            curr = curr.getSuperclass();
        }
        for (Class<?> clazz : hierarchy) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ConfigOption.class)) {
                    result.add(field);
                }
            }
        }
        return result;
    }

    static void invokeSaveMethod(Class<?> configClass, Object targetInstance) {
        try {
            Method saveMethod = null;
            try {
                saveMethod = configClass.getMethod("save");
            } catch (NoSuchMethodException ignored) {
                try {
                    saveMethod = configClass.getDeclaredMethod("save");
                    saveMethod.setAccessible(true);
                } catch (NoSuchMethodException ignored2) {}
            }

            if (saveMethod != null) {
                Object target = Modifier.isStatic(saveMethod.getModifiers()) ? null : targetInstance;
                saveMethod.invoke(target);
                LOGGER.debug("Executed parameterless save() method on {}", configClass.getName());
            }
        } catch (Throwable t) {
            LOGGER.warn("Failed to invoke save() method on {}: {}", configClass.getName(), t.getMessage());
        }
    }

    // ==========================================
    // TRANSLATION RESOLUTION UTILITIES
    // ==========================================

    static Component resolveCategoryName(String category, String modId) {
        if (category == null || category.isBlank()) {
            category = "general";
        }
        if (category.startsWith("config.") || category.contains(".")) {
            return Component.translatable(category);
        }
        return Component.translatable("config." + modId + ".category." + category);
    }

    static Component resolveGroupName(String group, String modId) {
        if (group.startsWith("config.") || group.contains(".")) {
            return Component.translatable(group);
        }
        return Component.translatable("config." + modId + ".group." + group);
    }

    static Component resolveOptionName(ConfigOption annotation, Field field, String modId) {
        if (!annotation.nameKey().isEmpty()) {
            return Component.translatable(annotation.nameKey());
        }
        return Component.translatable("config." + modId + "." + field.getName());
    }

    static Component resolveOptionDescription(ConfigOption annotation, Field field, String modId) {
        if (!annotation.descriptionKey().isEmpty()) {
            return Component.translatable(annotation.descriptionKey());
        }
        return Component.translatable("config." + modId + "." + field.getName() + ".description");
    }

    static String inferModId(Class<?> configClass) {
        // 1. Inspect public static MOD_ID, MODID, or ID constant
        for (String fieldName : List.of("MOD_ID", "MODID", "MOD_NAME", "ID")) {
            try {
                Field f = configClass.getField(fieldName);
                if (Modifier.isStatic(f.getModifiers()) && f.getType() == String.class) {
                    String val = (String) f.get(null);
                    if (val != null && !val.isBlank()) {
                        return val;
                    }
                }
            } catch (Throwable ignored) {}
        }

        // 2. Inspect package name segment preceding "config"
        String pkg = configClass.getPackageName();
        String[] parts = pkg.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equalsIgnoreCase("config") && i > 0) {
                return parts[i - 1].toLowerCase(Locale.ROOT);
            }
        }

        // 3. Fall back to simplified class name without "Config" suffix
        String name = configClass.getSimpleName();
        name = name.replaceAll("(?i)config(uration)?$", "").toLowerCase(Locale.ROOT);
        if (!name.isBlank()) {
            return name;
        }

        return "general";
    }

    // ==========================================
    // INTERNAL DATA STRUCTURES
    // ==========================================

    private static class CategoryData {
        final String categoryKey;
        final List<OptionItem> rootOptions = new ArrayList<>();
        final Map<String, List<OptionItem>> groups = new LinkedHashMap<>();

        CategoryData(String categoryKey) {
            this.categoryKey = categoryKey;
        }
    }

    private static class OptionItem {
        final Field field;
        final ConfigOption annotation;
        final Object builtOption;

        OptionItem(Field field, ConfigOption annotation, Object builtOption) {
            this.field = field;
            this.annotation = annotation;
            this.builtOption = builtOption;
        }
    }
}
