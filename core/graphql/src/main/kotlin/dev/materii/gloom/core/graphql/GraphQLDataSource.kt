package dev.materii.gloom.core.graphql

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.cache.normalized.doNotStore
import dev.materii.gloom.core.graphql.response.GraphQLResponse
import dev.materii.gloom.core.graphql.response.response
import dev.materii.gloom.core.graphql.type.IssueState
import dev.materii.gloom.core.graphql.type.PullRequestState
import dev.materii.gloom.core.graphql.type.ReactionContent
import dev.materii.gloom.core.graphql.type.TrendingPeriod
import dev.materii.gloom.core.graphql.util.toOptional

interface GraphQLDataSource {

    // Account

    suspend fun getAccountInfo(token: String): GraphQLResponse<AccountInfoQuery.Data>

    suspend fun identify(): GraphQLResponse<IdentifyQuery.Data>

    // Starrable

    suspend fun star(id: String): GraphQLResponse<StarMutation.Data>

    suspend fun unstar(id: String): GraphQLResponse<UnstarMutation.Data>

    // Reactable

    suspend fun react(id: String, reaction: ReactionContent): GraphQLResponse<ReactMutation.Data>

    suspend fun unreact(id: String, reaction: ReactionContent): GraphQLResponse<UnreactMutation.Data>

    // Profile

    suspend fun getCurrentProfile(): GraphQLResponse<ProfileQuery.Data>

    suspend fun getProfile(login: String): GraphQLResponse<UserProfileQuery.Data>

    suspend fun getUserRepositories(login: String, after: String? = null, count: Int = 30): GraphQLResponse<RepoListQuery.Data>

    suspend fun getStarredRepositories(login: String, after: String? = null, count: Int = 30): GraphQLResponse<StarredReposQuery.Data>

    suspend fun getJoinedOrgs(login: String, after: String? = null, count: Int = 30): GraphQLResponse<JoinedOrgsQuery.Data>

    suspend fun getFollowers(login: String, after: String? = null, count: Int = 30): GraphQLResponse<FollowersQuery.Data>

    suspend fun getFollowing(login: String, after: String? = null, count: Int = 30): GraphQLResponse<FollowingQuery.Data>

    suspend fun getSponsoring(login: String, after: String? = null, count: Int = 30): GraphQLResponse<SponsoringQuery.Data>

    suspend fun followUser(id: String): GraphQLResponse<FollowUserMutation.Data>

    suspend fun unfollowUser(id: String): GraphQLResponse<UnfollowUserMutation.Data>

    // Feed

    suspend fun getFeed(after: String? = null): GraphQLResponse<FeedQuery.Data>

    suspend fun getTrending(period: TrendingPeriod = TrendingPeriod.DAILY): GraphQLResponse<TrendingQuery.Data>

    // Repositories

    suspend fun getRepoName(owner: String, name: String): GraphQLResponse<RepoNameQuery.Data>

    suspend fun getRepoDetails(owner: String, name: String): GraphQLResponse<RepoDetailsQuery.Data>

    suspend fun getRepoLicense(owner: String, name: String): GraphQLResponse<RepoLicenseQuery.Data>

    suspend fun prefetchRepoTree(owner: String, name: String): GraphQLResponse<RepoTreePrefetchQuery.Data>

    suspend fun getRepoIssues(
        owner: String,
        name: String,
        after: String? = null,
        states: Set<IssueState> = setOf(IssueState.OPEN)
    ): GraphQLResponse<RepoIssuesQuery.Data>

    suspend fun getRepoPullRequests(
        owner: String,
        name: String,
        after: String? = null,
        states: List<PullRequestState> = listOf(PullRequestState.OPEN)
    ): GraphQLResponse<RepoPullRequestsQuery.Data>

    suspend fun getRepoReleases(
        owner: String,
        name: String,
        after: String? = null
    ): GraphQLResponse<RepoReleasesQuery.Data>

    suspend fun getRepoForks(
        owner: String,
        name: String,
        after: String? = null
    ): GraphQLResponse<RepoForksQuery.Data>

    suspend fun getRepoContributors(
        owner: String,
        name: String,
        after: String? = null
    ): GraphQLResponse<RepoContributorsQuery.Data>

    suspend fun getCommits(
        id: String,
        branch: String? = null,
        after: String? = null
    ): GraphQLResponse<CommitsQuery.Data>

    // Release

    suspend fun getReleaseDetails(
        owner: String,
        name: String,
        tag: String,
        after: String? = null
    ): GraphQLResponse<ReleaseDetailsQuery.Data>

