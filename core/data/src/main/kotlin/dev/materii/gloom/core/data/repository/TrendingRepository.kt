package dev.materii.gloom.core.data.repository

import dev.materii.gloom.core.graphql.GraphQLDataSource
import dev.materii.gloom.core.graphql.response.GraphQLResponse
import dev.materii.gloom.core.graphql.response.transform
import dev.materii.gloom.core.graphql.type.TrendingPeriod
import kotlinx.coroutines.flow.Flow
import dev.materii.gloom.core.graphql.fragment.TrendingRepository as TrendingRepositoryFragment

interface TrendingRepository {

    /**
     * Get the trending repositories for a given time period
     *
     * @param period The time period to compare in
     */
    fun getTrending(period: TrendingPeriod = TrendingPeriod.DAILY): Flow<GraphQLResponse<List<TrendingRepositoryFragment>>>

}

internal class TrendingRepositoryImpl(
    private val graphQL: GraphQLDataSource
): TrendingRepository {

    override fun getTrending(period: TrendingPeriod): Flow<GraphQLResponse<List<TrendingRepositoryFragment>>> {
        return graphQL.getTrending(period).transform { (trendingRepositories) ->
            trendingRepositories?.mapNotNull { it?.trendingRepository }.orEmpty()
        }
    }

}