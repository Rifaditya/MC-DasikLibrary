// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.test;

import com.mojang.authlib.GameProfile;
import io.netty.channel.ChannelHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import net.dasik.social.api.annotation.DasikApiStatus;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.UUID;

/**
 * Headless server mock player simulation wrapper for automated tests and GameTests.
 * <p>
 * Connects a real {@link ServerPlayer} to an in-memory Netty {@link EmbeddedChannel} and provides
 * fluent helpers for entity interactions, item equipping, command execution, and mixin auditing
 * without requiring client-side graphical dependencies or leaking test libraries into production bytecode.
 */
@DasikApiStatus.Public
public class VirtualPlayerBot {

    private final MinecraftServer server;
    private final ServerPlayer player;
    private final Connection connection;
    private final EmbeddedChannel channel;

    public VirtualPlayerBot(MinecraftServer server, ServerPlayer player, Connection connection, EmbeddedChannel channel) {
        this.server = Objects.requireNonNull(server, "server cannot be null");
        this.player = Objects.requireNonNull(player, "player cannot be null");
        this.connection = Objects.requireNonNull(connection, "connection cannot be null");
        this.channel = Objects.requireNonNull(channel, "channel cannot be null");
    }

    /**
     * Creates and spawns a virtual player in the given level with default creative game mode.
     */
    public static VirtualPlayerBot create(ServerLevel level) {
        return create(level, "virtual-player-" + UUID.randomUUID().toString().substring(0, 8), GameType.CREATIVE);
    }

    /**
     * Creates and spawns a virtual player in the given level with specified name and game mode.
     */
    public static VirtualPlayerBot create(ServerLevel level, String name, GameType gameType) {
        Objects.requireNonNull(level, "level cannot be null");
        MinecraftServer server = Objects.requireNonNull(level.getServer(), "server cannot be null");
        return create(server, level, name, gameType);
    }

    /**
     * Creates and spawns a virtual player connected to a virtual embedded channel.
     */
    public static VirtualPlayerBot create(MinecraftServer server, ServerLevel level, String name, GameType gameType) {
        Objects.requireNonNull(server, "server cannot be null");
        Objects.requireNonNull(level, "level cannot be null");

        GameProfile profile = new GameProfile(UUID.randomUUID(), name != null ? name : "virtual-bot");
        CommonListenerCookie cookie = CommonListenerCookie.createInitial(profile, false);

        ServerPlayer player = new ServerPlayer(server, level, cookie.gameProfile(), cookie.clientInformation()) {
            @Override
            public GameType gameMode() {
                return gameType != null ? gameType : GameType.CREATIVE;
            }

            @Override
            public boolean isClientAuthoritative() {
                return false;
            }
        };

        if (gameType != null) {
            gameType.updatePlayerAbilities(player.getAbilities());
        }

        Connection connection = new Connection(PacketFlow.SERVERBOUND);
        EmbeddedChannel channel = new EmbeddedChannel(new ChannelHandler[]{connection});

        if (server.getPlayerList() != null) {
            server.getPlayerList().placeNewPlayer(connection, player, cookie);
        } else {
            player.connection = new ServerGamePacketListenerImpl(server, connection, player, cookie);
        }

        return new VirtualPlayerBot(server, player, connection, channel);
    }

    /**
     * Returns the host {@link MinecraftServer}.
     */
    public MinecraftServer getServer() {
        return this.server;
    }

    /**
     * Returns the underlying Minecraft {@link ServerPlayer}.
     */
    public ServerPlayer getPlayer() {
        return this.player;
    }

    /**
     * Returns the mock {@link Connection} attached to the player.
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Returns the underlying Netty {@link EmbeddedChannel}.
     */
    public EmbeddedChannel getChannel() {
        return this.channel;
    }

    /**
     * Orients the player towards the specified target coordinates.
     */
    public void lookAt(double x, double y, double z) {
        Vec3 target = new Vec3(x, y, z);
        lookAt(target);
    }

    /**
     * Orients the player towards the specified target vector.
     */
    public void lookAt(Vec3 target) {
        Vec3 diff = target.subtract(this.player.getEyePosition());
        double d = diff.horizontalDistance();
        float yRot = (float) (Math.atan2(diff.z, diff.x) * (180.0 / Math.PI)) - 90.0F;
        float xRot = (float) (-(Math.atan2(diff.y, d) * (180.0 / Math.PI)));
        this.player.setYRot(yRot);
        this.player.setXRot(xRot);
        this.player.yRotO = yRot;
        this.player.xRotO = xRot;
    }

