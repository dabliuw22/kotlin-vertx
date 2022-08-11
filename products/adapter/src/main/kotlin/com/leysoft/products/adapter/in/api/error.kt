package com.leysoft.products.adapter.`in`.api

import com.leysoft.core.error.*
import com.leysoft.infrastructure.http.ErrorResponse
import com.leysoft.infrastructure.http.HttpErrorHandler
import com.leysoft.infrastructure.http.respondJson
import io.ktor.http.*

val errorHandler: HttpErrorHandler<Throwable> = {
    { throwable ->
        when (throwable) {
            is NotFoundProductException -> respondJson(
                HttpStatusCode.NotFound,
                ErrorResponse(throwable.message)
            )
            is CreateProductException -> respondJson(
                HttpStatusCode.Conflict,
                ErrorResponse(throwable.message)
            )
            is DeleteProductException -> respondJson(
                HttpStatusCode.Conflict,
                ErrorResponse(throwable.message)
            )
            is CustomProductException -> respondJson(
                HttpStatusCode.Conflict,
                ErrorResponse(throwable.message)
            )
            else -> respondJson(
                HttpStatusCode.InternalServerError,
                ErrorResponse(throwable.message ?: "Internal Server Error")
            )
        }
    }
}
