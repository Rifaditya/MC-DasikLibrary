/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 */
package net.dasik.social.api;

import net.dasik.social.api.SocialEntity;
import net.minecraft.util.RandomSource;

public interface TickContext {
    public SocialEntity entity();

    public long gameTime();

    public float partialTick();

    public RandomSource random();
}

