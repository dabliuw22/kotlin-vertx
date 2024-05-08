package com.leysoft.infrastructure.http

import com.leysoft.core.error.*
import io.ktor.http.*

typealias HttpErrorHandler<A, B> = (A) -> B

data class ErrorResponse(
    val message: String,
    val code: HttpStatusCode
)

val throwableHandler: HttpErrorHandler<Throwable, ErrorResponse> = {
        throwable ->
    ErrorResponse(throwable.message ?: "Internal Server Error", HttpStatusCode.InternalServerError)
}

val errorHandler: HttpErrorHandler<BaseException, ErrorResponse> =
    { error ->
        when (error) {
            is NotFoundProductException -> ErrorResponse(error.message, HttpStatusCode.NotFound)
            is CreateProductException -> ErrorResponse(error.message, HttpStatusCode.Conflict)
            is DeleteProductException -> ErrorResponse(error.message, HttpStatusCode.Conflict)
            is CustomProductException -> ErrorResponse(error.message, HttpStatusCode.Conflict)
            else -> ErrorResponse("Internal Server Error", HttpStatusCode.InternalServerError)
        }
    }
