package io.xstefank;

import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Argument;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.core.Field;
import io.smallrye.graphql.client.core.Operation;
import io.smallrye.graphql.client.core.Variable;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClientBuilder;
import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import io.xstefank.model.graphql.SearchType;
import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.event.PullRequestPayload;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.LabelService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Argument.arg;
import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;
import static io.smallrye.graphql.client.core.Variable.var;

@Path("/ping")
public class TestResource {

    @ConfigProperty(name = "github.token")
    String gitHubToken;

    @Inject
    BuildTrigger buildTrigger;

    @GET
    public void testUmb() throws ExecutionException, InterruptedException {

        final DynamicGraphQLClient dynamicClient = DynamicGraphQLClientBuilder.newBuilder()
            .url("https://api.github.com/graphql").header("Authorization", "bearer " + gitHubToken)
            .build();



        Document query = document(
            operation(
                field("search(query: \"repo:xstefank/artemis-wildfly-integration is:pr merged:>2021-01-01 label:release\", type: ISSUE, last: 100) {\n" +
                    "    edges {\n" +
                    "      node {\n" +
                    "        ... on PullRequest {\n" +
                    "          number\n" +
                    "          title\n" +
                    "          repository {\n" +
                    "            nameWithOwner\n" +
                    "          }\n" +
                    "          createdAt\n" +
                    "          mergedAt\n" +
                    "          url\n" +
                    "          changedFiles\n" +
                    "          additions\n" +
                    "          deletions\n" +
                    "          labels(last: 100) {\n" +
                    "            edges {\n" +
                    "              node {\n" +
                    "                name\n" +
                    "                color\n" +
                    "              }\n" +
                    "            }\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }")
            )
        );

        System.out.println(query.build());

        final Response response = dynamicClient.executeSync(query);
        System.out.println(response);
        System.out.println(response.getData());

//        GitHubClient gitHubClient = new GitHubClient();
//        gitHubClient.setOAuth2Token(gitHubToken);
//        final PullRequestService pullRequestService = new PullRequestService(gitHubClient);
//        final List<PullRequest> pullRequests = pullRequestService.getPullRequests(new RepositoryId("xstefank", "artemis-wildfly-integration"), "closed");
////        pullRequests.forEach(pullRequest -> {
////            System.out.println(pullRequest.getMergedAt());
////            System.out.println(pullRequest.getUser().getLogin());
////
////        });
//
//        final IssueService issueService = new IssueService(gitHubClient);
//        final List<Issue> issues = issueService.getIssues(new RepositoryId("xstefank", "artemis-wildfly-integration"), Map.of("state", "closed"));
//        issues.forEach(issue -> {
//            System.out.println(issue.getLabels());
//        });
    }
}
