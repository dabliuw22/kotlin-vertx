package com.leysoft.main

import arrow.fx.IO
import arrow.fx.Ref
import arrow.fx.extensions.io.effect.effect
import arrow.fx.extensions.io.monadDefer.monadDefer
import arrow.fx.fix
import com.leysoft.infrastructure.http.HttpServer
import com.leysoft.products.adapter.`in`.api.ProductRouter
import com.leysoft.products.adapter.out.persistence.InMemoryProductRepository
import com.leysoft.products.application.DefaultProductService
import com.leysoft.products.domain.Product
import com.leysoft.products.domain.ProductCreatedAt
import com.leysoft.products.domain.ProductId
import com.leysoft.products.domain.ProductName
import com.leysoft.products.domain.ProductStock
import io.vertx.core.Vertx
import io.vertx.ext.web.Router
import java.time.OffsetDateTime
import java.util.UUID

class Main: HttpServer() {

    override fun port(): Int = 8080

    override fun host(): String = "localhost"

    override fun routes(router: Router) {
        products(router)
    }

    private fun products(router: Router) {
        val repository = InMemoryProductRepository.make(Q, store)
        val service = DefaultProductService.make(Q, repository)
        ProductRouter(service).routers(router)
    }

    private val Q = IO.effect()

    private val initId = UUID.randomUUID().toString()

    private val db = mapOf(
        initId to Product(
            id = ProductId(initId),
            name = ProductName("Test"),
            stock = ProductStock(20.0),
            createdAt = ProductCreatedAt(OffsetDateTime.now())
        )
    )

    private val store = Ref(IO.monadDefer(), db).fix().unsafeRunSync()

    companion object {

        @JvmStatic
        fun main(args: Array<String>) {
            val vertx = Vertx.vertx()
            vertx.deployVerticle(Main::class.java.canonicalName)
        }
    }
}