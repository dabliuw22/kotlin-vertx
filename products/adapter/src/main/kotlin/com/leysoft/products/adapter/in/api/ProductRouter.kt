package com.leysoft.products.adapter.`in`.api

import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.core.Some
import arrow.fx.ForIO
import arrow.fx.fix
import com.leysoft.core.domain.ProductId
import com.leysoft.infrastructure.http.HttpJson
import com.leysoft.products.application.ProductService
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class ProductRouter(private val service: ProductService<ForIO>) : HttpJson() {

    fun routers(router: Router) {
        router.route(HttpMethod.GET, GetAll)
            .handler { allHandler(it) }
        router.route(HttpMethod.GET, GetById)
            .handler { getByIdHandler(it) }
        router.route(HttpMethod.POST, Create)
            .consumes(ApplicationJson)
            .handler { createHandler(it) }
        router.route(HttpMethod.DELETE, DelById)
            .handler { delByIdHandler(it) }
    }

    private fun allHandler(ctx: RoutingContext) {
        service.findAll().fix().unsafeRunAsync {
            when (it) {
                is Right ->
                    ctx.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, ApplicationJson)
                        .setStatusCode(HttpResponseStatus.OK.code())
                        .end(encode(it.b.map { products -> products.toDto() }))
                is Left ->
                    ctx.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
            }
        }
    }

    private fun getByIdHandler(ctx: RoutingContext) {
        service.findBy(ProductId(ctx.request().getParam("productId")))
            .fix().unsafeRunAsync { result ->
                when (result) {
                    is Right -> {
                        when (val product = result.b) {
                            is Some ->
                                ctx.response()
                                    .putHeader(HttpHeaders.CONTENT_TYPE, ApplicationJson)
                                    .setStatusCode(HttpResponseStatus.OK.code())
                                    .end(encode(product.t.toDto()))
                            else -> ctx.fail(HttpResponseStatus.NOT_FOUND.code())
                        }
                    }
                    else ->
                        ctx.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                }
            }
    }

    private fun createHandler(ctx: RoutingContext) {
        val product = decode(ctx.bodyAsString, PutProductDto::class).toDomain()
        service.save(product).fix().unsafeRunAsync {
            when (it) {
                is Right ->
                    ctx.response()
                        .setStatusCode(HttpResponseStatus.CREATED.code())
                        .end()
                is Left -> ctx.fail(HttpResponseStatus.CONFLICT.code())
            }
        }
    }

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
                    else ->
                        ctx.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
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
