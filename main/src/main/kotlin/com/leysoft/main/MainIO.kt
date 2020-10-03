package com.leysoft.main

import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.io.dispatchers.dispatchers
import arrow.fx.extensions.io.effect.effect
import com.leysoft.infrastructure.http.HttpRoute
import com.leysoft.infrastructure.http.HttpServer
import com.leysoft.infrastructure.http.handler
import com.leysoft.infrastructure.logger.LoggerFactory
import com.leysoft.products.adapter.`in`.api.ProductRoute
import com.leysoft.products.adapter.out.persistence.sql.SqlProductRepository
import com.leysoft.products.application.DefaultProductService
import com.leysoft.products.application.ProductService
import com.leysoft.products.domain.persistence.ProductRepository
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class MainIO : HttpServer() {

    override fun port(): Int = System.getenv("APP_PORT")?.toInt() ?: 8080

    override fun host(): String = System.getenv("APP_HOST") ?: "localhost"

    override fun routes(router: Router) = products(router)

    private fun products(router: Router) {
        val repository: ProductRepository<ForIO> = SqlProductRepository.make(Q)
        val service: ProductService<ForIO> = DefaultProductService.make(Q, repository)
        val route: HttpRoute = ProductRoute(handler, service)
        route.route(router)
    }

    companion object {

        private val Q = IO.effect()

        private val io = IO.dispatchers().io()

        private val handler = IO.handler()

        private val log = LoggerFactory.getLogger(Q, MainIO::class)

        @JvmStatic
        fun main(args: Array<String>) {
            val vertx = Vertx.vertx()
            vertx.deployVerticle(MainIO::class.java.canonicalName) {
                if (it.succeeded()) log.info("Server started successfully...")
                else log.error("Server startup failed...")
            }
        }
    }
}
