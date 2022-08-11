package com.leysoft.infrastructure.http

import arrow.core.Either
import arrow.core.Option
import com.leysoft.core.error.InfrastructureException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun <L, R> Either<L, R>.handle(
    success: suspend (R) -> Unit,
    failure: (L) -> Unit
): Unit =
    when (val result = this) {
        is Either.Right -> success(result.value)
        is Either.Left -> failure(result.value)
    }

suspend inline fun <reified A : Any> ApplicationCall.respondJson(
    status: HttpStatusCode,
    message: A
) {
    response.header(
        HttpHeaders.ContentType,
        ContentType.Application.Json.toString()
    )
    this.respond(status, message)
}

inline fun ApplicationCall.getParam(key: String): Option<String> =
    Option.fromNullable(parameters[key])

data class RequiredParameterException(override val message: String) :
    InfrastructureException(message)
