// Verified against: Minecraft.java (26.1.2+), ServerLevel.java (26.1.2+)
package net.dasik.social.api.gamerule;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gamerules.GameRule;

public class ClientGameRuleHelper {
    public static int getInt(Level level, GameRule<Integer> rule) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null) {
            MinecraftServer server = minecraft.getSingleplayerServer();
            if (server != null) {
                ServerLevel serverLevel = server.getLevel(level.dimension());
                if (serverLevel != null) {
                    return serverLevel.getGameRules().get(rule);
                }
            }
        }
        return rule != null ? rule.defaultValue() : 0;
    }

    public static boolean getBoolean(Level level, GameRule<Boolean> rule) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft != null) {
            MinecraftServer server = minecraft.getSingleplayerServer();
            if (server != null) {
                ServerLevel serverLevel = server.getLevel(level.dimension());
                if (serverLevel != null) {
                    return serverLevel.getGameRules().get(rule);
                }
            }
        }
        return rule != null ? rule.defaultValue() : false;
    }
}
