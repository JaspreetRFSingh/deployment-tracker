package com.deploytracker.dto;

import com.deploytracker.model.Deployment;
import com.deploytracker.model.DeploymentStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Public-facing DTO for a deployment event.
 */
public class DeploymentDto {

    private final String id;
    private final String service;
    private final DeploymentStatus status;
    private final Integer duration;
    private final String timestamp;

    @JsonProperty("commit_sha")
    private final String commitSha;

    private DeploymentDto(Builder builder) {
        this.id        = builder.id;
        this.service   = builder.service;
        this.status    = builder.status;
        this.duration  = builder.duration;
        this.timestamp = builder.timestamp;
        this.commitSha = builder.commitSha;
    }

    /** Converts a domain entity to its DTO representation. */
    public static DeploymentDto from(Deployment d) {
        return DeploymentDto.builder()
                .id(d.getId())
                .service(d.getService())
                .status(d.getStatus())
                .duration(d.getDuration())
                .timestamp(d.getTimestamp().toString())
                .commitSha(d.getCommitSha())
                .build();
    }

    // --- Getters ---

    public String getId()        { return id; }
    public String getService()   { return service; }
    public DeploymentStatus getStatus() { return status; }
    public Integer getDuration() { return duration; }
    public String getTimestamp() { return timestamp; }
    public String getCommitSha() { return commitSha; }

    // --- Builder ---

    public static Builder builder() { return new Builder(); }

    public static final class Builder {
        private String id;
        private String service;
        private DeploymentStatus status;
        private Integer duration;
        private String timestamp;
        private String commitSha;

        public Builder id(String id)               { this.id = id;             return this; }
        public Builder service(String service)     { this.service = service;   return this; }
        public Builder status(DeploymentStatus status) { this.status = status; return this; }
        public Builder duration(Integer duration)  { this.duration = duration; return this; }
        public Builder timestamp(String ts)        { this.timestamp = ts;      return this; }
        public Builder commitSha(String sha)       { this.commitSha = sha;     return this; }

        public DeploymentDto build() { return new DeploymentDto(this); }
    }
}
