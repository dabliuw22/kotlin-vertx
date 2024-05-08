package com.leysoft.products.adapter.`in`.api

import arrow.core.raise.effect
import arrow.core.raise.toEither
import com.leysoft.core.domain.toProductId
import com.leysoft.infrastructure.http.*
import com.leysoft.products.application.ProductService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*

fun Application.products(
    service: ProductService
) {
    routing {
        route("/products") {
            all(service)
            get(service)
            create(service)
            delete(service)
        }
    }
}

private fun Route.all(service: ProductService) {
    get {
        val program = effect {
            service.getAll().map { product -> product.toDto() }
        }
        program.toEither().fold({ errorHandler }, { call.respondJson(HttpStatusCode.OK, it) })
    }
}

private fun Route.get(service: ProductService) {
    get("/{id}") {
        val program = effect {
            val id = call.getRequiredParam("id") { it.toProductId() }
            service.getBy(id)
        }
        program.toEither().fold({ errorHandler }, { call.respondJson(HttpStatusCode.OK, it) })
    }
}

private fun Route.create(service: ProductService) {
    post {
        val program = effect {
            val product = call.receive<PutProductDto>().toDomain()
            service.create(product)
        }
        program.toEither().fold({ errorHandler }, { call.respondJson(HttpStatusCode.OK, it) })
    }
}

private fun Route.delete(service: ProductService) {
    delete("/{id}") {
        val program = effect {
            val id = call.getRequiredParam("id") { it.toProductId() }
            service.deleteBy(id)
        }
        program.toEither().fold({ errorHandler }, { call.respondJson(HttpStatusCode.OK, it) })
    }
}
