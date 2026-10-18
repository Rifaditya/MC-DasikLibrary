// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.signal;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import net.dasik.social.api.Scope;
import net.dasik.social.api.SignalType;
import net.dasik.social.api.SocialEntity;
import net.dasik.social.api.SocialScheduler;
import net.dasik.social.signal.Signal;
import net.dasik.social.signal.SignalBus;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import static org.junit.jupiter.api.Assertions.*;

public class SignalBusTest {

    private static Unsafe unsafe;

    @BeforeAll
    public static void setUpAll() {
        try {
            net.minecraft.SharedConstants.tryDetectVersion();
            net.minecraft.server.Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to acquire Unsafe", e);
        }
    }

    @AfterEach
    public void tearDown() {
        SignalBus.clear();
    }

    private static void setField(Class<?> clazz, Object target, String fieldName, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set field " + fieldName + " on " + clazz.getSimpleName(), e);
        }
    }

    private static LivingEntity createDummyLivingEntity(Level level, Vec3 pos) {
        try {
            LivingEntity entity = (LivingEntity) unsafe.allocateInstance(net.minecraft.world.entity.decoration.ArmorStand.class);
            setField(Entity.class, entity, "position", pos != null ? pos : Vec3.ZERO);
            setField(Entity.class, entity, "level", level);
            return entity;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate dummy LivingEntity", e);
        }
    }

    private static ServerLevel createDummyLevel() {
        try {
            return (ServerLevel) unsafe.allocateInstance(ServerLevel.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate dummy ServerLevel", e);
        }
    }

    private static class DummySocialEntity implements SocialEntity {
        private final LivingEntity entity;
        private final String speciesId;
        private final SocialScheduler scheduler;

        public DummySocialEntity(LivingEntity entity, String speciesId, SocialScheduler scheduler) {
            this.entity = entity;
            this.speciesId = speciesId;
            this.scheduler = scheduler;
        }

        @Override
        public long dasik$getDNA() {
            return 12345L;
        }

        @Override
        public String dasik$getSpeciesId() {
            return this.speciesId;
        }

        @Override
        public LivingEntity dasik$asEntity() {
            return this.entity;
        }

        @Override
        public float dasik$getSocialScale() {
            return 1.0f;
        }

        @Override
        public SocialScheduler dasik$getScheduler() {
            return this.scheduler;
        }
    }

    private static class RecordingScheduler implements SocialScheduler {
        private final AtomicInteger receivedCount = new AtomicInteger(0);

        @Override
        public void tick() {}

        @Override
        public void onSignalReceived(Signal signal) {
            receivedCount.incrementAndGet();
        }

        @Override
        public boolean isEventActive(String eventId) {
            return false;
        }

        public int getReceivedCount() {
            return receivedCount.get();
        }
    }

    @Test
    @DisplayName("Pillar 1: Thunder signal with Double.MAX_VALUE dispatches in under 10ms without watchdog hang")
    public void testThunderSignalDispatchesInstantly() {
        RecordingScheduler scheduler = new RecordingScheduler();
        ServerLevel level = createDummyLevel();
        LivingEntity dummyTarget = createDummyLivingEntity(level, new Vec3(10.0, 64.0, 10.0));
        DummySocialEntity socialEntity = new DummySocialEntity(dummyTarget, "minecraft:wolf", scheduler);

        SignalBus.updatePosition(socialEntity, null, new ChunkPos(0, 0));

        long start = System.currentTimeMillis();
        for (int i = 0; i < 50; i++) {
            SignalBus.broadcast(SignalType.THUNDER, Vec3.ZERO, null, Scope.PUBLIC);
        }
        long duration = System.currentTimeMillis() - start;

        assertTrue(duration < 200, "50 Thunder dispatches should complete well under 200ms, took " + duration + "ms");
        assertTrue(scheduler.getReceivedCount() >= 50, "Target entity must have received the thunder signals");
    }

    @Test
    @DisplayName("Pillar 1: Chaos Fuzzing with Double.MAX_VALUE, Infinity, and NaN terminates safely")
    public void testChaosFuzzingSignals() {
        ServerLevel level = createDummyLevel();
        LivingEntity dummyTarget = createDummyLivingEntity(level, new Vec3(0, 0, 0));
        DummySocialEntity socialEntity = new DummySocialEntity(dummyTarget, "minecraft:wolf", new RecordingScheduler());
        SignalBus.updatePosition(socialEntity, null, new ChunkPos(0, 0));

        SignalType infiniteType = new SignalType("fuzz_infinite", 32.0, Double.POSITIVE_INFINITY, 50);
        SignalType nanType = new SignalType("fuzz_nan", 32.0, Double.NaN, 50);
        SignalType maxType = new SignalType("fuzz_max", 32.0, Double.MAX_VALUE, 50);

        assertDoesNotThrow(() -> {
            for (int i = 0; i < 1000; i++) {
                SignalBus.broadcast(infiniteType, new Vec3(Double.NaN, 0, 0), null, Scope.PUBLIC);
                SignalBus.broadcast(nanType, new Vec3(0, Double.POSITIVE_INFINITY, 0), null, Scope.PUBLIC);
                SignalBus.broadcast(maxType, Vec3.ZERO, null, Scope.PUBLIC);
            }
        });
    }

    @Test
    @DisplayName("Pillar 3: Non-SocialEntity source emits SAME_SPECIES without ClassCastException")
    public void testNonSocialEntitySourceDoesNotThrowClassCast() {
        ServerLevel level = createDummyLevel();
        LivingEntity nonSocialSource = createDummyLivingEntity(level, new Vec3(0, 64, 0));
        LivingEntity dummyTarget = createDummyLivingEntity(level, new Vec3(1, 64, 1));
        RecordingScheduler scheduler = new RecordingScheduler();
        DummySocialEntity targetSocialEntity = new DummySocialEntity(dummyTarget, "minecraft:wolf", scheduler);

        SignalBus.updatePosition(targetSocialEntity, null, new ChunkPos(0, 0));

        // When a non-SocialEntity emits SAME_SPECIES, it should be safely rejected without ClassCastException
        assertDoesNotThrow(() -> {
            SignalBus.broadcast(SignalType.DANGER, new Vec3(0, 64, 0), nonSocialSource, Scope.SAME_SPECIES);
        });

        assertEquals(0, scheduler.getReceivedCount(), "Target must not receive SAME_SPECIES signal from non-SocialEntity source");
    }

    @Test
    @DisplayName("Pillar 4: Dimension Isolation prevents cross-dimension signal bleed")
    public void testDimensionIsolation() {
        ServerLevel overworld = createDummyLevel();
        ServerLevel nether = createDummyLevel();

        LivingEntity overworldTarget = createDummyLivingEntity(overworld, new Vec3(5, 64, 5));
        RecordingScheduler overworldScheduler = new RecordingScheduler();
        DummySocialEntity overworldEntity = new DummySocialEntity(overworldTarget, "minecraft:wolf", overworldScheduler);

        LivingEntity netherTarget = createDummyLivingEntity(nether, new Vec3(5, 64, 5));
        RecordingScheduler netherScheduler = new RecordingScheduler();
        DummySocialEntity netherEntity = new DummySocialEntity(netherTarget, "minecraft:wolf", netherScheduler);

        SignalBus.updatePosition(overworldEntity, null, new ChunkPos(0, 0));
        SignalBus.updatePosition(netherEntity, null, new ChunkPos(0, 0));

        // Source is in Overworld
        LivingEntity overworldSource = createDummyLivingEntity(overworld, new Vec3(0, 64, 0));
        SignalBus.broadcast(SignalType.DANGER, new Vec3(0, 64, 0), overworldSource, Scope.PUBLIC);

        assertEquals(1, overworldScheduler.getReceivedCount(), "Overworld entity must receive signal from Overworld source");
        assertEquals(0, netherScheduler.getReceivedCount(), "Nether entity must NOT receive signal from Overworld source");
    }

    @Test
    @DisplayName("SignalBus.clear() empties the index cleanly")
    public void testClearEmptiesIndex() {
        ServerLevel level = createDummyLevel();
        LivingEntity dummyTarget = createDummyLivingEntity(level, new Vec3(2, 64, 2));
        RecordingScheduler scheduler = new RecordingScheduler();
        DummySocialEntity socialEntity = new DummySocialEntity(dummyTarget, "minecraft:wolf", scheduler);

        SignalBus.updatePosition(socialEntity, null, new ChunkPos(0, 0));
        SignalBus.clear();

        SignalBus.broadcast(SignalType.DANGER, new Vec3(0, 64, 0), null, Scope.PUBLIC);
        assertEquals(0, scheduler.getReceivedCount(), "No signals should be received after SignalBus.clear()");
    }

    @Test
    @DisplayName("Pillar 2: Concurrent multi-threaded dispatch and position updates execute without race conditions")
    public void testConcurrentDispatchAndUpdates() throws InterruptedException {
        ServerLevel level = createDummyLevel();
        int threadCount = 4;
        Thread[] threads = new Thread[threadCount];

        for (int t = 0; t < threadCount; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                for (int i = 0; i < 200; i++) {
                    LivingEntity dummy = createDummyLivingEntity(level, new Vec3(threadId * 10, 64, i));
                    DummySocialEntity entity = new DummySocialEntity(dummy, "minecraft:wolf", new RecordingScheduler());
                    ChunkPos pos = new ChunkPos(threadId, i % 10);
                    SignalBus.updatePosition(entity, null, pos);
                    SignalBus.broadcast(SignalType.THUNDER, Vec3.ZERO, null, Scope.PUBLIC);
                    SignalBus.updatePosition(entity, pos, null);
                }
            });
            threads[t].start();
        }

        for (Thread thread : threads) {
            thread.join(5000);
            assertFalse(thread.isAlive(), "Thread must complete within 5 seconds");
        }
    }
}
