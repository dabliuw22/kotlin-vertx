package com.leysoft.products.adapter.`in`.api

import com.leysoft.core.error.*
import io.ktor.http.*
import io.ktor.server.application.*

val errorHandler: suspend ApplicationCall.() -> (Throwable) -> Unit = {
    { throwable ->
        when (throwable) {
            is NotFoundProductException -> response.status(HttpStatusCode.NotFound)
            is CreateProductException -> response.status(HttpStatusCode.Conflict)
            is DeleteProductException -> response.status(HttpStatusCode.Conflict)
            is CustomProductException -> response.status(HttpStatusCode.Conflict)
            else -> response.status(HttpStatusCode.InternalServerError)
        }
    }
}