    // Files

    suspend fun getTree(owner: String, name: String, branchAndPath: String): GraphQLResponse<TreeQuery.Data>

    suspend fun getFile(owner: String, name: String, branch: String, path: String): GraphQLResponse<FileQuery.Data>

    suspend fun getRawMarkdown(owner: String, name: String, branch: String, path: String): GraphQLResponse<RawMarkdownQuery.Data>

}

internal class NetworkGraphQLDataSource(
    private val apolloClient: ApolloClient
): GraphQLDataSource {


    private fun <D: Operation.Data> ApolloCall<D>.addToken(): ApolloCall<D> {
        return addHttpHeader("Authorization", "") // TODO: Retrieve token from storage
    }

    // Account

    override suspend fun getAccountInfo(token: String): GraphQLResponse<AccountInfoQuery.Data> {
        return apolloClient.query(AccountInfoQuery())
            .doNotStore(true)
            .addHttpHeader("Authorization", token)
            .response()
    }

    override suspend fun identify(): GraphQLResponse<IdentifyQuery.Data> {
        return apolloClient.query(IdentifyQuery())
            .addToken()
            .response()
    }

    // Starrable

    override suspend fun star(id: String): GraphQLResponse<StarMutation.Data> {
        return apolloClient.mutation(StarMutation(id))
            .addToken()
            .response()
    }

    override suspend fun unstar(id: String): GraphQLResponse<UnstarMutation.Data> {
        return apolloClient.mutation(UnstarMutation(id))
            .addToken()
            .response()
    }

    // Reactable

    override suspend fun react(
        id: String,
        reaction: ReactionContent
    ): GraphQLResponse<ReactMutation.Data> {
        return apolloClient.mutation(ReactMutation(id, reaction))
            .addToken()
            .response()
    }

    override suspend fun unreact(
        id: String,
        reaction: ReactionContent
    ): GraphQLResponse<UnreactMutation.Data> {
        return apolloClient.mutation(UnreactMutation(id, reaction))
            .addToken()
            .response()
    }

    // Profile

    override suspend fun getCurrentProfile(): GraphQLResponse<ProfileQuery.Data> {
        return apolloClient.query(ProfileQuery())
            .addToken()
            .response()
    }

    override suspend fun getProfile(login: String): GraphQLResponse<UserProfileQuery.Data> {
        return apolloClient.query(UserProfileQuery(login))
            .addToken()
            .response()
    }

    override suspend fun getUserRepositories(
        login: String,
        after: String?,
        count: Int
    ): GraphQLResponse<RepoListQuery.Data> {
        return apolloClient.query(
            RepoListQuery(
                username = login,
                cursor = after.toOptional(),
                total = count.toOptional()
            )
        )
            .addToken()
            .response()
    }

    override suspend fun getStarredRepositories(
        login: String,
        after: String?,
        count: Int
    ): GraphQLResponse<StarredReposQuery.Data> {
        return apolloClient.query(
            StarredReposQuery(
                username = login,
                cursor = after.toOptional(),
                total = count.toOptional()
            )
        )
            .addToken()
            .response()
    }

    override suspend fun getJoinedOrgs(
        login: String,
        after: String?,
        count: Int
    ): GraphQLResponse<JoinedOrgsQuery.Data> {
        return apolloClient.query(
            JoinedOrgsQuery(
                username = login,
                cursor = after.toOptional(),
                total = count.toOptional()
            )
        )
            .addToken()
            .response()
    }

    override suspend fun getFollowers(
        login: String,
        after: String?,
        count: Int
    ): GraphQLResponse<FollowersQuery.Data> {
        return apolloClient.query(
            FollowersQuery(
                username = login,
                cursor = after.toOptional(),
                total = count.toOptional()
            )
        )
            .addToken()
            .response()
    }

    override suspend fun getFollowing(
        login: String,
        after: String?,
        count: Int
    ): GraphQLResponse<FollowingQuery.Data> {
        return apolloClient.query(
            FollowingQuery(
                username = login,
                cursor = after.toOptional(),
                total = count.toOptional()
            )
        )
            .addToken()
            .response()
    }

    override suspend fun getSponsoring(
        login: String,
        after: String?,
        count: Int
    ): GraphQLResponse<SponsoringQuery.Data> {
        return apolloClient.query(
            SponsoringQuery(
                username = login,
                cursor = after.toOptional(),
                total = count.toOptional()
            )
        )
            .addToken()
            .response()
    }

    override suspend fun followUser(id: String): GraphQLResponse<FollowUserMutation.Data> {
        return apolloClient.mutation(FollowUserMutation(id))
            .addToken()
            .response()
    }

    override suspend fun unfollowUser(id: String): GraphQLResponse<UnfollowUserMutation.Data> {
        return apolloClient.mutation(UnfollowUserMutation(id))
            .addToken()
            .response()
    }

    // Feed

    override suspend fun getFeed(after: String?): GraphQLResponse<FeedQuery.Data> {
        return apolloClient.query(FeedQuery(after.toOptional()))
            .addToken()
            .response()
    }

    override suspend fun getTrending(period: TrendingPeriod): GraphQLResponse<TrendingQuery.Data> {
        return apolloClient.query(TrendingQuery(period))
            .addToken()
            .response()
    }

    // Repositories

    override suspend fun getRepoName(
        owner: String,
        name: String
    ): GraphQLResponse<RepoNameQuery.Data> {
        return apolloClient.query(RepoNameQuery(owner, name))
            .addToken()
            .response()
    }

    override suspend fun getRepoDetails(
        owner: String,
        name: String
    ): GraphQLResponse<RepoDetailsQuery.Data> {
        return apolloClient.query(RepoDetailsQuery(owner, name))
            .addToken()
            .response()
    }

    override suspend fun getRepoLicense(
        owner: String,
        name: String
    ): GraphQLResponse<RepoLicenseQuery.Data> {
        return apolloClient.query(RepoLicenseQuery(owner, name))
            .addToken()
            .response()
    }

    override suspend fun prefetchRepoTree(
        owner: String,
        name: String
    ): GraphQLResponse<RepoTreePrefetchQuery.Data> {
        return apolloClient.query(RepoTreePrefetchQuery(owner, name))
            .addToken()
            .response()
    }

    override suspend fun getRepoIssues(
        owner: String,
        name: String,
        after: String?,
        states: Set<IssueState>
    ): GraphQLResponse<RepoIssuesQuery.Data> {
        return apolloClient.query(RepoIssuesQuery(owner, name, after.toOptional(), states.toList()))
            .addToken()
            .response()
    }

    override suspend fun getRepoPullRequests(
        owner: String,
        name: String,
        after: String?,
        states: List<PullRequestState>
    ): GraphQLResponse<RepoPullRequestsQuery.Data> {
        return apolloClient.query(RepoPullRequestsQuery(owner, name, after.toOptional(), states.toList()))
            .addToken()
            .response()
    }

    override suspend fun getRepoReleases(
        owner: String,
        name: String,
        after: String?
    ): GraphQLResponse<RepoReleasesQuery.Data> {
        return apolloClient.query(RepoReleasesQuery(owner, name, after.toOptional()))
            .addToken()
            .response()
    }

    override suspend fun getRepoForks(
        owner: String,
        name: String,
        after: String?
    ): GraphQLResponse<RepoForksQuery.Data> {
        return apolloClient.query(RepoForksQuery(owner, name, after.toOptional()))
            .addToken()
            .response()
    }

    override suspend fun getRepoContributors(
        owner: String,
        name: String,
        after: String?
    ): GraphQLResponse<RepoContributorsQuery.Data> {
        return apolloClient.query(RepoContributorsQuery(owner, name, after.toOptional()))
            .addToken()
            .response()
    }

    override suspend fun getCommits(
        id: String,
        branch: String?,
        after: String?
    ): GraphQLResponse<CommitsQuery.Data> {
        return apolloClient.query(CommitsQuery(id, branch.toOptional(), after.toOptional()))
            .addToken()
            .response()
    }

    // Releases

    override suspend fun getReleaseDetails(
        owner: String,
        name: String,
        tag: String,
        after: String?
    ): GraphQLResponse<ReleaseDetailsQuery.Data> {
        return apolloClient.query(ReleaseDetailsQuery(owner, name, tag, after.toOptional()))
            .addToken()
            .response()
    }

    // Files

    override suspend fun getTree(
        owner: String,
        name: String,
        branchAndPath: String
    ): GraphQLResponse<TreeQuery.Data> {
        return apolloClient.query(TreeQuery(owner, name, branchAndPath))
            .addToken()
            .response()
    }

    override suspend fun getFile(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): GraphQLResponse<FileQuery.Data> {
        return apolloClient.query(FileQuery(owner, name, branch, path))
            .addToken()
            .response()
    }

    override suspend fun getRawMarkdown(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): GraphQLResponse<RawMarkdownQuery.Data> {
        return apolloClient.query(RawMarkdownQuery(owner, name, branch, path))
            .addToken()
            .response()
    }

}