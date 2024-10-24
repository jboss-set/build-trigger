package org.jboss.set.model.json;

import java.util.ArrayList;
import java.util.List;

public class BuildJMSTriggerPayload {

    public String email;
    public String tag;
    public String gitRepo;
    public String projectVersion;
    public String commitSha;
    public List<String> streams;

    public static BuildJMSTriggerPayload from(BuildInfo buildInfo, String email) {
        BuildJMSTriggerPayload createBuildPayload = new BuildJMSTriggerPayload();

        createBuildPayload.email = email;
        createBuildPayload.tag = buildInfo.getTag();
        createBuildPayload.gitRepo = buildInfo.getGitRepo();
        createBuildPayload.projectVersion = buildInfo.getProjectVersion();
        createBuildPayload.commitSha = buildInfo.getCommitSha();
        createBuildPayload.streams = getStreams(buildInfo.getStreams());

        return createBuildPayload;
    }

    private static List<String> getStreams(List<String> streamsFromFronted) {
        List<String> streams = new ArrayList<>();
        streamsFromFronted.forEach(s -> streams.add(Stream.getJsonStreamName(s)));
        return streams;
    }

    @Override
    public String toString() {
        return "BuildJMSTriggerPayload{" +
                "email='" + email + '\'' +
                ", publicTag='" + tag + '\'' +
                ", gitRepo=" + gitRepo +
                ", version='" + projectVersion + '\'' +
                ", commitSha='" + commitSha + '\'' +
                ", streams='" + streams + '\'' +
                '}';
    }
}
