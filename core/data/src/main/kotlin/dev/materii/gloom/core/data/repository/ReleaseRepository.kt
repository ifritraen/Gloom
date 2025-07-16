package dev.materii.gloom.core.data.repository

import dev.materii.gloom.core.graphql.GraphQLDataSource
import dev.materii.gloom.core.graphql.fragment.ReleaseDetails
import dev.materii.gloom.core.graphql.response.GraphQLResponse
import dev.materii.gloom.core.graphql.response.transform
import kotlinx.coroutines.flow.Flow

interface ReleaseRepository {

    /**
     * Get the details and assets for a release
     *
     * @param owner Owner of the repository the release is from
     * @param name Name of the repository the release is from
     * @param tag Tag associated with the release
     * @param after Cursor used to get the next set of assets
     */
    fun getReleaseDetails(
        owner: String,
        name: String,
        tag: String,
        after: String? = null
    ): Flow<GraphQLResponse<ReleaseDetails?>>

}

internal class ReleaseRepositoryImpl(
    private val graphQL: GraphQLDataSource
): ReleaseRepository {

    override fun getReleaseDetails(
        owner: String,
        name: String,
        tag: String,
        after: String?
    ): Flow<GraphQLResponse<ReleaseDetails?>> {
        return graphQL.getReleaseDetails(owner, name, tag, after).transform {
            it.repository?.release?.releaseDetails
        }
    }

}