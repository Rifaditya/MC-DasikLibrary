// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.command;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CommandConcurrencyTest {

    @Test
    @DisplayName("Pillar 2 Concurrency Load: 50 concurrent threads mutating and reading GameRuleCommandBridge")
    void testConcurrentMutations() throws InterruptedException {
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("threads", null, 1, 1, 1000);
        bridge.registerBoolean("toggle", null, false);
        bridge.registerString("status", null, "idle");

        int threadCount = 50;
        int operationsPerThread = 500;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    for (int op = 0; op < operationsPerThread; op++) {
                        // Integer mutation
                        int targetVal = (threadId * 10 + op) % 1000 + 1;
                        GameRuleCommandBridge.SetResult intRes = bridge.setValue(null, "threads", String.valueOf(targetVal));
                        if (intRes == null || intRes.isError()) {
                            errorCount.incrementAndGet();
                        }

                        // Boolean mutation
                        boolean bVal = (op % 2 == 0);
                        GameRuleCommandBridge.SetResult boolRes = bridge.setValue(null, "toggle", String.valueOf(bVal));
                        if (boolRes == null || boolRes.isError()) {
                            errorCount.incrementAndGet();
                        }

                        // String mutation
                        GameRuleCommandBridge.SetResult strRes = bridge.setValue(null, "status", "thread-" + threadId + "-op-" + op);
                        if (strRes == null || strRes.isError()) {
                            errorCount.incrementAndGet();
                        }

                        // Read back
                        Object val = bridge.getValue(null, "threads");
                        if (val == null || !(val instanceof Integer)) {
                            errorCount.incrementAndGet();
                        }
                    }
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(10, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(completed, "All 50 threads must complete within timeout");
        assertEquals(0, errorCount.get(), "Zero exceptions or errors allowed during high concurrency load");
    }

    @Test
    @DisplayName("Pillar 2 Concurrency Load: 50 concurrent threads registering command suites and callbacks")
    void testConcurrentDispatcherRegistrations() throws InterruptedException {
        ModCommandDispatcher.clearForTesting();

        int threadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int t = 0; t < threadCount; t++) {
            final int id = t;
            executor.submit(() -> {
                try {
                    ModCommandDispatcher.registerSuite("mod-" + id, builder -> {
                        builder.withModName("Mod " + id);
                    });
                    ModCommandDispatcher.register("callback-" + id, dispatcher -> {
                        // no-op callback
                    });
                } catch (Exception e) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(completed, "Dispatcher registration threads must complete within timeout");
        assertEquals(0, errorCount.get(), "Concurrent registrations must not throw or deadlock");
    }
}
