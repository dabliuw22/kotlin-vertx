@file:JvmName("App")

package com.leysoft.main

import arrow.fx.coroutines.Resource
import arrow.fx.coroutines.resource
import com.leysoft.infrastructure.exposed.Exposed
import com.leysoft.infrastructure.exposed.ExposedResource
import com.leysoft.infrastructure.http.Server.run
import com.leysoft.infrastructure.http.ServerResource
import com.leysoft.infrastructure.jdbc.Jdbc
import com.leysoft.infrastructure.jdbc.JdbcResource
import com.leysoft.products.adapter.`in`.api.products
import com.leysoft.products.adapter.out.persistence.exposed.ExposedProductRepository
import com.leysoft.products.adapter.out.persistence.sql.SqlProductRepository
import com.leysoft.products.application.ProductService
import io.ktor.server.application.*
import io.ktor.server.netty.*

suspend fun main(): Unit = serverWithExposed().use { it.run() }

private fun serverWithJdbc(): Resource<NettyApplicationEngine> =
    resource {
        val config = Config.env().load().bind()
        with(config.jdbc) {
            with(this) {
                val jdbc = JdbcResource.invoke().bind()
                with(jdbc) {
                    with(config.server) {
                        val server =
                            ServerResource
                                .invoke {
                                    productModuleWithJdbc()
                                }.bind()
                        server
                    }
                }
            }
        }
    }

context(Jdbc)
private fun Application.productModuleWithJdbc() {
    val repository = SqlProductRepository.invoke()
    val service = ProductService.invoke(repository)
    products(service)
}

private fun serverWithExposed(): Resource<NettyApplicationEngine> =
    resource {
        val config = Config.env().load().bind()
        with(config.exposed) {
            with(this) {
                val exposed = ExposedResource.invoke().bind()
                with(exposed) {
                    with(config.server) {
                        val server =
                            ServerResource
                                .invoke {
                                    productModuleWithExposed()
                                }.bind()
                        server
                    }
                }
            }
        }
    }

context(Exposed)
private fun Application.productModuleWithExposed() {
    val repository = ExposedProductRepository.invoke()
    val service = ProductService.invoke(repository)
    products(service)
}
