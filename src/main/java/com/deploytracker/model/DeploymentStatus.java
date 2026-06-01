package com.deploytracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Valid lifecycle statuses for a deployment event.
 */
public enum DeploymentStatus {
    SUCCESS,
    FAILED,
    IN_PROGRESS,
    ROLLED_BACK;

    /** Serializes to lowercase for JSON responses (e.g. "in_progress"). */
    @JsonValue
    public String toJsonValue() {
        return name().toLowerCase();
    }

    /**
     * Case-insensitive factory used by Jackson for deserialization and by the
     * controller when parsing query parameters.
     */
    @JsonCreator
    public static DeploymentStatus fromString(String value) {
        if (value == null) return null;
        try {
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid deployment status: '" + value + "'. " +
                    "Accepted values: success, failed, in_progress, rolled_back");
        }
    }
}