package com.deploytracker.controller;

import com.deploytracker.dto.ApiResponse;
import com.deploytracker.dto.DeploymentDto;
import com.deploytracker.dto.PagedResponse;
import com.deploytracker.model.DeploymentStatus;
import com.deploytracker.service.DeploymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller exposing deployment event endpoints.
 */
@RestController
@RequestMapping("/deployments")
public class DeploymentController {

    private final DeploymentService deploymentService;

    public DeploymentController(DeploymentService deploymentService) {
        this.deploymentService = deploymentService;
    }

    /**
     * List deployments with optional filtering and pagination.
     */
    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<DeploymentDto>>> listDeployments(
            @RequestParam(required = false) String service,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        DeploymentStatus statusEnum = DeploymentStatus.fromString(status);
        PagedResponse<DeploymentDto> result = deploymentService.listDeployments(service, statusEnum, page, size);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * Retrieve a single deployment by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeploymentDto>> getDeployment(@PathVariable String id) {
        DeploymentDto dto = deploymentService.getDeployment(id);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }
}
