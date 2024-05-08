package com.leysoft.products.adapter.`in`.api

import com.leysoft.core.error.*
import com.leysoft.infrastructure.http.ErrorResponse
import com.leysoft.infrastructure.http.HttpErrorHandler
import com.leysoft.infrastructure.http.respondJson
import io.ktor.http.*

val errorHandler: HttpErrorHandler<BaseException> = {
    { error ->
        when (error) {
            is NotFoundProductException -> respondJson(
                HttpStatusCode.NotFound,
                ErrorResponse(error.message)
            )
            is CreateProductException -> respondJson(
                HttpStatusCode.Conflict,
                ErrorResponse(error.message)
            )
            is DeleteProductException -> respondJson(
                HttpStatusCode.Conflict,
                ErrorResponse(error.message)
            )
            is CustomProductException -> respondJson(
                HttpStatusCode.Conflict,
                ErrorResponse(error.message)
            )
            else -> respondJson(
                HttpStatusCode.InternalServerError,
                ErrorResponse("Internal Server Error")
            )
        }
    }
}

val throwableHandler: HttpErrorHandler<Throwable> = {
    { throwable ->
        respondJson(
            HttpStatusCode.InternalServerError,
            ErrorResponse(throwable.message ?: "Internal Server Error")
        )
    }
}
