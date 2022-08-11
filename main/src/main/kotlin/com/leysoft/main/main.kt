@file:JvmName("App")

package com.leysoft.main

import com.leysoft.infrastructure.http.Server.run
import com.leysoft.infrastructure.http.ServerResource
import com.leysoft.infrastructure.jdbc.Sql
import com.leysoft.infrastructure.jdbc.SqlResource
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
                with(config.sql) {
                    SqlResource.make()
                        .flatMap { sql ->
                            with(config.server) {
                                with(sql) {
                                    ServerResource.make {
                                        productModule()
                                    }
                                }
                            }
                        }
                }
            }.use { it.run() }
    }

context(Sql)
private fun Application.productModule() {
    val repository = SqlProductRepository.make()
    val service = ProductService.Instance.make(repository)
    products(service)
}
