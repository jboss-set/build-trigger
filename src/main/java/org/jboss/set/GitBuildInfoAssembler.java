package org.jboss.set;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.set.client.GithubRestClient;
import org.jboss.set.client.GitlabCeeRestClient;
import org.jboss.set.client.GitlabRestClient;
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

    private final static String GITLAB_API = "/api/v4/projects/";
    private final static String GITLAB_TAGS = "/repository/tags/";

    private final static String COMMIT = "commit";
    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String SHA = "sha";
    private final static String OBJECT = "object";
    private final static String TAG = "tag";

    public BuildInfo constructBuildFromURL(URL tagUrl) {
        String urlPath = tagUrl.getPath();
        String[] splitURL = urlPath.split("/");
        if (splitURL.length < 3) {
            // Valid URL is always <host>/<repo>/<project>/../<tag>
            // This applies to Github and Gitlab
            logger.warn("Invalid URL, returning null");
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
        logger.warnf("%s is unsupported, only Github and Gitlab are supported at this moment.", tagUrl.getHost());
        return null;
    }

    private BuildInfo constructGithubBuild(String[] splitUrl, URL tagUrl) {
        String codeSpace = splitUrl[1];
        String repository = splitUrl[2];
        String version = splitUrl[splitUrl.length - 1];

        JsonNode refsJson = githubRestClient.getRefsInfo(codeSpace, repository, version, githubKey);
        if (refsJson != null) {
            String tagSHA = refsJson.get(OBJECT).get(SHA).textValue();
            JsonNode tagJson = githubRestClient.getTagInfo(codeSpace, repository, tagSHA, githubKey);
            if (tagJson != null) {
                BuildInfo buildInfo = new BuildInfo();
                buildInfo.tag = tagUrl.toString();
                buildInfo.commitSha = tagJson.get(OBJECT).get(SHA).textValue();
                buildInfo.gitRepo = tagUrl.getProtocol() + "://" +  tagUrl.getHost() + "/" + codeSpace + "/" + repository;
                buildInfo.projectVersion = tagJson.get(TAG).textValue();;

                logger.infof("Build created with following parameters - sha: %s, version: %s, link: %s", buildInfo.commitSha, buildInfo.projectVersion, buildInfo.tag);
                return buildInfo;
            }
        }
        logger.warnf("Failed to create TagInfo for %s", tagUrl.toString());
        return null;
    }

    private BuildInfo constructGitlabBuild(String[] splitUrl, URL tagUrl, boolean isGitlabCee) {
        String codeSpace = splitUrl[1];
        String repository = splitUrl[2];
        String version = splitUrl[splitUrl.length - 1];

        JsonNode tagJson = isGitlabCee ? gitlabCeeRestClient.getTagInfo(codeSpace + "/" + repository, version, "Bearer " + gitlabKey) : gitlabRestClient.getTagInfo(codeSpace + "/" + repository, version);
        if (tagJson != null) {
            BuildInfo buildInfo = new BuildInfo();
            buildInfo.tag = tagUrl.toString();
            buildInfo.commitSha = tagJson.get(COMMIT).get(ID).textValue();
            buildInfo.gitRepo = tagUrl.getProtocol() + "://" +  tagUrl.getHost() + "/" + codeSpace + "/" + repository;;
            buildInfo.projectVersion = tagJson.get(NAME).textValue();

            logger.infof("Build created with following parameters - sha: %s, version: %s, link: %s", buildInfo.commitSha, buildInfo.projectVersion, buildInfo.tag);
            return buildInfo;
        }
        logger.warnf("Failed to create TagInfo for %s", tagUrl.toString());
        return null;
    }
}
