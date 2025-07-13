package dev.materii.gloom.core.data.repository

import dev.materii.gloom.core.graphql.*
import dev.materii.gloom.core.graphql.fragment.UserProfile
import dev.materii.gloom.core.graphql.response.GraphQLResponse
import dev.materii.gloom.core.graphql.response.transform

interface ProfileRepository {

    /**
     * Retrieves the profile for the logged in user
     */
    suspend fun getCurrentProfile(): GraphQLResponse<UserProfile>

    /**
     * Retrieves the profile for a user or organization
     *
     * @param login The username of the user or organization
     *
     * @return The profile and a boolean indicating if they sponsor the project
     */
    suspend fun getProfile(login: String): GraphQLResponse<Pair<UserProfileQuery.RepositoryOwner?, Boolean>>

    /**
     * Get a list of repositories owned by a User or Org
     *
     * @param login The username of the user or organization
     * @param after The key to use to get the next page
     * @param count Number of repositories to return
     */
    suspend fun getUserRepositories(login: String, after: String? = null, count: Int = 30): GraphQLResponse<RepoListQuery.Repositories?>

    /**
     * Get a list of repositories starred by a User
     *
     * @param login The username of the user
     * @param after The key to use to get the next page
     * @param count Number of repositories to return
     */
    suspend fun getStarredRepositories(
        login: String,
        after: String? = null,
        count: Int = 30
    ): GraphQLResponse<StarredReposQuery.StarredRepositories?>

    /**
     * Get a list of organizations a User is a member of
     *
     * @param login The username of the user
     * @param after The key to use to get the next page
     * @param count Number of organizations to return
     */
    suspend fun getJoinedOrgs(
        login: String,
        after: String? = null,
        count: Int = 30
    ): GraphQLResponse<JoinedOrgsQuery.Organizations?>

    /**
     * Get a list of users following a User or Org
     *
     * @param login The username of the user
     * @param after The key to use to get the next page
     * @param count Number of users to return
     */
    suspend fun getFollowers(
        login: String,
        after: String? = null,
        count: Int = 30
    ): GraphQLResponse<FollowersQuery.Followers?>

    /**
     * Get a list of users followed by a User or Org
     *
     * @param login The username of the user
     * @param after The key to use to get the next page
     * @param count Number of users to return
     */
    suspend fun getFollowing(
        login: String,
        after: String? = null,
        count: Int = 30
    ): GraphQLResponse<FollowingQuery.Following?>

    /**
     * Get a list of users being sponsored by a User or Org
     *
     * @param login The username of the user
     * @param after The key to use to get the next page
     * @param count Number of users to return
     */
    suspend fun getSponsoring(
        login: String,
        after: String? = null,
        count: Int = 30
    ): GraphQLResponse<SponsoringQuery.RepositoryOwner?>

    /**
     * Follow a User
     *
     * @param id The user's ID
     */
    suspend fun followUser(id: String): GraphQLResponse<FollowUserMutation.User?>

    /**
     * Unfollow a User
     *
     * @param id The user's ID
     */
    suspend fun unfollowUser(id: String): GraphQLResponse<UnfollowUserMutation.User?>

}

internal class ProfileRepositoryImpl(
    private val graphQL: GraphQLDataSource
): ProfileRepository {

    override suspend fun getCurrentProfile(): GraphQLResponse<UserProfile> {
        return graphQL.getCurrentProfile().transform {
            it.viewer.userProfile
        }
    }

    override suspend fun getProfile(login: String): GraphQLResponse<Pair<UserProfileQuery.RepositoryOwner?, Boolean>> {
        return graphQL.getProfile(login).transform {
            it.repositoryOwner to (it.user?.isSponsoredBy ?: false)
        }
    }

    override suspend fun getUserRepositories(login: String, after: String?, count: Int): GraphQLResponse<RepoListQuery.Repositories?> {
        return graphQL.getUserRepositories(login, after, count).transform {
            it.repositoryOwner?.repositories
        }
    }

    override suspend fun getStarredRepositories(
        login: String,
        after: String?,
        count: Int
    ): GraphQLResponse<StarredReposQuery.StarredRepositories?> {
        return graphQL.getStarredRepositories(login, after, count).transform {
            it.user?.starredRepositories
        }
    }

    override suspend fun getJoinedOrgs(login: String, after: String?, count: Int): GraphQLResponse<JoinedOrgsQuery.Organizations?> {
        return graphQL.getJoinedOrgs(login, after, count).transform {
            it.user?.organizations
        }
    }

    override suspend fun getFollowers(login: String, after: String?, count: Int): GraphQLResponse<FollowersQuery.Followers?> {
        return graphQL.getFollowers(login, after, count).transform {
            it.user?.followers
        }
    }

    override suspend fun getFollowing(login: String, after: String?, count: Int): GraphQLResponse<FollowingQuery.Following?> {
        return graphQL.getFollowing(login, after, count).transform {
            it.user?.following
        }
    }

    override suspend fun getSponsoring(login: String, after: String?, count: Int): GraphQLResponse<SponsoringQuery.RepositoryOwner?> {
        return graphQL.getSponsoring(login, after, count).transform {
            it.repositoryOwner
        }
    }

    override suspend fun followUser(id: String): GraphQLResponse<FollowUserMutation.User?> {
        return graphQL.followUser(id).transform {
            it.followUser?.user
        }
    }

    override suspend fun unfollowUser(id: String): GraphQLResponse<UnfollowUserMutation.User?> {
        return graphQL.unfollowUser(id).transform {
            it.unfollowUser?.user
        }
    }

}