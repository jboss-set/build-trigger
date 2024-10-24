package org.jboss.set.model.json;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

class BuildInfoTest {

    private static final String VALID_TAG = "https://github.com/test/repo";
    private static final String VALID_REPO = "https://github.com/test/repo.git";
    private static final String VALID_VERSION = "1.0.0";
    private static final String VALID_COMMIT_SHA = "61ec86095de795f5fb817a7cc824d8d7cfb9ae51";
    private static final List<String> VALID_STREAMS = List.of(Stream.EAP_7_3_X.frontEnd);

    private static final String INVALID_TAG = "invalid-tag";
    private static final String INVALID_REPO = "invalid-url";
    private static final String INVALID_COMMIT_SHA = "invalidSha";

    private final Validator validator;

    @Test
    void testValidBuildInfo() {
        Assertions.assertDoesNotThrow(() -> {
            new BuildInfo.Builder()
                    .tag(VALID_TAG)
                    .gitRepo(VALID_REPO)
                    .projectVersion(VALID_VERSION)
                    .commitSha(VALID_COMMIT_SHA)
                    .streams(VALID_STREAMS)
                    .build();
        }, "Expected no exception for valid BuildInfo creation");
    }

    @Test
    void testInvalidTag() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BuildInfo.Builder()
                    .tag(INVALID_TAG)
                    .gitRepo(VALID_REPO)
                    .projectVersion(VALID_VERSION)
                    .commitSha(VALID_COMMIT_SHA)
                    .streams(VALID_STREAMS)
                    .build();
        });

        Assertions.assertEquals("Tag must start with https://", thrown.getMessage());
    }

    @Test
    void testInvalidCommitSha() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BuildInfo.Builder()
                    .tag(VALID_TAG)
                    .gitRepo(VALID_REPO)
                    .projectVersion(VALID_VERSION)
                    .commitSha(INVALID_COMMIT_SHA)
                    .streams(VALID_STREAMS)
                    .build();
        });

        Assertions.assertEquals("Commit SHA must be a valid hexadecimal string (7 to 40 characters).", thrown.getMessage());
    }

    @Test
    void testModifyValidBuildInfoWithInvalidTag() {
        BuildInfo originalBuildInfo = new BuildInfo.Builder()
                .tag(VALID_TAG)
                .gitRepo(VALID_REPO)
                .projectVersion(VALID_VERSION)
                .commitSha(VALID_COMMIT_SHA)
                .streams(VALID_STREAMS)
                .build();

        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            originalBuildInfo.toBuilder()
                    .gitRepo(INVALID_REPO)
                    .build();
        });

        Assertions.assertEquals("Repository must start with https://", thrown.getMessage());
    }

    @Test
    void testExceedingStreamLimit() {
        IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new BuildInfo.Builder()
                    .tag(VALID_TAG)
                    .gitRepo(VALID_REPO)
                    .projectVersion(VALID_VERSION)
                    .commitSha(VALID_COMMIT_SHA)
                    .streams(Collections.nCopies(Stream.values().length + 1, Stream.EAP_7_3_X.backEnd))
                    .build();
        });

        Assertions.assertTrue(thrown.getMessage().contains("The maximum number of streams allowed is"));
    }

    // Test field annotations validation

    @Test
    void testAnnotationValidBuildInfo() {
        BuildInfo buildInfo = new TestBuilder()
                .tag(VALID_TAG)
                .gitRepo(VALID_REPO)
                .projectVersion(VALID_VERSION)
                .commitSha(VALID_COMMIT_SHA)
                .streams(VALID_STREAMS)
                .build();

        var violations = validator.validate(buildInfo);
        Assertions.assertTrue(violations.isEmpty(), "Expected no validation violations for valid BuildInfo");
    }

    @Test
    void testAnnotationInvalidTag() {
        BuildInfo buildInfo = new TestBuilder()
                    .tag(INVALID_TAG)
                    .gitRepo(VALID_REPO)
                    .projectVersion(VALID_VERSION)
                    .commitSha(VALID_COMMIT_SHA)
                    .streams(VALID_STREAMS)
                    .build();

        var violations = validator.validate(buildInfo);
        Assertions.assertFalse(violations.isEmpty(), "Expected validation violations for invalid tag");
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Tag must start with https://", violations.iterator().next().getMessage());
    }

    @Test
    void testAnnotationInvalidCommitSha() {
        BuildInfo buildInfo = new TestBuilder()
                .tag(VALID_TAG)
                .gitRepo(VALID_REPO)
                .projectVersion(VALID_VERSION)
                .commitSha(INVALID_COMMIT_SHA)
                .streams(VALID_STREAMS)
                .build();

        var violations = validator.validate(buildInfo);
        Assertions.assertFalse(violations.isEmpty(), "Expected validation violations for invalid commit SHA");
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Commit SHA must be a valid hexadecimal string (7 to 40 characters).", violations.iterator().next().getMessage());
    }

    @Test
    void testAnnotationInvalidStreamsSize() {
        BuildInfo buildInfo = new TestBuilder()
                .tag(VALID_TAG)
                .gitRepo(VALID_REPO)
                .projectVersion(VALID_VERSION)
                .commitSha(VALID_COMMIT_SHA)
                .streams(Collections.nCopies(Stream.values().length + 1, Stream.EAP_7_3_X.backEnd))
                .build();

        var violations = validator.validate(buildInfo);
        Assertions.assertFalse(violations.isEmpty(), "Expected validation violations for exceeding maximum streams");
        Assertions.assertEquals(1, violations.size());
        Assertions.assertTrue(violations.iterator().next().getMessage().contains("The maximum number of streams allowed is"));
    }

    @Test
    void testEmptyProjectVersion() {
        BuildInfo buildInfo = new TestBuilder()
                .tag(VALID_TAG)
                .gitRepo(VALID_REPO)
                .projectVersion("")
                .commitSha(VALID_COMMIT_SHA)
                .streams(VALID_STREAMS)
                .build();

        var violations = validator.validate(buildInfo);
        Assertions.assertFalse(violations.isEmpty(), "Expected validation violations for blank projectVersion");
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("must not be blank", violations.iterator().next().getMessage());
    }

    public BuildInfoTest() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    // Custom builder for testing that bypasses Builder validation
    private static class TestBuilder extends BuildInfo.Builder {
        @Override
        public BuildInfo build() {
            // Directly construct and return a BuildInfo object without validation
            return new BuildInfo(this);
        }
    }
}
