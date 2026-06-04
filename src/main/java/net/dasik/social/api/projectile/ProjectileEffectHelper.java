package net.dasik.social.api.projectile;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ProjectileEffectHelper {

    /**
     * Triggers a "Sonic Juice" effect (sound + particles) if the projectile's power exceeds the threshold.
     * Uses client-side prediction for zero-latency sound if the shooter is a Player.
     *
     * @param level         The level the projectile was fired in
     * @param shooter       The entity that fired the projectile
     * @param actualPower   The calculated power/velocity of the shot
     * @param baseVelocity  The base velocity to calculate the threshold against
     * @param thresholdMult The multiplier applied to baseVelocity to determine the threshold (e.g., 1.2f for +20%)
     */
    public static void playSonicJuice(Level level, Entity shooter, float actualPower, float baseVelocity, float thresholdMult) {
        if (actualPower > baseVelocity * thresholdMult) {
            // Sonic Crack (pitch 0.5f) - Play on both sides for zero latency
            Player player = shooter instanceof Player p ? p : null;
            level.playSound(player, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST_FAR, SoundSource.PLAYERS, 1.0f, 0.5f);
            
            if (level instanceof ServerLevel serverLevel) {
                // Cloud particles - Sent by server to tracking clients
                serverLevel.sendParticles(ParticleTypes.CLOUD, 
                    shooter.getX(), shooter.getEyeY() - 0.15, shooter.getZ(), 
                    5, 0.1, 0.1, 0.1, 0.05);
            }
        }
    }
}
