/*
 * Dasik Library
 * Verified against: RandomSource.java (26.1.2+)
 */
package net.dasik.social.api.genetics;

import net.minecraft.util.RandomSource;

public record MutationRule(String type, float param1, float param2) {
    public float roll(RandomSource random) {
        if ("triangular".equalsIgnoreCase(type)) {
            return random.triangle(param1, param2);
        } else if ("uniform".equalsIgnoreCase(type) || "flat".equalsIgnoreCase(type)) {
            return param1 + (random.nextFloat() * (param2 - param1));
        } else { // constant
            return param1;
        }
    }
}
