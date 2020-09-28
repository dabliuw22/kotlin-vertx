package com.leysoft.products.adapter.`in`.api

import arrow.fx.reactor.ForMonoK
import arrow.fx.reactor.fix
import com.leysoft.infrastructure.http.HttpJson
import com.leysoft.products.application.ProductService
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class ProductRouterMono(private val service: ProductService<ForMonoK>) : HttpJson() {

    fun routers(router: Router) {
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
        service.getAll().fix().mono.subscribe(
            {
                ctx.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, ApplicationJson)
                    .setStatusCode(HttpResponseStatus.OK.code())
                    .end(encode(it.map { products -> products.toDto() }))
            }
        ) { errorHandler(ctx)(it) }
    }

    private val getByIdHandler: (RoutingContext) -> Unit = { ctx ->
        service.getBy(com.leysoft.core.domain.ProductId(ctx.request().getParam(ProductId)))
            .fix().mono.subscribe(
                {
                    ctx.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, ApplicationJson)
                        .setStatusCode(HttpResponseStatus.OK.code())
                        .end(encode(it.toDto()))
                }
            ) { errorHandler(ctx)(it) }
    }

    private val createHandler: (RoutingContext) -> Unit = { ctx ->
        val product = decode(ctx.bodyAsString, PutProductDto::class).toDomain()
        service.create(product).fix().mono.subscribe(
            {
                ctx.response()
                    .setStatusCode(HttpResponseStatus.CREATED.code())
                    .end()
            }
        ) { errorHandler(ctx)(it) }
    }

    private val delByIdHandler: (RoutingContext) -> Unit = { ctx ->
        service.deleteBy(com.leysoft.core.domain.ProductId(ctx.request().getParam(ProductId)))
            .fix().mono.subscribe(
                {
                    ctx.response()
                        .setStatusCode(HttpResponseStatus.OK.code())
                        .end()
                }
            ) { errorHandler(ctx)(it) }
    }

    companion object {
        private const val ProductId = "productId"
        private const val Products = "/products"
        private const val ProductsById = "$Products/:$ProductId"
    }
}
