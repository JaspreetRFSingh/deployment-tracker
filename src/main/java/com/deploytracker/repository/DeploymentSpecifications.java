package com.deploytracker.repository;

import com.deploytracker.model.Deployment;
import com.deploytracker.model.DeploymentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

/**
 * Factory class for JPA Specifications against the Deployment entity.
 *
 * Each method returns a single, composable predicate. Callers (service layer)
 * combine them with {@code Specification.where(...).and(...)} to build
 * arbitrary filter queries without touching SQL or adding repository methods.
 */
public final class DeploymentSpecifications {

    private DeploymentSpecifications() {}

    public static Specification<Deployment> hasService(String service) {
        return (root, query, cb) -> service == null
                ? null
                : cb.equal(cb.lower(root.get("service")), service.toLowerCase());
    }

    public static Specification<Deployment> hasStatus(DeploymentStatus status) {
        return (root, query, cb) -> status == null
                ? null
                : cb.equal(root.get("status"), status);
    }

    /** Inclusive lower bound on timestamp */
    public static Specification<Deployment> after(Instant from) {
        return (root, query, cb) -> from == null
                ? null
                : cb.greaterThanOrEqualTo(root.get("timestamp"), from);
    }

    /** Inclusive upper bound on timestamp. */
    public static Specification<Deployment> before(Instant to) {
        return (root, query, cb) -> to == null
                ? null
                : cb.lessThanOrEqualTo(root.get("timestamp"), to);
    }
}
