package com.deploytracker.service;

import com.deploytracker.dto.DeploymentDto;
import com.deploytracker.dto.PagedResponse;
import com.deploytracker.model.DeploymentStatus;

/**
 * Contract for deployment retrieval operations.
 * Keeping this as an interface lets us swap implementations (e.g., cache-backed,
 * remote data source) and simplifies unit testing via mocks.
 */
public interface DeploymentService {

    /**
     * Returns a paginated, optionally filtered list of deployments.
     * @param service filter by service name (null = all services)
     * @param status  filter by status (null = all statuses)
     * @param page    zero-based page index
     * @param size    number of items per page (max 100)
     */
    PagedResponse<DeploymentDto> listDeployments(String service, DeploymentStatus status, int page, int size);

    /**
     * Returns a single deployment by its unique ID.
     */
    DeploymentDto getDeployment(String id);
}
