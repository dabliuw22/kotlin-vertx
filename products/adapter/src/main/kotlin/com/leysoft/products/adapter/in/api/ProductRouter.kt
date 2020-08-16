package com.leysoft.products.adapter.`in`.api

import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.fx.ForIO
import arrow.fx.fix
import com.leysoft.core.domain.ProductId
import com.leysoft.core.error.CreateProductException
import com.leysoft.core.error.DeleteProductException
import com.leysoft.core.error.NotFoundProductException
import com.leysoft.infrastructure.http.HttpJson
import com.leysoft.products.application.ProductService
import io.netty.handler.codec.http.HttpResponseStatus
import io.vertx.core.http.HttpHeaders
import io.vertx.core.http.HttpMethod
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

class ProductRouter(private val service: ProductService<ForIO>) : HttpJson() {

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

    private fun allHandler(ctx: RoutingContext) {
        service.findAll().fix().unsafeRunAsync {
            when (it) {
                is Right ->
                    ctx.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, ApplicationJson)
                        .setStatusCode(HttpResponseStatus.OK.code())
                        .end(encode(it.b.map { products -> products.toDto() }))
                is Left -> errorHandler(ctx, it.a)
            }
        }
    }

    private fun getByIdHandler(ctx: RoutingContext) {
        service.findBy(ProductId(ctx.request().getParam(ProductId)))
            .fix().unsafeRunAsync {
                when (it) {
                    is Right ->
                        ctx.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, ApplicationJson)
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .end(encode(it.b.toDto()))
                    is Left -> errorHandler(ctx, it.a)
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
                is Left -> errorHandler(ctx, it.a)
            }
        }
    }

    private fun delByIdHandler(ctx: RoutingContext) {
        service.deleteBy(ProductId(ctx.request().getParam(ProductId)))
            .fix().unsafeRunAsync {
                when (it) {
                    is Right ->
                        ctx.response()
                            .setStatusCode(HttpResponseStatus.OK.code())
                            .end()
                    is Left -> errorHandler(ctx, it.a)
                }
            }
    }

    private fun errorHandler(ctx: RoutingContext, throwable: Throwable) {
        when (throwable) {
            is NotFoundProductException -> ctx.fail(HttpResponseStatus.NOT_FOUND.code())
            is CreateProductException -> ctx.fail(HttpResponseStatus.CONFLICT.code())
            is DeleteProductException -> ctx.fail(HttpResponseStatus.CONFLICT.code())
            else -> ctx.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
        }
    }

    companion object {
        private const val ProductId = "productId"
        private const val Products = "/products"
        private const val ProductsById = "$Products/:$ProductId"
    }
}
