package org.jboss.set;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.set.client.GithubRestClient;
import org.jboss.set.client.GitlabCeeRestClient;
import org.jboss.set.client.GitlabRestClient;
import org.jboss.set.exception.GitRestClientException;
import org.jboss.set.model.json.BuildInfo;

import java.net.URL;

@ApplicationScoped
public class GitBuildInfoAssembler {

    @ConfigProperty(name = "github-key")
    private String githubKey;

    @ConfigProperty(name = "gitlab-key")
    private String gitlabKey;

    @Inject
    @RestClient
    GitlabRestClient gitlabRestClient;

    @Inject
    @RestClient
    GithubRestClient githubRestClient;

    @Inject
    @RestClient
    GitlabCeeRestClient gitlabCeeRestClient;

    Logger logger = Logger.getLogger(GitBuildInfoAssembler.class);

    private static final String COMMIT = "commit";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String SHA = "sha";
    private static final String OBJECT = "object";
    private static final String TAG = "tag";
    private static final String TYPE = "type";

    public BuildInfo constructBuildFromURL(URL tagUrl) {
        String urlPath = tagUrl.getPath();
        String[] splitURL = urlPath.split("/");
        if (splitURL.length < 3) {
            // Valid URL is always <host>/<repo>/<project>/../<tag>
            // This applies to Github and Gitlab
            logger.warnf("Invalid URL format provided. The URL must be in the format <host>/<repo>/<project>/../<tag> for Gitlab/Github. "
                    + "URL: " + tagUrl + ". Returning null.");
            return null;
        }
        if (tagUrl.getHost().equals("gitlab.cee.redhat.com")) {
            return constructGitlabBuild(splitURL, tagUrl, true);
        }
        if (tagUrl.getHost().contains("github.")) {
            return constructGithubBuild(splitURL, tagUrl);
        }
        if (tagUrl.getHost().contains("gitlab.")) {
            return constructGitlabBuild(splitURL, tagUrl, false);
        }
        logger.warnf("Unsupported host: %s. Currently, only GitHub and GitLab hosts are supported.", tagUrl.getHost());
        return null;
    }

    private BuildInfo constructGithubBuild(String[] splitUrl, URL tagUrl) {
        String codeSpace = splitUrl[1];
        String repository = splitUrl[2];
        String version = splitUrl[splitUrl.length - 1];
        JsonNode refsJson;

        try {
            refsJson = githubRestClient.getRefsInfo(codeSpace, repository, version, "Bearer " + githubKey);
        } catch (ClientWebApplicationException e) {
            logger.errorf("Failed to retrieve reference info for URL: %s from GitHub. Exception: %s", tagUrl.toString(), e.getMessage());
            throw new GitRestClientException("Failed to retrieve refs info from Github. " + e.getMessage(), e, e.getResponse());
        }

        if (refsJson == null) {
            logger.warnf("GitHub API returned null for reference info for URL: %s", tagUrl.toString());
            return null;
        }

        String refsType = refsJson.get(OBJECT).get(TYPE).textValue();
        String commitSHA = refsJson.get(OBJECT).get(SHA).textValue();
        if (refsType.equals(TAG)) {
            JsonNode tagJson;
            try {
                tagJson = githubRestClient.getTagInfo(codeSpace, repository, commitSHA, "Bearer " + githubKey);
            } catch (ClientWebApplicationException e) {
                logger.errorf("Failed to retrieve tag info for URL: %s from GitHub. Exception: %s", tagUrl.toString(), e.getMessage());
                throw new GitRestClientException("Failed to retrieve tag info from Github. " + e.getMessage(), e, e.getResponse());
            }

            if (tagJson == null) {
                logger.warnf("GitHub API returned null for tag info for URL: %s", tagUrl.toString());
                return null;
            }
            commitSHA = tagJson.path(OBJECT).path(SHA).asText();  // Update SHA from tag info
        }
        return buildBuildInfo(tagUrl, codeSpace, repository, version, commitSHA);
    }

    private BuildInfo constructGitlabBuild(String[] splitUrl, URL tagUrl, boolean isGitlabCee) {
        String codeSpace = splitUrl[1];
        String repository = splitUrl[2];
        String version = splitUrl[splitUrl.length - 1];
        JsonNode tagJson;

        try {
            tagJson = isGitlabCee ?
                    gitlabCeeRestClient.getTagInfo(codeSpace + "/" + repository, version, "Bearer " + gitlabKey) :
                    gitlabRestClient.getTagInfo(codeSpace + "/" + repository, version);
        } catch (ClientWebApplicationException e) {
            logger.errorf("Failed to retrieve tag info for URL: %s from GitLab. Exception: %s", tagUrl.toString(), e.getMessage());
            throw new GitRestClientException("Failed to retrieve tag info from Gitlab. " + e.getMessage(), e, e.getResponse());
        }

        if (tagJson != null) {
            return buildBuildInfo(tagUrl, codeSpace, repository, tagJson.get(NAME).textValue(), tagJson.get(COMMIT).get(ID).textValue());
        }
        logger.warnf("GitLab API returned null for tag info for URL: %s", tagUrl.toString());
        return null;
    }

    private BuildInfo buildBuildInfo(URL tagUrl, String codespace, String repository, String version, String commitSHA) {
        BuildInfo buildInfo = new BuildInfo.Builder()
                .tag(tagUrl.toString())
                .commitSha(commitSHA)
                .gitRepo(String.format("%s://%s/%s/%s", tagUrl.getProtocol(), tagUrl.getHost(), codespace, repository))
                .projectVersion(version)
                .build();

        logger.infof("Build created with following parameters - sha: %s, version: %s, link: %s", buildInfo.getCommitSha(), buildInfo.getProjectVersion(), buildInfo.getTag());
        return buildInfo;
    }
}
