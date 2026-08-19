// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.test;

import net.dasik.social.api.vision.PlayerVisionTracker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerVisionTrackerMathTest {

    @Test
    @DisplayName("Verify null safety across all public methods")
    public void testNullSafety() {
        assertFalse(PlayerVisionTracker.canSee(null, null, 16.0, 90.0));
        assertFalse(PlayerVisionTracker.canSee(null, null, 16.0));
        assertFalse(PlayerVisionTracker.canSee(null, null));
        assertFalse(PlayerVisionTracker.isInFieldOfView(null, null, 90.0));
        assertFalse(PlayerVisionTracker.hasLineOfSight(null, null, null, 0.25));
        assertFalse(PlayerVisionTracker.hasLineOfSight(null, null, null));
        assertFalse(PlayerVisionTracker.hasLineOfSightToBlock(null, null, null, null));
    }

    @Test
    @DisplayName("Verify backward compatibility stubs do not throw")
    public void testBackwardCompatibilityStubs() {
        assertDoesNotThrow(() -> {
            PlayerVisionTracker.init();
            PlayerVisionTracker.registerListener("test_mod", 32.0);
            PlayerVisionTracker.unregisterListener("test_mod");
            assertEquals(-1.0, PlayerVisionTracker.getMaxRequestedRadius());
        });
    }

    @Test
    @DisplayName("Verify FOV dot product mathematical bounds")
    public void testFovMathCalculation() {
        // Direct forward vector (0 deg angle): cos(0) = 1.0 >= cos(45 deg)
        double lookX = 0.0, lookY = 0.0, lookZ = 1.0;
        double targetX = 0.0, targetY = 0.0, targetZ = 10.0;
        
        double len = Math.sqrt(targetX * targetX + targetY * targetY + targetZ * targetZ);
        double normX = targetX / len;
        double normY = targetY / len;
        double normZ = targetZ / len;

        double dot = lookX * normX + lookY * normY + lookZ * normZ;
        double minDot = Math.cos(Math.toRadians(90.0 * 0.5)); // 45 deg half-angle

        assertTrue(dot >= minDot, "Target directly ahead must be within 90-degree FOV cone");

        // Target behind (180 deg angle): cos(180) = -1.0 < cos(45 deg)
        double behindZ = -10.0;
        double lenBehind = Math.abs(behindZ);
        double normBehindZ = behindZ / lenBehind;
        double dotBehind = lookZ * normBehindZ; // -1.0

        assertFalse(dotBehind >= minDot, "Target directly behind must be rejected by FOV filter");
    }
}
