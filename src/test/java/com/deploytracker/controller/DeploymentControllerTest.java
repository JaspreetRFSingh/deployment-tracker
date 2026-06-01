package com.deploytracker.controller;

import com.deploytracker.model.Deployment;
import com.deploytracker.model.DeploymentStatus;
import com.deploytracker.repository.DeploymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class DeploymentControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired DeploymentRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();

        repository.save(deployment("t1", "billing-api",  DeploymentStatus.SUCCESS, 200));
        repository.save(deployment("t2", "billing-api",  DeploymentStatus.FAILED,  400));
        repository.save(deployment("t3", "user-service", DeploymentStatus.SUCCESS, 100));
    }

    // ── GET /deployments ────────────────────────────────────────────────────────

    @Test
    void listAll_returnsAllEvents() throws Exception {
        mockMvc.perform(get("/deployments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.meta.total", is(3)))
                .andExpect(jsonPath("$.data.data", hasSize(3)));
    }

    @Test
    void listByService_filtersCorrectly() throws Exception {
        mockMvc.perform(get("/deployments").param("service", "billing-api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.meta.total", is(2)))
                .andExpect(jsonPath("$.data.data[*].service", everyItem(is("billing-api"))));
    }

    @Test
    void listByStatus_filtersCorrectly() throws Exception {
        mockMvc.perform(get("/deployments").param("status", "failed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.meta.total", is(1)))
                .andExpect(jsonPath("$.data.data[0].status", is("failed")));
    }

    @Test
    void listByServiceAndStatus_filtersCorrectly() throws Exception {
        mockMvc.perform(get("/deployments")
                        .param("service", "billing-api")
                        .param("status", "success"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.meta.total", is(1)))
                .andExpect(jsonPath("$.data.data[0].id", is("t1")));
    }

    @Test
    void listWithNegativePage_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/deployments").param("page", "-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", notNullValue()));
    }

    // ── GET /deployments/:id ────────────────────────────────────────────────────

    @Test
    void getById_returnsCorrectDeployment() throws Exception {
        mockMvc.perform(get("/deployments/t1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id", is("t1")))
                .andExpect(jsonPath("$.data.service", is("billing-api")))
                .andExpect(jsonPath("$.data.status", is("success")))
                .andExpect(jsonPath("$.data.commit_sha", notNullValue()));
    }

    @Test
    void getById_unknownId_returns404() throws Exception {
        mockMvc.perform(get("/deployments/does-not-exist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", containsString("does-not-exist")));
    }

    // ── helpers ─────────────────────────────────────────────────────────────────

    private Deployment deployment(String id, String service, DeploymentStatus status, int duration) {
        return Deployment.builder()
                .id(id)
                .service(service)
                .status(status)
                .duration(duration)
                .timestamp(Instant.now())
                .commitSha("abc" + id)
                .build();
    }
}
