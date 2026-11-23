// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.prompt;

import net.dasik.social.api.annotation.DasikApiStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BooleanSupplier;

/**
 * Universal registration hub for creator support prompts triggered by milestone advancements.
 * <p>
 * Safe for server, common, and client execution. Downstream mods call {@link #register} during
 * common initialization without pulling client-side dependencies.
 */
@DasikApiStatus.Public
public final class DasikPromptRegistry {
    private static final Map<String, PromptRegistration> REGISTRATIONS = new ConcurrentHashMap<>();

    private DasikPromptRegistry() {}

    /**
     * Immutable data contract representing a registered milestone prompt.
     *
     * @param modId the registering mod identifier (e.g. "betterdogs")
     * @param displayName player-facing mod or feature name shown in chat (e.g. "Better Dogs")
     * @param advancementId namespaced advancement ID (e.g. "betterdogs:milestone/tame_all_breeds")
     * @param enabledSupplier dynamic supplier to evaluate whether this prompt is currently active
     */
    public record PromptRegistration(
            String modId,
            String displayName,
            String advancementId,
            BooleanSupplier enabledSupplier
    ) {
        public PromptRegistration {
            if (modId == null || modId.isBlank()) {
                throw new IllegalArgumentException("modId cannot be null or blank");
            }
            if (displayName == null || displayName.isBlank()) {
                displayName = modId;
            }
            if (advancementId == null || advancementId.isBlank()) {
                throw new IllegalArgumentException("advancementId cannot be null or blank");
            }
            if (enabledSupplier == null) {
                enabledSupplier = () -> true;
            }
        }

        public boolean isEnabled() {
            try {
                return enabledSupplier != null && enabledSupplier.getAsBoolean();
            } catch (Throwable t) {
                return false;
            }
        }
    }

    /**
     * Registers a milestone advancement prompt with an explicit enabled supplier.
     */
    public static void register(String modId, String displayName, String advancementId, BooleanSupplier enabledSupplier) {
        PromptRegistration registration = new PromptRegistration(modId, displayName, advancementId, enabledSupplier);
        REGISTRATIONS.put(advancementId, registration);
    }

    /**
     * Registers a milestone advancement prompt unconditionally enabled by default.
     */
    public static void register(String modId, String displayName, String advancementId) {
        register(modId, displayName, advancementId, () -> true);
    }

    /**
     * Registers a pre-constructed {@link PromptRegistration}.
     */
    public static void register(PromptRegistration registration) {
        if (registration != null) {
            REGISTRATIONS.put(registration.advancementId(), registration);
        }
    }

    /**
     * Looks up a registered prompt by its namespaced advancement identifier.
     */
    public static Optional<PromptRegistration> get(String advancementId) {
        if (advancementId == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(REGISTRATIONS.get(advancementId));
    }

    /**
     * Checks whether an advancement identifier has a registered prompt.
     */
    public static boolean isRegistered(String advancementId) {
        return advancementId != null && REGISTRATIONS.containsKey(advancementId);
    }

    /**
     * Returns an unmodifiable snapshot collection of all registered prompts.
     */
    public static Collection<PromptRegistration> getAll() {
        return Collections.unmodifiableCollection(new ArrayList<>(REGISTRATIONS.values()));
    }

    /**
     * Returns all registered prompts associated with a given mod identifier.
     */
    public static List<PromptRegistration> getForMod(String modId) {
        if (modId == null || modId.isBlank()) {
            return Collections.emptyList();
        }
        List<PromptRegistration> result = new ArrayList<>();
        for (PromptRegistration reg : REGISTRATIONS.values()) {
            if (modId.equalsIgnoreCase(reg.modId())) {
                result.add(reg);
            }
        }
        return Collections.unmodifiableList(result);
    }

    /**
     * Clears all registrations. Intended primarily for testing suites.
     */
    public static void clear() {
        REGISTRATIONS.clear();
    }
}
