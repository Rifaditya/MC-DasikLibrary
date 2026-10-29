// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import net.dasik.social.api.Scope;
import net.dasik.social.api.SignalType;
import net.dasik.social.api.SocialEntity;
import net.dasik.social.api.SocialScheduler;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.signal.Signal;
import net.dasik.social.signal.SignalBus;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DasikLibraryConcurrencyLoadTest {

    private ExecutorService executor;

    @BeforeAll
    public static void setUpAll() {
        try {
            net.minecraft.SharedConstants.tryDetectVersion();
            net.minecraft.server.Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
    }

    @AfterEach
    public void tearDown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdownNow();
            try {
                executor.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException ignored) {
            }
        }
        DynamicGameRuleManager.getGeneratedTranslations().clear();
        SignalBus.clear();
    }

    @Test
    @DisplayName("Pillar 2 Concurrency Load: 50 concurrent threads registering and unregistering dynamic GameRules")
    public void testConcurrentGameRuleOperations() throws InterruptedException {
        int threadCount = 50;
        int operationsPerThread = 200;
        executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    for (int op = 0; op < operationsPerThread; op++) {
                        String modId = "concmod" + threadId;
                        String ruleName = modId + ":rule_" + op;

                        // 1. Dynamic integer builder configuration and bounds
                        DynamicGameRuleManager.integerRule(ruleName, null, 10)
                                .min(0)
                                .max(100)
                                .range(0, 100)
                                .description("Concurrent description " + op)
                                .name("Concurrent Name " + op);

                        // 2. Dynamic translations map operations
                        DynamicGameRuleManager.getGeneratedTranslations().put("gamerule." + ruleName, "Concurrent Rule " + op);
                        DynamicGameRuleManager.getGeneratedTranslations().put("gamerule." + ruleName + ".description", "Description " + op);

                        // 3. Mod ID and readable name extraction
                        String extracted = DynamicGameRuleManager.extractModId(ruleName);
                        if (!modId.equals(extracted)) {
                            errorCount.incrementAndGet();
                        }

                        String readable = DynamicGameRuleManager.generateReadableName(ruleName);
                        if (readable == null || readable.isEmpty()) {
                            errorCount.incrementAndGet();
                        }

                        // 4. Dynamic unregistration
                        if (op % 10 == 0) {
                            DynamicGameRuleManager.unregisterModRules(modId);
                        } else {
                            DynamicGameRuleManager.unregister(ruleName);
                        }
                    }
                } catch (Throwable t1) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(10, TimeUnit.SECONDS);
        assertTrue(completed, "All 50 threads must complete within 10 seconds timeout");
        assertEquals(0, errorCount.get(), "Zero exceptions or errors allowed during high concurrency GameRule operations");
    }

    @Test
    @DisplayName("Pillar 2 Concurrency Load: 50 concurrent threads posting signals and registering listeners on SignalBus")
    public void testConcurrentSignalBusDispatch() throws InterruptedException {
        int threadCount = 50;
        int operationsPerThread = 100;
        executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger errorCount = new AtomicInteger(0);
        AtomicInteger receivedCount = new AtomicInteger(0);

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            executor.submit(() -> {
                try {
                    for (int op = 0; op < operationsPerThread; op++) {
                        ConcurrentSocialEntity entity = new ConcurrentSocialEntity("minecraft:wolf", threadId * 1000L + op, receivedCount);
                        ChunkPos initialPos = new ChunkPos(threadId, op % 16);
                        ChunkPos movedPos = new ChunkPos(threadId + 1, (op + 1) % 16);

                        // 1. Register listener in spatial index
                        SignalBus.updatePosition(entity, null, initialPos);

                        // 2. Concurrently broadcast signals
                        SignalBus.broadcast(SignalType.THUNDER, Vec3.ZERO, null, Scope.PUBLIC);
                        SignalBus.broadcast(SignalType.DANGER, new Vec3(threadId * 16.0, 64.0, (op % 16) * 16.0), null, Scope.PUBLIC);

                        // 3. Update listener position in spatial index
                        SignalBus.updatePosition(entity, initialPos, movedPos);

                        // 4. Concurrently broadcast again
                        SignalBus.broadcast(SignalType.FOOD_DETECTED, new Vec3((threadId + 1) * 16.0, 64.0, ((op + 1) % 16) * 16.0), null, Scope.PUBLIC);

                        // 5. Unregister listener from spatial index
                        SignalBus.updatePosition(entity, movedPos, null);
                    }
                } catch (Throwable t2) {
                    errorCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }

        boolean completed = latch.await(10, TimeUnit.SECONDS);
        assertTrue(completed, "All 50 threads must complete within 10 seconds timeout");
        assertEquals(0, errorCount.get(), "Zero exceptions or errors allowed during high concurrency SignalBus operations");
        assertTrue(receivedCount.get() > 0, "Signal listeners must have received dispatched signals");
    }

    private static class ConcurrentSocialEntity implements SocialEntity {
        private final String speciesId;
        private final long dna;
        private final AtomicInteger counter;

        public ConcurrentSocialEntity(String speciesId, long dna, AtomicInteger counter) {
            this.speciesId = speciesId;
            this.dna = dna;
            this.counter = counter;
        }

        @Override public long dasik$getDNA() { return dna; }
        @Override public String dasik$getSpeciesId() { return speciesId; }
        @Override public @Nullable LivingEntity dasik$asEntity() { return null; }
        @Override public float dasik$getSocialScale() { return 1.0f; }

        @Override
        public SocialScheduler dasik$getScheduler() {
            return new SocialScheduler() {
                @Override public void tick() {}
                @Override public void onSignalReceived(Signal signal) {
                    counter.incrementAndGet();
                }
                @Override public boolean isEventActive(String eventId) { return false; }
            };
        }

        @Override
        public double dasik$getSignalRange(SignalType type) {
            return type != null ? type.getMaxRange() : 64.0;
        }
    }
}
