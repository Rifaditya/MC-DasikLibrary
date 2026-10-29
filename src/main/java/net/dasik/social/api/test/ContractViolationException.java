// Copyright (C) 2026 Dasik (Rifaditya) | GNU GPLv3
package net.dasik.social.api.test;

import net.dasik.social.api.annotation.DasikApiStatus;

/**
 * Thrown when a mod fails any pillar of the studio architectural contract verification
 * (sidedness isolation, legacy version guard absence, naming standards, or entrypoint resolution).
 */
@DasikApiStatus.Public
public class ContractViolationException extends IllegalStateException {

    public ContractViolationException(String message) {
        super(message);
    }

    public ContractViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
