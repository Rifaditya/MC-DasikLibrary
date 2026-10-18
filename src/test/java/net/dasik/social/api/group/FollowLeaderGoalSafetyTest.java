// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.group;

import java.lang.reflect.Field;
import java.util.EnumMap;
import net.dasik.social.ai.goal.FollowLeaderGoal;
import net.dasik.social.api.group.strategy.AerialFlockingStrategy;
import net.dasik.social.api.group.strategy.GroupParameters;
import net.dasik.social.core.group.FlockState;
import net.dasik.social.core.group.GroupManager;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

public class FollowLeaderGoalSafetyTest {

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

    private static DummyZombie createDummyZombie() {
        try {
            DummyZombie zombie = (DummyZombie) unsafe.allocateInstance(DummyZombie.class);
            setField(Mob.class, zombie, "pathfindingMalus", new EnumMap<PathType, Float>(PathType.class));
            setField(Entity.class, zombie, "position", Vec3.ZERO);
            setField(Entity.class, zombie, "deltaMovement", Vec3.ZERO);
            setField(Entity.class, zombie, "random", RandomSource.create());
            return zombie;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate dummy Zombie", e);
        }
    }

    private static DummyGroupMemberMob createDummyGroupMemberMob() {
        try {
            DummyGroupMemberMob mob = (DummyGroupMemberMob) unsafe.allocateInstance(DummyGroupMemberMob.class);
            setField(Mob.class, mob, "pathfindingMalus", new EnumMap<PathType, Float>(PathType.class));
            setField(Entity.class, mob, "position", Vec3.ZERO);
            setField(Entity.class, mob, "deltaMovement", Vec3.ZERO);
            setField(Entity.class, mob, "random", RandomSource.create());
            return mob;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate dummy GroupMember mob", e);
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

    public static class DummyZombie extends Zombie {
        protected DummyZombie() {
            super(null, null);
        }

        @Override
        public boolean isAlive() {
            return true;
        }
    }

    public static class DummyGroupMemberMob extends Zombie implements GroupMember {
        private LivingEntity leader;
        private FlockState flockState;

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

    @Test
    @DisplayName("Pillar 1: Instantiation with non-GroupMember Mob succeeds without ClassCastException")
    public void testInstantiationWithNonGroupMemberMob() {
        DummyZombie nonMemberMob = createDummyZombie();
        Assertions.assertDoesNotThrow(() -> {
            FollowLeaderGoal<DummyZombie> goal = new FollowLeaderGoal<>(nonMemberMob, GroupParameters.DEFAULT_TERRESTRIAL, 16.0);
            Assertions.assertFalse(goal.canUse(), "canUse() must return false for non-GroupMember mob");
            Assertions.assertFalse(goal.canContinueToUse(), "canContinueToUse() must return false for non-GroupMember mob");
            goal.stop();
            goal.tick();
        });
    }

    @Test
    @DisplayName("Pillar 1: Instantiation with null Mob succeeds without NullPointerException or ClassCastException")
    public void testInstantiationWithNullMob() {
        Assertions.assertDoesNotThrow(() -> {
            FollowLeaderGoal<Mob> goal = new FollowLeaderGoal<>(null, GroupParameters.DEFAULT_TERRESTRIAL, 16.0);
            Assertions.assertFalse(goal.canUse(), "canUse() must return false for null mob");
            Assertions.assertFalse(goal.canContinueToUse(), "canContinueToUse() must return false for null mob");
            goal.start();
            goal.stop();
            goal.tick();
        });
    }

    @Test
    @DisplayName("Pillar 1: GroupManager.findAndSetLeader safely ignores non-GroupMember entities")
    public void testGroupManagerFindAndSetLeaderNonGroupMember() {
        DummyZombie nonMemberMob = createDummyZombie();
        Assertions.assertDoesNotThrow(() -> {
            GroupManager.findAndSetLeader(nonMemberMob, 16.0);
        });
    }

    @Test
    @DisplayName("Pillar 1: GroupManager.computeFlockState safely handles non-GroupMember leader or null")
    public void testGroupManagerComputeFlockStateDefensive() {
        DummyZombie nonMemberLeader = createDummyZombie();
        Assertions.assertDoesNotThrow(() -> {
            GroupManager.computeFlockState(nonMemberLeader, 16.0);
            GroupManager.computeFlockState(null, 16.0);
        });
    }

    @Test
    @DisplayName("Pillar 1: AerialFlockingStrategy safely handles non-GroupMember leader")
    public void testAerialFlockingStrategyNonGroupMemberLeader() {
        DummyGroupMemberMob bat = createDummyGroupMemberMob();
        DummyZombie nonMemberLeader = createDummyZombie();

        AerialFlockingStrategy strategy = new AerialFlockingStrategy();
        Assertions.assertDoesNotThrow(() -> {
            strategy.execute(bat, nonMemberLeader, GroupParameters.DEFAULT_AERIAL);
        });
    }

    @Test
    @DisplayName("Pillar 1: FollowLeaderGoal tick with non-GroupMember leader executes safely")
    public void testFollowLeaderGoalTickWithNonGroupMemberLeader() {
        DummyGroupMemberMob follower = createDummyGroupMemberMob();
        DummyZombie nonMemberLeader = createDummyZombie();
        follower.setLeader(nonMemberLeader);

        FollowLeaderGoal<DummyGroupMemberMob> goal = new FollowLeaderGoal<>(follower, GroupParameters.DEFAULT_AERIAL, 16.0);
        Assertions.assertDoesNotThrow(() -> {
            goal.tick();
        });
    }
}
