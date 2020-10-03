package com.leysoft.main

import arrow.fx.reactor.ForMonoK
import arrow.fx.reactor.MonoK
import arrow.fx.reactor.extensions.monok.effect.effect
import arrow.fx.rx2.extensions.asCoroutineContext
import com.leysoft.infrastructure.http.HttpRoute
import com.leysoft.infrastructure.http.HttpServer
import com.leysoft.infrastructure.http.handler
import com.leysoft.infrastructure.logger.LoggerFactory
import com.leysoft.products.adapter.`in`.api.ProductRoute
import com.leysoft.products.adapter.out.persistence.sql.SqlProductRepository
import com.leysoft.products.application.DefaultProductService
import com.leysoft.products.application.ProductService
import com.leysoft.products.domain.persistence.ProductRepository
import io.reactivex.schedulers.Schedulers
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class MainMono : HttpServer() {

    override fun port(): Int = System.getenv("APP_PORT")?.toInt() ?: 8080

    override fun host(): String = System.getenv("APP_HOST") ?: "localhost"

    override fun routes(router: Router) = products(router)

    private fun products(router: Router) {
        val repository: ProductRepository<ForMonoK> = SqlProductRepository.make(Q)
        val service: ProductService<ForMonoK> = DefaultProductService.make(Q, repository)
        val route: HttpRoute = ProductRoute(handler, service)
        route.route(router)
    }

    companion object {

        private val Q = MonoK.effect()

        private val io = Schedulers.io().asCoroutineContext()

        private val handler = MonoK.handler()

        private val log = LoggerFactory.getLogger(Q, MainMono::class)

        @JvmStatic
        fun main(args: Array<String>) {
            val vertx = Vertx.vertx()
            vertx.deployVerticle(MainMono::class.java.canonicalName) {
                if (it.succeeded()) log.info("Server started successfully...")
                else log.error("Server startup failed...")
            }
        }
    }
}
