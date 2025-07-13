package dev.materii.gloom.core.data.repository

import dev.materii.gloom.core.graphql.FeedQuery
import dev.materii.gloom.core.graphql.GraphQLDataSource
import dev.materii.gloom.core.graphql.response.GraphQLResponse
import dev.materii.gloom.core.graphql.response.transform

interface FeedRepository {

    /**
     * Get the current user's activity feed
     *
     * @param after The cursor used to retrieve the next page of items
     */
    suspend fun getFeed(after: String? = null): GraphQLResponse<FeedQuery.Items?>

}

internal class FeedRepositoryImpl(
    private val graphQL: GraphQLDataSource
): FeedRepository {

    override suspend fun getFeed(after: String?): GraphQLResponse<FeedQuery.Items?> {
        return graphQL.getFeed(after).transform {
            it.viewer.dashboard?.feed?.items
        }
    }

}