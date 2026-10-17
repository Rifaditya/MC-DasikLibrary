// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.client.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import net.dasik.social.api.config.ConfigOption;
import net.minecraft.network.chat.Component;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class YaclSafetyBridgeTest {

    public static class SampleConfig {
        @ConfigOption(category = "general")
        public static boolean testValue = true;
    }

    @Test
    @DisplayName("Knot ClassLoader Resilience: isYaclAvailable returns boolean without throwing errors")
    void testIsYaclAvailableDoesNotThrow() {
        assertDoesNotThrow(() -> {
            boolean available = YaclSafetyBridge.isYaclAvailable();
            // In headless unit test environment without YACL present on classpath, should be false
            assertFalse(available, "YACL must not be reported as available when not on test runtime classpath");
        });
    }

    @Test
    @DisplayName("Knot ClassLoader Resilience: isModMenuAvailable returns boolean without throwing errors")
    void testIsModMenuAvailableDoesNotThrow() {
        assertDoesNotThrow(() -> {
            boolean available = YaclSafetyBridge.isModMenuAvailable();
            assertNotNull(Boolean.valueOf(available));
        });
    }

    @Test
    @DisplayName("Knot ClassLoader Resilience: All reflection builders safely return null when YACL is absent")
    void testReflectionBuildersReturnNullWhenAbsent() {
        if (!YaclSafetyBridge.isYaclAvailable()) {
            assertNull(YaclSafetyBridge.createYaclBuilder(), "createYaclBuilder must return null when YACL absent");
            assertNull(YaclSafetyBridge.createCategoryBuilder(), "createCategoryBuilder must return null when YACL absent");
            assertNull(YaclSafetyBridge.createCategoryBuilder(Component.literal("General")), "createCategoryBuilder with name must return null when YACL absent");
            assertNull(YaclSafetyBridge.createOptionGroupBuilder(), "createOptionGroupBuilder must return null when YACL absent");
            assertNull(YaclSafetyBridge.createOptionGroupBuilder(Component.literal("Group")), "createOptionGroupBuilder with name must return null when YACL absent");
            assertNull(YaclSafetyBridge.createOptionBuilder(Boolean.class), "createOptionBuilder must return null when YACL absent");
            assertNull(YaclSafetyBridge.createOptionDescription((Component) null), "createOptionDescription must return null when YACL absent");
            assertNull(YaclSafetyBridge.buildScreen(null, null), "buildScreen must return null when builder is null");

            // Controller builders safe null returns
            assertNull(YaclSafetyBridge.createBooleanController(null, false));
            assertNull(YaclSafetyBridge.createIntegerController(null, false, 0, 10, 1));
            assertNull(YaclSafetyBridge.createFloatController(null, false, 0.0f, 1.0f, 0.1f));
            assertNull(YaclSafetyBridge.createDoubleController(null, false, 0.0, 1.0, 0.1));
            assertNull(YaclSafetyBridge.createStringController(null));
            assertNull(YaclSafetyBridge.createEnumController(null, Object.class, false));

            // Null target safety for operations
            assertFalse(YaclSafetyBridge.addOptionToCategory(null, null));
            assertFalse(YaclSafetyBridge.addGroupToCategory(null, null));
            assertFalse(YaclSafetyBridge.addOptionToGroup(null, null));
            assertFalse(YaclSafetyBridge.addCategoryToYacl(null, null));
            assertFalse(YaclSafetyBridge.setYaclTitle(null, null));
            assertFalse(YaclSafetyBridge.setYaclSave(null, null));
            assertFalse(YaclSafetyBridge.setOptionName(null, null));
            assertFalse(YaclSafetyBridge.setOptionDescription(null, null));
            assertFalse(YaclSafetyBridge.setOptionBinding(null, null, null, null));
            assertFalse(YaclSafetyBridge.setOptionController(null, null));
            assertNull(YaclSafetyBridge.buildCategory(null));
            assertNull(YaclSafetyBridge.buildGroup(null));
            assertNull(YaclSafetyBridge.buildOption(null));
        }
    }

    @Test
    @DisplayName("ModMenu Optional Contract: UniversalConfigScreen.createFactory returns non-null factory that produces null when YACL is absent")
    void testModMenuOptionalContract() {
        if (!YaclSafetyBridge.isYaclAvailable()) {
            // Static class factory
            ConfigScreenFactory<?> staticFactory = UniversalConfigScreen.createFactory(SampleConfig.class);
            assertNotNull(staticFactory, "Factory itself must never be null");
            assertNull(staticFactory.create(null), "Factory must produce null screen when YACL is absent");

            // Instance factory
            ConfigScreenFactory<?> instanceFactory = UniversalConfigScreen.createFactory(new SampleConfig());
            assertNotNull(instanceFactory, "Instance factory must never be null");
            assertNull(instanceFactory.create(null), "Instance factory must produce null screen when YACL is absent");

            // Explicit modId factory
            ConfigScreenFactory<?> explicitFactory = UniversalConfigScreen.createFactory("testmod", new SampleConfig());
            assertNotNull(explicitFactory, "Explicit modId factory must never be null");
            assertNull(explicitFactory.create(null), "Explicit modId factory must produce null screen when YACL is absent");

            // Custom save action factory
            ConfigScreenFactory<?> saveActionFactory = UniversalConfigScreen.createFactory("testmod", SampleConfig.class, () -> {});
            assertNotNull(saveActionFactory, "Save action factory must never be null");
            assertNull(saveActionFactory.create(null), "Save action factory must produce null screen when YACL is absent");
        }
    }
}
