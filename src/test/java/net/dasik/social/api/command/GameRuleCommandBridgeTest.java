// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.command;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameRuleCommandBridgeTest {

    @Test
    @DisplayName("Verify boolean key registration, queries, mutations, and parsing")
    void testBooleanRegistrationAndMutations() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerBoolean("enabled", null, true);

        assertEquals(true, bridge.getValue(null, "enabled"));

        // Mutate to false
        GameRuleCommandBridge.SetResult res1 = bridge.setValue(null, "enabled", "false");
        assertEquals(GameRuleCommandBridge.Status.SUCCESS, res1.getStatus());
        assertEquals(false, res1.getValue());
        assertEquals(false, bridge.getValue(null, "enabled"));

        // Mutate with alternate true representations
        GameRuleCommandBridge.SetResult res2 = bridge.setValue(null, "enabled", "1");
        assertEquals(GameRuleCommandBridge.Status.SUCCESS, res2.getStatus());
        assertEquals(true, res2.getValue());

        GameRuleCommandBridge.SetResult res3 = bridge.setValue(null, "enabled", "yes");
        assertEquals(GameRuleCommandBridge.Status.SUCCESS, res3.getStatus());
        assertEquals(true, res3.getValue());

        // Error parse
        GameRuleCommandBridge.SetResult resError = bridge.setValue(null, "enabled", "maybe");
        assertEquals(GameRuleCommandBridge.Status.ERROR_PARSE, resError.getStatus());
        assertTrue(resError.isError());
    }

    @Test
    @DisplayName("Verify string key registration, queries, and mutations")
    void testStringRegistrationAndMutations() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerString("mode", null, "default");

        assertEquals("default", bridge.getValue(null, "mode"));

        GameRuleCommandBridge.SetResult res = bridge.setValue(null, "mode", "aggressive");
        assertEquals(GameRuleCommandBridge.Status.SUCCESS, res.getStatus());
        assertEquals("aggressive", res.getValue());
        assertEquals("aggressive", bridge.getValue(null, "mode"));
    }

    @Test
    @DisplayName("Pillar 1 Chaos Fuzzing: 20k iterations of saturated integer and double fuzzing")
    void testChaosFuzzingSaturatedBounds() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("count", null, 10, 0, 100);
        bridge.registerDouble("multiplier", null, 1.5, 0.5, 5.0);

        Random random = new Random(42);

        // 10k integer iterations
        for (int i = 0; i < 10_000; i++) {
            long testVal = random.nextLong();
            String raw = String.valueOf(testVal);
            GameRuleCommandBridge.SetResult res = bridge.setValue(null, "count", raw);

            assertNotNull(res);
            assertFalse(res.isError(), "Integer input must not produce error parse for valid digits");

            int current = (int) bridge.getValue(null, "count");
            assertTrue(current >= 0 && current <= 100, "Current value must be within bounds: " + current);

            if (testVal > 100) {
                assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, res.getStatus());
                assertEquals(100, current);
            } else if (testVal < 0) {
                assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, res.getStatus());
                assertEquals(0, current);
            } else {
                assertEquals(GameRuleCommandBridge.Status.SUCCESS, res.getStatus());
                assertEquals((int) testVal, current);
            }
        }

        // 10k double iterations
        for (int i = 0; i < 10_000; i++) {
            double testVal = (random.nextDouble() - 0.5) * 100.0;
            String raw = String.valueOf(testVal);
            GameRuleCommandBridge.SetResult res = bridge.setValue(null, "multiplier", raw);

            assertNotNull(res);
            assertFalse(res.isError(), "Double input must not produce error parse for valid float");

            double current = (double) bridge.getValue(null, "multiplier");
            assertTrue(current >= 0.5 && current <= 5.0, "Current double value must be within bounds: " + current);

            if (testVal > 5.0) {
                assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, res.getStatus());
                assertEquals(5.0, current, 1e-9);
            } else if (testVal < 0.5) {
                assertEquals(GameRuleCommandBridge.Status.SATURATED_WARNING, res.getStatus());
                assertEquals(0.5, current, 1e-9);
            } else {
                assertEquals(GameRuleCommandBridge.Status.SUCCESS, res.getStatus());
                assertEquals(testVal, current, 1e-9);
            }
        }
    }

    @Test
    @DisplayName("Verify resetToDefaults restores all keys and triggers save")
    void testResetToDefaults() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("radius", null, 16, 1, 64);
        bridge.registerBoolean("active", null, true);

        bridge.setValue(null, "radius", "50");
        bridge.setValue(null, "active", "false");

        assertEquals(50, bridge.getValue(null, "radius"));
        assertEquals(false, bridge.getValue(null, "active"));

        AtomicBoolean saveCalled = new AtomicBoolean(false);
        bridge.setSaveCallback(server -> saveCalled.set(true));

        bridge.resetToDefaults(null);

        assertEquals(16, bridge.getValue(null, "radius"));
        assertEquals(true, bridge.getValue(null, "active"));
        assertTrue(saveCalled.get(), "Reset must invoke save callback");
    }

    @Test
    @DisplayName("Verify unknown keys return ERROR_NOT_FOUND")
    void testUnknownKey() {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        GameRuleCommandBridge.SetResult res = bridge.setValue(null, "nonexistent", "123");
        assertEquals(GameRuleCommandBridge.Status.ERROR_NOT_FOUND, res.getStatus());
        assertTrue(res.isError());
        assertNull(bridge.getValue(null, "nonexistent"));
    }
}
