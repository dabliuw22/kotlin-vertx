package com.leysoft.main

import arrow.fx.IO
import arrow.fx.extensions.io.dispatchers.dispatchers
import arrow.fx.extensions.io.effect.effect
import com.leysoft.infrastructure.http.HttpServer
import com.leysoft.products.adapter.`in`.api.ProductRouterIO
import com.leysoft.products.adapter.out.persistence.sql.SqlProductRepository
import com.leysoft.products.application.DefaultProductService
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class MainIO : HttpServer() {

    private val Q = IO.effect()

    private val io = IO.dispatchers().io()

    override fun port(): Int = System.getenv("APP_PORT")?.toInt() ?: 8080

    override fun host(): String = System.getenv("APP_HOST") ?: "localhost"

    override fun routes(router: Router) = products(router)

    private fun products(router: Router) {
        val repository = SqlProductRepository.make(Q)
        val service = DefaultProductService.make(Q, repository)
        ProductRouterIO(service).routers(router)
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val vertx = Vertx.vertx()
            vertx.deployVerticle(MainIO::class.java.canonicalName)
        }
    }
}
