package net.dasik.social.api.group.strategy;

/**
 * Access point for common default flocking strategies.
 */
public class Strategies {

    public static final FlockingStrategy AERIAL = new AerialFlockingStrategy();
    public static final FlockingStrategy TERRESTRIAL = new TerrestrialFlockingStrategy();

}
