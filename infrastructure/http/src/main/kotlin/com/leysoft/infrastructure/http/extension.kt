package com.leysoft.infrastructure.http

import arrow.core.*
import arrow.core.raise.EagerEffect
import arrow.core.raise.Raise
import arrow.core.raise.fold
import arrow.core.raise.getOrElse
import com.leysoft.core.error.BaseException
import com.leysoft.core.error.InfrastructureException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

private fun <R, A> EagerEffect<R, A>.getOrElse(
    error: (e: Throwable) -> A = { throw it },
    recover: (raise: R) -> A
): A = fold(
    block = { invoke(this) },
    catch = error,
    recover = recover,
    transform = ::identity
)

suspend fun <A> ApplicationCall.respond(
    f: suspend Raise<BaseException>.() -> A,
    error: HttpErrorHandler<BaseException>
) = f.getOrElse { error.invoke(this) }

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

context(Raise<BaseException>)
fun <A> ApplicationCall.getRequiredParam(
    key: String,
    f: (String) -> A
): A =
    when (val id = getParam(key)) {
        is Some -> f(id.value)
        else -> raise(RequiredParameterException(key))
    }

data class RequiredParameterException(override val message: String) :
    InfrastructureException(message)
