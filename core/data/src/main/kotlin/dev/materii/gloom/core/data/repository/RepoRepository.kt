package dev.materii.gloom.core.data.repository

import dev.materii.gloom.core.graphql.*
import dev.materii.gloom.core.graphql.fragment.RepoDetails
import dev.materii.gloom.core.graphql.fragment.RepoLicense
import dev.materii.gloom.core.graphql.fragment.RepoOverview
import dev.materii.gloom.core.graphql.response.GraphQLResponse
import dev.materii.gloom.core.graphql.response.transform
import dev.materii.gloom.core.graphql.type.IssueState
import dev.materii.gloom.core.graphql.type.PullRequestState

interface RepoRepository {

    /**
     * Get basic metadata for a repository
     *
     * @param owner Owner of the repository
     * @param name Name of the repository
     */
    suspend fun getRepoName(owner: String, name: String): GraphQLResponse<RepoOverview?>

    /**
     * Get the details for a particular repository
     *
     * @param owner Owner of the repository
     * @param name Name of the repository
     */
    suspend fun getRepoDetails(owner: String, name: String): GraphQLResponse<RepoDetails?>

    /**
     * Get license info for a repository
     *
     * @param owner Owner of the repository
     * @param name Name of the repository
     */
    suspend fun getRepoLicense(owner: String, name: String): GraphQLResponse<RepoLicense?>

    /**
     * Get metadata needed to fetch a repository's file tree
     *
     * @param owner Owner of the repository
     * @param name Name of the repository
     */
    suspend fun prefetchRepoTree(owner: String, name: String): GraphQLResponse<RepoTreePrefetchQuery.Repository?>

    /**
     * Get the issues made in a repository
     *
     * @param owner Owner of the repository
     * @param name Name of the repository
     * @param after Cursor used to get the next page of issues
     * @param states States to filter with
     */
    suspend fun getRepoIssues(
        owner: String,
        name: String,
        after: String? = null,
        states: Set<IssueState> = setOf(IssueState.OPEN)
    ): GraphQLResponse<RepoIssuesQuery.Issues?>

    /**
     * Get the pull requests made to a repository
     *
     * @param owner Owner of the repository
     * @param name Name of the repository
     * @param after Cursor used to get the next page of pull requests
     * @param states States to filter with
     */
    suspend fun getRepoPullRequests(
        owner: String,
        name: String,
        after: String? = null,
        states: Set<PullRequestState> = setOf(PullRequestState.OPEN)
    ): GraphQLResponse<RepoPullRequestsQuery.PullRequests?>

    /**
     * Get the releases from a repository
     *
     * @param owner Owner of the repository
     * @param name Name of the repository
     * @param after Cursor used to get the next page of releases
     */
    suspend fun getRepoReleases(
        owner: String,
        name: String,
        after: String? = null
    ): GraphQLResponse<RepoReleasesQuery.Releases?>

    /**
     * Get the forks made from a repository
     *
     * @param owner Owner of the repository
     * @param name Name of the repository
     * @param after Cursor used to get the next page of forks
     */
    suspend fun getRepoForks(
        owner: String,
        name: String,
        after: String? = null
    ): GraphQLResponse<RepoForksQuery.Forks?>

    /**
     * Get the contributors to a repository
     *
     * @param owner Owner of the repository
     * @param name Name of the repository
     * @param after Cursor used to get the next page of contributors
     */
    suspend fun getRepoContributors(
        owner: String,
        name: String,
        after: String? = null
    ): GraphQLResponse<RepoContributorsQuery.Contributors?>

    /**
     * Get the commits to a repository branch
     *
     * @param id Id of the repository
     * @param branch Branch to view commits from
     * @param after Cursor used to get the next page of commits
     */
    suspend fun getCommits(
        id: String,
        branch: String,
        after: String? = null,
    ): GraphQLResponse<CommitsQuery.History?>

    /**
     * Star a repository
     */
    suspend fun star(id: String): GraphQLResponse<StarMutation.AddStar?>

    /**
     * Unstar a repository
     */
    suspend fun unstar(id: String): GraphQLResponse<UnstarMutation.RemoveStar?>

}

internal class RepoRepositoryImpl(
    private val graphQL: GraphQLDataSource
): RepoRepository {

    override suspend fun getRepoName(
        owner: String,
        name: String
    ): GraphQLResponse<RepoOverview?> {
        return graphQL.getRepoName(owner, name).transform {
            it.repository?.repoOverview
        }
    }

    override suspend fun getRepoDetails(
        owner: String,
        name: String
    ): GraphQLResponse<RepoDetails?> {
        return graphQL.getRepoDetails(owner, name).transform {
            it.repository?.repoDetails
        }
    }

    override suspend fun getRepoLicense(
        owner: String,
        name: String
    ): GraphQLResponse<RepoLicense?> {
        return graphQL.getRepoLicense(owner, name).transform {
            it.repository?.licenseInfo?.repoLicense
        }
    }

    override suspend fun prefetchRepoTree(
        owner: String,
        name: String
    ): GraphQLResponse<RepoTreePrefetchQuery.Repository?> {
        return graphQL.prefetchRepoTree(owner, name).transform {
            it.repository
        }
    }

    override suspend fun getRepoIssues(
        owner: String,
        name: String,
        after: String?,
        states: Set<IssueState>
    ): GraphQLResponse<RepoIssuesQuery.Issues?> {
        return graphQL.getRepoIssues(owner, name, after, states).transform {
            it.repository?.issues
        }
    }

    override suspend fun getRepoPullRequests(
        owner: String,
        name: String,
        after: String?,
        states: Set<PullRequestState>
    ): GraphQLResponse<RepoPullRequestsQuery.PullRequests?> {
        return graphQL.getRepoPullRequests(owner, name, after, states).transform {
            it.repository?.pullRequests
        }
    }

    override suspend fun getRepoReleases(
        owner: String,
        name: String,
        after: String?
    ): GraphQLResponse<RepoReleasesQuery.Releases?> {
        return graphQL.getRepoReleases(owner, name, after).transform {
            it.repository?.releases
        }
    }

    override suspend fun getRepoForks(
        owner: String,
        name: String,
        after: String?
    ): GraphQLResponse<RepoForksQuery.Forks?> {
        return graphQL.getRepoForks(owner, name, after).transform {
            it.repository?.forks
        }
    }

    override suspend fun getRepoContributors(
        owner: String,
        name: String,
        after: String?
    ): GraphQLResponse<RepoContributorsQuery.Contributors?> {
        return graphQL.getRepoContributors(owner, name, after).transform {
            it.repository?.contributors
        }
    }

    override suspend fun getCommits(
        id: String,
        branch: String,
        after: String?
    ): GraphQLResponse<CommitsQuery.History?> {
        return graphQL.getCommits(id, branch, after).transform {
            it.node?.onRepository?.gitObject?.onCommit?.history
        }
    }

    override suspend fun star(id: String): GraphQLResponse<StarMutation.AddStar?> {
        return graphQL.star(id).transform {
            it.addStar
        }
    }

    override suspend fun unstar(id: String): GraphQLResponse<UnstarMutation.RemoveStar?> {
        return graphQL.unstar(id).transform {
            it.removeStar
        }
    }

}