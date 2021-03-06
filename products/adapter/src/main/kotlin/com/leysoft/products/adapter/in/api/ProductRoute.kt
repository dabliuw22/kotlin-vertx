package com.leysoft.products.adapter.`in`.api

import com.leysoft.core.domain.ProductId
import com.leysoft.infrastructure.http.HttpHandler
import com.leysoft.infrastructure.http.HttpJson
import com.leysoft.infrastructure.http.HttpRoute
import com.leysoft.products.application.ProductService
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class ProductRoute<F> (
    private val H: HttpHandler<F>,
    private val service: ProductService<F>
) : HttpJson(), HttpRoute, HttpHandler<F> by H {

    override fun route(router: Router) {
        router.route(HttpMethod.GET, Products)
            .handler { allHandler(it) }
        router.route(HttpMethod.GET, ProductsById)
            .handler { getByIdHandler(it) }
        router.route(HttpMethod.POST, Products)
            .consumes(ApplicationJson)
            .handler { createHandler(it) }
        router.route(HttpMethod.DELETE, ProductsById)
            .handler { delByIdHandler(it) }
    }

    private val allHandler: (RoutingContext) -> Unit = { ctx ->
        service.getAll().handle(
            {
                ctx.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, ApplicationJson)
                    .setStatusCode(HttpResponseStatus.OK.code())
                    .end(encode(it.map { products -> products.toDto() }))
            }
        ) { errorHandler(ctx) }
    }

    private val getByIdHandler: (RoutingContext) -> Unit = { ctx ->
        service.getBy(ProductId(ctx.request().getParam(ProductId))).handle(
            {
                ctx.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, ApplicationJson)
                    .setStatusCode(HttpResponseStatus.OK.code())
                    .end(encode(it.toDto()))
            }) { errorHandler(ctx) }
    }

    private val createHandler: (RoutingContext) -> Unit = { ctx ->
        val product = decode(ctx.bodyAsString, PutProductDto::class).toDomain()
        service.create(product).handle(
            {
                ctx.response()
                    .setStatusCode(HttpResponseStatus.CREATED.code())
                    .end()
            }) { errorHandler(ctx) }
    }

    private val delByIdHandler: (RoutingContext) -> Unit = { ctx ->
        service.deleteBy(ProductId(ctx.request().getParam(ProductId))).handle(
            {
                ctx.response()
                    .setStatusCode(HttpResponseStatus.OK.code())
                    .end()
            }) { errorHandler(ctx) }
    }

    companion object {
        private const val ProductId = "productId"
        private const val Products = "/products"
        private const val ProductsById = "$Products/:$ProductId"
    }
}
