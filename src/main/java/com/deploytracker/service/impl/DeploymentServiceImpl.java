package com.deploytracker.service.impl;

import com.deploytracker.dto.DeploymentDto;
import com.deploytracker.dto.PagedResponse;
import com.deploytracker.exception.DeploymentNotFoundException;
import com.deploytracker.model.Deployment;
import com.deploytracker.model.DeploymentStatus;
import com.deploytracker.repository.DeploymentRepository;
import com.deploytracker.repository.DeploymentSpecifications;
import com.deploytracker.service.DeploymentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class DeploymentServiceImpl implements DeploymentService {

    private static final int MAX_PAGE_SIZE = 100;

    private final DeploymentRepository repository;

    public DeploymentServiceImpl(DeploymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public PagedResponse<DeploymentDto> listDeployments(String service, DeploymentStatus status, int page, int size) {
        validatePagination(page, size);

        // Compose filter predicates — null values are treated as "no restriction"
        Specification<Deployment> spec = Specification
                .where(DeploymentSpecifications.hasService(service))
                .and(DeploymentSpecifications.hasStatus(status));

        PageRequest pageable = PageRequest.of(page, Math.min(size, MAX_PAGE_SIZE),
                Sort.by(Sort.Direction.DESC, "timestamp"));

        Page<Deployment> resultPage = repository.findAll(spec, pageable);

        List<DeploymentDto> dtos = resultPage.getContent()
                .stream()
                .map(DeploymentDto::from)
                .toList();

        return new PagedResponse<>(dtos, resultPage.getTotalElements(), page, size);
    }

    @Override
    public DeploymentDto getDeployment(String id) {
        Deployment deployment = repository.findById(id)
                .orElseThrow(() -> new DeploymentNotFoundException(id));
        return DeploymentDto.from(deployment);
    }

    private void validatePagination(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("Page index must not be negative");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Page size must be at least 1");
        }
    }
}
