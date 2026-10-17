// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import net.dasik.social.api.annotation.DasikApiStatus;

/**
 * Declares metadata for a configuration field to be automatically discovered
 * and rendered into client configuration GUIs (such as YACL v3).
 * <p>
 * This annotation is strictly side-neutral (0 client imports) so common
 * configuration classes on dedicated servers can safely declare it without
 * runtime classloader crashes.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@DasikApiStatus.Public
public @interface ConfigOption {

    /**
     * Category name key or identifier to which this option belongs.
     * Defaults to {@code "general"}.
     */
    String category() default "general";

    /**
     * Sub-group name key or identifier within the category.
     * If empty, the option is placed directly in the category's root group.
     */
    String group() default "";

    /**
     * Explicit localization key for the option display name.
     * If empty, the generator auto-infers {@code config.<modId>.<fieldName>}.
     */
    String nameKey() default "";

    /**
     * Explicit localization key for the option description / tooltip.
     * If empty, the generator auto-infers {@code config.<modId>.<fieldName>.description}.
     */
    String descriptionKey() default "";

    /**
     * Lower bound for numerical options.
     * Defaults to negative infinity (unbounded).
     */
    double min() default Double.NEGATIVE_INFINITY;

    /**
     * Upper bound for numerical options.
     * Defaults to positive infinity (unbounded).
     */
    double max() default Double.POSITIVE_INFINITY;

    /**
     * Step increment for numerical sliders or step adjustments.
     * Defaults to {@code 1.0}.
     */
    double step() default 1.0;

    /**
     * When {@code true} and numerical bounds are finite, renders a slider
     * widget instead of a numeric text input field.
     */
    boolean slider() default false;

    /**
     * Relative ordering priority within the category and group.
     * Lower numbers appear first.
     */
    int order() default 0;

    /**
     * When {@code true}, automatically synchronizes value changes to the matching
     * in-game GameRule when running in a singleplayer world.
     */
    boolean syncGameRule() default false;

    /**
     * Explicit GameRule identifier if different from {@code <modId>:<fieldName>}.
     * If empty and {@link #syncGameRule()} is true, the generator attempts to resolve
     * {@code <modId>:<fieldName>}.
     */
    String gameRuleName() default "";
}