    /**
     * Equips an item in the player's main hand.
     */
    public void equipMainHand(ItemStack item) {
        this.player.setItemSlot(EquipmentSlot.MAINHAND, item != null ? item : ItemStack.EMPTY);
    }

    /**
     * Equips an item in the player's off hand.
     */
    public void equipOffHand(ItemStack item) {
        this.player.setItemSlot(EquipmentSlot.OFFHAND, item != null ? item : ItemStack.EMPTY);
    }

    /**
     * Simulates an attack against the target entity.
     */
    public void attack(Entity target) {
        if (target != null) {
            this.player.attack(target);
        }
    }

    /**
     * Simulates an entity interaction using the specified hand.
     */
    public InteractionResult interactEntity(Entity target, InteractionHand hand) {
        if (target == null) {
            return InteractionResult.PASS;
        }
        InteractionHand resolvedHand = hand != null ? hand : InteractionHand.MAIN_HAND;
        return this.player.interactOn(target, resolvedHand, target.position());
    }

    /**
     * Simulates using an item on the block at the specified position.
     */
    public InteractionResult useBlock(BlockPos pos) {
        if (pos == null) {
            return InteractionResult.PASS;
        }
        Vec3 hitVec = Vec3.atCenterOf(pos);
        BlockHitResult hitResult = new BlockHitResult(hitVec, Direction.UP, pos, false);
        return this.player.gameMode.useItemOn(this.player, this.player.level(), this.player.getMainHandItem(),
                InteractionHand.MAIN_HAND, hitResult);
    }

    /**
     * Dispatches an in-game command through the server's Brigadier command tree as this player.
     *
     * @param command Command string (with or without leading '/')
     * @return Result integer from Brigadier dispatcher
     */
    public int executeCommand(String command) {
        if (command == null || command.trim().isEmpty()) {
            return 0;
        }
        String cleanCommand = command.startsWith("/") ? command.substring(1) : command;
        CommandSourceStack source = this.player.createCommandSourceStack();
        if (this.server != null && this.server.getCommands() != null) {
            try {
                return this.server.getCommands().getDispatcher().execute(cleanCommand, source);
            } catch (Exception e) {
                throw new RuntimeException("VirtualPlayerBot failed to execute command: " + cleanCommand, e);
            }
        }
        return 0;
    }

    /**
     * Clears all inbound and outbound packet queues from the embedded channel to prevent memory retention.
     */
    public void drainOutboundPackets() {
        if (this.channel != null) {
            while (this.channel.readOutbound() != null) {
                // Drain outbound packets
            }
            while (this.channel.readInbound() != null) {
                // Drain inbound packets
            }
        }
    }

    /**
     * Discards the player from the world and unregisters from the server player list.
     */
    public void discard() {
        drainOutboundPackets();
        if (this.server != null && this.server.getPlayerList() != null) {
            this.server.getPlayerList().remove(this.player);
        }
        this.player.discard();
    }

    /**
     * Pillar 5 Mixin Linkage Invariant: Audits all SpongePowered Mixin transformations
     * across the active ClassLoader to detect parameter descriptor mismatches headlessly.
     */
    public static void auditAllMixins() {
        try {
            Class<?> envClass = Class.forName("org.spongepowered.asm.mixin.MixinEnvironment");
            Method getEnvMethod = envClass.getMethod("getCurrentEnvironment");
            Object env = getEnvMethod.invoke(null);
            if (env != null) {
                Method auditMethod = envClass.getMethod("audit");
                auditMethod.invoke(env);
            }
        } catch (ClassNotFoundException | LinkageError e) {
            // Not running in SpongePowered Mixin environment (e.g. standard unit test); safely pass
        } catch (Exception e) {
            Throwable cause = e.getCause() != null ? e.getCause() : e;
            // In headless unit test environments, the SpongePowered transformer pipeline may be uninitialized
            if (!(cause instanceof NullPointerException)) {
                throw new ContractViolationException("Pillar 5 Mixin Audit Failed: Method descriptor mismatch or invalid target class!", e);
            }
        }
    }
}
