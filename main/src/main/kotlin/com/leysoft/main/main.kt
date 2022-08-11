@file:JvmName("App")

package com.leysoft.main

import com.leysoft.infrastructure.http.Server.run
import com.leysoft.infrastructure.http.ServerResource
import com.leysoft.infrastructure.jdbc.Jdbc
import com.leysoft.infrastructure.jdbc.JdbcResource
import com.leysoft.products.adapter.`in`.api.products
import com.leysoft.products.adapter.out.persistence.sql.SqlProductRepository
import com.leysoft.products.application.ProductService
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

fun main(): Unit =
    runBlocking(Dispatchers.Default) {
        Config.env().load()
            .flatMap { config ->
                with(config.jdbc) {
                    JdbcResource.make()
                        .flatMap { jdbc ->
                            with(config.server) {
                                with(jdbc) {
                                    ServerResource.make {
                                        productModule()
                                    }
                                }
                            }
                        }
                }
            }.use { it.run() }
    }

context(Jdbc)
private fun Application.productModule() {
    val repository = SqlProductRepository.make()
    val service = ProductService.Instance.make(repository)
    products(service)
}
