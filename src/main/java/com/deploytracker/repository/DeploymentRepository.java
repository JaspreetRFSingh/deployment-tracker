package com.deploytracker.repository;

import com.deploytracker.model.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Data access layer for deployments.
 * Extends both JpaRepository (CRUD + pagination) and JpaSpecificationExecutor
 * (dynamic predicate composition via the Specification pattern). This keeps
 * filtering logic out of the repository.
 */
@Repository
public interface DeploymentRepository
        extends JpaRepository<Deployment, String>,
                JpaSpecificationExecutor<Deployment> {
}
