/*
 * Zenith Sovereign Engineering - Dasik Library
 * Verified against: ServerPlayer.java (Snapshot 10)
 */
package net.dasik.social.api.vision;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Optimized vision tracking for player-entity interactions.
 * Uses a spatial sweep and raycasting budget to minimize performance impact.
 */
public class PlayerVisionTracker {
    private static final Map<String, Double> globalListeners = new ConcurrentHashMap<>();
    private static final WeakHashMap<ServerPlayer, TrackerInstance> trackers = new WeakHashMap<>();

    public static void registerListener(String modId, double radius) {
        globalListeners.put(modId, radius);
    }

    public static void unregisterListener(String modId) {
        globalListeners.remove(modId);
    }

    public static double getMaxRequestedRadius() {
        if (globalListeners.isEmpty()) {
            return -1.0;
        }
        double max = 0.0;
        for (Double r : globalListeners.values()) {
            if (r > max) {
                max = r;
            }
        }
        return max;
    }

    public static boolean canSee(ServerPlayer player, Entity target) {
        if (globalListeners.isEmpty()) {
            return false;
        }
        TrackerInstance tracker = trackers.get(player);
        if (tracker == null) {
            return false;
        }
        return tracker.canSee(target);
    }

    public static void init() {
        NeoForge.EVENT_BUS.addListener(PlayerVisionTracker::onServerTick);
    }

    private static void onServerTick(ServerTickEvent.Post event) {
        if (globalListeners.isEmpty()) {
            return;
        }
        double maxRadius = PlayerVisionTracker.getMaxRequestedRadius();
        if (maxRadius <= 0.0) {
            return;
        }
        for (ServerPlayer player : event.getServer().getPlayerList().getPlayers()) {
            TrackerInstance tracker = trackers.computeIfAbsent(player, TrackerInstance::new);
            tracker.tick(maxRadius);
        }
    }

    private static class TrackerInstance {
        private final ServerPlayer player;
        private final Set<Entity> visibleEntities = Collections.newSetFromMap(new ConcurrentHashMap<>());
        private final Queue<Entity> scanQueue = new LinkedList<>();
        private final Set<BlockPos> sweepVisibleBlocks = new HashSet<>();
        private final Set<BlockPos> sweepHiddenBlocks = new HashSet<>();

        public TrackerInstance(ServerPlayer player) {
            this.player = player;
        }

        public boolean canSee(Entity entity) {
            return this.visibleEntities.contains(entity);
        }

        public void tick(double maxRadius) {
            if (this.player.level().getGameTime() % 2L != 0L) {
                return;
            }
            if (this.scanQueue.isEmpty()) {
                int simDist = this.player.level().getServer().getPlayerList().getViewDistance();
                double safeRadius = Math.min(maxRadius, (double)simDist * 16.0);
                AABB box = this.player.getBoundingBox().inflate(safeRadius);
                List<Entity> nearby = this.player.level().getEntities(this.player, box, e -> e.distanceToSqr(this.player) <= safeRadius * safeRadius);
                this.scanQueue.addAll(nearby);
                this.visibleEntities.clear();
                this.sweepVisibleBlocks.clear();
                this.sweepHiddenBlocks.clear();
            }
            int processed = 0;
            Vec3 eyePos = this.player.getEyePosition();
            while (!this.scanQueue.isEmpty() && processed < 10) {
                Entity target = this.scanQueue.poll();
                if (target == null || target.isRemoved()) continue;
                Vec3 targetPos = new Vec3(target.getX(), target.getY() + (double)target.getBbHeight() / 2.0, target.getZ());
                BlockPos targetBlockPos = BlockPos.containing(targetPos);
                if (this.sweepVisibleBlocks.contains(targetBlockPos)) {
                    this.visibleEntities.add(target);
                    ++processed;
                    continue;
                }
                if (this.sweepHiddenBlocks.contains(targetBlockPos)) {
                    ++processed;
                    continue;
                }
                ClipContext context = new ClipContext(eyePos, targetPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, this.player);
                BlockHitResult result = this.player.level().clip(context);
                if (result.getType() == HitResult.Type.MISS) {
                    this.visibleEntities.add(target);
                    this.sweepVisibleBlocks.add(targetBlockPos);
                } else {
                    this.sweepHiddenBlocks.add(targetBlockPos);
                }
                ++processed;
            }
        }
    }
}
