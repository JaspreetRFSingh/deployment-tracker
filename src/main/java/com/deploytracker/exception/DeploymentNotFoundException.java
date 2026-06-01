package com.deploytracker.exception;

/**
 * Thrown when a deployment with the requested ID does not exist.
 * Mapped to HTTP 404 in {@link GlobalExceptionHandler}.
 */
public class DeploymentNotFoundException extends RuntimeException {

    public DeploymentNotFoundException(String id) {
        super("Deployment not found: " + id);
    }
}
