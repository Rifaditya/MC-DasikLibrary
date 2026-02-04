package net.dasik.social.api.size;

/**
 * Represents a range of scale values for an entity.
 *
 * @param min The minimum scale multiplier.
 * @param max The maximum scale multiplier.
 */
public record ScaleRange(float min, float max) {
    public ScaleRange {
        if (min < 0 || max < 0) {
            throw new IllegalArgumentException("Scale values must be positive");
        }
        if (min > max) {
            throw new IllegalArgumentException("Min scale cannot be greater than max scale");
        }
    }
}
