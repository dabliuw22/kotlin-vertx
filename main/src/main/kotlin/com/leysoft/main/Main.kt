package com.leysoft.main

import arrow.Kind
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.Resource
import arrow.fx.extensions.io.bracket.bracket
import arrow.fx.extensions.io.effect.effect
import com.leysoft.infrastructure.http.HttpServer
import com.leysoft.products.adapter.`in`.api.ProductRouter
import com.leysoft.products.adapter.out.persistence.SqlProductRepository
import com.leysoft.products.application.DefaultProductService
import io.vertx.core.Vertx
import io.vertx.ext.web.Router

class Main : HttpServer() {

    private val Q = IO.effect()

    override fun port(): Int = 8080

    override fun host(): String = "localhost"

    override fun routes(router: Router) {
        products(router)
    }

    private fun products(router: Router) {
        val repository = SqlProductRepository.make(Q)
        val service = DefaultProductService.make(Q, repository)
        ProductRouter(service).routers(router)
    }

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val vertx = Vertx.vertx()
            vertx.deployVerticle(Main::class.java.canonicalName)
        }

        private fun program() = Resource(acquire, release, IO.bracket())

        private val acquire: () -> Kind<ForIO, Vertx> = { IO.just(Vertx.vertx()) }

        private val release: (Vertx) -> Kind<ForIO, Unit> = { IO.just(it.close()) }
    }
}
