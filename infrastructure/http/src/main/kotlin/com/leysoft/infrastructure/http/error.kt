package com.leysoft.infrastructure.http

import io.ktor.server.application.ApplicationCall

typealias HttpErrorHandler<A> =
    suspend ApplicationCall.() -> suspend (A) -> Unit

data class ErrorResponse(
    val message: String
)
