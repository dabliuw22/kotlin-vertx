package com.leysoft.infrastructure.http

import arrow.core.*
import com.leysoft.core.error.BaseException
import com.leysoft.core.error.InfrastructureException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun <L, R> Either<L, R>.handle(
    success: suspend (R) -> Unit,
    failure: suspend (L) -> Unit
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

fun ApplicationCall.getParam(key: String): Option<String> =
    Option.fromNullable(parameters[key])

fun <A> ApplicationCall.getRequiredParam(key: String, f: (String) -> A): Either<BaseException, A> =
    when (val id = getParam(key)) {
        is Some -> f(id.value).right()
        else -> RequiredParameterException(key).left()
    }

data class RequiredParameterException(override val message: String) :
    InfrastructureException(message)
