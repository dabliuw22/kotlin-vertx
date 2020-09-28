package com.leysoft.main

import arrow.fx.reactor.MonoK
import arrow.fx.reactor.extensions.monok.effect.effect
import arrow.fx.rx2.extensions.asCoroutineContext
import com.leysoft.infrastructure.http.HttpServer
import com.leysoft.products.adapter.`in`.api.ProductRouterMono
import com.leysoft.products.adapter.out.persistence.sql.SqlProductRepository
import com.leysoft.products.application.DefaultProductService
import io.reactivex.schedulers.Schedulers
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class MainMono : HttpServer() {

    private val Q = MonoK.effect()

    private val io = Schedulers.io().asCoroutineContext()

    override fun port(): Int = System.getenv("APP_PORT")?.toInt() ?: 8080

    override fun host(): String = System.getenv("APP_HOST") ?: "localhost"

    override fun routes(router: Router) = products(router)

    private fun products(router: Router) {
        val repository = SqlProductRepository.make(Q)
        val service = DefaultProductService.make(Q, repository)
        ProductRouterMono(service).routers(router)
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val vertx = Vertx.vertx()
            vertx.deployVerticle(MainMono::class.java.canonicalName)
        }
    }
}
