// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.command;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandFeedbackFormatterTest {

    @Test
    @DisplayName("Verify gold prefix formatting contains mod name")
    void testPrefix() {
        MutableComponent prefix = CommandFeedbackFormatter.prefix("TestMod");
        assertNotNull(prefix);
        assertTrue(prefix.getString().contains("TestMod"));
        assertTrue(prefix.getString().startsWith("§6[TestMod]§r"));
    }

    @Test
    @DisplayName("Verify success formatting contains green styling and message")
    void testSuccess() {
        Component resStr = CommandFeedbackFormatter.success("TestMod", "Configuration saved");
        assertNotNull(resStr);
        assertTrue(resStr.getString().contains("§aConfiguration saved"));
        assertTrue(resStr.getString().contains("TestMod"));

        Component resComp = CommandFeedbackFormatter.success("TestMod", Component.literal("Saved"));
        assertNotNull(resComp);
        assertTrue(resComp.getString().contains("Saved"));
    }

    @Test
    @DisplayName("Verify warning formatting contains yellow styling and message")
    void testWarning() {
        Component res = CommandFeedbackFormatter.warning("TestMod", "Saturated bound reached");
        assertNotNull(res);
        assertTrue(res.getString().contains("§eSaturated bound reached"));
    }

    @Test
    @DisplayName("Verify error formatting contains red styling and message")
    void testError() {
        Component res = CommandFeedbackFormatter.error("TestMod", "Key not found");
        assertNotNull(res);
        assertTrue(res.getString().contains("§cKey not found"));
    }

    @Test
    @DisplayName("Verify info formatting contains aqua styling and message")
    void testInfo() {
        Component res = CommandFeedbackFormatter.info("TestMod", "Rule value is 42");
        assertNotNull(res);
        assertTrue(res.getString().contains("§bRule value is 42"));
    }

    @Test
    @DisplayName("Verify support footers append Discord and Ko-fi elements")
    void testSupportFooters() {
        MutableComponent base = Component.literal("Help Text");
        MutableComponent withFooters = CommandFeedbackFormatter.appendSupportFooters(base);
        assertNotNull(withFooters);
        // Base string should contain newlines and child components
        assertTrue(withFooters.getSiblings().size() >= 2);
    }
}
