// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.ApiStatus;

/**
 * Standard API governance annotations for the DasikLibrary ecosystem.
 * <p>
 * Classes and members within the Dasik ecosystem are categorized into three lifecycle stages:
 * <ul>
 *     <li>{@link Public}: Fully supported, stable public API contracts designed for consumer mods and integrations.</li>
 *     <li>{@link Experimental}: Early-stage or evolving API contracts subject to tuning, redesign, or migration across minor versions.</li>
 *     <li>{@link Internal}: Internal engine mechanics, schedulers, and implementation details reserved strictly for DasikLibrary internals.</li>
 * </ul>
 */
public final class DasikApiStatus {
    private DasikApiStatus() {}

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
