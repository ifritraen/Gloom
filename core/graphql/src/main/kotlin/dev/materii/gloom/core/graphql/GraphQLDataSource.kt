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
import kotlinx.coroutines.flow.Flow

interface GraphQLDataSource {

    // Account

    fun getAccountInfo(token: String): Flow<GraphQLResponse<AccountInfoQuery.Data>>

    fun identify(): Flow<GraphQLResponse<IdentifyQuery.Data>>

    // Starrable

    fun star(id: String): Flow<GraphQLResponse<StarMutation.Data>>

    fun unstar(id: String): Flow<GraphQLResponse<UnstarMutation.Data>>

    // Reactable

    fun react(id: String, reaction: ReactionContent): Flow<GraphQLResponse<ReactMutation.Data>>

    fun unreact(id: String, reaction: ReactionContent): Flow<GraphQLResponse<UnreactMutation.Data>>

    // Profile

    fun getCurrentProfile(): Flow<GraphQLResponse<ProfileQuery.Data>>

    fun getProfile(login: String): Flow<GraphQLResponse<UserProfileQuery.Data>>

    fun getUserRepositories(login: String, after: String? = null, count: Int = 30): Flow<GraphQLResponse<RepoListQuery.Data>>

    fun getStarredRepositories(login: String, after: String? = null, count: Int = 30): Flow<GraphQLResponse<StarredReposQuery.Data>>

    fun getJoinedOrgs(login: String, after: String? = null, count: Int = 30): Flow<GraphQLResponse<JoinedOrgsQuery.Data>>

    fun getFollowers(login: String, after: String? = null, count: Int = 30): Flow<GraphQLResponse<FollowersQuery.Data>>

    fun getFollowing(login: String, after: String? = null, count: Int = 30): Flow<GraphQLResponse<FollowingQuery.Data>>

    fun getSponsoring(login: String, after: String? = null, count: Int = 30): Flow<GraphQLResponse<SponsoringQuery.Data>>

    fun followUser(id: String): Flow<GraphQLResponse<FollowUserMutation.Data>>

    fun unfollowUser(id: String): Flow<GraphQLResponse<UnfollowUserMutation.Data>>

    // Feed

    fun getFeed(after: String? = null): Flow<GraphQLResponse<FeedQuery.Data>>

    fun getTrending(period: TrendingPeriod = TrendingPeriod.DAILY): Flow<GraphQLResponse<TrendingQuery.Data>>

    // Repositories

    fun getRepoName(owner: String, name: String): Flow<GraphQLResponse<RepoNameQuery.Data>>

    fun getRepoDetails(owner: String, name: String): Flow<GraphQLResponse<RepoDetailsQuery.Data>>

    fun getRepoLicense(owner: String, name: String): Flow<GraphQLResponse<RepoLicenseQuery.Data>>

    fun prefetchRepoTree(owner: String, name: String): Flow<GraphQLResponse<RepoTreePrefetchQuery.Data>>

    fun getRepoIssues(
        owner: String,
        name: String,
        after: String? = null,
        states: Set<IssueState> = setOf(IssueState.OPEN)
    ): Flow<GraphQLResponse<RepoIssuesQuery.Data>>

    fun getRepoPullRequests(
        owner: String,
        name: String,
        after: String? = null,
        states: Set<PullRequestState> = setOf(PullRequestState.OPEN)
    ): Flow<GraphQLResponse<RepoPullRequestsQuery.Data>>

    fun getRepoReleases(
        owner: String,
        name: String,
        after: String? = null
    ): Flow<GraphQLResponse<RepoReleasesQuery.Data>>

    fun getRepoForks(
        owner: String,
        name: String,
        after: String? = null
    ): Flow<GraphQLResponse<RepoForksQuery.Data>>

    fun getRepoContributors(
        owner: String,
        name: String,
        after: String? = null
    ): Flow<GraphQLResponse<RepoContributorsQuery.Data>>

    fun getCommits(
        id: String,
        branch: String? = null,
        after: String? = null
    ): Flow<GraphQLResponse<CommitsQuery.Data>>

    // Release

    fun getReleaseDetails(
        owner: String,
        name: String,
        tag: String,
        after: String? = null
    ): Flow<GraphQLResponse<ReleaseDetailsQuery.Data>>

