package dev.materii.gloom.api.util

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Optional

suspend fun <D: Operation.Data> ApolloCall<D>.response(): GraphQLResponse<D> {
    return try {
        val response = execute()

        when {
            !response.hasErrors() -> GraphQLResponse.Success(response.dataAssertNoErrors, emptyList())
            response.hasErrors() && response.data != null -> GraphQLResponse.Success(response.data!!, response.errors.orEmpty())
            else -> GraphQLResponse.Error(response.errors.orEmpty())
        }
    } catch (e: Throwable) {
        GraphQLResponse.Failure(ApiFailure(e, null))
    }
}

fun <T> T?.toOptional(): Optional<T> = Optional.presentIfNotNull(this)