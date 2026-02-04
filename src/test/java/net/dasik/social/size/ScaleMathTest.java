package net.dasik.social.size;

import java.util.UUID;

public class ScaleMathTest {

    public static void main(String[] args) {
        testDeterministicScale();
    }

    private static void testDeterministicScale() {
        System.out.println("Running ScaleMathTest...");

        float min = 0.5f;
        float max = 2.0f;

        UUID uuid1 = UUID.fromString("00000000-0000-0000-0000-000000000001");
        float scale1 = calculateScale(uuid1, min, max);
        System.out.println("UUID1 Scale: " + scale1);

        // Verify repeatability
        float scale1Again = calculateScale(uuid1, min, max);
        if (scale1 != scale1Again) {
            throw new RuntimeException("Test Failed: Deterministic calculation is not consistent within same runtime.");
        }

        UUID uuid2 = UUID.fromString("00000000-0000-0000-0000-000000000002");
        float scale2 = calculateScale(uuid2, min, max);
        System.out.println("UUID2 Scale: " + scale2);

        if (scale1 == scale2) {
            System.out.println(
                    "Warning: UUID1 and UUID2 produced same scale. This is possible but unlikely for large ranges. Just noting.");
        }

        // Simulating the logic from SizeRegistry
        System.out.println("Test Passed: deterministic logic holds.");
    }

    private static float calculateScale(UUID uuid, float min, float max) {
        long hash = uuid.getLeastSignificantBits() ^ uuid.getMostSignificantBits();
        float randomFactor = (Math.abs(hash) % 10000) / 10000f;
        return min + (randomFactor * (max - min));
    }
}
