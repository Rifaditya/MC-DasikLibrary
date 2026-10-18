// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.core;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import net.dasik.social.api.SignalType;
import net.dasik.social.api.SocialEntity;
import net.dasik.social.api.SocialEvent;
import net.dasik.social.api.SocialScheduler;
import net.dasik.social.api.TickContext;
import net.dasik.social.api.genetics.EntityGenetics;
import net.dasik.social.api.genetics.EntityGeneticsRegistry;
import net.dasik.social.api.genetics.GeneticsConfig;
import net.dasik.social.api.genetics.GeneticsEngine;
import net.dasik.social.api.genetics.TraitConfig;
import net.dasik.social.api.group.FlockType;
import net.dasik.social.api.group.GroupMember;
import net.dasik.social.core.group.FlockState;
import net.dasik.social.core.group.GroupManager;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

public class SocialRegistryAndHotPathTest {

    private static Unsafe unsafe;

    static {
        try {
            SharedConstants.tryDetectVersion();
            Bootstrap.bootStrap();
        } catch (Throwable ignored) {
        }
    }

    @BeforeAll
    public static void setUpAll() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to acquire Unsafe", e);
        }
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

    public static class TestServerLevel extends ServerLevel {
        private List<LivingEntity> testEntities;
        private long testGameTime;

        protected TestServerLevel() {
            super(null, null, null, null, null, null, false, 0L, null, false);
        }

        public void addTestEntity(LivingEntity e) {
            if (this.testEntities == null) {
                this.testEntities = new ArrayList<>();
            }
            this.testEntities.add(e);
        }

        @Override
        public <T extends Entity> List<T> getEntitiesOfClass(Class<T> baseClass, AABB bb, Predicate<? super T> selector) {
            List<T> result = new ArrayList<>();
            if (this.testEntities != null) {
                for (LivingEntity e : this.testEntities) {
                    if (baseClass.isInstance(e)) {
                        T cast = baseClass.cast(e);
                        if (selector == null || selector.test(cast)) {
                            result.add(cast);
                        }
                    }
                }
            }
            return result;
        }

        @Override
        public long getGameTime() {
            return this.testGameTime;
        }
    }

    public static class DummyGroupMemberMob extends Zombie implements GroupMember {
        private LivingEntity leader;
        private FlockState flockState;
        private LevelEntityGetterHelper helper;

        protected DummyGroupMemberMob() {
            super(null, null);
        }

        @Override
        public boolean isAlive() {
            return true;
        }

        @Override
        public LivingEntity getLeader() {
            return this.leader;
        }

        @Override
        public void setLeader(LivingEntity leader) {
            this.leader = leader;
        }

        @Override
        public boolean hasLeader() {
            return this.leader != null;
        }

        @Override
        public FlockType getFlockType() {
            return FlockType.AERIAL;
        }

        @Override
        public int getGroupSize() {
            return 1;
        }

        @Override
        public FlockState getFlockState() {
            return this.flockState;
        }

        @Override
        public void setFlockState(FlockState state) {
            this.flockState = state;
        }
    }

    public static class LevelEntityGetterHelper {
    }

    private static TestServerLevel createTestServerLevel() {
        try {
            TestServerLevel level = (TestServerLevel) unsafe.allocateInstance(TestServerLevel.class);
            setField(TestServerLevel.class, level, "testEntities", new ArrayList<LivingEntity>());
            setField(TestServerLevel.class, level, "testGameTime", 100L);
            setField(net.minecraft.world.level.Level.class, level, "random", net.minecraft.util.RandomSource.create());
            return level;
        } catch (Exception e) {
            throw new RuntimeException("Failed to allocate TestServerLevel", e);
        }
    }

    private static int entityIdCounter = 1000;

    private static DummyGroupMemberMob createDummyMob(UUID uuid, Vec3 position, Vec3 deltaMovement, TestServerLevel level) {
        try {
            DummyGroupMemberMob mob = (DummyGroupMemberMob) unsafe.allocateInstance(DummyGroupMemberMob.class);
            setField(Entity.class, mob, "id", ++entityIdCounter);
            setField(Entity.class, mob, "type", EntityTypes.ZOMBIE);
            setField(Entity.class, mob, "uuid", uuid);
            setField(Entity.class, mob, "position", position);
            setField(Entity.class, mob, "deltaMovement", deltaMovement);
            setField(Entity.class, mob, "bb", new AABB(position.x - 0.5, position.y, position.z - 0.5, position.x + 0.5, position.y + 1.8, position.z + 0.5));
            setField(Entity.class, mob, "level", level);
            return mob;
        } catch (Exception e) {
            throw new RuntimeException("Failed to allocate DummyGroupMemberMob", e);
        }
    }

    static class DummySocialEntity implements SocialEntity {
        private final LivingEntity entity;
        private final String speciesId;
        private final SocialScheduler scheduler;

        DummySocialEntity(LivingEntity entity, String speciesId, SocialScheduler scheduler) {
            this.entity = entity;
            this.speciesId = speciesId;
            this.scheduler = scheduler;
        }

        @Override public long dasik$getDNA() { return 42L; }
        @Override public String dasik$getSpeciesId() { return this.speciesId; }
        @Override public LivingEntity dasik$asEntity() { return this.entity; }
        @Override public float dasik$getSocialScale() { return 1.0f; }
        @Override public SocialScheduler dasik$getScheduler() { return this.scheduler; }
    }

    static class TestSocialEvent implements SocialEvent {
        private final String id;
        private final String trackId;
        private final boolean continueTicking;
        private boolean started = false;
        private boolean ended = false;
        private EndReason endReason = null;

        TestSocialEvent(String id, String trackId, boolean continueTicking) {
            this.id = id;
            this.trackId = trackId;
            this.continueTicking = continueTicking;
        }

        @Override public String getId() { return this.id; }
        @Override public int getPriorityValue() { return 10; }
        @Override public String getTrackId() { return this.trackId; }
        @Override public boolean canPreempt(SocialEvent other) { return true; }
        @Override public void onStart(TickContext ctx) { this.started = true; }
        @Override public boolean tick(TickContext ctx) { return this.continueTicking; }
        @Override public void onEnd(SocialEntity entity, EndReason reason) {
            this.ended = true;
            this.endReason = reason;
        }
    }

    @Test
    @DisplayName("Verify SocialRegistry.clear() and non-negative unregister() bounds")
    public void testSocialRegistryClearAndUnregister() {
        SocialRegistry.clear();
        Assertions.assertEquals(0, SocialRegistry.getCount());

        DummySocialEntity ghostEntity = new DummySocialEntity(null, "minecraft:phantom", null);
        SocialRegistry.unregister(ghostEntity);
        Assertions.assertEquals(0, SocialRegistry.getCount(), "Unregistering non-present entity must not make count negative");

        DummySocialEntity e1 = new DummySocialEntity(null, "minecraft:zombie", null);
        DummySocialEntity e2 = new DummySocialEntity(null, "minecraft:skeleton", null);

        SocialRegistry.register(e1);
        SocialRegistry.register(e2);
        Assertions.assertEquals(2, SocialRegistry.getCount());
        Assertions.assertTrue(SocialRegistry.contains(e1));
        Assertions.assertTrue(SocialRegistry.contains(e2));

        // Unregister e1
        SocialRegistry.unregister(e1);
        Assertions.assertEquals(1, SocialRegistry.getCount());
        Assertions.assertFalse(SocialRegistry.contains(e1));
        Assertions.assertTrue(SocialRegistry.contains(e2));

        // Second unregister of e1 must be a no-op
        SocialRegistry.unregister(e1);
        Assertions.assertEquals(1, SocialRegistry.getCount());

        // Clear all
        SocialRegistry.clear();
        Assertions.assertEquals(0, SocialRegistry.getCount());
        Assertions.assertFalse(SocialRegistry.contains(e2));
        Assertions.assertNull(SocialRegistry.getRandomEntity());
    }

    @Test
    @DisplayName("Verify GroupManager flock calculation and lowest UUID leader election")
    public void testGroupManagerFlockAndLeaderElection() {
        TestServerLevel level = createTestServerLevel();

        UUID uuidA = UUID.fromString("00000000-0000-0000-0000-000000000001");
        UUID uuidB = UUID.fromString("00000000-0000-0000-0000-000000000002");

        DummyGroupMemberMob mobA = createDummyMob(uuidA, new Vec3(0, 0, 0), new Vec3(0, 0, 0), level);
        DummyGroupMemberMob mobB = createDummyMob(uuidB, new Vec3(10, 20, 30), new Vec3(2, 4, 6), level);

        level.addTestEntity(mobA);
        level.addTestEntity(mobB);

        // Leader election for mobB (mobA has lower UUID, so mobA should become leader)
        GroupManager.findAndSetLeader(mobB, 64.0);
        Assertions.assertTrue(mobB.hasLeader());
        Assertions.assertEquals(mobA, mobB.getLeader());

        // Leader election for mobA (mobA itself is lowest UUID, so it should not have a leader)
        GroupManager.findAndSetLeader(mobA, 64.0);
        Assertions.assertFalse(mobA.hasLeader());

        // Compute flock state for leader mobA
        GroupManager.computeFlockState(mobA, 64.0);
        FlockState state = mobA.getFlockState();
        Assertions.assertNotNull(state);
        Assertions.assertEquals(2, state.getMemberCount());

        // Center of mass: average of (0,0,0) and (10,20,30) = (5, 10, 15)
        Vec3 com = state.getCenterOfMass();
        Assertions.assertEquals(5.0, com.x, 0.0001);
        Assertions.assertEquals(10.0, com.y, 0.0001);
        Assertions.assertEquals(15.0, com.z, 0.0001);

        // Average velocity: average of (0,0,0) and (2,4,6) = (1, 2, 3)
        Vec3 avgVel = state.getAverageVelocity();
        Assertions.assertEquals(1.0, avgVel.x, 0.0001);
        Assertions.assertEquals(2.0, avgVel.y, 0.0001);
        Assertions.assertEquals(3.0, avgVel.z, 0.0001);
    }

    @Test
    @DisplayName("Verify GeneticsEngine division guard with 0.0f multiplier")
    public void testGeneticsEngineDivisionGuard() {
        TestServerLevel level = createTestServerLevel();

        DummyGroupMemberMob parent1 = createDummyMob(UUID.randomUUID(), Vec3.ZERO, Vec3.ZERO, level);
        DummyGroupMemberMob parent2 = createDummyMob(UUID.randomUUID(), Vec3.ZERO, Vec3.ZERO, level);
        DummyGroupMemberMob baby = createDummyMob(UUID.randomUUID(), Vec3.ZERO, Vec3.ZERO, level);

        // Register a trait with 0.0f inbreeding multiplier to test division-by-zero protection
        TraitConfig zeroMultTrait = new TraitConfig(
            "vitality",
            "minecraft:generic.max_health",
            "ADD_VALUE",
            2.0f,
            0.0f, // zero inbreeding multiplier
            0.0f,
            100.0f
        );

        GeneticsConfig config = new GeneticsConfig(
            Map.of("vitality", zeroMultTrait),
            Map.of()
        );
        EntityGeneticsRegistry.register(baby.getType(), config);

        // Parent 1 was inbred, parent 2 was not. The baby itself is not inbred.
        EntityGenetics d1 = new EntityGenetics(
            Optional.empty(),
            Optional.empty(),
            true, // inbred parent
            true,
            Map.of("vitality", 10.0f)
        );
        EntityGenetics d2 = new EntityGenetics(
            Optional.empty(),
            Optional.empty(),
            false,
            true,
            Map.of("vitality", 10.0f)
        );

        GeneticsEngine.setGenetics(parent1, d1);
        GeneticsEngine.setGenetics(parent2, d2);

        // Must not produce NaN or Infinity
        GeneticsEngine.inheritGenetics(baby, parent1, parent2, "normal");

        EntityGenetics babyGenetics = GeneticsEngine.getGenetics(baby);
        Float vitality = babyGenetics.traits().get("vitality");
        Assertions.assertNotNull(vitality);
        Assertions.assertFalse(Float.isNaN(vitality), "Inherited trait must not be NaN with 0.0f inbreeding multiplier");
        Assertions.assertFalse(Float.isInfinite(vitality), "Inherited trait must not be Infinite with 0.0f inbreeding multiplier");
    }

    @Test
    @DisplayName("Verify EntitySocialScheduler event lifecycle matching SocialEvent.tick() contract")
    public void testEntitySocialSchedulerEventLifecycle() {
        TestServerLevel level = createTestServerLevel();
        DummyGroupMemberMob mob = createDummyMob(UUID.randomUUID(), Vec3.ZERO, Vec3.ZERO, level);

        EntitySocialScheduler scheduler = new EntitySocialScheduler(new DummySocialEntity(mob, "minecraft:zombie", null));
        DummySocialEntity socialEntity = new DummySocialEntity(mob, "minecraft:zombie", scheduler);
        // Replace scheduler with one bound to socialEntity
        EntitySocialScheduler activeScheduler = new EntitySocialScheduler(socialEntity);

        // Event A: tick returns true (event should CONTINUE running)
        TestSocialEvent continuousEvent = new TestSocialEvent("continuous", "track1", true);
        activeScheduler.schedule(continuousEvent);
        activeScheduler.tick();

        Assertions.assertTrue(continuousEvent.started, "Event must be started");
        Assertions.assertFalse(continuousEvent.ended, "Continuous event must not end while tick() returns true");
        Assertions.assertTrue(activeScheduler.isEventActive("continuous"), "Continuous event must remain active");
        Assertions.assertFalse(activeScheduler.isIdle(), "Scheduler must not be idle");

        // Event B: tick returns false (event should EXPIRE / TERMINATE)
        TestSocialEvent expiringEvent = new TestSocialEvent("expiring", "track2", false);
        activeScheduler.schedule(expiringEvent);
        activeScheduler.tick();

        Assertions.assertTrue(expiringEvent.started, "Expiring event must be started");
        Assertions.assertTrue(expiringEvent.ended, "Expiring event must end when tick() returns false");
        Assertions.assertEquals(SocialEvent.EndReason.EXPIRED, expiringEvent.endReason, "End reason must be EXPIRED");
        Assertions.assertFalse(activeScheduler.isEventActive("expiring"), "Expiring event must no longer be active");
    }
}
