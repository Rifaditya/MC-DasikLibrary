// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.genetics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GeneticsScaleTest {

    @Test
    @DisplayName("Verify scale factor delta modifier math")
    public void testScaleModifierCalculation() {
        // Base Minecraft scale attribute is 1.0.
        // A scale factor of 1.0 represents vanilla normal size (modifier must be 0.0).
        float normalScale = 1.0f;
        float normalModifier = normalScale - 1.0f;
        Assertions.assertEquals(0.0f, normalModifier, 0.0001f, "1.0x scale should produce 0.0 modifier delta");

        // A scale factor of 1.30 represents 130% size (+30% modifier).
        float largeScale = 1.30f;
        float largeModifier = largeScale - 1.0f;
        Assertions.assertEquals(0.30f, largeModifier, 0.0001f, "1.30x scale should produce +0.30 modifier delta");

        // A scale factor of 0.50 represents 50% size (-50% modifier).
        float smallScale = 0.50f;
        float smallModifier = smallScale - 1.0f;
        Assertions.assertEquals(-0.50f, smallModifier, 0.0001f, "0.50x scale should produce -0.50 modifier delta");
    }

    @Test
    @DisplayName("Verify linked attribute calculations from scale factor")
    public void testLinkedAttributeModifierCalculation() {
        LinkedAttribute propLink = new LinkedAttribute("minecraft:generic.max_health", LinkMode.PROPORTIONAL, 1.0f);
        Assertions.assertEquals(0.30f, propLink.computeModifier(1.30f), 0.0001f);
        Assertions.assertEquals(-0.50f, propLink.computeModifier(0.50f), 0.0001f);
        Assertions.assertEquals(0.0f, propLink.computeModifier(1.00f), 0.0001f);

        LinkedAttribute invLink = new LinkedAttribute("minecraft:generic.movement_speed", LinkMode.INVERSE, 0.5f);
        Assertions.assertEquals(-0.15f, invLink.computeModifier(1.30f), 0.0001f);
        Assertions.assertEquals(0.25f, invLink.computeModifier(0.50f), 0.0001f);
    }
}
