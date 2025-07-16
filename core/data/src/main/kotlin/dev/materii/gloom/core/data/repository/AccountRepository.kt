package dev.materii.gloom.core.data.repository

import dev.materii.gloom.core.graphql.GraphQLDataSource
import dev.materii.gloom.core.graphql.IdentifyQuery
import dev.materii.gloom.core.graphql.response.GraphQLResponse
import dev.materii.gloom.core.graphql.response.transform
import dev.materii.gloom.core.model.account.UserAccount
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    /**
     * Gets account info for the user authenticated with a given token
     *
     * @param token The token belonging to the desired account
     */
    fun getAccountInfo(token: String): Flow<GraphQLResponse<UserAccount>>

    /**
     * Used to check if the active token has been revoked
     */
    fun identify(): Flow<GraphQLResponse<IdentifyQuery.Data>>

}

internal class AccountRepositoryImpl(
    private val graphQL: GraphQLDataSource
): AccountRepository {

    override fun getAccountInfo(token: String): Flow<GraphQLResponse<UserAccount>> {
        return graphQL.getAccountInfo(token).transform { (viewer) ->
            UserAccount.fromFragment(viewer.userAccount)
        }
    }

    override fun identify(): Flow<GraphQLResponse<IdentifyQuery.Data>> {
        return graphQL.identify()
    }

}