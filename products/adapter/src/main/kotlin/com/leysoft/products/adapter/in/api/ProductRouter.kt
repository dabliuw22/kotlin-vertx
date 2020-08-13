package com.leysoft.products.adapter.`in`.api

import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.fx.ForIO
import arrow.fx.fix
import com.leysoft.infrastructure.http.HttpJson
import com.leysoft.products.application.ProductService
import com.leysoft.products.domain.ProductId
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class ProductRouter(private val service: ProductService<ForIO>): HttpJson() {

    fun routers(router: Router) {
        router.route(HttpMethod.GET, GetAll)
            .handler { allHandler(it) }
        router.route(HttpMethod.GET, GetById)
            .handler { getByIdHandler(it) }
        router.route(HttpMethod.POST, Create)
            .handler { createHandler(it) }
        router.route(HttpMethod.DELETE, DelById)
            .handler { delByIdHandler(it) }
    }

    private fun allHandler(ctx: RoutingContext) {
        service.findAll().fix().unsafeRunAsync {
            when (it) {
                is Right -> ctx.response()
                    .setStatusCode(HttpResponseStatus.OK.code())
                    .putHeader(HttpHeaders.CONTENT_TYPE, ApplicationJson)
                    .end(Json.encodePrettily(it.b.map { products ->  products.toDto() }))
                is Left  -> ctx.response()
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end()
            }
        }
    }

    private fun getByIdHandler(ctx: RoutingContext) {
        service.findBy(ProductId(ctx.request().getParam("productId")))
            .fix().unsafeRunAsync {
                when (it) {
                    is Right -> ctx.response()
                        .setStatusCode(HttpResponseStatus.OK.code())
                        .end(Json.encodePrettily(it.b.map { product -> product.toDto() }))
                    else     -> ctx.response()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end()
                }
            }
    }

    private fun createHandler(ctx: RoutingContext) {}

    private fun delByIdHandler(ctx: RoutingContext) {
        service.deleteBy(ProductId(ctx.request().getParam("productId")))
            .fix().unsafeRunAsync {
                when (it) {
                    is Right ->
                        if (it.b) ctx.response()
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .end()
                        else ctx.response()
                            .setStatusCode(HttpResponseStatus.CONFLICT.code())
                            .end()
                    else     -> ctx.response()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end()
                }
            }
    }

    companion object {
        private const val Products = "/products"
        private const val GetAll = Products
        private const val GetById = "$Products/:productId"
        private const val Create = Products
        private const val DelById = "$Products/:productId"
    }
}