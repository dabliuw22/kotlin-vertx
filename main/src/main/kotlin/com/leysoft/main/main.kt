@file:JvmName("App")

package com.leysoft.main

import arrow.fx.coroutines.resource
import com.leysoft.infrastructure.http.Server.run
import com.leysoft.infrastructure.http.ServerResource
import com.leysoft.infrastructure.jdbc.Jdbc
import com.leysoft.infrastructure.jdbc.JdbcResource
import com.leysoft.products.adapter.`in`.api.products
import com.leysoft.products.adapter.out.persistence.sql.SqlProductRepository
import com.leysoft.products.application.ProductService
import io.ktor.server.application.*

suspend fun main(): Unit =
    resource {
        val config = Config.env().load().bind()
        with(config.jdbc) {
            with(this) {
                val jdbc = JdbcResource.invoke().bind()
                with(jdbc) {
                    with(config.server) {
                        val server = ServerResource.invoke {
                            productModule()
                        }.bind()
                        server
                    }
                }
            }
        }
    }.use { it.run() }

context(Jdbc)
private fun Application.productModule() {
    val repository = SqlProductRepository.invoke()
    val service = ProductService.invoke(repository)
    products(service)
}
