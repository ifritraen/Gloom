package dev.materii.gloom.core.graphql.response

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Operation

internal typealias GQLErrors = List<com.apollographql.apollo.api.Error>

sealed interface GraphQLResponse<out T> {

    data class Success<T>(val data: T, val errors: GQLErrors): GraphQLResponse<T>

    data class Error<T>(val errors: GQLErrors): GraphQLResponse<T>

    data class Failure<T>(val error: Throwable): GraphQLResponse<T>

}

@Suppress("TooGenericExceptionCaught")
suspend fun <D: Operation.Data> ApolloCall<D>.response(): GraphQLResponse<D> {
    return try {
        val response = execute()

        when {
            !response.hasErrors() -> GraphQLResponse.Success(response.dataAssertNoErrors, emptyList())
            response.hasErrors() && response.data != null -> GraphQLResponse.Success(response.data!!, response.errors.orEmpty())
            else -> GraphQLResponse.Error(response.errors.orEmpty())
        }
    } catch (e: Throwable) {
        GraphQLResponse.Failure(e)
    }
}