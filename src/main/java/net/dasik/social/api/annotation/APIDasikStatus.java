// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.ApiStatus;

/**
 * Branded alias for {@link DasikApiStatus}, providing direct access to
 * {@code @APIDasikStatus.Public}, {@code @APIDasikStatus.Internal}, and {@code @APIDasikStatus.Experimental}.
 */
public final class APIDasikStatus {
    private APIDasikStatus() {}

    /**
     * Marks an API element as stable, officially supported public API for the Dasik ecosystem.
     * Consumer mods and add-ons may safely depend on elements marked with this annotation across minor versions.
     */
    @Documented
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
    public @interface Public {
    }

    /**
     * Marks an API element as internal to the Dasik engine or library infrastructure.
     * Elements marked with this annotation are subject to arbitrary change, renaming, or removal at any time without notice.
     * External mods must NOT depend on internal elements.
     */
    @Documented
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
    @ApiStatus.Internal
    public @interface Internal {
    }

    /**
     * Marks an API element as experimental or in active development.
     * While available for trial, elements marked with this annotation may undergo breaking adjustments prior to stabilization.
     */
    @Documented
    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PACKAGE})
    @ApiStatus.Experimental
    public @interface Experimental {
    }
}
