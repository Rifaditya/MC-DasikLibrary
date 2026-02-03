package net.dasik.social.api;

public enum Scope {
    /** Everyone in range hears it. */
    PUBLIC,

    /** Only entities of the same species hear it. */
    SAME_SPECIES,

    /** Only the specific target entity hears it. */
    DIRECTED,

    /** Internal use only. */
    PRIVATE
}
