// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("VirtualPlayerBot Reality & Concurrency Test Suite")
class VirtualPlayerBotTest {

    @Test
    @DisplayName("Pillar 5 Sidedness: VirtualPlayerBot bytecode has zero client-side imports")
    void testVirtualPlayerBotSidedness() throws Exception {
        String classResource = "net/dasik/social/api/test/VirtualPlayerBot.class";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(classResource)) {
            assertNotNull(in, "Could not load VirtualPlayerBot.class from classpath");
            byte[] bytes = in.readAllBytes();
            Set<String> constants = DasikContractVerifier.extractUtf8Constants(bytes);

            for (String constant : constants) {
                assertFalse(constant.contains("net/minecraft/client/") || constant.contains("net.minecraft.client."),
                        "VirtualPlayerBot contains client-side reference: " + constant);
            }
        }
    }

    @Test
    @DisplayName("Pillar 5 Sidedness: DasikContractVerifier bytecode has zero client-side imports")
    void testContractVerifierSidedness() throws Exception {
        String classResource = "net/dasik/social/api/test/DasikContractVerifier.class";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(classResource)) {
            assertNotNull(in, "Could not load DasikContractVerifier.class from classpath");
            byte[] bytes = in.readAllBytes();
            Set<String> constants = DasikContractVerifier.extractUtf8Constants(bytes);

            for (String constant : constants) {
                assertFalse(constant.contains("net/minecraft/client/") || constant.contains("net.minecraft.client."),
                        "DasikContractVerifier contains client-side reference: " + constant);
            }
        }
    }

    @Test
    @DisplayName("Pillar 5 Mixin Audit: auditAllMixins runs safely headlessly")
    void testAuditAllMixins() {
        assertDoesNotThrow(VirtualPlayerBot::auditAllMixins);
    }

    @Test
    @DisplayName("Pillar 2 Concurrency Load: 50 concurrent worker threads stress contract verifier")
    void testConcurrentContractVerificationLoad() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successCounter = new AtomicInteger(0);

        byte[] sampleBytes = new byte[]{
                (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE,
                0, 0, 0, 65,
                0, 2,
                1, 0, 4, 't', 'e', 's', 't'
        };

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    for (int j = 0; j < 100; j++) {
                        Set<String> constants = DasikContractVerifier.extractUtf8Constants(sampleBytes);
                        if (constants.contains("test")) {
                            successCounter.incrementAndGet();
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean finished = latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(finished, "Concurrency load test timed out");
        assertEquals(threadCount * 100, successCounter.get(), "Thread safety counter mismatch under concurrency");
    }
}
