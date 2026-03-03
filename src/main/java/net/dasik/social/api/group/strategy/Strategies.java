/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.api.group.strategy;

import net.dasik.social.api.group.strategy.AerialFlockingStrategy;
import net.dasik.social.api.group.strategy.FlockingStrategy;
import net.dasik.social.api.group.strategy.TerrestrialFlockingStrategy;

public class Strategies {
    public static final FlockingStrategy AERIAL = new AerialFlockingStrategy();
    public static final FlockingStrategy TERRESTRIAL = new TerrestrialFlockingStrategy();
}

