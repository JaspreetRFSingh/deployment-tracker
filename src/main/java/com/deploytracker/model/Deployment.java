package com.deploytracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;

/**
 * Core domain entity representing a single deployment event.
 */
@Entity
@Table(name = "deployments")
public class Deployment {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "service", nullable = false)
    private String service;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeploymentStatus status;

    /** Duration of the deployment in seconds. */
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "commit_sha", nullable = false)
    private String commitSha;

    protected Deployment() {}

    private Deployment(Builder builder) {
        this.id        = builder.id;
        this.service   = builder.service;
        this.status    = builder.status;
        this.duration  = builder.duration;
        this.timestamp = builder.timestamp;
        this.commitSha = builder.commitSha;
    }

    // --- Getters ---

    public String getId()        { return id; }
    public String getService()   { return service; }
    public DeploymentStatus getStatus() { return status; }
    public Integer getDuration() { return duration; }
    public Instant getTimestamp(){ return timestamp; }
    public String getCommitSha() { return commitSha; }

    // --- Builder ---

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String id;
        private String service;
        private DeploymentStatus status;
        private Integer duration;
        private Instant timestamp;
        private String commitSha;

        public Builder id(String id)               { this.id = id;             return this; }
        public Builder service(String service)     { this.service = service;   return this; }
        public Builder status(DeploymentStatus status) { this.status = status; return this; }
        public Builder duration(Integer duration)  { this.duration = duration; return this; }
        public Builder timestamp(Instant ts)       { this.timestamp = ts;      return this; }
        public Builder commitSha(String sha)       { this.commitSha = sha;     return this; }

        public Deployment build() {
            if (id == null || service == null || status == null
                    || duration == null || timestamp == null || commitSha == null) {
                throw new IllegalStateException("All fields are required to build a Deployment");
            }
            return new Deployment(this);
        }
    }
}
