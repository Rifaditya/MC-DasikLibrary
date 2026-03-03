/*
 * Decompiled with CFR 0.152.
 */
package net.dasik.social.api;

import java.util.Objects;

public class SignalType {
    private final String id;
    private final double defaultRange;
    private final double maxRange;
    private final int priority;
    public static final SignalType DANGER = new SignalType("danger", 32.0, 64.0, 100);
    public static final SignalType OWNER_ACTION = new SignalType("owner_action", 16.0, 32.0, 50);
    public static final SignalType THUNDER = new SignalType("thunder", 1024.0, Double.MAX_VALUE, 80);
    public static final SignalType DEATH_CRY = new SignalType("death_cry", 64.0, 128.0, 90);
    public static final SignalType FOOD_DETECTED = new SignalType("food_detected", 8.0, 16.0, 20);
    public static final SignalType SOCIAL_INVITE = new SignalType("social_invite", 16.0, 32.0, 30);

    public SignalType(String id, double defaultRange, double maxRange, int priority) {
        this.id = id;
        this.defaultRange = defaultRange;
        this.maxRange = maxRange;
        this.priority = priority;
    }

    public String getId() {
        return this.id;
    }

    public double getDefaultRange() {
        return this.defaultRange;
    }

    public double getMaxRange() {
        return this.maxRange;
    }

    public int getPriority() {
        return this.priority;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        SignalType that = (SignalType)o;
        return Objects.equals(this.id, that.id);
    }

    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String toString() {
        return "SignalType{" + this.id + "}";
    }
}

