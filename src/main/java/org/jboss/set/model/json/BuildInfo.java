package org.jboss.set.model.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

@JsonDeserialize(builder = BuildInfo.Builder.class)
public class BuildInfo {

    @NotBlank
    @Pattern(regexp = "^https://.+$", message = "Tag must start with https://")
    private final String tag;

    @NotBlank
    @Pattern(regexp = "^https://.+$", message = "Repository must start with https://")
    private final String gitRepo;

    @NotBlank
    private final String projectVersion;

    @NotBlank
    @Pattern(regexp = "^[a-fA-F0-9]{7,40}$", message = "Commit SHA must be a valid hexadecimal string (7 to 40 characters).")
    private final String commitSha;

    // Maximum of 5 streams allowed, based on Stream.values().length.
    @Size(max = 5, message = "The maximum number of streams allowed is 5.")
    private final List<String> streams;

    BuildInfo(Builder builder) {
        this.tag = builder.tag;
        this.gitRepo = builder.gitRepo;
        this.projectVersion = builder.projectVersion;
        this.commitSha = builder.commitSha;
        this.streams = builder.streams;
    }

    public Builder toBuilder() {
        return new Builder()
                .tag(this.tag)
                .gitRepo(this.gitRepo)
                .projectVersion(this.projectVersion)
                .commitSha(this.commitSha)
                .streams(this.streams);
    }

    public @NotNull String getTag() {
        return tag;
    }

    public @NotNull String getGitRepo() {
        return gitRepo;
    }

    public @NotNull String getProjectVersion() {
        return projectVersion;
    }

    public @NotNull String getCommitSha() {
        return commitSha;
    }

    public List<String> getStreams() {
        return streams;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private String tag;
        private String gitRepo;
        private String projectVersion;
        private String commitSha;
        private List<String> streams;

        public Builder tag(String tag) {
            this.tag = tag.trim();
            return this;
        }

        public Builder gitRepo(String gitRepo) {
            this.gitRepo = gitRepo.trim();
            return this;
        }

        public Builder projectVersion(String projectVersion) {
            this.projectVersion = projectVersion.trim();
            return this;
        }

        public Builder commitSha(String commitSha) {
            this.commitSha = commitSha.trim();
            return this;
        }

        public Builder streams(List<String> streams) {
            this.streams = streams;
            return this;
        }

        public BuildInfo build() {
            validateFields();
            return new BuildInfo(this);
        }

        private void validateFields() {
            if (tag == null || !tag.startsWith("https://")) {
                throw new IllegalArgumentException("Tag must start with https://");
            }

            if (gitRepo == null || !gitRepo.startsWith("https://")) {
                throw new IllegalArgumentException("Repository must start with https://");
            }

            if (commitSha == null || !commitSha.matches("^[a-fA-F0-9]{7,40}$")) {
                throw new IllegalArgumentException("Commit SHA must be a valid hexadecimal string (7 to 40 characters).");
            }

            if (streams != null && streams.size() > Stream.values().length) {
                throw new IllegalArgumentException(String.format("The maximum number of streams allowed is %d.", Stream.values().length));
            }
        }
    }

    @Override
    public String toString() {
        return "BuildInfo{" +
            "tag='" + tag + '\'' +
            ", gitRepo=" + gitRepo +
            ", projectVersion='" + projectVersion + '\'' +
            ", commitSha='" + commitSha + '\'' +
            ", streams='" + streams + '\'' +
            '}';
    }
}
