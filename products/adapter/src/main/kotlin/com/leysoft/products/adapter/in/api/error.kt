package com.leysoft.products.adapter.`in`.api

import com.leysoft.core.error.CreateProductException
import com.leysoft.core.error.DeleteProductException
import com.leysoft.core.error.NotFoundProductException
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.ext.web.RoutingContext

val errorHandler: (RoutingContext) -> (Throwable) -> Unit = { ctx ->
    { throwable ->
        when (throwable) {
            is NotFoundProductException -> ctx.fail(HttpResponseStatus.NOT_FOUND.code())
            is CreateProductException -> ctx.fail(HttpResponseStatus.CONFLICT.code())
            is DeleteProductException -> ctx.fail(HttpResponseStatus.CONFLICT.code())
            else -> ctx.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
        }
    }
}
