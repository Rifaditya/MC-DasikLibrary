/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.api.group.strategy;

public record GroupParameters(float cohesionRadius, float separationRadius, float maxSpeed) {
    public static final GroupParameters DEFAULT_AERIAL = new GroupParameters(3.0f, 1.0f, 0.4f);
    public static final GroupParameters DEFAULT_TERRESTRIAL = new GroupParameters(5.0f, 1.5f, 1.2f);
}

