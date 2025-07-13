package dev.materii.gloom.core.data.repository

import dev.materii.gloom.core.graphql.GraphQLDataSource
import dev.materii.gloom.core.graphql.IdentifyQuery
import dev.materii.gloom.core.graphql.fragment.UserAccount
import dev.materii.gloom.core.graphql.response.GraphQLResponse
import dev.materii.gloom.core.graphql.response.transform

interface AccountRepository {

    /**
     * Gets account info for the user authenticated with a given token
     *
     * @param token The token belonging to the desired account
     */
    suspend fun getAccountInfo(token: String): GraphQLResponse<UserAccount>

    /**
     * Used to check if a token has been revoked
     */
    suspend fun identify(): GraphQLResponse<IdentifyQuery.Data>

}

internal class AccountRepositoryImpl(
    private val graphQL: GraphQLDataSource
): AccountRepository {

    override suspend fun getAccountInfo(token: String): GraphQLResponse<UserAccount> {
        return graphQL.getAccountInfo(token).transform { it.viewer.userAccount }
    }

    override suspend fun identify(): GraphQLResponse<IdentifyQuery.Data> {
        return graphQL.identify()
    }

}