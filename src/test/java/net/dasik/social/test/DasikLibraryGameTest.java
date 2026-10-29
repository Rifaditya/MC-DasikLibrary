// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.test;

import com.mojang.brigadier.CommandDispatcher;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import net.dasik.social.ai.goal.FollowLeaderGoal;
import net.dasik.social.api.command.GameRuleCommandBridge;
import net.dasik.social.api.command.UniversalCommandRegistry;
import net.dasik.social.api.gamerule.DynamicGameRuleManager;
import net.dasik.social.api.group.FlockType;
import net.dasik.social.api.group.GroupMember;
import net.dasik.social.api.group.strategy.GroupParameters;
import net.dasik.social.api.test.DasikContractVerifier;
import net.dasik.social.api.test.VirtualPlayerBot;
import net.dasik.social.core.group.FlockState;
import net.dasik.social.core.group.GroupManager;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.Bootstrap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import static org.junit.jupiter.api.Assertions.*;

public class DasikLibraryGameTest {

    private static Unsafe unsafe;

    @BeforeAll
    public static void setUpAll() {
        try {
            SharedConstants.tryDetectVersion();
            Bootstrap.bootStrap();
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

        public void setTestGameTime(long time) {
            this.testGameTime = time;
        }

        @Override
        public <T extends Entity> List<T> getEntitiesOfClass(Class<T> baseClass, AABB bb, Predicate<? super T> selector) {
            List<T> result = new ArrayList<>();
            if (this.testEntities != null) {
                for (LivingEntity e : this.testEntities) {
                    if (baseClass.isInstance(e)) {
                        T cast = baseClass.cast(e);
                        if (bb == null || bb.intersects(e.getBoundingBox())) {
                            if (selector == null || selector.test(cast)) {
                                result.add(cast);
                            }
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

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return Blocks.AIR.defaultBlockState();
        }
    }

    public static class DummyGroupMemberMob extends Zombie implements GroupMember {
        private LivingEntity leader;
        private FlockState flockState;
        private FlockType flockType = FlockType.AERIAL;

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
            return this.flockType;
        }

        public void setFlockType(FlockType type) {
            this.flockType = type;
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

        @Override
        public float getPathfindingMalus(PathType type) {
            return 0.0f;
        }

        @Override
        public void setPathfindingMalus(PathType type, float malus) {
        }

        @Override
        public PathNavigation getNavigation() {
            try {
                return (PathNavigation) unsafe.allocateInstance(GroundPathNavigation.class);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public BlockPos blockPosition() {
            Vec3 pos = this.position();
            return pos != null ? BlockPos.containing(pos.x, pos.y, pos.z) : BlockPos.ZERO;
        }

        @Override
        public AttributeMap getAttributes() {
            return new AttributeMap(createLivingAttributes().build());
        }
    }

    private static TestServerLevel createTestServerLevel() {
        try {
            TestServerLevel level = (TestServerLevel) unsafe.allocateInstance(TestServerLevel.class);
            setField(TestServerLevel.class, level, "testEntities", new ArrayList<LivingEntity>());
            setField(TestServerLevel.class, level, "testGameTime", 100L);
            setField(net.minecraft.world.level.Level.class, level, "random", RandomSource.create());
            return level;
        } catch (Exception e) {
            throw new RuntimeException("Failed to allocate TestServerLevel", e);
        }
    }

    private static int entityIdCounter = 2000;

    private static DummyGroupMemberMob createDummyMob(UUID uuid, Vec3 pos, Vec3 vel, TestServerLevel level) {
        try {
            DummyGroupMemberMob mob = (DummyGroupMemberMob) unsafe.allocateInstance(DummyGroupMemberMob.class);
            setField(Entity.class, mob, "id", ++entityIdCounter);
            setField(Entity.class, mob, "type", EntityTypes.ZOMBIE);
            setField(Entity.class, mob, "uuid", uuid);
            setField(Entity.class, mob, "position", pos);
            setField(Entity.class, mob, "deltaMovement", vel);
            setField(Entity.class, mob, "bb", new AABB(pos.x - 0.5, pos.y, pos.z - 0.5, pos.x + 0.5, pos.y + 1.8, pos.z + 0.5));
            setField(Entity.class, mob, "level", level);
            setField(Entity.class, mob, "random", RandomSource.create());
            setField(DummyGroupMemberMob.class, mob, "flockType", FlockType.AERIAL);
            return mob;
        } catch (Exception e) {
            throw new RuntimeException("Failed to allocate DummyGroupMemberMob", e);
        }
    }

    @Test
    @DisplayName("Pillar 4 Headless Simulation: Compute flock state centroid and average velocity")
    void testComputeFlockStateCentroid() {
        TestServerLevel level = createTestServerLevel();

        UUID leaderUuid = new UUID(0L, 1L);
        UUID f1Uuid = new UUID(0L, 2L);
        UUID f2Uuid = new UUID(0L, 3L);

        DummyGroupMemberMob leader = createDummyMob(leaderUuid, new Vec3(10.0, 64.0, 10.0), new Vec3(0.3, 0.0, 0.6), level);
        DummyGroupMemberMob follower1 = createDummyMob(f1Uuid, new Vec3(20.0, 64.0, 10.0), new Vec3(0.6, 0.0, 0.3), level);
        DummyGroupMemberMob follower2 = createDummyMob(f2Uuid, new Vec3(30.0, 64.0, 10.0), new Vec3(0.0, 0.0, 0.3), level);

        follower1.setLeader(leader);
        follower2.setLeader(leader);

        level.addTestEntity(leader);
        level.addTestEntity(follower1);
        level.addTestEntity(follower2);

        GroupManager.computeFlockState(leader, 50.0);

        FlockState state = leader.getFlockState();
        assertNotNull(state, "FlockState must be generated and cached on the leader");
        assertEquals(3, state.getMemberCount(), "Flock member count must equal 3");

        // Centroid X: (10 + 20 + 30) / 3 = 20.0
        assertEquals(20.0, state.getCenterOfMass().x, 0.001);
        assertEquals(64.0, state.getCenterOfMass().y, 0.001);
        assertEquals(10.0, state.getCenterOfMass().z, 0.001);

        // Average velocity X: (0.3 + 0.6 + 0.0) / 3 = 0.3, Z: (0.6 + 0.3 + 0.3) / 3 = 0.4
        assertEquals(0.3, state.getAverageVelocity().x, 0.001);
        assertEquals(0.0, state.getAverageVelocity().y, 0.001);
        assertEquals(0.4, state.getAverageVelocity().z, 0.001);
    }

    @Test
    @DisplayName("Pillar 4 Headless Simulation: Deterministic leader election selects lowest UUID")
    void testDeterministicLeaderElection() {
        TestServerLevel level = createTestServerLevel();

        UUID lowestUuid = new UUID(0L, 10L);
        UUID middleUuid = new UUID(0L, 20L);
        UUID highestUuid = new UUID(0L, 30L);

        DummyGroupMemberMob mobA = createDummyMob(lowestUuid, new Vec3(10.0, 64.0, 10.0), Vec3.ZERO, level);
        DummyGroupMemberMob mobB = createDummyMob(middleUuid, new Vec3(12.0, 64.0, 10.0), Vec3.ZERO, level);
        DummyGroupMemberMob mobC = createDummyMob(highestUuid, new Vec3(14.0, 64.0, 10.0), Vec3.ZERO, level);

        level.addTestEntity(mobA);
        level.addTestEntity(mobB);
        level.addTestEntity(mobC);

        GroupManager.findAndSetLeader(mobB, 32.0);
        GroupManager.findAndSetLeader(mobC, 32.0);
        GroupManager.findAndSetLeader(mobA, 32.0);

        // mobB and mobC should follow mobA (lowest UUID)
        assertSame(mobA, mobB.getLeader(), "mobB must elect mobA with lowest UUID as leader");
        assertSame(mobA, mobC.getLeader(), "mobC must elect mobA with lowest UUID as leader");
        assertNull(mobA.getLeader(), "mobA is the lowest UUID so its leader must remain null");

        // When leader moves far away (> 2 * searchRadius), leader is dropped
        setField(Entity.class, mobA, "position", new Vec3(1000.0, 64.0, 1000.0));
        setField(Entity.class, mobA, "bb", new AABB(999.5, 64.0, 999.5, 1000.5, 65.8, 1000.5));
        GroupManager.findAndSetLeader(mobB, 32.0);
        assertNull(mobB.getLeader(), "Leader must be cleared when distance exceeds threshold");
    }

    @Test
    @DisplayName("Pillar 4 Headless Simulation: FollowLeaderGoal tick loop lifecycle and parameter propagation")
    void testFollowLeaderGoalTickLoop() {
        TestServerLevel level = createTestServerLevel();

        UUID leaderUuid = new UUID(0L, 100L);
        UUID followerUuid = new UUID(0L, 101L);

        DummyGroupMemberMob leader = createDummyMob(leaderUuid, new Vec3(15.0, 64.0, 0.0), Vec3.ZERO, level);
        DummyGroupMemberMob follower = createDummyMob(followerUuid, new Vec3(0.0, 64.0, 0.0), Vec3.ZERO, level);

        level.addTestEntity(leader);
        level.addTestEntity(follower);

        GroupParameters params = new GroupParameters(
                3.0f,   // cohesionRadius
                1.0f,   // separationRadius
                0.2f,   // maxSpeed
                false,  // canTeleport
                100.0f, // teleportDistance
                4.0f,   // startDistance
                1.0f,   // stopDistance
                0.05f,  // alignmentWeight
                0.05f,  // cohesionWeight
                0.05f   // separationWeight
        );

        FollowLeaderGoal<DummyGroupMemberMob> goal = new FollowLeaderGoal<>(follower, params, 32.0);

        // 1. Without leader, goal cannot be used
        assertFalse(goal.canUse(), "Goal must not activate without designated leader");

        // 2. Set leader at distance 15.0 > startDistance 4.0 -> canUse is true
        follower.setLeader(leader);
        assertTrue(goal.canUse(), "Goal must activate when leader distance exceeds startDistance");

        // 3. Start goal and simulate tick loop
        goal.start();

        for (int tick = 0; tick < 20; tick++) {
            level.setTestGameTime(100L + tick);
            if (goal.canContinueToUse()) {
                goal.tick();
            }
        }

        assertTrue(goal.canContinueToUse(), "Goal should continue to use while distance > stopDistance");

        // 4. Move follower to distance 0.5 < stopDistance 1.0 -> canContinueToUse becomes false
        setField(Entity.class, follower, "position", new Vec3(14.8, 64.0, 0.0));
        assertFalse(goal.canContinueToUse(), "Goal must stop when follower reaches stopDistance");

        goal.stop();

        // 5. Dynamic parameters update at runtime
        GroupParameters updatedParams = new GroupParameters(
                4.0f, 1.5f, 0.4f, false, 100.0f, 8.0f, 2.0f, 0.1f, 0.1f, 0.1f
        );
        assertDoesNotThrow(() -> goal.setParameters(updatedParams), "Updating parameters must not throw");
    }

    @Test
    @DisplayName("Pillar 4 Headless Simulation: Dynamic GameRule mutation propagates to goal parameters")
    void testGameRulePropagationSimulation() {
        String ruleName = "dasik_sim:flock_speed";
        DynamicGameRuleManager.integerRule(ruleName, null, 10)
                .min(1)
                .max(50)
                .description("Simulation flock speed test rule");

        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("flock_speed", null, 10, 1, 50);

        assertEquals(10, bridge.getValue(null, "flock_speed"));

        // Mutate rule via bridge
        GameRuleCommandBridge.SetResult res = bridge.setValue(null, "flock_speed", "25");
        assertEquals(GameRuleCommandBridge.Status.SUCCESS, res.getStatus());
        assertEquals(25, bridge.getValue(null, "flock_speed"));

        // Propagate into GroupParameters
        int speedVal = (int) bridge.getValue(null, "flock_speed");
        float maxSpeed = speedVal * 0.02f;

        GroupParameters params = new GroupParameters(
                3.0f, 1.0f, maxSpeed, false, 100.0f, 4.0f, 1.0f, 0.05f, 0.05f, 0.05f
        );
        assertEquals(0.5f, params.maxSpeed(), 0.001);

        DynamicGameRuleManager.unregister(ruleName);
    }

    @Test
    @DisplayName("Pillar 4 Headless Simulation: VirtualPlayerBot action dispatch and Mixin audit safety")
    void testVirtualPlayerBotSimulation() throws Exception {
        // 1. Pillar 5 Mixin Linkage Audit safety
        assertDoesNotThrow(VirtualPlayerBot::auditAllMixins, "VirtualPlayerBot.auditAllMixins() must execute headlessly without NPE or crash");

        // 2. Pillar 5 Sidedness check on VirtualPlayerBot bytecode
        String botClassResource = "net/dasik/social/api/test/VirtualPlayerBot.class";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(botClassResource)) {
            assertNotNull(in, "VirtualPlayerBot.class must be loadable from classpath");
            byte[] bytes = in.readAllBytes();
            Set<String> constants = DasikContractVerifier.extractUtf8Constants(bytes);
            for (String constant : constants) {
                assertFalse(constant.contains("net/minecraft/client/") || constant.contains("net.minecraft.client."),
                        "VirtualPlayerBot contains client-side reference in bytecode: " + constant);
            }
        }

        // 3. Simulated player command action dispatch
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        GameRuleCommandBridge bridge = new GameRuleCommandBridge();
        bridge.registerInteger("param", null, 10, 1, 100);

        UniversalCommandRegistry.builder("botmod", dispatcher)
                .withModName("Bot Mod")
                .withBridge(bridge)
                .withStatusSupplier(level -> List.of("Param: 10"))
                .register();

        CommandSourceStack playerSource = new CommandSourceStack(
                CommandSource.NULL,
                Vec3.ZERO,
                Vec2.ZERO,
                null,
                PermissionSet.ALL_PERMISSIONS,
                "VirtualBotPlayer",
                Component.literal("VirtualBotPlayer"),
                null,
                null
        );

        // Player executes help
        int helpRes = dispatcher.execute("botmod help", playerSource);
        assertEquals(1, helpRes);

        // Player executes status
        int statusRes = dispatcher.execute("botmod status", playerSource);
        assertEquals(1, statusRes);

        // Player executes get
        int getRes = dispatcher.execute("botmod get param", playerSource);
        assertEquals(1, getRes);

        // Player executes set
        int setRes = dispatcher.execute("botmod set param 42", playerSource);
        assertEquals(1, setRes);
        assertEquals(42, bridge.getValue(null, "param"));
    }
}