    // Files

    fun getTree(owner: String, name: String, branchAndPath: String): Flow<GraphQLResponse<TreeQuery.Data>>

    fun getFile(owner: String, name: String, branch: String, path: String): Flow<GraphQLResponse<FileQuery.Data>>

    fun getRawMarkdown(owner: String, name: String, branch: String, path: String): Flow<GraphQLResponse<RawMarkdownQuery.Data>>

}

internal class NetworkGraphQLDataSource(
    private val apolloClient: ApolloClient
): GraphQLDataSource {


    private fun <D: Operation.Data> ApolloCall<D>.addToken(): ApolloCall<D> {
        return addHttpHeader("Authorization", "") // TODO: Retrieve token from storage
    }

    // Account

    override fun getAccountInfo(token: String): Flow<GraphQLResponse<AccountInfoQuery.Data>> {
        return apolloClient.query(AccountInfoQuery())
            .doNotStore(true)
            .addHttpHeader("Authorization", token)
            .response()
    }

    override fun identify(): Flow<GraphQLResponse<IdentifyQuery.Data>> {
        return apolloClient.query(IdentifyQuery())
            .addToken()
            .response()
    }

    // Starrable

    override fun star(id: String): Flow<GraphQLResponse<StarMutation.Data>> {
        return apolloClient.mutation(StarMutation(id))
            .addToken()
            .response()
    }

    override fun unstar(id: String): Flow<GraphQLResponse<UnstarMutation.Data>> {
        return apolloClient.mutation(UnstarMutation(id))
            .addToken()
            .response()
    }

    // Reactable

    override fun react(
        id: String,
        reaction: ReactionContent
    ): Flow<GraphQLResponse<ReactMutation.Data>> {
        return apolloClient.mutation(ReactMutation(id, reaction))
            .addToken()
            .response()
    }

    override fun unreact(
        id: String,
        reaction: ReactionContent
    ): Flow<GraphQLResponse<UnreactMutation.Data>> {
        return apolloClient.mutation(UnreactMutation(id, reaction))
            .addToken()
            .response()
    }

    // Profile

    override fun getCurrentProfile(): Flow<GraphQLResponse<ProfileQuery.Data>> {
        return apolloClient.query(ProfileQuery())
            .addToken()
            .response()
    }

    override fun getProfile(login: String): Flow<GraphQLResponse<UserProfileQuery.Data>> {
        return apolloClient.query(UserProfileQuery(login))
            .addToken()
            .response()
    }

    override fun getUserRepositories(
        login: String,
        after: String?,
        count: Int
    ): Flow<GraphQLResponse<RepoListQuery.Data>> {
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

    override fun getStarredRepositories(
        login: String,
        after: String?,
        count: Int
    ): Flow<GraphQLResponse<StarredReposQuery.Data>> {
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

    override fun getJoinedOrgs(
        login: String,
        after: String?,
        count: Int
    ): Flow<GraphQLResponse<JoinedOrgsQuery.Data>> {
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

    override fun getFollowers(
        login: String,
        after: String?,
        count: Int
    ): Flow<GraphQLResponse<FollowersQuery.Data>> {
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

    override fun getFollowing(
        login: String,
        after: String?,
        count: Int
    ): Flow<GraphQLResponse<FollowingQuery.Data>> {
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

    override fun getSponsoring(
        login: String,
        after: String?,
        count: Int
    ): Flow<GraphQLResponse<SponsoringQuery.Data>> {
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

    override fun followUser(id: String): Flow<GraphQLResponse<FollowUserMutation.Data>> {
        return apolloClient.mutation(FollowUserMutation(id))
            .addToken()
            .response()
    }

    override fun unfollowUser(id: String): Flow<GraphQLResponse<UnfollowUserMutation.Data>> {
        return apolloClient.mutation(UnfollowUserMutation(id))
            .addToken()
            .response()
    }

    // Feed

    override fun getFeed(after: String?): Flow<GraphQLResponse<FeedQuery.Data>> {
        return apolloClient.query(FeedQuery(after.toOptional()))
            .addToken()
            .response()
    }

    override fun getTrending(period: TrendingPeriod): Flow<GraphQLResponse<TrendingQuery.Data>> {
        return apolloClient.query(TrendingQuery(period))
            .addToken()
            .response()
    }

    // Repositories

    override fun getRepoName(
        owner: String,
        name: String
    ): Flow<GraphQLResponse<RepoNameQuery.Data>> {
        return apolloClient.query(RepoNameQuery(owner, name))
            .addToken()
            .response()
    }

    override fun getRepoDetails(
        owner: String,
        name: String
    ): Flow<GraphQLResponse<RepoDetailsQuery.Data>> {
        return apolloClient.query(RepoDetailsQuery(owner, name))
            .addToken()
            .response()
    }

    override fun getRepoLicense(
        owner: String,
        name: String
    ): Flow<GraphQLResponse<RepoLicenseQuery.Data>> {
        return apolloClient.query(RepoLicenseQuery(owner, name))
            .addToken()
            .response()
    }

    override fun prefetchRepoTree(
        owner: String,
        name: String
    ): Flow<GraphQLResponse<RepoTreePrefetchQuery.Data>> {
        return apolloClient.query(RepoTreePrefetchQuery(owner, name))
            .addToken()
            .response()
    }

    override fun getRepoIssues(
        owner: String,
        name: String,
        after: String?,
        states: Set<IssueState>
    ): Flow<GraphQLResponse<RepoIssuesQuery.Data>> {
        return apolloClient.query(RepoIssuesQuery(owner, name, after.toOptional(), states.toList()))
            .addToken()
            .response()
    }

    override fun getRepoPullRequests(
        owner: String,
        name: String,
        after: String?,
        states: Set<PullRequestState>
    ): Flow<GraphQLResponse<RepoPullRequestsQuery.Data>> {
        return apolloClient.query(RepoPullRequestsQuery(owner, name, after.toOptional(), states.toList()))
            .addToken()
            .response()
    }

    override fun getRepoReleases(
        owner: String,
        name: String,
        after: String?
    ): Flow<GraphQLResponse<RepoReleasesQuery.Data>> {
        return apolloClient.query(RepoReleasesQuery(owner, name, after.toOptional()))
            .addToken()
            .response()
    }

    override fun getRepoForks(
        owner: String,
        name: String,
        after: String?
    ): Flow<GraphQLResponse<RepoForksQuery.Data>> {
        return apolloClient.query(RepoForksQuery(owner, name, after.toOptional()))
            .addToken()
            .response()
    }

    override fun getRepoContributors(
        owner: String,
        name: String,
        after: String?
    ): Flow<GraphQLResponse<RepoContributorsQuery.Data>> {
        return apolloClient.query(RepoContributorsQuery(owner, name, after.toOptional()))
            .addToken()
            .response()
    }

    override fun getCommits(
        id: String,
        branch: String?,
        after: String?
    ): Flow<GraphQLResponse<CommitsQuery.Data>> {
        return apolloClient.query(CommitsQuery(id, branch.toOptional(), after.toOptional()))
            .addToken()
            .response()
    }

    // Releases

    override fun getReleaseDetails(
        owner: String,
        name: String,
        tag: String,
        after: String?
    ): Flow<GraphQLResponse<ReleaseDetailsQuery.Data>> {
        return apolloClient.query(ReleaseDetailsQuery(owner, name, tag, after.toOptional()))
            .addToken()
            .response()
    }

    // Files

    override fun getTree(
        owner: String,
        name: String,
        branchAndPath: String
    ): Flow<GraphQLResponse<TreeQuery.Data>> {
        return apolloClient.query(TreeQuery(owner, name, branchAndPath))
            .addToken()
            .response()
    }

    override fun getFile(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): Flow<GraphQLResponse<FileQuery.Data>> {
        return apolloClient.query(FileQuery(owner, name, branch, path))
            .addToken()
            .response()
    }

    override fun getRawMarkdown(
        owner: String,
        name: String,
        branch: String,
        path: String
    ): Flow<GraphQLResponse<RawMarkdownQuery.Data>> {
        return apolloClient.query(RawMarkdownQuery(owner, name, branch, path))
            .addToken()
            .response()
    }

}