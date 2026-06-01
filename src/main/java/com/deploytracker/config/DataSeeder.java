package com.deploytracker.config;

import com.deploytracker.model.Deployment;
import com.deploytracker.model.DeploymentStatus;
import com.deploytracker.repository.DeploymentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Component
@Profile("!test")
public class DataSeeder implements CommandLineRunner {

    private final DeploymentRepository repository;

    public DataSeeder(DeploymentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count() > 0) return; // idempotent

        Instant now = Instant.now();

        List<Deployment> deployments = List.of(

            // ── billing-api (6 events) ──────────────────────────────────────────
            deploy("deploy_001", "billing-api", DeploymentStatus.SUCCESS,      185, now.minus(87, ChronoUnit.DAYS), "a1b2c3d"),
            deploy("deploy_002", "billing-api", DeploymentStatus.SUCCESS,      202, now.minus(74, ChronoUnit.DAYS), "b2c3d4e"),
            deploy("deploy_003", "billing-api", DeploymentStatus.FAILED,       320, now.minus(61, ChronoUnit.DAYS), "c3d4e5f"),
            deploy("deploy_004", "billing-api", DeploymentStatus.ROLLED_BACK,  410, now.minus(45, ChronoUnit.DAYS), "d4e5f6a"),
            deploy("deploy_005", "billing-api", DeploymentStatus.SUCCESS,      195, now.minus(22, ChronoUnit.DAYS), "e5f6a7b"),
            deploy("deploy_006", "billing-api", DeploymentStatus.FAILED,       540, now.minus(3,  ChronoUnit.DAYS), "f6a7b8c"),

            // ── user-service (5 events) ─────────────────────────────────────────
            deploy("deploy_007", "user-service", DeploymentStatus.SUCCESS,  95,  now.minus(83, ChronoUnit.DAYS), "1a2b3c4"),
            deploy("deploy_008", "user-service", DeploymentStatus.SUCCESS,  102, now.minus(67, ChronoUnit.DAYS), "2b3c4d5"),
            deploy("deploy_009", "user-service", DeploymentStatus.SUCCESS,  88,  now.minus(50, ChronoUnit.DAYS), "3c4d5e6"),
            deploy("deploy_010", "user-service", DeploymentStatus.FAILED,   310, now.minus(28, ChronoUnit.DAYS), "4d5e6f7"),
            deploy("deploy_011", "user-service", DeploymentStatus.SUCCESS,  99,  now.minus(7,  ChronoUnit.DAYS), "5e6f7a8"),

            // ── payment-service (5 events) ──────────────────────────────────────
            deploy("deploy_012", "payment-service", DeploymentStatus.SUCCESS,      230, now.minus(80, ChronoUnit.DAYS), "9z8y7x6"),
            deploy("deploy_013", "payment-service", DeploymentStatus.FAILED,       480, now.minus(65, ChronoUnit.DAYS), "8y7x6w5"),
            deploy("deploy_014", "payment-service", DeploymentStatus.ROLLED_BACK,  390, now.minus(52, ChronoUnit.DAYS), "7x6w5v4"),
            deploy("deploy_015", "payment-service", DeploymentStatus.SUCCESS,      210, now.minus(30, ChronoUnit.DAYS), "6w5v4u3"),
            deploy("deploy_016", "payment-service", DeploymentStatus.SUCCESS,      225, now.minus(10, ChronoUnit.DAYS), "5v4u3t2"),

            // ── auth-service (4 events) ─────────────────────────────────────────
            deploy("deploy_017", "auth-service", DeploymentStatus.SUCCESS,  60,  now.minus(78, ChronoUnit.DAYS), "aa1bb2cc"),
            deploy("deploy_018", "auth-service", DeploymentStatus.SUCCESS,  72,  now.minus(55, ChronoUnit.DAYS), "bb2cc3dd"),
            deploy("deploy_019", "auth-service", DeploymentStatus.FAILED,   290, now.minus(33, ChronoUnit.DAYS), "cc3dd4ee"),
            deploy("deploy_020", "auth-service", DeploymentStatus.SUCCESS,  65,  now.minus(5,  ChronoUnit.DAYS), "dd4ee5ff"),

            // ── notification-service (4 events) ────────────────────────────────
            deploy("deploy_021", "notification-service", DeploymentStatus.SUCCESS,  140, now.minus(88, ChronoUnit.DAYS), "n1o2t3i"),
            deploy("deploy_022", "notification-service", DeploymentStatus.SUCCESS,  155, now.minus(60, ChronoUnit.DAYS), "o2t3i4f"),
            deploy("deploy_023", "notification-service", DeploymentStatus.FAILED,   600, now.minus(35, ChronoUnit.DAYS), "t3i4f5y"),
            deploy("deploy_024", "notification-service", DeploymentStatus.SUCCESS,  148, now.minus(14, ChronoUnit.DAYS), "i4f5y6z"),

            // ── order-service (4 events) ────────────────────────────────────────
            deploy("deploy_025", "order-service", DeploymentStatus.SUCCESS,      175, now.minus(85, ChronoUnit.DAYS), "or1de2rs"),
            deploy("deploy_026", "order-service", DeploymentStatus.SUCCESS,      182, now.minus(58, ChronoUnit.DAYS), "de2rs3er"),
            deploy("deploy_027", "order-service", DeploymentStatus.ROLLED_BACK,  430, now.minus(40, ChronoUnit.DAYS), "rs3er4vi"),
            deploy("deploy_028", "order-service", DeploymentStatus.SUCCESS,      168, now.minus(18, ChronoUnit.DAYS), "er4vi5ce"),

            // ── inventory-service (4 events) ────────────────────────────────────
            deploy("deploy_029", "inventory-service", DeploymentStatus.SUCCESS,  300, now.minus(82, ChronoUnit.DAYS), "in1ve2nt"),
            deploy("deploy_030", "inventory-service", DeploymentStatus.SUCCESS,  315, now.minus(56, ChronoUnit.DAYS), "ve2nt3or"),
            deploy("deploy_031", "inventory-service", DeploymentStatus.FAILED,   520, now.minus(38, ChronoUnit.DAYS), "nt3or4y5"),
            deploy("deploy_032", "inventory-service", DeploymentStatus.SUCCESS,  295, now.minus(12, ChronoUnit.DAYS), "or4y5z6a"),

            // ── api-gateway (2 events, with one currently in_progress) ──────────
            deploy("deploy_033", "api-gateway", DeploymentStatus.SUCCESS,      45, now.minus(70, ChronoUnit.DAYS), "gw1at2e3"),
            deploy("deploy_034", "api-gateway", DeploymentStatus.IN_PROGRESS,  0,  now.minus(0,  ChronoUnit.HOURS), "gw2at3e4")
        );

        repository.saveAll(deployments);
    }

    private Deployment deploy(String id, String service, DeploymentStatus status,
                               int duration, Instant timestamp, String commitSha) {
        return Deployment.builder()
                .id(id)
                .service(service)
                .status(status)
                .duration(duration)
                .timestamp(timestamp)
                .commitSha(commitSha)
                .build();
    }
}
