// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
// Verified against: YetAnotherConfigLib v3 (26.2+)
package net.dasik.social.api.client.gui;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Knot ClassLoader safe reflection bridge for YetAnotherConfigLib (YACL) v3.
 * <p>
 * Provides dynamic discovery, reflection-based builders, and controller instantiations
 * without requiring any compile-time or runtime hard dependencies on YACL.
 */
@DasikApiStatus.Public
@Environment(EnvType.CLIENT)
public class YaclSafetyBridge {
    private static final Logger LOGGER = LoggerFactory.getLogger("DasikLibrary|YACL");

    private static final String YACL_MOD_ID_V3 = "yet_another_config_lib_v3";
    private static final String YACL_MOD_ID_HYPHEN = "yet-another-config-lib";
    private static final String MODMENU_MOD_ID = "modmenu";

    private static final String YACL_MAIN_CLASS = "dev.isxander.yacl3.api.YetAnotherConfigLib";
    private static final String YACL_CATEGORY_CLASS = "dev.isxander.yacl3.api.ConfigCategory";
    private static final String YACL_GROUP_CLASS = "dev.isxander.yacl3.api.OptionGroup";
    private static final String YACL_OPTION_CLASS = "dev.isxander.yacl3.api.Option";
    private static final String YACL_DESC_CLASS = "dev.isxander.yacl3.api.OptionDescription";

    private static final String TICKBOX_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.TickBoxControllerBuilder";
    private static final String BOOLEAN_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.BooleanControllerBuilder";
    private static final String INT_SLIDER_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder";
    private static final String INT_FIELD_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder";
    private static final String FLOAT_SLIDER_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder";
    private static final String FLOAT_FIELD_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.FloatFieldControllerBuilder";
    private static final String DOUBLE_SLIDER_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.DoubleSliderControllerBuilder";
    private static final String DOUBLE_FIELD_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.DoubleFieldControllerBuilder";
    private static final String STRING_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.StringControllerBuilder";
    private static final String ENUM_DROPDOWN_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.EnumDropdownControllerBuilder";
    private static final String ENUM_CONTROLLER_CLASS = "dev.isxander.yacl3.api.controller.EnumControllerBuilder";

