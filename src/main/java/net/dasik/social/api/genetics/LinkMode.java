/*
 * Zenith Sovereign Engineering - Dasik Library
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
// Verified against: Attributes.java (26.2+)
package net.dasik.social.api.genetics;

/**
 * Defines how a rolled trait value (e.g., size) affects a linked attribute.
 *
 * <ul>
 *   <li>{@link #PROPORTIONAL} — bigger entity = higher attribute value (linear)</li>
 *   <li>{@link #INVERSE} — bigger entity = lower attribute value (linear)</li>
 *   <li>{@link #SQUARED} — attribute scales with size² (area/weight-based)</li>
 * </ul>
 */
public enum LinkMode {
    /**
     * Linear proportional scaling. A size of 1.2 with strength 1.0 produces
     * a +20% modifier on the linked attribute.
     */
    PROPORTIONAL,

    /**
     * Inverse linear scaling. A size of 1.2 with strength 1.0 produces
     * a -20% modifier on the linked attribute (bigger = slower, etc.).
     */
    INVERSE,

    /**
     * Squared scaling. A size of 1.2 with strength 1.0 produces
     * a +44% modifier (1.2² = 1.44). Useful for weight or area-based mechanics.
     */
    SQUARED
}
