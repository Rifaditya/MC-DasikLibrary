// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.client.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.dasik.social.api.config.ConfigOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.Bootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UniversalConfigScreenTest {

    @BeforeAll
    public static void setUpAll() {
        try {
            SharedConstants.tryDetectVersion();
            Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
    }

    // ==========================================
    // MOCK CONFIGURATION STRUCTURES
    // ==========================================

    public enum MockMode {
        FAST, BALANCED, QUALITY
    }

    public static class MockStaticConfig {
        public static final String MOD_ID = "testmod";

        @ConfigOption(category = "general", order = 1, nameKey = "key.custom.toggle", descriptionKey = "key.custom.toggle.desc")
        public static boolean toggleFeature = true;

        @ConfigOption(category = "general", order = 2, min = 0, max = 100, step = 5, slider = true)
        public static int entityLimit = 50;

        @ConfigOption(category = "performance", group = "rendering", order = 10, min = 0.1f, max = 2.0f, step = 0.1f, slider = true)
        public static float renderScale = 1.0f;

        @ConfigOption(category = "performance", group = "physics", order = 20, min = 0.0, max = 10.0, step = 0.5)
        public static double gravity = 9.81;

        @ConfigOption(category = "network", order = 30)
        public static String serverAddress = "127.0.0.1";

        @ConfigOption(category = "gameplay", order = 40, syncGameRule = true, gameRuleName = "testmod:gameplay_mode")
        public static MockMode qualityMode = MockMode.BALANCED;

        public static boolean saved = false;

        public static void save() {
            saved = true;
        }
    }

    public static class MockInstanceConfig {
        @ConfigOption(category = "ui", order = 1)
        public boolean showHud = true;

        @ConfigOption(category = "ui", order = 2, min = 1, max = 5)
        public int hudScale = 2;

        @ConfigOption(category = "general", order = 3)
        public String title = "Custom HUD";

        public boolean saved = false;

        public void save() {
            saved = true;
        }
    }

    // ==========================================
    // PILLAR 1: CHAOS FUZZING
    // ==========================================

    @Test
    @DisplayName("Pillar 1 (Chaos Fuzzing): 10,000 iterations of extreme bounds, NaN/infinite numbers, and null targets")
    void testChaosFuzzingBoundaries() {
        Random random = new Random(42);
        for (int i = 0; i < 10000; i++) {
            double min;
            double max;
            double step;
            switch (i % 8) {
                case 0 -> { min = Double.MAX_VALUE; max = Double.MIN_VALUE; step = Double.NaN; }
                case 1 -> { min = Double.NEGATIVE_INFINITY; max = Double.POSITIVE_INFINITY; step = Double.POSITIVE_INFINITY; }
                case 2 -> { min = Double.NaN; max = Double.NaN; step = 0.0; }
                case 3 -> { min = -Double.MAX_VALUE; max = 0.0; step = -1.0; }
                case 4 -> { min = Double.MIN_NORMAL; max = Double.MAX_VALUE; step = random.nextDouble() * 1000.0; }
                case 5 -> { min = -100.0; max = -50.0; step = 0.0001; }
                case 6 -> { min = random.nextDouble(); max = random.nextDouble(); step = random.nextDouble(); }
                default -> { min = Double.NEGATIVE_INFINITY; max = 100.0; step = 1.0; }
            }

            String category = (i % 3 == 0) ? "" : (i % 3 == 1) ? "   " : null;
            String modId = (i % 4 == 0) ? null : (i % 4 == 1) ? "" : (i % 4 == 2) ? "custom_mod_" + i : "   ";
            Object target = (i % 5 == 0) ? null : (i % 5 == 1) ? MockStaticConfig.class : (i % 5 == 2) ? new MockInstanceConfig() : Object.class;

            assertDoesNotThrow(() -> {
                ConfigScreenFactory<?> factory = UniversalConfigScreen.createFactory(modId, target);
                assertNotNull(factory);
                factory.create(null);
            }, "Chaos fuzzing iteration " + i + " must not throw unhandled exception");

            assertDoesNotThrow(() -> {
                UniversalConfigScreen.createScreen(null, modId, target, null);
            }, "createScreen in chaos iteration " + i + " must safely return null or handle gracefully");
        }
    }

    // ==========================================
    // PILLAR 2: CONCURRENCY LOAD
    // ==========================================

    @Test
    @DisplayName("Pillar 2 (Concurrency Load): 50 concurrent threads inspecting configs and invoking createFactory")
    void testConcurrencyLoadFiftyThreads() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger();
        AtomicReference<Throwable> failure = new AtomicReference<>();

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    for (int j = 0; j < 100; j++) {
                        ConfigScreenFactory<?> f1 = UniversalConfigScreen.createFactory(MockStaticConfig.class);
                        assertNotNull(f1);
                        f1.create(null);

                        ConfigScreenFactory<?> f2 = UniversalConfigScreen.createFactory("mod_" + threadId, new MockInstanceConfig());
                        assertNotNull(f2);
                        f2.create(null);

                        List<Field> fields = UniversalConfigScreen.discoverConfigFields(MockStaticConfig.class);
                        assertEquals(6, fields.size());

                        String inferred = UniversalConfigScreen.inferModId(MockStaticConfig.class);
                        assertEquals("testmod", inferred);
                    }
                    successCount.incrementAndGet();
                } catch (Throwable t) {
                    failure.compareAndSet(null, t);
                }
            });
        }

        assertTrue(readyLatch.await(10, TimeUnit.SECONDS), "All threads must be ready within 10s");
        startLatch.countDown();
        executor.shutdown();
        assertTrue(executor.awaitTermination(20, TimeUnit.SECONDS), "All threads must complete within 20s");

        if (failure.get() != null) {
            fail("Concurrency assertion failed: " + failure.get().getMessage(), failure.get());
        }
        assertEquals(threadCount, successCount.get(), "All 50 threads must complete successfully");
    }

    // ==========================================
    // PILLAR 3: ANNOTATION AST & FIELD INTROSPECTION
    // ==========================================

    @Test
    @DisplayName("Pillar 3 (Annotation AST): Field discovery across all supported types")
    void testFieldDiscoveryAcrossTypes() {
        List<Field> fields = UniversalConfigScreen.discoverConfigFields(MockStaticConfig.class);
        assertEquals(6, fields.size(), "MockStaticConfig must contain exactly 6 annotated fields");

        Field f0 = fields.get(0);
        Field f1 = fields.get(1);
        Field f2 = fields.get(2);
        Field f3 = fields.get(3);
        Field f4 = fields.get(4);
        Field f5 = fields.get(5);

        assertEquals("toggleFeature", f0.getName());
        assertEquals(boolean.class, f0.getType());

        assertEquals("entityLimit", f1.getName());
        assertEquals(int.class, f1.getType());

        assertEquals("renderScale", f2.getName());
        assertEquals(float.class, f2.getType());

        assertEquals("gravity", f3.getName());
        assertEquals(double.class, f3.getType());

        assertEquals("serverAddress", f4.getName());
        assertEquals(String.class, f4.getType());

        assertEquals("qualityMode", f5.getName());
        assertEquals(MockMode.class, f5.getType());
        assertTrue(f5.getType().isEnum());

        // Instance config discovery
        List<Field> instanceFields = UniversalConfigScreen.discoverConfigFields(MockInstanceConfig.class);
        assertEquals(3, instanceFields.size(), "MockInstanceConfig must contain exactly 3 annotated fields");
    }

    @Test
    @DisplayName("Pillar 3 (Annotation AST): Relative ordering priority and category grouping")
    void testOrderingAndCategories() {
        List<Field> fields = UniversalConfigScreen.discoverConfigFields(MockStaticConfig.class);

        ConfigOption opt0 = fields.get(0).getAnnotation(ConfigOption.class);
        ConfigOption opt1 = fields.get(1).getAnnotation(ConfigOption.class);
        ConfigOption opt2 = fields.get(2).getAnnotation(ConfigOption.class);
        ConfigOption opt3 = fields.get(3).getAnnotation(ConfigOption.class);
        ConfigOption opt4 = fields.get(4).getAnnotation(ConfigOption.class);
        ConfigOption opt5 = fields.get(5).getAnnotation(ConfigOption.class);

        assertEquals(1, opt0.order());
        assertEquals("general", opt0.category());
        assertEquals("", opt0.group());

        assertEquals(2, opt1.order());
        assertEquals("general", opt1.category());
        assertTrue(opt1.slider());
        assertEquals(0, opt1.min());
        assertEquals(100, opt1.max());

        assertEquals(10, opt2.order());
        assertEquals("performance", opt2.category());
        assertEquals("rendering", opt2.group());

        assertEquals(20, opt3.order());
        assertEquals("performance", opt3.category());
        assertEquals("physics", opt3.group());

        assertEquals(30, opt4.order());
        assertEquals("network", opt4.category());

        assertEquals(40, opt5.order());
        assertEquals("gameplay", opt5.category());
        assertTrue(opt5.syncGameRule());
        assertEquals("testmod:gameplay_mode", opt5.gameRuleName());
    }

    @Test
    @DisplayName("Pillar 3 (Annotation AST): Localization key resolution and mod ID inference")
    void testLocalizationKeyResolution() {
        List<Field> fields = UniversalConfigScreen.discoverConfigFields(MockStaticConfig.class);
        Field toggleField = fields.get(0);
        ConfigOption toggleOpt = toggleField.getAnnotation(ConfigOption.class);

        // Explicit nameKey and descriptionKey
        Component explicitName = UniversalConfigScreen.resolveOptionName(toggleOpt, toggleField, "testmod");
        ComponentContents nameContents = explicitName.getContents();
        assertInstanceOf(TranslatableContents.class, nameContents);
        assertEquals("key.custom.toggle", ((TranslatableContents) nameContents).getKey());

        Component explicitDesc = UniversalConfigScreen.resolveOptionDescription(toggleOpt, toggleField, "testmod");
        ComponentContents descContents = explicitDesc.getContents();
        assertInstanceOf(TranslatableContents.class, descContents);
        assertEquals("key.custom.toggle.desc", ((TranslatableContents) descContents).getKey());

        // Auto-inferred name and description
        Field limitField = fields.get(1);
        ConfigOption limitOpt = limitField.getAnnotation(ConfigOption.class);
        Component inferredName = UniversalConfigScreen.resolveOptionName(limitOpt, limitField, "testmod");
        assertEquals("config.testmod.entityLimit", ((TranslatableContents) inferredName.getContents()).getKey());

        Component inferredDesc = UniversalConfigScreen.resolveOptionDescription(limitOpt, limitField, "testmod");
        assertEquals("config.testmod.entityLimit.description", ((TranslatableContents) inferredDesc.getContents()).getKey());

        // Category resolution
        Component cat1 = UniversalConfigScreen.resolveCategoryName("general", "testmod");
        assertEquals("config.testmod.category.general", ((TranslatableContents) cat1.getContents()).getKey());

        Component cat2 = UniversalConfigScreen.resolveCategoryName("custom.cat.override", "testmod");
        assertEquals("custom.cat.override", ((TranslatableContents) cat2.getContents()).getKey());

        Component catEmpty = UniversalConfigScreen.resolveCategoryName("", "testmod");
        assertEquals("config.testmod.category.general", ((TranslatableContents) catEmpty.getContents()).getKey());

        // Group resolution
        Component grp1 = UniversalConfigScreen.resolveGroupName("physics", "testmod");
        assertEquals("config.testmod.group.physics", ((TranslatableContents) grp1.getContents()).getKey());

        // Mod ID inference
        assertEquals("testmod", UniversalConfigScreen.inferModId(MockStaticConfig.class));
        assertEquals("mockinstance", UniversalConfigScreen.inferModId(MockInstanceConfig.class));
    }

    @Test
    @DisplayName("Pillar 3 (Annotation AST): Save method auto-invocation on static and instance targets")
    void testSaveMethodInvocation() {
        MockStaticConfig.saved = false;
        UniversalConfigScreen.invokeSaveMethod(MockStaticConfig.class, null);
        assertTrue(MockStaticConfig.saved, "invokeSaveMethod must invoke static save() method");

        MockInstanceConfig instance = new MockInstanceConfig();
        assertFalse(instance.saved);
        UniversalConfigScreen.invokeSaveMethod(MockInstanceConfig.class, instance);
        assertTrue(instance.saved, "invokeSaveMethod must invoke instance save() method");

        // Safe handling when save method does not exist
        assertDoesNotThrow(() -> UniversalConfigScreen.invokeSaveMethod(Object.class, new Object()));
    }

    // ==========================================
    // PILLAR 4: DYNAMIC GAMERULE SYNC INTEGRATION
    // ==========================================

    @Test
    @DisplayName("Pillar 4 (DynamicGameRule Sync): Safe graceful execution without NPE when server or rule is null")
    void testDynamicGameRuleSyncGraceful() {
        assertDoesNotThrow(() -> DynamicGameRuleConfigSync.syncToGameRule(null, null));
        assertDoesNotThrow(() -> DynamicGameRuleConfigSync.syncToGameRule("", 123));
        assertDoesNotThrow(() -> DynamicGameRuleConfigSync.syncToGameRule("unregistered_rule_xyz", true));
        assertDoesNotThrow(() -> DynamicGameRuleConfigSync.syncToGameRule("testmod:gameplay_mode", MockMode.BALANCED));
        assertDoesNotThrow(() -> DynamicGameRuleConfigSync.syncToGameRule("ig:ore_coal_ore", 42));

        // findGameRule null safety
        assertNull(DynamicGameRuleConfigSync.findGameRule(null, null));
        assertNull(DynamicGameRuleConfigSync.findGameRule(null, ""));
        assertNull(DynamicGameRuleConfigSync.findGameRule(null, "unregistered_rule_xyz"));
    }

    // ==========================================
    // PILLAR 5: SIDEDNESS & HYGIENE CONTRACT
    // ==========================================

    @Test
    @DisplayName("Pillar 5 (Sidedness): ConfigOption has ZERO client imports and no @Environment(CLIENT)")
    void testConfigOptionSideSafety() throws IOException {
        Path configOptionPath = Path.of("src/main/java/net/dasik/social/api/config/ConfigOption.java");
        assertTrue(Files.exists(configOptionPath), "ConfigOption.java must exist");

        List<String> lines = Files.readAllLines(configOptionPath);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.startsWith("import net.minecraft.client.")) {
                fail("Sidedness Violation: Found client import in ConfigOption.java at line " + (i + 1) + ": " + line);
            }
        }

        // Verify ConfigOption does not have @Environment(CLIENT)
        assertFalse(ConfigOption.class.isAnnotationPresent(Environment.class),
                "ConfigOption must NOT be marked with @Environment(CLIENT) so common server configs can use it safely");

        // Verify all methods/attributes of ConfigOption return side-safe types
        for (Method m : ConfigOption.class.getDeclaredMethods()) {
            assertFalse(m.getReturnType().getName().startsWith("net.minecraft.client."),
                    "ConfigOption method return type must not be client-only: " + m.getName());
        }
    }

    @Test
    @DisplayName("Pillar 5 (Sidedness): All classes in net.dasik.social.api.client.gui.* carry @Environment(EnvType.CLIENT)")
    void testClientGuiClassesEnvironmentAnnotation() throws IOException {
        Path universalConfigScreen = Path.of("src/main/java/net/dasik/social/api/client/gui/UniversalConfigScreen.java");
        assertTrue(Files.readString(universalConfigScreen).contains("@Environment(EnvType.CLIENT)"),
                "UniversalConfigScreen must carry @Environment(EnvType.CLIENT)");

        Path yaclSafetyBridge = Path.of("src/main/java/net/dasik/social/api/client/gui/YaclSafetyBridge.java");
        assertTrue(Files.readString(yaclSafetyBridge).contains("@Environment(EnvType.CLIENT)"),
                "YaclSafetyBridge must carry @Environment(EnvType.CLIENT)");

        Path dynamicGameRuleSync = Path.of("src/main/java/net/dasik/social/api/client/gui/DynamicGameRuleConfigSync.java");
        assertTrue(Files.readString(dynamicGameRuleSync).contains("@Environment(EnvType.CLIENT)"),
                "DynamicGameRuleConfigSync must carry @Environment(EnvType.CLIENT)");
    }

    @Test
    @DisplayName("Pillar 5 (Hygiene): Single-line GPLv3 license header and @DasikApiStatus.Public on 100% of GUI classes")
    void testGuiPackageHygieneAndPublicStatus() throws IOException {
        Path guiDir = Path.of("src/main/java/net/dasik/social/api/client/gui");
        assertTrue(Files.exists(guiDir), "GUI package directory must exist: " + guiDir);

        try (Stream<Path> files = Files.walk(guiDir)) {
            files.filter(p -> p.toString().endsWith(".java")).forEach(path -> {
                try {
                    List<String> fileLines = Files.readAllLines(path);
                    assertFalse(fileLines.isEmpty(), "File must not be empty: " + path);

                    String firstLine = fileLines.get(0).trim();
                    assertEquals("// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3", firstLine,
                            "Missing or invalid copyright header in " + path.getFileName());

                    String content = Files.readString(path);
                    assertTrue(content.contains("@DasikApiStatus.Public"),
                            "Missing @DasikApiStatus.Public in " + path.getFileName());
                    assertTrue(content.contains("@Environment(EnvType.CLIENT)"),
                            "Missing @Environment(EnvType.CLIENT) in " + path.getFileName());
                } catch (IOException e) {
                    fail("Failed to read file: " + path);
                }
            });
        }
    }
}