    /**
     * Checks whether YetAnotherConfigLib v3 is loaded and its main API class is resolvable.
     */
    public static boolean isYaclAvailable() {
        try {
            FabricLoader loader = FabricLoader.getInstance();
            if (loader == null) {
                return false;
            }
            if (!loader.isModLoaded(YACL_MOD_ID_V3) && !loader.isModLoaded(YACL_MOD_ID_HYPHEN)) {
                return false;
            }
            Class.forName(YACL_MAIN_CLASS, false, YaclSafetyBridge.class.getClassLoader());
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Checks whether ModMenu is loaded in the current runtime environment.
     */
    public static boolean isModMenuAvailable() {
        try {
            FabricLoader loader = FabricLoader.getInstance();
            return loader != null && loader.isModLoaded(MODMENU_MOD_ID);
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Reflectively constructs a new {@code YetAnotherConfigLib.Builder} instance.
     *
     * @return builder instance, or {@code null} if YACL is unavailable.
     */
    public static Object createYaclBuilder() {
        if (!isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(YACL_MAIN_CLASS);
            Method createBuilder = clazz.getMethod("createBuilder");
            return createBuilder.invoke(null);
        } catch (Throwable t) {
            LOGGER.warn("Failed to create YACL builder via reflection: {}", t.getMessage());
            return null;
        }
    }

    /**
     * Reflectively constructs a new {@code ConfigCategory.Builder} instance.
     */
    public static Object createCategoryBuilder() {
        if (!isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(YACL_CATEGORY_CLASS);
            Method createBuilder = clazz.getMethod("createBuilder");
            return createBuilder.invoke(null);
        } catch (Throwable t) {
            LOGGER.warn("Failed to create ConfigCategory builder via reflection: {}", t.getMessage());
            return null;
        }
    }

    /**
     * Reflectively constructs a new {@code ConfigCategory.Builder} initialized with the given name.
     */
    public static Object createCategoryBuilder(Component name) {
        Object builder = createCategoryBuilder();
        if (builder != null && name != null) {
            invokeMethod(builder, "name", new Class<?>[]{Component.class}, name);
        }
        return builder;
    }

    /**
     * Reflectively constructs a new {@code OptionGroup.Builder} instance.
     */
    public static Object createOptionGroupBuilder() {
        if (!isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(YACL_GROUP_CLASS);
            Method createBuilder = clazz.getMethod("createBuilder");
            return createBuilder.invoke(null);
        } catch (Throwable t) {
            LOGGER.warn("Failed to create OptionGroup builder via reflection: {}", t.getMessage());
            return null;
        }
    }

    /**
     * Reflectively constructs a new {@code OptionGroup.Builder} initialized with the given name.
     */
    public static Object createOptionGroupBuilder(Component name) {
        Object builder = createOptionGroupBuilder();
        if (builder != null && name != null) {
            invokeMethod(builder, "name", new Class<?>[]{Component.class}, name);
        }
        return builder;
    }

    /**
     * Reflectively constructs a new {@code Option.Builder} instance for the specified value type.
     */
    public static Object createOptionBuilder(Class<?> valueType) {
        if (!isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(YACL_OPTION_CLASS);
            try {
                Method createBuilderWithType = clazz.getMethod("createBuilder", Class.class);
                return createBuilderWithType.invoke(null, valueType);
            } catch (NoSuchMethodException e) {
                Method createBuilder = clazz.getMethod("createBuilder");
                return createBuilder.invoke(null);
            }
        } catch (Throwable t) {
            LOGGER.warn("Failed to create Option builder for type '{}' via reflection: {}", valueType != null ? valueType.getName() : "null", t.getMessage());
            return null;
        }
    }

    /**
     * Reflectively constructs an {@code OptionDescription} from a single text component.
     */
    public static Object createOptionDescription(Component description) {
        if (description == null || !isYaclAvailable()) {
            return null;
        }
        return createOptionDescription(List.of(description));
    }

    /**
     * Reflectively constructs an {@code OptionDescription} from a collection of text components.
     */
    public static Object createOptionDescription(Collection<Component> lines) {
        if (lines == null || lines.isEmpty() || !isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> descClass = Class.forName(YACL_DESC_CLASS);
            Method descCreateBuilderMethod = descClass.getMethod("createBuilder");
            Object descBuilder = descCreateBuilderMethod.invoke(null);
            for (Method m : descBuilder.getClass().getMethods()) {
                if (m.getName().equals("text") && m.getParameterCount() == 1 && Collection.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    m.invoke(descBuilder, lines);
                    break;
                }
            }
            Method buildMethod = descBuilder.getClass().getMethod("build");
            return buildMethod.invoke(descBuilder);
        } catch (Throwable t) {
            LOGGER.warn("Failed to create OptionDescription via reflection: {}", t.getMessage());
            return null;
        }
    }

    // ==========================================
    // CONTROLLER BUILDERS
    // ==========================================

    /**
     * Constructs a boolean controller builder (TickBox or Button).
     */
    public static Object createBooleanController(Object option, boolean tickBox) {
        if (option == null || !isYaclAvailable()) {
            return null;
        }
        try {
            String className = tickBox ? TICKBOX_CONTROLLER_CLASS : BOOLEAN_CONTROLLER_CLASS;
            Class<?> controllerClass = Class.forName(className);
            Class<?> optionClass = Class.forName(YACL_OPTION_CLASS);
            Method createMethod = controllerClass.getMethod("create", optionClass);
            Object controller = createMethod.invoke(null, option);
            if (!tickBox && controller != null) {
                // By default style boolean buttons with coloured state (true/false)
                invokeMethod(controller, "coloured", new Class<?>[]{boolean.class}, true);
            }
            return controller;
        } catch (Throwable t) {
            LOGGER.warn("Failed to create Boolean controller via reflection: {}", t.getMessage());
            return null;
        }
    }

    /**
     * Constructs an integer controller builder (Slider or numeric text field).
     */
    public static Object createIntegerController(Object option, boolean slider, int min, int max, int step) {
        if (option == null || !isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> optionClass = Class.forName(YACL_OPTION_CLASS);
            if (slider && min != Integer.MIN_VALUE && max != Integer.MAX_VALUE) {
                Class<?> controllerClass = Class.forName(INT_SLIDER_CONTROLLER_CLASS);
                Method createMethod = controllerClass.getMethod("create", optionClass);
                Object controller = createMethod.invoke(null, option);
                invokeRange(controller, min, max);
                if (step > 0) {
                    invokeStep(controller, step);
                }
                return controller;
            } else {
                Class<?> controllerClass = Class.forName(INT_FIELD_CONTROLLER_CLASS);
                Method createMethod = controllerClass.getMethod("create", optionClass);
                Object controller = createMethod.invoke(null, option);
                if (min != Integer.MIN_VALUE || max != Integer.MAX_VALUE) {
                    invokeRange(controller, min, max);
                }
                return controller;
            }
        } catch (Throwable t) {
            LOGGER.warn("Failed to create Integer controller via reflection: {}", t.getMessage());
            return null;
        }
    }

    /**
     * Constructs a float controller builder (Slider or numeric text field).
     */
    public static Object createFloatController(Object option, boolean slider, float min, float max, float step) {
        if (option == null || !isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> optionClass = Class.forName(YACL_OPTION_CLASS);
            if (slider && !Float.isInfinite(min) && !Float.isInfinite(max)) {
                Class<?> controllerClass = Class.forName(FLOAT_SLIDER_CONTROLLER_CLASS);
                Method createMethod = controllerClass.getMethod("create", optionClass);
                Object controller = createMethod.invoke(null, option);
                invokeRange(controller, min, max);
                if (step > 0.0f) {
                    invokeStep(controller, step);
                }
                return controller;
            } else {
                Class<?> controllerClass = Class.forName(FLOAT_FIELD_CONTROLLER_CLASS);
                Method createMethod = controllerClass.getMethod("create", optionClass);
                Object controller = createMethod.invoke(null, option);
                if (!Float.isInfinite(min) || !Float.isInfinite(max)) {
                    invokeRange(controller, min, max);
                }
                return controller;
            }
        } catch (Throwable t) {
            LOGGER.warn("Failed to create Float controller via reflection: {}", t.getMessage());
            return null;
        }
    }

    /**
     * Constructs a double controller builder (Slider or numeric text field).
     */
    public static Object createDoubleController(Object option, boolean slider, double min, double max, double step) {
        if (option == null || !isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> optionClass = Class.forName(YACL_OPTION_CLASS);
            if (slider && !Double.isInfinite(min) && !Double.isInfinite(max)) {
                Class<?> controllerClass = Class.forName(DOUBLE_SLIDER_CONTROLLER_CLASS);
                Method createMethod = controllerClass.getMethod("create", optionClass);
                Object controller = createMethod.invoke(null, option);
                invokeRange(controller, min, max);
                if (step > 0.0) {
                    invokeStep(controller, step);
                }
                return controller;
            } else {
                Class<?> controllerClass = Class.forName(DOUBLE_FIELD_CONTROLLER_CLASS);
                Method createMethod = controllerClass.getMethod("create", optionClass);
                Object controller = createMethod.invoke(null, option);
                if (!Double.isInfinite(min) || !Double.isInfinite(max)) {
                    invokeRange(controller, min, max);
                }
                return controller;
            }
        } catch (Throwable t) {
            LOGGER.warn("Failed to create Double controller via reflection: {}", t.getMessage());
            return null;
        }
    }

    /**
     * Constructs a string text field controller builder.
     */
    public static Object createStringController(Object option) {
        if (option == null || !isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> optionClass = Class.forName(YACL_OPTION_CLASS);
            Class<?> controllerClass = Class.forName(STRING_CONTROLLER_CLASS);
            Method createMethod = controllerClass.getMethod("create", optionClass);
            return createMethod.invoke(null, option);
        } catch (Throwable t) {
            LOGGER.warn("Failed to create String controller via reflection: {}", t.getMessage());
            return null;
        }
    }

    /**
     * Constructs an enum controller builder (Dropdown or cyclic button).
     */
    public static Object createEnumController(Object option, Class<?> enumClass, boolean dropdown) {
        if (option == null || !isYaclAvailable()) {
            return null;
        }
        try {
            Class<?> optionClass = Class.forName(YACL_OPTION_CLASS);
            if (dropdown) {
                try {
                    Class<?> controllerClass = Class.forName(ENUM_DROPDOWN_CONTROLLER_CLASS);
                    Method createMethod = controllerClass.getMethod("create", optionClass);
                    return createMethod.invoke(null, option);
                } catch (Throwable ignored) {
                    // Fall back to cyclic enum controller if dropdown is unavailable
                }
            }
            Class<?> controllerClass = Class.forName(ENUM_CONTROLLER_CLASS);
            Method createMethod = controllerClass.getMethod("create", optionClass);
            return createMethod.invoke(null, option);
        } catch (Throwable t) {
            LOGGER.warn("Failed to create Enum controller via reflection: {}", t.getMessage());
            return null;
        }
    }

    // ==========================================
    // CONTROLLER FUNCTION FACTORIES
    // ==========================================

    public static Function<Object, Object> booleanControllerFactory(boolean tickBox) {
        return option -> createBooleanController(option, tickBox);
    }

    public static Function<Object, Object> integerControllerFactory(boolean slider, int min, int max, int step) {
        return option -> createIntegerController(option, slider, min, max, step);
    }

    public static Function<Object, Object> floatControllerFactory(boolean slider, float min, float max, float step) {
        return option -> createFloatController(option, slider, min, max, step);
    }

    public static Function<Object, Object> doubleControllerFactory(boolean slider, double min, double max, double step) {
        return option -> createDoubleController(option, slider, min, max, step);
    }

    public static Function<Object, Object> stringControllerFactory() {
        return YaclSafetyBridge::createStringController;
    }

    public static Function<Object, Object> enumControllerFactory(Class<?> enumClass, boolean dropdown) {
        return option -> createEnumController(option, enumClass, dropdown);
    }

    // ==========================================
    // OPTION BUILDER OPERATIONS
    // ==========================================

    public static boolean setOptionName(Object optionBuilder, Component name) {
        return invokeMethod(optionBuilder, "name", new Class<?>[]{Component.class}, name) != null;
    }

    public static boolean setOptionDescription(Object optionBuilder, Object description) {
        if (optionBuilder == null || description == null) {
            return false;
        }
        try {
            Class<?> descClass = Class.forName(YACL_DESC_CLASS);
            return invokeMethod(optionBuilder, "description", new Class<?>[]{descClass}, description) != null;
        } catch (Throwable t) {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean setOptionBinding(Object optionBuilder, Object defaultValue, Supplier<?> getter, Consumer<?> setter) {
        if (optionBuilder == null || getter == null || setter == null) {
            return false;
        }
        for (Method m : optionBuilder.getClass().getMethods()) {
            if (m.getName().equals("binding") && m.getParameterCount() == 3) {
                try {
                    m.invoke(optionBuilder, defaultValue, (Supplier<Object>) getter, (Consumer<Object>) setter);
                    return true;
                } catch (Throwable t) {
                    LOGGER.warn("Failed to set Option binding via reflection: {}", t.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean setOptionController(Object optionBuilder, Function<?, ?> controllerFunction) {
        if (optionBuilder == null || controllerFunction == null) {
            return false;
        }
        for (Method m : optionBuilder.getClass().getMethods()) {
            if (m.getName().equals("controller") && m.getParameterCount() == 1 && Function.class.isAssignableFrom(m.getParameterTypes()[0])) {
                try {
                    m.invoke(optionBuilder, (Function<Object, Object>) controllerFunction);
                    return true;
                } catch (Throwable t) {
                    LOGGER.warn("Failed to set Option controller via reflection: {}", t.getMessage());
                    return false;
                }
            }
        }
        return false;
    }

    public static Object buildOption(Object optionBuilder) {
        return invokeMethod(optionBuilder, "build", new Class<?>[]{});
    }

    // ==========================================
    // CATEGORY & GROUP BUILDER OPERATIONS
    // ==========================================

    public static boolean addOptionToCategory(Object categoryBuilder, Object option) {
        if (categoryBuilder == null || option == null) {
            return false;
        }
        for (Method m : categoryBuilder.getClass().getMethods()) {
            if (m.getName().equals("option") && m.getParameterCount() == 1) {
                try {
                    m.invoke(categoryBuilder, option);
                    return true;
                } catch (Throwable ignored) {}
            }
        }
        return false;
    }

    public static boolean addGroupToCategory(Object categoryBuilder, Object optionGroup) {
        if (categoryBuilder == null || optionGroup == null) {
            return false;
        }
        for (Method m : categoryBuilder.getClass().getMethods()) {
            if (m.getName().equals("group") && m.getParameterCount() == 1) {
                try {
                    m.invoke(categoryBuilder, optionGroup);
                    return true;
                } catch (Throwable ignored) {}
            }
        }
        return false;
    }

    public static boolean addOptionToGroup(Object groupBuilder, Object option) {
        if (groupBuilder == null || option == null) {
            return false;
        }
        for (Method m : groupBuilder.getClass().getMethods()) {
            if (m.getName().equals("option") && m.getParameterCount() == 1) {
                try {
                    m.invoke(groupBuilder, option);
                    return true;
                } catch (Throwable ignored) {}
            }
        }
        return false;
    }

    public static Object buildCategory(Object categoryBuilder) {
        return invokeMethod(categoryBuilder, "build", new Class<?>[]{});
    }

    public static Object buildGroup(Object groupBuilder) {
        return invokeMethod(groupBuilder, "build", new Class<?>[]{});
    }

    // ==========================================
    // YACL MAIN BUILDER OPERATIONS
    // ==========================================

    public static boolean addCategoryToYacl(Object yaclBuilder, Object category) {
        if (yaclBuilder == null || category == null) {
            return false;
        }
        for (Method m : yaclBuilder.getClass().getMethods()) {
            if (m.getName().equals("category") && m.getParameterCount() == 1) {
                try {
                    m.invoke(yaclBuilder, category);
                    return true;
                } catch (Throwable ignored) {}
            }
        }
        return false;
    }

    public static boolean setYaclTitle(Object yaclBuilder, Component title) {
        return invokeMethod(yaclBuilder, "title", new Class<?>[]{Component.class}, title) != null;
    }

    public static boolean setYaclSave(Object yaclBuilder, Runnable saveAction) {
        return invokeMethod(yaclBuilder, "save", new Class<?>[]{Runnable.class}, saveAction) != null;
    }

    /**
     * Builds the YACL configuration instance and generates a displayable {@link Screen}.
     *
     * @param yaclBuilder the {@code YetAnotherConfigLib.Builder} instance.
     * @param parent the parent screen to return to when closed.
     * @return the generated {@link Screen}, or {@code null} if an error occurs.
     */
    public static Screen buildScreen(Object yaclBuilder, Screen parent) {
        if (yaclBuilder == null) {
            return null;
        }
        try {
            Method buildMethod = yaclBuilder.getClass().getMethod("build");
            Object yaclInstance = buildMethod.invoke(yaclBuilder);
            if (yaclInstance == null) {
                return null;
            }
            for (Method m : yaclInstance.getClass().getMethods()) {
                if (m.getName().equals("generateScreen") && m.getParameterCount() == 1 && Screen.class.isAssignableFrom(m.getParameterTypes()[0])) {
                    return (Screen) m.invoke(yaclInstance, parent);
                }
            }
            Method generateScreenMethod = yaclInstance.getClass().getMethod("generateScreen", Screen.class);
            return (Screen) generateScreenMethod.invoke(yaclInstance, parent);
        } catch (Throwable t) {
            LOGGER.warn("Failed to generate YACL screen via reflection: {}", t.getMessage());
            return null;
        }
    }

    // ==========================================
    // INTERNAL REFLECTION UTILITIES
    // ==========================================

    private static Object invokeMethod(Object target, String methodName, Class<?>[] paramTypes, Object... args) {
        if (target == null) {
            return null;
        }
        try {
            Method method = target.getClass().getMethod(methodName, paramTypes);
            return method.invoke(target, args);
        } catch (Throwable t) {
            for (Method m : target.getClass().getMethods()) {
                if (m.getName().equals(methodName) && m.getParameterCount() == args.length) {
                    try {
                        return m.invoke(target, args);
                    } catch (Throwable ignored) {}
                }
            }
            LOGGER.debug("Failed to invoke method '{}' on {}: {}", methodName, target.getClass().getName(), t.getMessage());
            return null;
        }
    }

    private static void invokeRange(Object builder, Object min, Object max) {
        if (builder == null || min == null || max == null) {
            return;
        }
        for (Method m : builder.getClass().getMethods()) {
            if (m.getName().equals("range") && m.getParameterCount() == 2) {
                try {
                    m.invoke(builder, min, max);
                    return;
                } catch (Throwable ignored) {}
            }
        }
    }

    private static void invokeStep(Object builder, Object step) {
        if (builder == null || step == null) {
            return;
        }
        for (Method m : builder.getClass().getMethods()) {
            if (m.getName().equals("step") && m.getParameterCount() == 1) {
                try {
                    m.invoke(builder, step);
                    return;
                } catch (Throwable ignored) {}
            }
        }
    }
}
